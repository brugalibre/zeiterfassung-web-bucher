package com.zeiterfassung.web.proles.proles2json.log;

public class ProlesLogger {

   private ProlesLogger() {
      // privé
   }

   public static void log(String msg, Object... args) {
      System.out.printf(msg + "%n", args);
   }
}
