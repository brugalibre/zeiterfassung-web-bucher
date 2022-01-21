package com.zeiterfassung.web.jira;

import com.zeiterfassung.web.common.book.BaseWebBooker;
import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.book.record.errorhandling.ErrorHandler;
import com.zeiterfassung.web.jira.navigator.JiraWebNavigator;

public class JiraWebBooker extends BaseWebBooker<JiraWebNavigator> {
   protected JiraWebBooker(String userName, String userPassword, String propertiesName) {
      super(userName, userPassword, propertiesName);
   }

   @Override
   protected JiraWebNavigator createWebNavigator(String userName, String userPassword, String propertiesName) {
      return new JiraWebNavigator(userName, userPassword, propertiesName);
   }

   @Override
   protected void bookSingleEntryInternal(BookRecordEntry prolesBookRecordEntry, ErrorHandler errorHandler) {

   }

   @Override
   protected String getComponentAttrName() {
      return null;
   }
}
