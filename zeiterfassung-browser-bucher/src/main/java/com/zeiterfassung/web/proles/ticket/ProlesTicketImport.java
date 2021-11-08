package com.zeiterfassung.web.proles.ticket;

import java.util.List;

public class ProlesTicketImport {
   public static final String CUSTOMER_PROJECT_CONCAT = "-";
   private static final CharSequence WHITE_SPACE_REPLACEMENT = CUSTOMER_PROJECT_CONCAT;
   private String customer;
   private String ticketNr;
   private String description;
   private String project;
   private List<TicketActivityImport> ticketActivities;

   public ProlesTicketImport(String customer, String project, List<TicketActivityImport> ticketActivities) {
      this.customer = customer;
      this.project = project;
      this.ticketNr = evalTicketNr();
      this.description = ticketActivities.stream()
              .map(TicketActivityImport::getActivityName)
              .reduce("", ProlesTicketImport::concatActivities);
      this.ticketActivities = ticketActivities;
   }

   private static String concatActivities(String prevName, String nextName) {
      // First entry
      if (prevName == null || prevName.length() == 0) {
         return nextName;
      }
      // Entry in between
      return prevName + ", " + nextName;
   }

   private String evalTicketNr() {
      return replaceWhiteSpace(customer) + CUSTOMER_PROJECT_CONCAT + replaceWhiteSpace(project);
   }

   private String replaceWhiteSpace(String string) {
      return string.replace(" ", WHITE_SPACE_REPLACEMENT)
              .replace("\t", WHITE_SPACE_REPLACEMENT);
   }

   @Override
   public String toString() {
      return "ProlesTicketImport{" +
              "customer='" + customer + '\'' +
              ", ticketNr='" + ticketNr + '\'' +
              ", description='" + description + '\'' +
              ", project='" + project + '\'' +
              ", ticketActivities=" + ticketActivities +
              '}';
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getTicketNr() {
      return ticketNr;
   }

   public void setTicketNr(String ticketNr) {
      this.ticketNr = ticketNr;
   }

   public String getCustomer() {
      return customer;
   }

   public void setCustomer(String customer) {
      this.customer = customer;
   }

   public String getProject() {
      return project;
   }

   public void setProject(String project) {
      this.project = project;
   }

   public List<TicketActivityImport> getTicketActivities() {
      return ticketActivities;
   }

   public void setTicketActivities(List<TicketActivityImport> ticketActivities) {
      this.ticketActivities = ticketActivities;
   }

}