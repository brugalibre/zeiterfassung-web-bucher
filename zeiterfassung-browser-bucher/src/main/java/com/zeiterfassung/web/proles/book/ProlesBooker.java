package com.zeiterfassung.web.proles.book;

import com.zeiterfassung.web.common.book.BaseWebBooker;
import com.zeiterfassung.web.common.book.record.BookRecord;
import com.zeiterfassung.web.common.book.record.BookRecordEntry;
import com.zeiterfassung.web.common.book.record.errorhandling.ErrorHandler;
import com.zeiterfassung.web.common.book.record.impl.DummyBookRecordBuilder;
import com.zeiterfassung.web.common.book.util.BookUtil;
import com.zeiterfassung.web.proles.book.record.ProlesBookRecordEntry;
import com.zeiterfassung.web.proles.constant.ProlesWebConst;
import com.zeiterfassung.web.proles.impl.navigate.ProlesWebNavigator;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.zeiterfassung.web.proles.constant.ProlesWebConst.*;

public class ProlesBooker extends BaseWebBooker<ProlesWebNavigator> {

   private static final Logger LOG = LoggerFactory.getLogger(ProlesBooker.class);

   private ProlesBooker(String userName, String userPassword) {
      super(userName, userPassword, ProlesWebConst.PROLES_BOOKING_PROPERTIES_FILE_NAME);
   }

   public static void main(String[] argv) {
      try {
         verifyArgs(argv);
         BookRecord bookedBookRecord = bookDummyBookRecord(argv[0], argv[1]);
         logBookingResult(bookedBookRecord);
      } catch (Exception e) {
         LOG.error("Error running ProlesBooker", e);
         e.printStackTrace();
      }
   }

   private static void verifyArgs(String[] argv) {
      if (argv.length < 1) {
         throw new IllegalStateException("You have to provide username and password in order to book");
      }
   }

   private static void logBookingResult(BookRecord bookedBookRecord) {
      for (BookRecordEntry bookerRecordEntry : bookedBookRecord.getBookerRecordEntries()) {
         LOG.info("Record '{}' was booked: {}", bookerRecordEntry, bookerRecordEntry.getIsBooked());
         System.out.println("Record '" + bookerRecordEntry + "' was booked: '" + bookerRecordEntry.getIsBooked() + "'");
      }
   }

   /**
    * Creates and prepares a new {@link ProlesBooker}
    *
    * @param userName     the username
    * @param userPassword the user-password
    * @return a new {@link ProlesBooker}
    */
   public static ProlesBooker createAndInitBooker(String userName, String userPassword) {
      ProlesBooker prolesBooker = new ProlesBooker(userName, userPassword);
      prolesBooker.initBooker();
      return prolesBooker;
   }

   public static BookRecord bookDummyBookRecord(String userName, String userPassword) {
      ProlesBooker booker = createAndInitBooker(userName, userPassword);
      return booker.bookRecords(DummyBookRecordBuilder.getDummyBookRecord());
   }

   @Override
   protected ProlesWebNavigator createWebNavigator(String userName, String userPassword, String propertiesName) {
      return new ProlesWebNavigator(userName, userPassword, propertiesName);
   }

   @Override
   protected void navigateToBookingPage(BookRecordEntry bookRecordEntry) {
      super.navigateToBookingPage(bookRecordEntry);
      enterDate(bookRecordEntry.getDate());
      baseWebNavigator.waitForElementWithId(WEB_ELEMENT_BOOK_TABLE_ID);
      BookUtil.waitForMiliseconds(300);// wait additionally time to make sure
   }

   @Override
   protected void bookSingleEntryInternal(BookRecordEntry prolesBookRecordEntry, ErrorHandler errorHandler) {
      String rowId = evalRowIdByCustomerProjectAndActivity((ProlesBookRecordEntry) prolesBookRecordEntry);
      enterHours(rowId, prolesBookRecordEntry.getAmountOfHours());
      enterDescription(rowId, prolesBookRecordEntry.getDescription());
      submitEntry(rowId);
      Optional<WebElement> messageboxOpt = baseWebNavigator.findWebElementById(WEB_ELEMENT_ERROR_MESSAGEBOX_ID);
      if (messageboxOpt.isPresent()) {
         WebElement messagebox = messageboxOpt.get();
         LOG.error("Error while booking record '{}': {}", prolesBookRecordEntry, messagebox.getText());
         errorHandler.handleError(messagebox.getText());
      } else {
         LOG.info("Booked entry with rowId '{}', amount of hours: '{}', with description '{}'", rowId, prolesBookRecordEntry.getAmountOfHours(), prolesBookRecordEntry.getDescription());
         prolesBookRecordEntry.flagAsBooked();
      }
   }

   @Override
   protected String getComponentAttrName() {
      return ProlesWebConst.AWAIT_HOUR_INPUT_FIELD_ATTR_NAME;
   }

   @Override
   protected void enterDescription(String id, String description) {
      super.enterDescription(ProlesWebConst.WEB_ELEMENT_DESCRIPTION_FIELD_ID_PREFIX + id, description);
   }

   @Override
   protected void submitEntry(String id) {
      super.submitEntry(ProlesWebConst.WEB_ELEMENT_DESCRIPTION_FIELD_ID_PREFIX + id);
   }

   private String evalRowIdByCustomerProjectAndActivity(ProlesBookRecordEntry prolesBookRecordEntry) {
      String customer = prolesBookRecordEntry.getCustomer();
      return this.baseWebNavigator.evalRowIdByCustomerProjectAndActivity(customer, prolesBookRecordEntry.getProject(), prolesBookRecordEntry.getActivity());
   }
}

