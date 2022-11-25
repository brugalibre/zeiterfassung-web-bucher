package com.zeiterfassung.web.common.impl;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
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
    * @return a new {@link WebDriver} instance for the {@link DriverManagerType} of this helper
    */
   public WebDriver createNewWebDriver() {
      try {
         if (driverManagerType == DriverManagerType.CHROME) {
            ChromeOptions options = buildChromeOptions();
            Class<?> webDriverClass = Class.forName(driverManagerType.browserClass());
            return (WebDriver) webDriverClass.getConstructor(Capabilities.class).newInstance(options);
         }
         Class<?> webDriverClass = Class.forName(driverManagerType.browserClass());
         return (WebDriver) webDriverClass.newInstance();
      } catch (ClassNotFoundException | InstantiationException
              | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
         throw new IllegalStateException("Error while creating new WebDriver instance!", e);
      }
   }

   private static ChromeOptions buildChromeOptions() {
      ChromeOptions options = new ChromeOptions();
      options.addArguments("disable-infobars"); // disabling infobars
      options.addArguments("--disable-extensions"); // disabling extensions
      // ChromeDriver is just AWFUL because every version or two it breaks unless you pass cryptic arguments
      //AGRESSIVE: options.setPageLoadStrategy(PageLoadStrategy.NONE); // https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
      options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
      options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
      options.addArguments("--no-sandbox"); //  Bypass OS security model -> https://stackoverflow.com/a/50725918/1689770
      options.addArguments("--disable-dev-shm-usage"); //overcome limited resource problems -> https://stackoverflow.com/a/50725918/1689770
      options.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
      options.addArguments("--disable-gpu"); // applicable to windows os only -> https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc

//This option was deprecated, see https://sqa.stackexchange.com/questions/32444/how-to-disable-infobar-from-chrome
      return options;
   }

   /**
    * @return the {@link DriverManagerType} of this helper
    */
   public DriverManagerType getDriverManagerType() {
      return driverManagerType;
   }
}
