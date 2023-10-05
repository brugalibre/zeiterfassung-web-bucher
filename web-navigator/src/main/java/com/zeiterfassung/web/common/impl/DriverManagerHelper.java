package com.zeiterfassung.web.common.impl;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * The {@link DriverManagerHelper} helps to determine the corresponding {@link DriverManagerType} and {@link WebDriver} instance
 * a given String value
 */
public class DriverManagerHelper {

   private static final DriverManagerType DEFAULT_WEB_DRIVER = DriverManagerType.CHROME;
   private static final Logger LOG = LoggerFactory.getLogger(DriverManagerHelper.class);
   private final DriverManagerType driverManagerType;

   /**
    * Creates a new {@link DriverManagerHelper} for the given browser name
    *
    * @param browserName the given browser name
    */
   public DriverManagerHelper(String browserName) {
      this.driverManagerType = getDriverManagerType4Name(browserName);
   }

   /*
    * Returns the {@link DriverManagerType} for a given browser name
    */
   private DriverManagerType getDriverManagerType4Name(String browserName) {
      for (DriverManagerType driverManager : DriverManagerType.values()) {
         if (driverManager.name().equalsIgnoreCase(browserName)) {
            return driverManager;
         }
      }
      LOG.warn("No suitable web-driver found for name '{}', fallback to default '{}'", browserName, DEFAULT_WEB_DRIVER.name());
      return DEFAULT_WEB_DRIVER;
   }

   /**
    * @param headless <code>true</code> if the browser should be started in a headless-mode or <code>false</code> if not
    * @return a new {@link WebDriver} instance for the {@link DriverManagerType} of this helper
    */
   public WebDriver createNewWebDriver(boolean headless) {
      try {
         if (driverManagerType == DriverManagerType.CHROME) {
            ChromeOptions options = buildChromeOptions(headless);
            return new ChromeDriver(options);
         }
         Class<?> webDriverClass = Class.forName(driverManagerType.browserClass());
         return (WebDriver) webDriverClass.getConstructor().newInstance();
      } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
         throw new IllegalStateException("Error while creating new WebDriver instance!", e);
      }
   }

   private static ChromeOptions buildChromeOptions(boolean headless) {
      ChromeOptions options = new ChromeOptions();
      //This option was deprecated, see https://sqa.stackexchange.com/questions/32444/how-to-disable-infobar-from-chrome
      options.addArguments("disable-infobars"); // disabling infobars
      options.addArguments("--disable-extensions"); // disabling extensions
      // ChromeDriver is just AWFUL because every version or two it breaks unless you pass cryptic arguments
      //AGRESSIVE:
      options.addArguments("--dns-prefetch-disable");
      options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
      options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
      options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
      options.addArguments("--no-sandbox"); //  Bypass OS security model -> https://stackoverflow.com/a/50725918/1689770
      options.addArguments("--disable-dev-shm-usage"); //overcome limited resource problems -> https://stackoverflow.com/a/50725918/1689770
      options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
      options.addArguments("--disable-features=VizDisplayCompositor");
      options.addArguments("--enable-features=NetworkServiceInProgress");
      options.addArguments("--remote-allow-origins=*");//https://groups.google.com/g/chromedriver-users/c/xL5-13_qGaA
      options.addArguments("--disable-gpu"); // applicable to windows os only -> https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
      if (headless) {
         options.addArguments("--headless"); //https://stackoverflow.com/questions/50790733/unknown-error-devtoolsactiveport-file-doesnt-exist-error-while-executing-selen/50791503#50791503
      }
      return options;
   }

   /**
    * @return the {@link DriverManagerType} of this helper
    */
   public DriverManagerType getDriverManagerType() {
      return driverManagerType;
   }
}
