package com.zeiterfassung.web.jira.constant;

public class JiraWebConstant {

   // Login
   public static final String USER_PASSWORD_INPUT_FIELD_ID = "login-form-password";
   public static final String USER_NAME_INPUT_FIELD_ID = "login-form-username";
   public static final String LOGIN_SUBMIT_BUTTON_ID = "login-form-submit";

   // Booking
   public static final String MORE_OPERATIONS_DROPDOWN_ID = "opsbar-operations_more";
   public static final String LOG_TIME_MENU_ITEM_ID = "add-hours-on-issue";
   /** Id of the element for entering the date, note: format is dd/mmm/yyyy. e.g. 14/Jan*/
   public static final String DATE_INPUT_FIELD_ID = "started";
   public static final String DESCRIPTION_INPUT_FIELD_ID = "comment";
   public static final String AMOUNT_OF_HOURS_INPUT_FIELD_ID = "timeSpentSeconds";

   private JiraWebConstant(){
      // privé
   }
}
