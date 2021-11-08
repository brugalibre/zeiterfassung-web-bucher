package com.zeiterfassung.web.common.book.record.impl;

import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.book.record.errorhandling.ExceptionEntry;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class BookRecordEntryImpl implements BookRecordEntry {
   private String id;
   private String externalId;
   private String amountOfHours;
   private String date;
   private String timeRepresentation;
   private String activity;
   private String description;
   private boolean isBooked;
   private String descriptionShort;
   private ExceptionEntry exceptionEntry;

   public BookRecordEntryImpl(String date, String timeRepresentation, String amountOfHours, String activity, String description, String externalId, String descriptionShort) {
      if (isNull(timeRepresentation) && isNull(amountOfHours)) {
         throw new NullPointerException("Either a 'from - upto'-representation or the amount of hours are required!");
      }
      this.externalId = externalId;
      this.timeRepresentation = timeRepresentation;
      this.amountOfHours = amountOfHours;
      this.date = requireNonNull(date);
      this.descriptionShort = requireNonNull(descriptionShort);
      this.description = requireNonNull(description);
      this.activity = requireNonNull(activity);
      this.id = UUID.randomUUID().toString();
   }

   @Override
   public String toString() {
      return "BookRecordEntryImpl{" +
              "id='" + id + '\'' +
              getAttrToString() +
              '}';
   }

   protected String getAttrToString() {
      return ", date='" + date + '\'' +
              ", externalId='" + externalId + '\'' +
              ", amountOfHours='" + amountOfHours + '\'' +
              ", activity='" + activity + '\'' +
              ", timeRepresentation='" + timeRepresentation + '\'' +
              ", description='" + description + '\'' +
              ", isBooked=" + isBooked +
              ", descriptionShort='" + descriptionShort + '\'' +
              ", exceptionEntry=" + exceptionEntry;
   }

   @Override
   public String getDate() {
      return date;
   }

   @Override
   public String getActivity() {
      return activity;
   }

   @Override
   public String getAmountOfHours() {
      return amountOfHours;
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public String getDescriptionShort() {
      return descriptionShort;
   }

   @Override
   public String getExternalId() {
      return externalId;
   }

   @Override
   public Optional<ExceptionEntry> getExceptionEntry() {
      return Optional.ofNullable(exceptionEntry);
   }

   @Override
   public boolean getIsBooked() {
      return isBooked;
   }

   @Override
   public void setErrorEntry(ExceptionEntry exceptionEntry) {
      this.exceptionEntry = exceptionEntry;
   }

   @Override
   public void flagAsBooked() {
      this.isBooked = true;
   }

}
