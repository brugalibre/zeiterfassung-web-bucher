package com.zeiterfassung.web.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface WebElementEvaluator {
   List<WebElement> findWebElementByTagNameAndTextValue(WebElement searchContext, String tagName, String expectedTextValue);

   List<WebElement> findWebElementByNameAndValueInternal(WebElement searchContext, String tagName, String expectedWebElementValue, Function<WebElement, String> webElementValueProvider);

   List<WebElement> findWebElementByFilterAndValueInternal(WebElement searchContext, By by, String expectedWebElementValue, Function<WebElement, String> webElementValueProvider);

   /**
    * Tries to find an {@link WebElement} with the given id. If no such elemnet is found, an empty Optional is returned.
    * Otherwise an {@link java.util.Optional} contained the found {@link WebElement}
    *
    * @param id the {@link By} in order to locate the element
    * @return an empty {@link java.util.Optional} or an {@link java.util.Optional} containing the desired {@link WebElement}
    */
   Optional<WebElement> findElement(By id);

   /**
    * Returns a {@link WebElement} for the given {@link By} or throws a {@link java.util.NoSuchElementException} if there is no such
    * element
    *
    * @param id the {@link By} in order to locate the element
    * @return the {@link WebElement}
    */
   WebElement getElement(By id);
}
