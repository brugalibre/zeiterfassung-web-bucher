package com.zeiterfassung.web.proles.impl.navigate;

import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.impl.navigate.BaseWebBookNavigator;
import com.zeiterfassung.web.common.impl.navigate.BaseWebNavigator;
import com.zeiterfassung.web.proles.ProlesNavigatorHelper;
import com.zeiterfassung.web.proles.constant.ProlesWebConst;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.zeiterfassung.web.proles.constant.ProlesWebConst.WEB_ELEMENT_DATE_FIELD_ID;

/**
 * The {@link ProlesWebNavigator} implements the proles specific methods, which
 * are defined as abstract  methods in the {@link BaseWebNavigator}
 */
public class ProlesWebNavigator extends BaseWebBookNavigator<ProlesNavigatorHelper> {
   public ProlesWebNavigator(String userName, String userPassword, String propertiesName) {
      super(userName, userPassword, propertiesName);
   }

   @Override
   public WebElement getBookingDateInputField() {
      return this.webNavigatorHelper.getElement(By.id(WEB_ELEMENT_DATE_FIELD_ID));
   }

   public String evalRowIdByCustomerProjectAndActivity(String customer, String project, String activity) {
      return webNavigatorHelper.evalRowIdByCustomerProjectAndActivity(customer, project, activity);
   }

   @Override
   protected ProlesNavigatorHelper createWebNavigatorHelper(WebDriver webDriver) {
      return new ProlesNavigatorHelper(webDriver);
   }

   @Override
   protected String getElementId2WaitForBookingPageReady() {
      return ProlesWebConst.WEB_ELEMENT_DATE_FIELD_ID;
   }

   @Override
   protected void navigateToBookingPage4BookRecord(BookRecordEntry bookRecordEntry) {
      navigateToPage(webNavigatorHelper.getBookingPageUrl());
   }

   @Override
   protected String getUserPasswordInputFieldId() {
      return ProlesWebConst.WEB_ELEMENT_PWD_FIELD;
   }

   @Override
   protected String getUserNameInputFieldId() {
      return ProlesWebConst.WEB_ELEMENT_USER_NAME_FIELD;
   }

   @Override
   protected String getLoginSubmitButtonId() {
      return ProlesWebConst.WEB_ELEMENT_ANMELDE_BUTTON;
   }
}
