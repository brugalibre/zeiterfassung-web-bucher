package com.zeiterfassung.web.common.impl.navigate;

import com.zeiterfassung.web.common.WebElementEvaluator;
import com.zeiterfassung.web.common.impl.WebElementEvaluatorImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class BaseWebNavigatorHelper {

   protected WebDriver webDriver;
   protected WebElementEvaluator webElementEvaluator;

   public BaseWebNavigatorHelper(WebDriver webDriver) {
      this.webDriver = requireNonNull(webDriver);
      this.webElementEvaluator = new WebElementEvaluatorImpl(webDriver);
   }

   /**
    * Returns the {@link WebElement} matching to the given {@link By}
    *
    * @param by the by to filter for
    * @return the {@link WebElement} matching to the given {@link By}
    */
   public WebElement getElement(By by) {
      return webElementEvaluator.getElement(by);
   }

   /**
    * Tries to find an {@link WebElement} with the given id. If no such element is found, an empty Optional is returned.
    * Otherwise an {@link Optional} contained the found {@link WebElement}
    *
    * @param id the {@link By} in order to locate the element
    * @return an empty {@link Optional} or an {@link Optional} containing the desired {@link WebElement}
    */
   public Optional<WebElement> findWebElementById(String id) {
      return webElementEvaluator.findElement(By.id(id));
   }

   /**
    * Finds a {@link WebElement} by a {@link By#name(String)} which matches the given <code>nameValue</code>.
    * Since this is not necessarily unique the {@link WebElement} must contain a attribut with the given name and value
    *
    * @param searchContext     the search context to look in
    * @param nameValue         the value of the 'name' attribut
    * @param attrName          the name of an additionally attribut to query
    * @param expectedAttrValue the value of the additionally attribute
    * @return the first found web-element - if there is more than one
    */
   public WebElement findWebElementByNameTagNameAndValue(WebElement searchContext, String nameValue, String attrName, String expectedAttrValue) {
      List<WebElement> webElementsByNameAndValue = webElementEvaluator.findWebElementByFilterAndValueInternal(searchContext, By.name(nameValue), expectedAttrValue, webElement -> webElement.getAttribute(attrName));
      return webElementsByNameAndValue.get(0);
   }

   public void waitForElementWithId(String elementId) {
      waitForElement(webElementEvaluator.getElement(By.id(elementId)));
   }

   private void waitForElement(WebElement webElement) {
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(2000));
      wait.until(ExpectedConditions.visibilityOf(webElement));
   }
}
