package com.zeiterfassung.web.proles.proles2json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zeiterfassung.web.proles.proles2json.log.ProlesLogger;
import com.zeiterfassung.web.proles.ticket.ProlesTicketImport;
import com.zeiterfassung.web.proles.ticket.ProlesTicketImports;
import com.zeiterfassung.web.proles.ticket.read.ProlesTicketExtractor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProlesTicketToJson {

   public static final String PROLES_TICKETS_JSON_FILE_NAME = "prolesTickets.json";

   public static void main(String[] args) {
      try {
         verifyArgs(args);
         ProlesLogger.log("ProlesTicketToJson started. Arguments '%s', '%s'", args[0], args[1]);
         String fileName = args.length == 3 ? args[2] : PROLES_TICKETS_JSON_FILE_NAME;
         ProlesTicketToJson prolesTicketToJson = new ProlesTicketToJson();
         prolesTicketToJson.readProlesTicketsAndWrite2Json(args[0], args[1], fileName);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static void verifyArgs(String[] args) {
      if (args.length < 2) {
         throw new IllegalStateException("No username and no password provided!\nusage: java -jar prolesTicketToJson.java <username> <userpassword>");
      }
   }

   private static ProlesTicketImports createProlesTicketImports(List<ProlesTicketImport> prolesTicketImports) {
      ProlesLogger.log("Read %s proles tickets", prolesTicketImports.size());
      ProlesTicketImports prolesTicketImportsObject = new ProlesTicketImports();
      prolesTicketImportsObject.setProlesTicketImports(prolesTicketImports.toArray(new ProlesTicketImport[]{}));
      return prolesTicketImportsObject;
   }

   public void readProlesTicketsAndWrite2Json(String username, String password, String fileName) throws IOException {
      ProlesLogger.log("Create ProlesTicketExtractor..");
      ProlesTicketExtractor prolesTicketExtractor = ProlesTicketExtractor.createProlesTicketExtractor(username, password);
      ProlesTicketImports prolesTicketImportsObject = createProlesTicketImports(prolesTicketExtractor.extractProlesTickets());
      writ2Json(prolesTicketImportsObject, fileName);
   }

   private void writ2Json(ProlesTicketImports prolesTicketImportsObject, String fileName) throws IOException {
      ProlesLogger.log("Write to json file '%s'", fileName);
      ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
      objectMapper.writeValue(new File(fileName), prolesTicketImportsObject);
   }
}
