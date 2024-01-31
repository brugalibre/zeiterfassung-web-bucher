package com.zeiterfassung.web.common.impl.navigate;

import com.zeiterfassung.web.common.impl.DriverManagerHelper;
import com.zeiterfassung.web.common.inout.PropertyReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;


/**
 * The {@link BaseWebNavigator} serves as base class for navigating through a web page
 * Therefore the {@link BaseWebNavigator} holds a field to the {@link WebDriver} which does the actual work for us
 */
public abstract class BaseWebNavigator<T extends BaseWebNavigatorHelper> {

   private static final Logger LOG = LoggerFactory.getLogger(BaseWebNavigator.class);
   protected T webNavigatorHelper;

   private WebDriver webDriver;
   private DriverManagerHelper driverManagerHelper;

   protected int pageLoadTimeOut;
   protected int implicitWaitTimeOut;
   private final String userName;
   private final boolean isHeadless;
   private char[] userPassword;
   private String loginPage;
   private String proxy;

   protected BaseWebNavigator(String userName, char[] userPassword, String propertiesName) {
      this.userName = requireNonNull(userName);
      this.userPassword = requireNonNull(userPassword);
      this.isHeadless = readBooleanProperty("isHeadless", propertiesName);
      readNonCredentialProperties(propertiesName);
      readTimeoutProperties(propertiesName);
   }

   public T getHelper() {
      return webNavigatorHelper;
   }

   /**
    * Initializes the {@link WebDriverManager} and creates a new {@link WebDriver}
    * This method may take a while and needs a connection to the internet
    */
   public void initWebDriver() {
      this.initWebDriver(isHeadless);
   }

   /**
    * Initializes the {@link WebDriverManager} and creates a new {@link WebDriver}
    * This method may take a while and needs a connection to the internet
    *
    * @param headless <code>true</code> if the browser should be started in a headless-mode or <code>false</code> if not
    */
   public void initWebDriver(boolean headless) {
      DriverManagerType driverManagerType = driverManagerHelper.getDriverManagerType();
      if (nonNull(proxy)) {
         WebDriverManager.getInstance(driverManagerType)
                 .proxy(proxy)
                 .proxyUser(userName)
                 .proxyPass(String.valueOf(userPassword))
                 .setup();
      } else {
         WebDriverManager.getInstance(driverManagerType).setup();
      }
      this.webDriver = driverManagerHelper.createNewWebDriver(headless);
      setOptions();
      this.webNavigatorHelper = createWebNavigatorHelper(webDriver);
   }

   /**
    * Performs a login on the page, defined in the properties file
    * This includes entering navigating to the login page and entering username & password as well as submitting the form.
    * Afterwards this {@link BaseWebNavigator} waits until the submit-button is disappeared
    */
   public void navigateToPageAndLogin() {
      navigateToPage(loginPage);
      login();
   }

   /**
    * Performs a login on the page, defined in the properties file
    * This includes entering username & password as well as submitting the form.
    * Afterwards this {@link BaseWebNavigator} waits until the submit-button is disappeared
    */
   public void login() {
      enterUserName();
      enterUserPassword();
      clickSubmitButton();
   }

   /**
    * Performs a logout and also closing the browser
    */
   public void logout() {
      webDriver.close();
   }

   public void waitForElementWithId(String id) {
      waitForVisibilityOfElement(By.id(id), 4000);
   }

   public void waitForVisibilityOfElement(By by, long millis) {
      this.webNavigatorHelper.waitForVisibilityOfElement(by, Duration.ofMillis(millis));
   }

   /**
    * Creates a new {@link Actions} for interactions with a {@link WebElement}
    *
    * @return a new {@link Actions} for interactions with a {@link WebElement}
    */
   public Actions createNewActions() {
      return new Actions(webDriver);
   }

   private void submitButtonByIdAndWait4Disappear(String buttonId) {
      WebElement button = webDriver.findElement(By.id(buttonId));
      submitButtonAndWait4Invisibility(button);
   }

