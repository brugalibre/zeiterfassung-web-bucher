package com.zeiterfassung.web.common.impl;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DriverManagerHelper} helps to determine the corresponding {@link DriverManagerType} and {@link WebDriver} instance
 * a given String value
 */
public class DriverManagerHelper {

   private static final DriverManagerType DEFAULT_WEB_DRIVER = DriverManagerType.CHROME;
   private static final Logger LOG = LoggerFactory.getLogger(DriverManagerHelper.class);
   private DriverManagerType driverManagerType;

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
         Class<?> webDriverClass = Class.forName(driverManagerType.browserClass());
         return (WebDriver) webDriverClass.newInstance();
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
         throw new IllegalStateException("Error while creating new WebDriver instance!", e);
      }
   }

   /**
    * @return the {@link DriverManagerType} of this helper
    */
   public DriverManagerType getDriverManagerType() {
      return driverManagerType;
   }
}
