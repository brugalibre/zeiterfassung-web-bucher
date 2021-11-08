package com.zeiterfassung.web.proles.helper.impl;

import com.zeiterfassung.web.common.WebElementEvaluator;
import com.zeiterfassung.web.common.exception.NoSuchIdException;
import com.zeiterfassung.web.common.util.WebElementUtil;
import com.zeiterfassung.web.proles.helper.ProlesWebElementIdEvaluator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.zeiterfassung.web.common.constant.BaseWebConst.HTML_TAG_SPAN;
import static com.zeiterfassung.web.common.constant.BaseWebConst.HTML_TAG_TABLE_TR;
import static com.zeiterfassung.web.common.util.WebElementUtil.appendWebElements2String;
import static com.zeiterfassung.web.common.util.WebElementUtil.getWebElementString;
import static com.zeiterfassung.web.proles.constant.ProlesWebConst.WEB_ELEMENT_DATA_ROW_ID;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class ProlesWebElementIdEvaluatorImpl implements ProlesWebElementIdEvaluator {

   private static final Logger LOG = LoggerFactory.getLogger(ProlesWebElementIdEvaluatorImpl.class);
   private WebElementEvaluator webElementEvaluator;

   public ProlesWebElementIdEvaluatorImpl(WebElementEvaluator webElementEvaluator) {
      this.webElementEvaluator = requireNonNull(webElementEvaluator);
   }

   private static String evalId4UniqueWebElementMatch(WebElement webElement) {
      WebElement tableRow = webElement.findElement(By.xpath("./.."));
      while (!tableRow.getTagName().equals(HTML_TAG_TABLE_TR)) {
         tableRow = tableRow.findElement(By.xpath("./.."));
      }
      return tableRow.getAttribute(WEB_ELEMENT_DATA_ROW_ID);
   }

   @Override
   public String evalRowIdByCustomerProjectAndActivity(WebElement searchContext, String customer, String projectName, String activity) {
      LOG.info("Evaluate row id by customer '{}', project '{}' and activity '{}'", customer, projectName, activity);
      List<WebElement> projectWebElements = webElementEvaluator.findWebElementByTagNameAndTextValue(searchContext, HTML_TAG_SPAN, projectName);
      String id = evalRowIdByCustomerProjectAndActivityInternal(searchContext, customer, activity, projectWebElements);
      if (isNull(id)) {
         throwNoSuchIdException(searchContext, customer, projectName, activity, projectWebElements);
      }
      LOG.info("Evaluated row id '{}", id);
      return id;
   }

   private void throwNoSuchIdException(WebElement searchContext, String customer, String projectName, String activity, List<WebElement> projectWebElements) {
      String allProjects = WebElementUtil.appendWebElements2String(projectWebElements);
      List<WebElement> activityWebElements = webElementEvaluator.findWebElementByTagNameAndTextValue(searchContext, HTML_TAG_SPAN, activity);
      String allActivities = WebElementUtil.appendWebElements2String(activityWebElements);
      throw new NoSuchIdException("No id found in WebElement " + getWebElementString(searchContext) + " for customer '" + customer + "', project '" + projectName + "' and activity '" + activity
              + "'.\nAll found projects: " + allProjects + "\nAll found activities: " + allActivities);
   }

   private String evalRowIdByCustomerProjectAndActivityInternal(WebElement searchContext, String customer, String activity, List<WebElement> projectWebElements) {
      if (projectWebElements.size() > 1) {
         return evalId4NonUniqueWebElementMatch(searchContext, customer, activity);
      } else if (!projectWebElements.isEmpty()) {
         LOG.info("Eval id for one unique found project-WebElement {}", getWebElementString(projectWebElements.get(0)));
         return evalId4UniqueWebElementMatch(projectWebElements.get(0));
      }
      LOG.error("No row-id evaluated for customer '{}' and activity '{}' when searching in WebElement '{}'!", customer, activity, getWebElementString(searchContext));
      return null;
   }

   private String evalId4NonUniqueWebElementMatch(WebElement searchContext, String customer, String activity) {
      List<WebElement> activities = webElementEvaluator.findWebElementByTagNameAndTextValue(searchContext, HTML_TAG_SPAN, activity);
      if (activities.size() == 1) {
         LOG.info("Eval id for one unique found activity-WebElement {}", getWebElementString(activities.get(0)));
         return evalId4UniqueWebElementMatch(activities.get(0));
      }
      // TODO Match multiple activities and customers found..
      List<WebElement> customers = webElementEvaluator.findWebElementByTagNameAndTextValue(searchContext, HTML_TAG_SPAN, customer);
      LOG.error("No unique activity-WebElement found.. Evaluation of id not implemented for multiple activities." +
                      "\nFound activity-WebElements: {}.\nFound customer-WebElements: {} ", appendWebElements2String(activities),
              appendWebElements2String(customers));
      return null;
   }
}
