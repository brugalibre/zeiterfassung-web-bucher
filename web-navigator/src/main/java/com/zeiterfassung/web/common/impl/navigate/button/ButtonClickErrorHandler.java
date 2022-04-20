package com.zeiterfassung.web.common.impl.navigate.button;

import org.openqa.selenium.WebElement;

/**
 * The {@link ButtonClickErrorHandler} handles errors when a button is not found
 * for a given identifier
 */
public interface ButtonClickErrorHandler {
   /**
    * Is called when the button for the given identifier was not found
    *
    * @param elementIdentifier the identifier of a {@link WebElement}
    */
   void handleElementNotFound(String elementIdentifier);
}
