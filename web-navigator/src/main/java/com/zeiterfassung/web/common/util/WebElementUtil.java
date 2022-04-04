package com.zeiterfassung.web.common.util;

import org.openqa.selenium.WebElement;

import java.util.List;

public class WebElementUtil {

   private WebElementUtil() {
      // private
   }

   /**
    * Maps all {@link WebElement}s to their String representation and combines all those Strings to one single line
    *
    * @param webElements all the web-elements to combine
    * @return a single String representing the given list of {@link WebElement}s
    */
   public static String appendWebElements2String(List<WebElement> webElements) {
      return webElements.stream()
              .map(WebElementUtil::getWebElementString)
              .reduce("", (prevWebElementString, nextWebElementString) -> appendStrings(prevWebElementString, nextWebElementString));
   }

   private static String appendStrings(String prevWebElementString, String nextWebElementString) {
      if (prevWebElementString.isEmpty()) {
         return nextWebElementString;
      }
      return prevWebElementString + (nextWebElementString.isEmpty() ? "" : ", " + nextWebElementString);
   }

   /**
    * Returns the string representation of the given {@link WebElement}
    *
    * @param webElement the given {@link WebElement}
    * @return the string representation of the given {@link WebElement}
    */
   public static String getWebElementString(WebElement webElement) {
      return "'tag-name: '" + webElement.getTagName() + "', id: '" +
              webElement.getAttribute("id") + "'";
   }
}
