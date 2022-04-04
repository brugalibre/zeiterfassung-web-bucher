package com.zeiterfassung.web;

import com.zeiterfassung.web.common.impl.DriverManagerHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DriverManagerHelperTest {

   private WebDriver testWebDriver;

   @Before
   public void setUp() {
      testWebDriver = null;
   }

   @After
   public void tearDown() {
      if (nonNull(testWebDriver)) {
         testWebDriver.close();
      }
   }

   @Test
   public void testGetDriverManagerType4Name_UnknownName() {
      // Given
      String browserName = "Schnappi";

      // When
      DriverManagerHelper driverManagerHelper = new DriverManagerHelper(browserName);
      DriverManagerType actualDriverManagerType4Name = driverManagerHelper.getDriverManagerType();

      // Then
      assertThat(actualDriverManagerType4Name, is(DriverManagerType.CHROME));
   }

   @Test
   public void testGetDriverManagerType4Name_Opera() {
      // Given
      String browserName = "OpErA";

      // When
      DriverManagerHelper driverManagerHelper = new DriverManagerHelper(browserName);
      DriverManagerType actualDriverManagerType4Name = driverManagerHelper.getDriverManagerType();

      // Then
      assertThat(actualDriverManagerType4Name, is(DriverManagerType.OPERA));
   }

   @Test
   public void testGetWebDriver4TypeFirefox() {
      String browserName = "firefox";
      DriverManagerHelper driverManagerHelper = new DriverManagerHelper(browserName);

      // When
      WebDriverManager.getInstance(driverManagerHelper.getDriverManagerType()).setup();
      this.testWebDriver = driverManagerHelper.createNewWebDriver();

      // Then
      assertThat(testWebDriver, is(CoreMatchers.instanceOf(FirefoxDriver.class)));
   }
}
