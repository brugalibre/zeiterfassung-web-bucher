package com.zeiterfassung.web.common.book.record.impl;

import com.zeiterfassung.web.common.book.record.BookRecord;
import com.zeiterfassung.web.common.book.record.BookRecordEntry;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class BookRecordImpl implements BookRecord {

   private List<BookRecordEntry> bookRecordEntries;

   private BookRecordImpl(List<BookRecordEntry> bookRecordEntries) {
      this.bookRecordEntries = bookRecordEntries;
   }

   @Override
   public String toString() {
      return "BookRecordImpl{" +
              "bookRecordEntries=" + bookRecordEntries +
              '}';
   }

   @Override
   public List<BookRecordEntry> getBookerRecordEntries() {
      return bookRecordEntries;
   }

   public static class BookRecordBuilder {
      private List<BookRecordEntry> bookRecordEntries;

      private BookRecordBuilder() {
         this.bookRecordEntries = new ArrayList<>();
      }

      public static BookRecordBuilder of() {
         return new BookRecordBuilder();
      }

      public BookRecordBuilder addBookRecordEntry(BookRecordEntry bookRecordEntries) {
         requireNonNull(bookRecordEntries);
         this.bookRecordEntries.add(bookRecordEntries);
         return this;
      }

      public BookRecordBuilder withBookRecordEntries(List<BookRecordEntry> bookRecordEntries) {
         this.bookRecordEntries = requireNonNull(bookRecordEntries);
         return this;
      }

      public BookRecordImpl build() {
         return new BookRecordImpl(bookRecordEntries);
      }
   }
}