   protected void submitButtonAndWait4Invisibility(WebElement button) {
      button.submit();
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(10000));
      wait.until(ExpectedConditions.invisibilityOf(button));
   }

   protected void enterUserName() {
      this.enterUserName(false);
   }

   protected void enterUserName(boolean withEnterKey) {
      WebElement textUserName = webDriver.findElement(By.id(getUserNameInputFieldId()));
      textUserName.sendKeys(userName);
      if (withEnterKey) {
         textUserName.sendKeys(Keys.RETURN);
      }
   }

   protected void setUserPassword(char[] userPassword) {
      this.userPassword = Objects.requireNonNull(userPassword);
   }

   /**
    * Creates a {@link BaseWebNavigatorHelper} with the given {@link WebDriver}
    *
    * @param webDriver the {@link WebDriver}
    * @return a new {@link BaseWebNavigatorHelper}
    */
   protected abstract T createWebNavigatorHelper(WebDriver webDriver);

   protected abstract String getUserPasswordInputFieldId();

   protected abstract String getUserNameInputFieldId();

   protected abstract String getLoginSubmitButtonId();

   protected void executeScript(String script, WebElement webElement) {
      this.webNavigatorHelper.executeScript(script, webElement);
   }

   protected void navigateToPage(String pageUrlAsString) {
      URL pageUrl = getUrl(pageUrlAsString);
      try {
         navigateToPageInternal(pageUrl);
      } catch (TimeoutException e) {
         // In case of time out, just try again.
         navigateToPageInternal(pageUrl);
      }
   }

   private static URL getUrl(String pageUrlAsString) {
      try {
         return new URL(pageUrlAsString);
      } catch (MalformedURLException e) {
         throw new IllegalStateException(e);
      }
   }

   private void navigateToPageInternal(URL pageUrl) {
      webDriver.navigate()
              .to(pageUrl);
   }

   private void clickSubmitButton() {
      String loginSubmitButtonId = getLoginSubmitButtonId();
      if (nonNull(loginSubmitButtonId)) {
         submitButtonByIdAndWait4Disappear(loginSubmitButtonId);
      } else {
         WebElement submitButton = findLoginSubmitButton();
         submitButtonAndWait4Invisibility(submitButton);
      }
   }

   protected WebElement findLoginSubmitButton() {
      return null;// by default this does nothing
   }

   protected void enterUserPassword() {
      this.enterUserPassword(false);
   }

   protected void enterUserPassword(boolean withEnterKey) {
      WebElement textUserPwd = webDriver.findElement(By.id(getUserPasswordInputFieldId()));
      executeScript("arguments[0].setAttribute('value', '" + String.valueOf(userPassword) + "')", textUserPwd);
      Arrays.fill(userPassword, '0');
      if (withEnterKey) {
         textUserPwd.sendKeys(Keys.RETURN);
      }
   }

   private void readTimeoutProperties(String propertiesName) {
      PropertyReader propertyReader = new PropertyReader(propertiesName);
      pageLoadTimeOut = Integer.parseInt(propertyReader.readValueOrDefault("pageLoadTimeOut", "10"));
      implicitWaitTimeOut = Integer.parseInt(propertyReader.readValueOrDefault("implicitWaitTimeOut", "10"));
   }

   private boolean readBooleanProperty(String propertyName, String propertiesName) {
      PropertyReader propertyReader = new PropertyReader(propertiesName);
      return Boolean.parseBoolean(propertyReader.readValueOrDefault(propertyName, "false"));
   }

   private void readNonCredentialProperties(String propertiesName) {
      PropertyReader propertyReader = new PropertyReader(propertiesName);
      loginPage = propertyReader.readValue("loginPage");
      proxy = propertyReader.readValue("proxy");
      String browserName = propertyReader.readValue("browserName");
      this.driverManagerHelper = new DriverManagerHelper(browserName);
   }

   private void setOptions() {
      LOG.info("Setting options: implicitWaitTimeOut={}, " +
              "pageLoadTimeOut={}, isHeadless={}", implicitWaitTimeOut, pageLoadTimeOut, isHeadless);
      this.webDriver.manage()
              .timeouts()
              .implicitlyWait(Duration.of(implicitWaitTimeOut, ChronoUnit.SECONDS))
              .pageLoadTimeout(Duration.of(pageLoadTimeOut, ChronoUnit.SECONDS));
   }
}

