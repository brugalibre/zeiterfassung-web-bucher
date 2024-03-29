package com.zeiterfassung.web.common.impl.navigate;

import com.zeiterfassung.web.common.WebElementEvaluator;
import com.zeiterfassung.web.common.impl.WebElementEvaluatorImpl;
import com.zeiterfassung.web.common.navigate.util.WebNavigateUtil;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.zeiterfassung.web.common.constant.BaseWebConst.CLICK_BUTTON_SCRIPT;
import static com.zeiterfassung.web.common.constant.BaseWebConst.SCROLL_INTO_VIEW_SCRIPT;
import static java.util.Objects.requireNonNull;

public class BaseWebNavigatorHelper {

   private static final Logger LOG = LoggerFactory.getLogger(BaseWebNavigatorHelper.class);
   private static final String SCREENSHOT_SAVE_PATH = "logs/";
   public static final String SCREENSHOT_NAME_PREFIX = "log-screenshot_%s_";
   public static final String SCREENSHOT_FILE_TYPE = ".png";
   protected WebDriver webDriver;
   protected WebElementEvaluator webElementEvaluator;

   public BaseWebNavigatorHelper(WebDriver webDriver) {
      this.webDriver = requireNonNull(webDriver);
      this.webElementEvaluator = new WebElementEvaluatorImpl(webDriver);

   }

