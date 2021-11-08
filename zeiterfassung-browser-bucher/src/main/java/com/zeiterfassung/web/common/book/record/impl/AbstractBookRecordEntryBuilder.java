package com.zeiterfassung.web.common.book.record.impl;

import com.zeiterfassung.web.common.book.record.BookRecordEntry;

import static java.util.Objects.requireNonNull;

public abstract class AbstractBookRecordEntryBuilder<T extends BookRecordEntry, B extends AbstractBookRecordEntryBuilder<T, B>> {
   protected String externalId;
   protected String activity;
   protected String amountOfHours;
   protected String timeRepresentation;
   protected String date;
   protected String description;
   protected String descriptionShort;

   public B withExternalId(String externalId) {
      this.externalId = requireNonNull(externalId);
      return getThis();
   }

   public B withActivity(String activity) {
      this.activity = requireNonNull(activity);
      return getThis();
   }

   public B withAmountOfHours(String amountOfHours) {
      this.amountOfHours = requireNonNull(amountOfHours);
      return getThis();
   }

   public B withDate(String date) {
      this.date = requireNonNull(date);
      return getThis();
   }

   public B withTimeRepresentation(String timeRepresentation) {
      this.timeRepresentation = requireNonNull(timeRepresentation);
      return getThis();
   }

   public B withDescription(String description) {
      this.description = requireNonNull(description);
      return getThis();
   }

   public B withDescriptionShort(String descriptionShort) {
      this.descriptionShort = requireNonNull(descriptionShort);
      return getThis();
   }

   protected abstract B getThis();

   protected abstract T build();

   public static class BookRecordEntryBuilder extends AbstractBookRecordEntryBuilder<BookRecordEntry, BookRecordEntryBuilder> {

      @Override
      protected BookRecordEntryBuilder getThis() {
         return this;
      }

      @Override
      public BookRecordEntryImpl build() {
         return new BookRecordEntryImpl(date, timeRepresentation, amountOfHours, activity, description, externalId, descriptionShort);
      }
   }
}
