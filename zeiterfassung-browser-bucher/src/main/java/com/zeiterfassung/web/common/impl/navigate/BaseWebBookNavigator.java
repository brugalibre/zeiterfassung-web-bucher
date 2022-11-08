package com.zeiterfassung.web.common.impl.navigate;

import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * The {@link BaseWebBookNavigator} serves as base class for navigating through a web page
 * Therefore the {@link BaseWebBookNavigator} holds a field to the {@link WebDriver} which does the actual work for us
 */
public abstract class BaseWebBookNavigator<T extends BaseWebNavigatorHelper> extends BaseWebNavigator<T> {

   protected BaseWebBookNavigator(String userName, char[] userPassword, String propertiesName) {
      super(userName, userPassword, propertiesName);
   }

   public void navigateToBookingPage(BookRecordEntry bookRecordEntry) {
      navigateToBookingPage4BookRecord(bookRecordEntry);
      webNavigatorHelper.waitForVisibilityOfElement(By.id(getElementId2WaitForBookingPageReady()), 4000);
   }

   /**
    * @return the {@link WebElement} in which the booking date is entered
    */
   public abstract WebElement getBookingDateInputField();

   protected abstract void navigateToBookingPage4BookRecord(BookRecordEntry bookRecordEntry);

   protected abstract String getElementId2WaitForBookingPageReady();
}