   /**
    * @return the {@link DevTools} if this {@link WebDriver} implements {@link HasDevTools}. Otherwise <code>null</code> is returned
    */
   public DevTools getDevTool() {
      if (webDriver instanceof HasDevTools) {
         return ((HasDevTools) webDriver).getDevTools();
      }
      return null;
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
    * Finds a {@link WebElement} by a {@link By#name(String)} which matches the given <code>tagName</code>.
    * Since this is not necessarily unique the {@link WebElement} must contain an attribute with the given name and value
    * <p>
    * If there is more than one match, the first one is returned. If none if found, an empty Optional is returned
    *
    * @param searchContext     the search context to look in
    * @param tagName           the value of the 'tag' attribute
    * @param attrName          the name of an additional attribute to query
    * @param expectedAttrValue the value of the additional attribute
    * @return the first found web-element - if there is more than one
    */
   public Optional<WebElement> findWebElementByNameTagNameAndValue(WebElement searchContext, String tagName, String attrName, String expectedAttrValue) {
      return this.findWebElementBy(searchContext, WebNavigateUtil.createXPathBy(tagName, attrName, expectedAttrValue));
   }

   /**
    * Gets a {@link WebElement} by a {@link By#name(String)} which matches the given <code>tagName</code>.
    * Since this is not necessarily unique the {@link WebElement} must contain an attribute with the given name and value
    * <p>
    * If there is more than one match, the first one is returned.
    *
    * @param searchContext     the search context to look in
    * @param tagName           the value of the 'tag' attribute
    * @param attrName          the name of an additional attribute to query
    * @param expectedAttrValue the value of the additional attribute
    * @return the first found web-element - if there is more than one
    */
   public WebElement getWebElementByNameTagNameAndValue(WebElement searchContext, String tagName, String attrName, String expectedAttrValue) {
      LOG.debug("Looking for WebElement with tag-name '{}' and expected attr-value '{}'", tagName, expectedAttrValue);
      return findWebElementBy(searchContext, WebNavigateUtil.createXPathBy(tagName, attrName, expectedAttrValue)).
              orElseThrow(() -> new IllegalStateException("No WebElement found for tagName '" + tagName + "', attrName '" + attrName + "' and expected expectedAttrValue='" + expectedAttrValue + "'"));
   }

   /**
    * Finds a {@link WebElement} by a {@link By#name(String)} which matches the given <code>tag-name</code> and the
    * displayed html value. If there is more than one match, the first one is returned
    *
    * @param searchContext          the search context to look in
    * @param tagName                the tagName of the {@link WebElement}, e.g. 'Button' or 'span'
    * @param expectedInnerHtmlValue the value of the inner html of the {@link WebElement
    * @return the first found web-element - if there is more than one
    */
   public WebElement getWebElementByTageNameAndInnerHtmlValue(WebElement searchContext, String tagName, String expectedInnerHtmlValue) {
      LOG.debug("Looking for WebElement with tag-name '{}' and inner html value '{}'", tagName, expectedInnerHtmlValue);
      return findWebElementByPredicateAndBy(searchContext, By.tagName(tagName), webElement4TagName -> expectedInnerHtmlValue.equals(webElement4TagName.getText())).
              orElseThrow(() -> new IllegalStateException("No WebElement found for tagName '" + tagName + "' and expected inner-html value='" + expectedInnerHtmlValue + "'"));
   }

   /**
    * Finds a {@link WebElement} by a {@link By#tagName(String)} which matches the given <code>tag-name</code> and the
    * displayed html value. If there is more than one match, the first one is returned.
    * An empty Optional is returned, if no element is found
    *
    * @param searchContext          the search context to look in
    * @param tagName                the tagName of the {@link WebElement}, e.g. 'Button' or 'span'
    * @param expectedInnerHtmlValue the value of the inner html of the {@link WebElement
    * @return the first found web-element - if there is more than one
    */
   public Optional<WebElement> findWebElementByTageNameAndInnerHtmlValue(WebElement searchContext, String tagName, String expectedInnerHtmlValue) {
      LOG.debug("Looking for WebElement with tag-name '{}' and inner html value '{}'", tagName, expectedInnerHtmlValue);
      return findWebElementByPredicateAndBy(searchContext, By.tagName(tagName), webElement4TagName -> expectedInnerHtmlValue.equals(webElement4TagName.getText()));
   }

   /**
    * Finds a parent {@link WebElement} for a child, defined by the given <code>tagName</code> and <code>expectedInnerHtmlValue</code>
    * The parent can be matched with the given <code>parentsWebElementType</code>.
    * This method is e.g. used to find a button {@link WebElement} which displays a certain text. This text is not displayed by the button itself,
    * but by a child element of this button. So this child element defined by a certain tag as well as the buttons text
    * <p>
    * The tree with {@link WebElement} is traversed, using a <code>By.xpath("./..")</code> starting by the given child (<code>expectedInnerHtmlValue</code>)
    * If this child is found, than it's parent, which is also defined by a certaint tag name, is located by traversing the tree of {@link WebElement} upwards
    * until a {@link WebElement} with the <code>parentsWebElementType</code> html-tag is found
    * <p>
    *
    * @param searchContext          the search context to look in
    * @param tagName                the tagName of the {@link WebElement}, e.g. 'Button' or 'span'
    * @param expectedInnerHtmlValue the value of the inner html of the {@link WebElement
    * @param parentsWebElementType  the type of the parent
    * @return the first {@link WebElement} which is a parent of the given child and matches the given predicate
    * @see findWebElementByTageNameAndInnerHtmlValue
    */
   public Optional<WebElement> findParentWebElement4ChildTagNameAndInnerHtmlValue(WebElement searchContext, String tagName, String expectedInnerHtmlValue, String parentsWebElementType) {
      LOG.debug("Looking for parent-WebElement with tag-name '{}', childs-tag-name '{}' and inner html value '{}'", parentsWebElementType, tagName, expectedInnerHtmlValue);
      Optional<WebElement> childWebElement = findWebElementByTageNameAndInnerHtmlValue(searchContext, tagName, expectedInnerHtmlValue);
      if (childWebElement.isEmpty()) {
         return Optional.empty();
      }
      return findParentWebElement4ChildAndType(childWebElement.get(), parentsWebElementType);
   }

   /**
    * Finds a parent {@link WebElement} for the given <code>childWebElement</code> and the type of {@link WebElement} of
    * its parent
    * The parent can be matched with the given type. The tree with {@link WebElement} is traversed, using
    * a <code>By.xpath("./..")</code> starting by the given child
    *
    * @param childWebElement       the child {@link WebElement}
    * @param parentsWebElementType the type of the parent
    * @return the first {@link WebElement} which is a parent of the given child and matches the given predicate
    */
   public Optional<WebElement> findParentWebElement4ChildAndType(WebElement childWebElement, String parentsWebElementType) {
      Predicate<WebElement> webElementPredicate = webElement2Check -> webElement2Check.getTagName().equals(parentsWebElementType);
      return findParentWebElement4ChildAndType(childWebElement, webElementPredicate);
   }

   /**
    * Finds all {@link WebElement} within the given <code>searchContext</code> which matches the given by criteria and
    * predicate
    *
    * @param searchContext       the {@link WebElement} to search in
    * @param by                  the {@link By} to identify all relevant {@link WebElement}s
    * @param webElementPredicate the filter predicate for the relevant {@link WebElement}s
    * @return a {@link List} with found {@link WebElement}s
    */
   public List<WebElement> findAllWebElementsByPredicateAndBy(WebElement searchContext, By by, Predicate<WebElement> webElementPredicate) {
      return webElementEvaluator.findAllWebElementsByPredicateAndBy(searchContext, by, webElementPredicate);
   }

   public Optional<WebElement> findWebElementByPredicateAndBy(WebElement searchContext, By by, Predicate<WebElement> webElementPredicate) {
      List<WebElement> webElementsByNameAndValue = webElementEvaluator.findAllWebElementsByPredicateAndBy(searchContext, by, webElementPredicate);
      return webElementsByNameAndValue.isEmpty() ? Optional.empty() : Optional.of(webElementsByNameAndValue.get(0));
   }

   public Optional<WebElement> findWebElementBy(WebElement searchContext, By by) {
      List<WebElement> webElementsByNameAndValue = webElementEvaluator.findAllWebElementsByPredicateAndBy(searchContext, by, webElement -> true);
      return webElementsByNameAndValue.isEmpty() ? Optional.empty() : Optional.of(webElementsByNameAndValue.get(0));
   }

   /**
    * Finds a parent {@link WebElement} for the given <code>childWebElement</code>.
    * The parent can be matched with the given {@link Predicate}. The tree with {@link WebElement} is traversed, using
    * a <code>By.xpath("./..")</code> starting by the given child
    *
    * @param childWebElement     the child {@link WebElement}
    * @param findParentPredicate the Predicate to identify the parent within the tree of {@link WebElement}s
    * @return the first {@link WebElement} which is a parent of the given child and matches the given predicate.
    * If none is found, then an empty Optionals is returned
    */
   public Optional<WebElement> findParentWebElement4ChildAndType(WebElement childWebElement, Predicate<WebElement> findParentPredicate) {
      requireNonNull(findParentPredicate, "findParentPredicate must not be null");
      requireNonNull(childWebElement, "childWebElement must not be null");
      Optional<WebElement> nextWebElementOpt = webElementEvaluator.findElementBy(childWebElement, By.xpath("./.."));
      while (nextWebElementOpt.isPresent()
              && !findParentPredicate.test(nextWebElementOpt.get())) {
         nextWebElementOpt = webElementEvaluator.findElementBy(nextWebElementOpt.get(), By.xpath("./.."));
      }
      return nextWebElementOpt;
   }

   public void waitForVisibilityOfElement(By by, Duration duration) {
      WebDriverWait wait = new WebDriverWait(webDriver, duration);
      wait.until(ExpectedConditions.visibilityOfElementLocated(by));
   }

   public void waitForElementToBeClickable(WebElement webElement) {
      waitForElementToBeClickable(webElement, Duration.ofMillis(8000));
   }

   public void waitForElementToBeClickable(By by, Duration duration) {
      WebDriverWait wait = new WebDriverWait(webDriver, duration);
      wait.until(ExpectedConditions.elementToBeClickable(by));
   }

   public void waitForElementToBeClickable(WebElement webElement, Duration duration) {
      WebDriverWait wait = new WebDriverWait(webDriver, duration);
      wait.until(ExpectedConditions.elementToBeClickable(webElement));
   }

   public void waitForInvisibilityOfElement(WebElement webElement) {
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(4000));
      wait.until(ExpectedConditions.invisibilityOf(webElement));
   }

   public void waitForInvisibilityOfElementBy(By by) {
      this.waitForInvisibilityOfElementBy(by, 4000);
   }

   public void waitForInvisibilityOfElementBy(By by, long millis) {
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(millis));
      wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
   }

