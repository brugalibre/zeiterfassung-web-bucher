package com.zeiterfassung.web.common.book.record.errorhandling;

import static java.util.Objects.requireNonNull;

public class ExceptionEntry {
   private String errorMsg;

   private ExceptionEntry(String errorMsg, String bookEntryDescription) {
      this.errorMsg = "An error occurred while booking entry '" + requireNonNull(bookEntryDescription) + "':\n" + errorMsg;
   }

   public static ExceptionEntry of(String errorMsg, String bookEntryDescription) {
      return new ExceptionEntry(errorMsg, bookEntryDescription);
   }

   @Override
   public String toString() {
      return "ExceptionEntry{" +
              ", exceptionMsg='" + errorMsg + '\'' +
              '}';
   }

   public String getErrorMsg() {
      return errorMsg;
   }
}
