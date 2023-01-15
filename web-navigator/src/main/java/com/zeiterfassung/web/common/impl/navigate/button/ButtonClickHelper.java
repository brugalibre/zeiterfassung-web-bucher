package com.zeiterfassung.web.common.impl.navigate.button;

import com.zeiterfassung.web.common.impl.navigate.BaseWebNavigatorHelper;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

public class ButtonClickHelper {
   private final static Logger LOG = LoggerFactory.getLogger(ButtonClickHelper.class);
   private final BaseWebNavigatorHelper baseWebNavigatorHelper;

   public ButtonClickHelper(BaseWebNavigatorHelper baseWebNavigatorHelper) {
      this.baseWebNavigatorHelper = baseWebNavigatorHelper;
   }

   /**
    * Clicks the given, optional button as a {@link WebElement}
    * If the button is not yet available, the button is again retrieved, using the given {@link Supplier}
    * If there is an exception, the click with the button is also retried. If a {@link ElementClickInterceptedException}
    * occurs, javascript is used to click the button programmatically
    * <p>
    * This method should only be used when the webpage does not provide a robust way to click a button
    *
    * @param buttonWebElementSupplier a {@link Supplier} which provides the clickable button, if the optional button is not available
    * @param errorHandler             a {@link ButtonClickErrorHandler} in case there is an erro
    * @param elementIdentifier        the identifier of the button
    * @param retriesLeft              the amount of retries left
    */
   public void clickButtonOrHandleErrorRecursively(Supplier<Optional<WebElement>> buttonWebElementSupplier, ButtonClickErrorHandler errorHandler, String elementIdentifier, int retriesLeft) {
      try {
         Optional<WebElement> webElementButtonOptional = buttonWebElementSupplier.get();
         if (webElementButtonOptional.isPresent()) {
            WebElement buttonWebElement = webElementButtonOptional.get();
            LOG.debug("Button '{}' available. Waiting for it to become clickable", elementIdentifier);
            baseWebNavigatorHelper.waitForElementToBeClickable(buttonWebElement);
            buttonWebElement.click();
            LOG.debug("Button clicked '{}'", elementIdentifier);
         } else {
            LOG.warn("Button '{}' NOT available", elementIdentifier);
            doRetryIfPossible(buttonWebElementSupplier, errorHandler, elementIdentifier, retriesLeft);
         }
      } catch (StaleElementReferenceException e) {
         logError("StaleElementReferenceException: " + elementIdentifier, String.format("Button '%s' not attached to webpage!", elementIdentifier), e);
         doRetryIfPossible(buttonWebElementSupplier, errorHandler, elementIdentifier, retriesLeft);
      } catch (ElementClickInterceptedException e) {
         String errorMsg = String.format("Click on button '%s' intercepted! Using script instead of direct click", elementIdentifier);
         logError("ElementClickInterceptedException: " + elementIdentifier, errorMsg, e);
         try {
            LOG.debug("Try to get the button again, using the provided Supplier {}..", buttonWebElementSupplier);
            WebElement buttonWebElement = buttonWebElementSupplier.get().orElseThrow(() -> new IllegalStateException("Button for identifier '" + elementIdentifier + "'not present!"));
            baseWebNavigatorHelper.executeClickButtonScript(buttonWebElement);
            LOG.debug("Button successfully clicked with script");
         } catch (Exception clickScriptEx) {
            logError(clickScriptEx.getClass().getSimpleName() + ": " + elementIdentifier, String.format("Klick on button '%s' failed!", elementIdentifier), clickScriptEx);
            doRetryIfPossible(buttonWebElementSupplier, errorHandler, elementIdentifier, retriesLeft);
         }
      }
   }

   private void doRetryIfPossible(Supplier<Optional<WebElement>> buttonWebElementSupplier, ButtonClickErrorHandler errorHandler, String elementIdentifier, int retriesLeft) {
      retriesLeft--;
      if (retriesLeft > 0) {
         LOG.info("Do a retry. {} retries left", retriesLeft);
         clickButtonOrHandleErrorRecursively(buttonWebElementSupplier, errorHandler, elementIdentifier, retriesLeft - 1);
      } else {
         LOG.warn("No retries left, abort!");
         errorHandler.handleElementNotFound(elementIdentifier);
      }
   }

   private void logError(String context, String errorMsg, Exception e) {
      LOG.error(errorMsg, e);
      this.baseWebNavigatorHelper.takeScreenshot(context);
   }
}
