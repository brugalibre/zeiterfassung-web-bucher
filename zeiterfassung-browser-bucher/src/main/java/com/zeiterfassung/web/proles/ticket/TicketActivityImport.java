package com.zeiterfassung.web.proles.ticket;

public class TicketActivityImport {
   private String activityName;
   private int activityCode;

   public TicketActivityImport(String activityName, int activityCode) {
      this.activityName = activityName;
      this.activityCode = activityCode;
   }

   @Override
   public String toString() {
      return "TicketActivityImport{" +
              "activityName='" + activityName + '\'' +
              ", activityCode=" + activityCode +
              '}';
   }

   public String getActivityName() {
      return activityName;
   }

   public long getActivityCode() {
      return activityCode;
   }
}
