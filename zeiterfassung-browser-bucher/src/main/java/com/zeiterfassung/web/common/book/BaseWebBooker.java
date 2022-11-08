package com.zeiterfassung.web.common.book;

import com.zeiterfassung.web.common.book.record.BookRecord;
import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.book.record.errorhandling.ErrorHandler;
import com.zeiterfassung.web.common.book.record.errorhandling.ExceptionEntry;
import com.zeiterfassung.web.common.constant.BaseWebConst;
import com.zeiterfassung.web.common.impl.navigate.BaseWebBookNavigator;
import com.zeiterfassung.web.common.impl.navigate.BaseWebNavigator;
import com.zeiterfassung.web.common.impl.navigate.BaseWebNavigatorHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The {@link BaseWebBooker} describes the common procedure for booking on a web page
 * It therefore contains a {@link BaseWebNavigator} in order to navigate through the page
 *
 * @param <B> the specific type of the {@link BaseWebNavigator}
 */
public abstract class BaseWebBooker<B extends BaseWebBookNavigator<H>, H extends BaseWebNavigatorHelper> {

   private static final Logger LOG = LoggerFactory.getLogger(BaseWebBooker.class);
   protected B baseWebNavigator;
   protected H baseWebNavigatorHelper;

   protected BaseWebBooker(String userName, char[] userPassword, String propertiesName) {
      this.baseWebNavigator = createWebNavigator(userName, userPassword, propertiesName);
   }

   /**
    * Creates the {@link BaseWebNavigator}
    *
    * @param userName       the username
    * @param userPassword   the userpassword
    * @param propertiesName the name of the properties file with further details
    * @return a new created but not yet initialized {@link BaseWebNavigator}
    */
   protected abstract B createWebNavigator(String userName, char[] userPassword, String propertiesName);

   /**
    * Initializes this {@link BaseWebBooker} and its resources
    */
   protected void initBooker() {
      this.baseWebNavigator.initWebDriver();
      this.baseWebNavigatorHelper = baseWebNavigator.getHelper();
   }

   /**
    * Books the given {@link BookRecord} and all its {@link BookRecordEntry}
    *
    * @param bookRecord the {@link BookRecordEntry to book}
    * @return the booked {@link BookRecord}
    */
   public BookRecord bookRecords(BookRecord bookRecord) {
      baseWebNavigator.navigateToPageAndLogin();
      book(bookRecord);
      baseWebNavigator.logout();
      return bookRecord;
   }

   private void book(BookRecord bookRecord) {
      LOG.info("Start booking...");
      long startDate = System.currentTimeMillis();
      for (BookRecordEntry bookerRecordEntry : bookRecord.getBookerRecordEntries()) {
         bookSingleEntry(bookerRecordEntry);
      }
      LOG.info("Done booking record. Time elapsed '{}'", (System.currentTimeMillis() - startDate));
   }

   protected void enterDate(String date) {
      WebElement elementDate = baseWebNavigator.getBookingDateInputField();
      // Note, that elementDate.clear() does not work always. Sometimes this causes an error, because the date-field contains 'not a valid date'
      // Anyway, setting the date directly also does not work without either clearing or selecting its current input.
      // However.. just sending 'Keys.Control, "a", Keys.DELETE' (or BACK_SPACE) also suddenly don't work anymore, since then a strange character like [] remains on the input field.
      // Getting tired of this
      baseWebNavigator.createNewActions()
              .keyDown(elementDate, Keys.CONTROL)
              .pause(200)
              .sendKeys(elementDate, "a")
              .keyUp(Keys.CONTROL)
              .pause(200)
              .sendKeys(elementDate, Keys.BACK_SPACE)
              .sendKeys(date)
              .sendKeys("\t")
              .perform();
   }

   private void bookSingleEntry(BookRecordEntry bookRecordEntry) {
      ErrorHandler errorHandler = exceptionMsg -> ExceptionEntry.of(exceptionMsg, bookRecordEntry.getDescriptionShort());
      try {
         navigateToBookingPage(bookRecordEntry);
         bookSingleEntryInternal(bookRecordEntry, errorHandler);
      } catch (Exception e) {
         ExceptionEntry exceptionEntry = errorHandler.handleError(e.getLocalizedMessage());
         bookRecordEntry.setErrorEntry(exceptionEntry);
         LOG.error(exceptionEntry.getErrorMsg(), e);
      }
   }

   protected void navigateToBookingPage(BookRecordEntry bookRecordEntry) {
      baseWebNavigator.navigateToBookingPage(bookRecordEntry);
   }

   protected abstract void bookSingleEntryInternal(BookRecordEntry prolesBookRecordEntry, ErrorHandler errorHandler);

   protected void enterHours(String id, String amountOfWorkedHours) {
      WebElement hourWebElement = baseWebNavigatorHelper.getWebElementByNameTagNameAndValue(null, BaseWebConst.HTML_TAG_INPUT, getComponentAttrName(), id);
      hourWebElement.sendKeys(amountOfWorkedHours);
   }

   protected abstract String getComponentAttrName();

   protected void enterDescription(String id, String description) {
      WebElement descriptionWebElement = baseWebNavigatorHelper.getElement(By.id(id));
      descriptionWebElement.sendKeys(description);
   }

   protected void submitEntry(String id) {
      WebElement descriptionWebElement = baseWebNavigatorHelper.getElement(By.id(id));
      descriptionWebElement.sendKeys(Keys.ENTER);
   }
}

