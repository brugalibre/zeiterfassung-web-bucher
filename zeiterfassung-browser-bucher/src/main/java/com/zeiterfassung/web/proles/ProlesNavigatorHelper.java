package com.zeiterfassung.web.proles;

import com.zeiterfassung.web.common.impl.navigate.BaseWebNavigatorHelper;
import com.zeiterfassung.web.common.inout.PropertyReader;
import com.zeiterfassung.web.proles.constant.ProlesWebConst;
import com.zeiterfassung.web.proles.helper.ProlesWebElementIdEvaluator;
import com.zeiterfassung.web.proles.helper.impl.ProlesWebElementIdEvaluatorImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProlesNavigatorHelper extends BaseWebNavigatorHelper {

   private static final Logger LOG = LoggerFactory.getLogger(ProlesNavigatorHelper.class);
   private ProlesWebElementIdEvaluator webElementIdEvaluator;

   public ProlesNavigatorHelper(WebDriver webDriver) {
      super(webDriver);
      this.webElementIdEvaluator = new ProlesWebElementIdEvaluatorImpl(webElementEvaluator);
   }

   public String evalRowIdByCustomerProjectAndActivity(String customer, String projectNameValue, String activity) {
      LOG.info("Evaluate row id by customer '{}', project '{}' and activity '{}'", customer, projectNameValue, activity);
      WebElement tblforminfoTable = webElementEvaluator.getElement(By.id(ProlesWebConst.WEB_ELEMENT_BOOK_TABLE_ID));
      return webElementIdEvaluator.evalRowIdByCustomerProjectAndActivity(tblforminfoTable, customer, projectNameValue, activity);
   }

   /**
    * @return the url at which the proles-booking page is located
    */
   public String getBookingPageUrl() {
      PropertyReader propertyReader = new PropertyReader(ProlesWebConst.PROLES_BOOKING_PROPERTIES_FILE_NAME);
      return propertyReader.readValue("bookingPage");
   }
}
