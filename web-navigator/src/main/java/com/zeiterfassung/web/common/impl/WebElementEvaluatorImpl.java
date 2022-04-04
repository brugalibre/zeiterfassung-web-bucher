package com.zeiterfassung.web.common.impl;

import com.zeiterfassung.web.common.WebElementEvaluator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class WebElementEvaluatorImpl implements WebElementEvaluator {

   private static final Logger LOG = LoggerFactory.getLogger(WebElementEvaluatorImpl.class);
   private WebDriver webDriver;

   public WebElementEvaluatorImpl(WebDriver webDriver) {
      this.webDriver = requireNonNull(webDriver);
   }

   private static void logFindWebElementByTagNameAndTextValue(WebElement searchContext, String tagName, String expectedTextValue) {
      LOG.info("Evaluate WebElements for tag-name '{}' in search-context ({}) with expected tag value '{}'", tagName, getWebElementString(searchContext), expectedTextValue);
   }

   private static String getWebElementString(WebElement searchContext) {
      return "tag-name: '" + searchContext.getTagName() + "', id: '" +
              searchContext.getAttribute("id") + "'";
   }

   @Override
   public List<WebElement> findWebElementByTagNameAndTextValue(WebElement searchContext, String tagName, String expectedTextValue) {
      logFindWebElementByTagNameAndTextValue(searchContext, tagName, expectedTextValue);
      return findWebElementByNameAndValueInternal(searchContext, tagName, expectedTextValue, WebElement::getText);
   }

   @Override
   public List<WebElement> findWebElementByNameAndValueInternal(WebElement searchContext, String tagName, String expectedWebElementValue, Function<WebElement, String> webElementValueProvider) {
      logFindWebElementByTagNameAndTextValue(searchContext, tagName, expectedWebElementValue);
      return findWebElementByFilterAndValueInternal(searchContext, By.tagName(tagName), expectedWebElementValue, webElementValueProvider);
   }

   @Override
   public List<WebElement> findWebElementByFilterAndValueInternal(WebElement searchContext, By by, String expectedWebElementValue, Function<WebElement, String> webElementValueProvider) {
      List<WebElement> webElements4TageName = findElementsByBy(searchContext, by);
      List<WebElement> foundWebElements4TagName = new ArrayList<>();
      for (WebElement webElement4TagName : webElements4TageName) {
         String webElementValue = webElementValueProvider.apply(webElement4TagName);
         // We found the matching span value
         if (expectedWebElementValue.equals(webElementValue)) {
            foundWebElements4TagName.add(webElement4TagName);
         }
      }
      LOG.info("Found {} WebElements", foundWebElements4TagName.size());
      return foundWebElements4TagName;
   }

   public List<WebElement> findElementsByBy(WebElement searchContext, By by) {
      if (nonNull(searchContext)) {
         return searchContext.findElements(by);
      }
      return webDriver.findElements(by);
   }

   @Override
   public WebElement getElement(By by) {
      return webDriver.findElement(by);
   }

   @Override
   public Optional<WebElement> findElement(By by) {
      WebElement webElement = null;
      try {
         webElement = webDriver.findElement(by);
      } catch (Exception e) {
         // ignore
      }
      return Optional.ofNullable(webElement);
   }

}
