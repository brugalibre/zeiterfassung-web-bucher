package com.zeiterfassung.web.jira;

import com.zeiterfassung.web.common.book.BaseWebBooker;
import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.book.record.errorhandling.ErrorHandler;
import com.zeiterfassung.web.jira.navigator.JiraWebNavigator;
import com.zeiterfassung.web.jira.navigator.JiraWebNavigatorHelper;

public class JiraWebBooker extends BaseWebBooker<JiraWebNavigator, JiraWebNavigatorHelper> {
   protected JiraWebBooker(String userName, char[] userPassword, String propertiesName) {
      super(userName, userPassword, propertiesName);
   }

   @Override
   protected JiraWebNavigator createWebNavigator(String userName, char[] userPassword, String propertiesName) {
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
