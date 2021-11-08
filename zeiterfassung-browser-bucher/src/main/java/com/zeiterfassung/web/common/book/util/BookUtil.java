package com.zeiterfassung.web.common.book.util;

public class BookUtil {

   private BookUtil() {
      // Private
   }

   /**
    * Waits the given amount of miliseconds. If a {@link InterruptedException} occures, this is thrown as a {@link IllegalStateException}
    *
    * @param timeInMilis the time to wait
    */
   public static void waitForMiliseconds(int timeInMilis) {
      try {
         Thread.sleep(timeInMilis);
      } catch (InterruptedException e) {
         throw new IllegalStateException(e);
      }
   }
}
