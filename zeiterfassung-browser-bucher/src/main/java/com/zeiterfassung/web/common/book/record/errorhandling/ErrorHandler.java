package com.zeiterfassung.web.common.book.record.errorhandling;


import com.zeiterfassung.web.common.book.record.BookRecordEntry;

/**
 * The {@link ErrorHandler} handles errors which occurs during booking of a {@link BookRecordEntry}
 */
@FunctionalInterface
public interface ErrorHandler {

   /**
    * Handles an error during booking of the given {@link BookRecordEntry}
    *
    * @param errorMsg the error message
    * @return an {@link ExceptionEntry} which contains detailed information about the handled error
    */
   ExceptionEntry handleError(String errorMsg);
}
