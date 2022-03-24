package com.zeiterfassung.web.jira.navigator;

import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.impl.navigate.BaseWebBookNavigator;
import com.zeiterfassung.web.common.impl.navigate.BaseWebNavigatorHelper;
import com.zeiterfassung.web.jira.constant.JiraWebConstant;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JiraWebNavigator extends BaseWebBookNavigator<JiraWebNavigatorHelper> {
   public JiraWebNavigator(String userName, String userPassword, String propertiesName) {
      super(userName, userPassword, propertiesName);
   }

   @Override
   public WebElement getBookingDateInputField() {
      return this.webNavigatorHelper.getElement(By.id(JiraWebConstant.DATE_INPUT_FIELD_ID));
   }

   @Override
   protected JiraWebNavigatorHelper createWebNavigatorHelper(WebDriver webDriver) {
      return new JiraWebNavigatorHelper(webDriver);
   }

   @Override
   protected void navigateToBookingPage4BookRecord(BookRecordEntry bookRecordEntry) {

   }

   @Override
   protected String getElementId2WaitForBookingPageReady() {
      return JiraWebConstant.MORE_OPERATIONS_DROPDOWN_ID;
   }

   @Override
   protected String getUserPasswordInputFieldId() {
      return JiraWebConstant.USER_PASSWORD_INPUT_FIELD_ID;
   }

   @Override
   protected String getUserNameInputFieldId() {
      return JiraWebConstant.USER_NAME_INPUT_FIELD_ID;
   }

   @Override
   protected String getLoginSubmitButtonId() {
      return JiraWebConstant.LOGIN_SUBMIT_BUTTON_ID;
   }
}
