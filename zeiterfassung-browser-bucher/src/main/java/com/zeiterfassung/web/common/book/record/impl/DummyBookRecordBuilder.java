package com.zeiterfassung.web.common.book.record.impl;

import com.zeiterfassung.web.common.book.record.BookRecord;
import com.zeiterfassung.web.proles.book.record.ProlesBookRecordEntry;
import com.zeiterfassung.web.proles.book.record.ProlesBookRecordEntry.ProlesBookRecordEntryBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DummyBookRecordBuilder {

   private DummyBookRecordBuilder() {
      // private
   }

   public static BookRecord getDummyBookRecord() {
      String tomorrowAsString = LocalDate.now()
              .minusDays(1)
              .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
      return BookRecordImpl.BookRecordBuilder.of()
              .addBookRecordEntry(createAusbildungCamundaEntry(tomorrowAsString, "3", "leeeeernen"))
              .addBookRecordEntry(createNxtEntwicklungEntry(tomorrowAsString, "2", "schaffen am nxt"))
              .build();
   }

   private static ProlesBookRecordEntry createAusbildungCamundaEntry(String date, String amountOfHours, String description) {
      return ProlesBookRecordEntryBuilder.of()
              .withCustomer("nag")
              .withDate(date)
              .withProject("Ausbildung")
              .withActivity("Interne Ausbildung")
              .withAmountOfHours(amountOfHours)
              .withDescription(description)
              .build();
   }

   private static ProlesBookRecordEntry createNxtEntwicklungEntry(String date, String amountOfHours, String description) {
      return ProlesBookRecordEntryBuilder.of()
              .withCustomer("nag")
              .withDate(date)
              .withProject("nxT")
              .withActivity("Entwicklung")
              .withAmountOfHours(amountOfHours)
              .withDescription(description)
              .build();
   }
}
