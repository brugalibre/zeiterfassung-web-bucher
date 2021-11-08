package com.zeiterfassung.web.proles.book.record;

import com.zeiterfassung.web.common.book.record.impl.AbstractBookRecordEntryBuilder;
import com.zeiterfassung.web.common.book.record.impl.BookRecordEntryImpl;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class ProlesBookRecordEntry extends BookRecordEntryImpl {

   private String customer;
   private String project;

   private ProlesBookRecordEntry(String date, String timeRepresentation, String amountOfHours, String customer, String project, String activity, String description, String externalId, String descriptionShort) {
      super(date, timeRepresentation, amountOfHours, activity, description, externalId, descriptionShort);
      this.customer = requireNonNull(customer);
      this.project = requireNonNull(project);
   }

   @Override
   public String toString() {
      return "ProlesBookRecordEntry{" +
              super.getAttrToString() +
              "customer='" + customer + '\'' +
              ", project='" + project + '\'' +
              '}';
   }

   public String getCustomer() {
      return customer;
   }

   public String getProject() {
      return project;
   }

   public static class ProlesBookRecordEntryBuilder extends AbstractBookRecordEntryBuilder<ProlesBookRecordEntry, ProlesBookRecordEntryBuilder> {

      private String project;
      private String customer;

      private ProlesBookRecordEntryBuilder() {
         super();
      }

      public static ProlesBookRecordEntryBuilder of() {
         return new ProlesBookRecordEntryBuilder();
      }

      public ProlesBookRecordEntryBuilder withProject(String project) {
         this.project = requireNonNull(project);
         return this;
      }

      public ProlesBookRecordEntryBuilder withCustomer(String customer) {
         this.customer = requireNonNull(customer);
         return this;
      }

      @Override
      protected ProlesBookRecordEntryBuilder getThis() {
         return this;
      }

      @Override
      public ProlesBookRecordEntry build() {
         if (isNull(descriptionShort)) {
            this.descriptionShort = "Customer = '" + customer + "', project = '" + project + "', activity = '" + activity + "'";
         }
         return new ProlesBookRecordEntry(date, timeRepresentation, amountOfHours, customer, project, activity, description, externalId, descriptionShort);
      }
   }
}