   public void executeClickButtonScript(WebElement webElement) {
      executeScript(SCROLL_INTO_VIEW_SCRIPT, webElement);
      executeScript(CLICK_BUTTON_SCRIPT, webElement);
   }

   protected void executeScript(String script, WebElement webElement) {
      JavascriptExecutor executor = (JavascriptExecutor) webDriver;
      executor.executeScript(script, webElement);
   }

   /**
    * Takes a screenshot of the current displayed browser and copies the file
    * to 'logs\name-of-file'
    *
    * @param fileSuffixIn the suffix which is appended to the created
    */
   public void takeScreenshot(String fileSuffixIn) {
      String fileSuffix = removeInvalidCharacters(fileSuffixIn);
      File screenshotFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
      try {
         String screenshotFileName = String.format(SCREENSHOT_SAVE_PATH + SCREENSHOT_NAME_PREFIX + fileSuffix + SCREENSHOT_FILE_TYPE, System.currentTimeMillis());
         FileUtils.copyFile(screenshotFile, new File(screenshotFileName));
      } catch (IOException e) {
         LOG.error("Error while saving a screenshot!", e);
      }
   }

   private static String removeInvalidCharacters(String fileSuffixIn) {
      return fileSuffixIn.replace("\n", "").replace(" ", "_").replace(".", "_").replace(":", "_");
   }
}