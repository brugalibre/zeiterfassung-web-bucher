package com.zeiterfassung.web.common.book.record;

import com.zeiterfassung.web.common.book.record.errorhandling.ExceptionEntry;

import java.util.Optional;

public interface BookRecordEntry {

   /**
    * @return the activity of this {@link BookRecordEntry}
    */
   String getActivity();

   /**
    * @return the amount of worked hours
    */
   String getAmountOfHours();

   /**
    * @return the complete description of this {@link BookRecordEntry}
    */
   String getDescription();

   /**
    * @return the date as string at which date this {@link BookRecord} was recorded
    */
   String getDate();

   /**
    * @return an optional {@link ExceptionEntry}
    */
   Optional<ExceptionEntry> getExceptionEntry();

   /**
    * @return <code>true</code> if this {@link BookRecordEntry} was booked or <code>false</code>
    */
   boolean getIsBooked();

   /**
    * @return the id of a object from an external system
    */
   String getExternalId();

   /**
    * @return a short description of this {@link BookRecordEntry}
    */
   String getDescriptionShort();

   /**
    * marks this {@link BookRecordEntry} as booked
    */
   void flagAsBooked();

   /**
    * Adds an {@link ExceptionEntry} which occurred while this {@link BookRecordEntry} was booked
    *
    * @param exceptionEntry the {@link ExceptionEntry} to add
    */
   void setErrorEntry(ExceptionEntry exceptionEntry);
}
