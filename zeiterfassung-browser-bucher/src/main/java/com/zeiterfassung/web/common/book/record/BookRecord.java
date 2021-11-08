package com.zeiterfassung.web.common.book.record;

import java.util.List;

/**
 * The {@link BookRecord} contains one or more bookable element, so called {@link BookRecordEntry} and a date,
 * at which the booking takes place
 */
public interface BookRecord {

   /**
    * @return all {@link BookRecordEntry} of this {@link BookRecord}
    */
   List<BookRecordEntry> getBookerRecordEntries();
}
