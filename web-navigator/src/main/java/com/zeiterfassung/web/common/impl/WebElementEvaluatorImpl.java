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
import java.util.function.Predicate;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class WebElementEvaluatorImpl implements WebElementEvaluator {

   private static final Logger LOG = LoggerFactory.getLogger(WebElementEvaluatorImpl.class);
   private final WebDriver webDriver;

   public WebElementEvaluatorImpl(WebDriver webDriver) {
      this.webDriver = requireNonNull(webDriver);
   }

   @Override
   public List<WebElement> findAllWebElementsByPredicateAndBy(WebElement searchContext, By by, Predicate<WebElement> webElementPredicate) {
      List<WebElement> webElements4TageName = findElementsByBy(searchContext, by);
      List<WebElement> foundWebElements4TagName = new ArrayList<>();
      for (WebElement webElement4TagName : webElements4TageName) {
         // We found the matching span value
         if (webElementPredicate.test(webElement4TagName)) {
            foundWebElements4TagName.add(webElement4TagName);
         }
      }
      LOG.info("Found {} WebElements for 'By'-condition '{}' and search-context '{}'", foundWebElements4TagName.size(), by, searchContext);
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

   @Override
   public Optional<WebElement> findElementBy(WebElement webElement2FindWithing, By by) {
      WebElement webElement = null;
      try {
         webElement = webElement2FindWithing.findElement(by);
      } catch (Exception e) {
         // ignore
      }
      return Optional.ofNullable(webElement);
   }
}
