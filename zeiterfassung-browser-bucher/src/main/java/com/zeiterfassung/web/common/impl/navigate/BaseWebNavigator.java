package com.zeiterfassung.web.common.impl.navigate;

import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.impl.DriverManagerHelper;
import com.zeiterfassung.web.common.inout.PropertyReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;


/**
 * The {@link BaseWebNavigator} serves as base class for navigating through a web page
 * Therefore the {@link BaseWebNavigator} holds a field to the {@link WebDriver} which does the actual work for us
 */
public abstract class BaseWebNavigator<T extends BaseWebNavigatorHelper> {

   protected T webNavigatorHelper;

   private WebDriver webDriver;
   private DriverManagerHelper driverManagerHelper;

   private final String userName;
   private final String userPassword;
   private String loginPage;
   private String proxy;

   protected BaseWebNavigator(String userName, String userPassword, String propertiesName) {
      this.userName = requireNonNull(userName);
      this.userPassword = requireNonNull(userPassword);
      readNonCredentialProperties(propertiesName);
   }

   /**
    * Initializes the {@link WebDriverManager} and creates a new {@link WebDriver}
    * This method may take a while and needs a connection to the internet
    */
   public void initWebDriver() {
      DriverManagerType driverManagerType = driverManagerHelper.getDriverManagerType();
      if (nonNull(proxy)) {
         WebDriverManager.getInstance(driverManagerType)
                 .proxy(proxy)
                 .proxyUser(userName)
                 .proxyPass(userPassword)
                 .setup();
      } else {
         WebDriverManager.getInstance(driverManagerType).setup();
      }
      this.webDriver = driverManagerHelper.createNewWebDriver();
      this.webNavigatorHelper = createWebNavigatorHelper(webDriver);
   }

   /**
    * Performs a login on the page, defined in the properties file
    * This includes entering username & password as well as submitting the form.
    * Afterwards this {@link BaseWebNavigator} waits until the submit-button is disappeared
    */
   public void login() {
      navigateToPage(loginPage);
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

   public void navigateToBookingPage(BookRecordEntry bookRecordEntry) {
      navigateToBookingPage4BookRecord(bookRecordEntry);
      webNavigatorHelper.waitForElementWithId(getElementId2WaitForBookingPageReady());
   }

   public WebElement getElementById(String s) {
      return webDriver.findElement(By.id(s));
   }

   public Optional<WebElement> findWebElementById(String id) {
      return webNavigatorHelper.findWebElementById(id);
   }

   public WebElement findWebElementByNameTagNameAndValue(WebElement searchContext, String nameValue, String attrName, String expectedAttrValue) {
      return this.webNavigatorHelper.findWebElementByNameTagNameAndValue(searchContext, nameValue, attrName, expectedAttrValue);
   }

   public void waitForElementWithId(String id) {
      this.webNavigatorHelper.waitForElementWithId(id);
   }

   /**
    * @return the {@link WebElement} in which the booking date is entered
    */
   public abstract WebElement getBookingDateInputField();

   protected abstract void navigateToBookingPage4BookRecord(BookRecordEntry bookRecordEntry);

   protected abstract String getElementId2WaitForBookingPageReady();

   protected void clickButtonByIdAndWait(String buttonId) {
      WebElement button = getElementById(buttonId);
      button.submit();
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(10000));
      wait.until(ExpectedConditions.invisibilityOf(button));
   }

   protected void enterUserName() {
      WebElement textUserName = getElementById(getUserNameInputFieldId());
      textUserName.sendKeys(userName);
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

   protected void navigateToPage(String pageUrl) {
      try {
         webDriver.navigate()
                 .to(new URL(pageUrl));
      } catch (MalformedURLException e) {
         throw new IllegalStateException(e);
      }
   }

   private void clickSubmitButton() {
      clickButtonByIdAndWait(getLoginSubmitButtonId());
   }

   private void enterUserPassword() {
      WebElement textUserPwd = getElementById(getUserPasswordInputFieldId());
      JavascriptExecutor executor = (JavascriptExecutor) webDriver;
      executor.executeScript("arguments[0].setAttribute('value', '" + userPassword + "')", textUserPwd);
      // textUserPwd.sendKeys(userPassword); // does not work with !: is filtered by sendKeys
   }

   private void readNonCredentialProperties(String propertiesName) {
      PropertyReader propertyReader = new PropertyReader(propertiesName);
      loginPage = propertyReader.readValue("loginPage");
      proxy = propertyReader.readValue("proxy");
      String browserName = propertyReader.readValue("browserName");
      this.driverManagerHelper = new DriverManagerHelper(browserName);
   }
}

