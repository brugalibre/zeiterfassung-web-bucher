package com.zeiterfassung.web.proles.ticket.read;

import com.zeiterfassung.web.common.inout.PropertyReader;
import com.zeiterfassung.web.proles.impl.navigate.ProlesWebNavigator;
import com.zeiterfassung.web.proles.ticket.ProlesTicketImport;
import com.zeiterfassung.web.proles.ticket.TicketActivityImport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zeiterfassung.web.common.constant.BaseWebConst.HTML_TAG_SPAN;
import static com.zeiterfassung.web.common.constant.BaseWebConst.HTML_TAG_TABLE_TR;
import static com.zeiterfassung.web.proles.constant.ProlesWebConst.*;
import static java.util.Objects.nonNull;

public class ProlesTicketExtractor extends ProlesWebNavigator {

   /*
    * Since we use the activity names as key, to retrieve the activity code, we need to 'escape' white space
    * Otherwise we can't read from the properties file
    * */
   private static final String WHITE_SPACE_REPLACEMENT = "_";
   private static final String WHITE_SPACE = " ";

   public ProlesTicketExtractor(String userName, String userPassword) {
      super(userName, userPassword, PROLES_BOOKING_PROPERTIES_FILE_NAME);
   }

   public static void main(String[] args) {
      createProlesTicketExtractor(args[0], args[1]).extractProlesTickets();
   }

   /**
    * Creates and prepares a new {@link com.zeiterfassung.web.proles.ticket.read.ProlesTicketExtractor}
    *
    * @param userName     the username
    * @param userPassword the user-password
    * @return a new {@link com.zeiterfassung.web.proles.ticket.read.ProlesTicketExtractor}
    */
   public static com.zeiterfassung.web.proles.ticket.read.ProlesTicketExtractor createProlesTicketExtractor(String userName, String userPassword) {
      ProlesTicketExtractor prolesTicketExtractor = new ProlesTicketExtractor(userName, userPassword);
      prolesTicketExtractor.initWebDriver();
      return prolesTicketExtractor;
   }

   /**
    * Extracts all proles tickets directly from the proles page and maps them into a {@link ProlesTicketImport}
    *
    * @return a list of {@link ProlesTicketImport}
    */
   public List<ProlesTicketImport> extractProlesTickets() {
      login();
      navigateToBookingPage(null);
      waitForElementWithId(WEB_ELEMENT_BOOK_TABLE_ID);
      BookableTableContent bookableTableContent = readTableAndCreateBookableTableContent();
      List<ProlesTicketImport> prolesTicketImport = createProlesTicketImport(bookableTableContent);
      logout();
      return prolesTicketImport;
   }

   private static List<ProlesTicketImport> createProlesTicketImport(BookableTableContent bookableTableContent) {
      List<ProlesTicketImport> prolesTicketImports = new ArrayList<>();
      for (String customer : bookableTableContent.customer2ProjectsMap.keySet()) {
         for (String project : bookableTableContent.customer2ProjectsMap.get(customer)) {
            List<TicketActivityImport> ticketActivities = map2TicketActivities(bookableTableContent.projects2ActivitiesMap.get(project));
            prolesTicketImports.add(new ProlesTicketImport(customer, project, ticketActivities));
         }
      }
      return prolesTicketImports;
   }

   private static List<TicketActivityImport> map2TicketActivities(List<String> activities) {
      PropertyReader propertyReader = new PropertyReader(PROLES_TICKET_ACTIVITIES_PROPERTIES_FILE_NAME);
      return activities.stream()
              .map(toTicketActivityImport(propertyReader))
              .collect(Collectors.toList());
   }

   private static Function<String, TicketActivityImport> toTicketActivityImport(PropertyReader propertyReader) {
      return activity -> {
         int activityCode = Integer.parseInt(propertyReader.readValue(activity.replace(WHITE_SPACE, WHITE_SPACE_REPLACEMENT)));
         return new TicketActivityImport(activity, activityCode);
      };
   }

   private static void put2Map(Map<String, List<String>> key2ValuesMap, String key, String value) {
      if (key2ValuesMap.containsKey(key)) {
         List<String> values = key2ValuesMap.get(key);
         if (!values.contains(value)) {
            values.add(value);
         }
      } else {
         List<String> values = new ArrayList<>();
         values.add(value);
         key2ValuesMap.put(key, values);
      }
   }

   private static List<String> evalTableRowSpanElementValues(WebElement tableRowElement) {
      return tableRowElement.findElements(new By.ByTagName(HTML_TAG_SPAN))
              .stream()
              .map(WebElement::getText)
              .collect(Collectors.toList());
   }

   /*
    *  Only the bookable elements have ids. The header-row e.g. doesn't have one
    */
   private static List<WebElement> findBookableTableRowElements(WebElement tblforminfoTable) {
      return tblforminfoTable.findElements(new By.ByTagName(HTML_TAG_TABLE_TR))
              .stream()
              .filter(tableRowElement -> nonNull(tableRowElement.getAttribute(WEB_ELEMENT_DATA_ROW_ID)))
              .collect(Collectors.toList());
   }

   /**
    * Reads the table with all bookable customer, project & activities
    * All projects are mapped to a customer as well as all activities are mapped to a project
    */
   private BookableTableContent readTableAndCreateBookableTableContent() {
      BookableTableContent readBookableTableContent = new BookableTableContent();
      List<WebElement> tableRowElements = findBookableTableRowElements();
      for (WebElement tableRowElement : tableRowElements) {
         List<String> tableRowSpanElements = evalTableRowSpanElementValues(tableRowElement);
         assert tableRowSpanElements.size() == 3;
         put2Map(readBookableTableContent.customer2ProjectsMap, tableRowSpanElements.get(0), tableRowSpanElements.get(1));
         put2Map(readBookableTableContent.projects2ActivitiesMap, tableRowSpanElements.get(1), tableRowSpanElements.get(2));
      }
      return readBookableTableContent;
   }

   private List<WebElement> findBookableTableRowElements() {
      WebElement tblforminfoTable = webNavigatorHelper.getElement(By.id(WEB_ELEMENT_BOOK_TABLE_ID));
      return findBookableTableRowElements(tblforminfoTable);
   }

   /**
    * The result of reading the table with id {@link com.zeiterfassung.web.proles.constant.ProlesWebConst#WEB_ELEMENT_BOOK_TABLE_ID}
    * customer2ProjectsMap:   the map with customer and it's projects
    * projects2ActivitiesMap: the map with a project and it's activities
    */
   private static class BookableTableContent {
      Map<String, List<String>> customer2ProjectsMap = new HashMap<>();
      Map<String, List<String>> projects2ActivitiesMap = new HashMap<>();
   }
}
