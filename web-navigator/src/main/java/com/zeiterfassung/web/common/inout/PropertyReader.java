package com.zeiterfassung.web.common.inout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class PropertyReader {

   private static final Logger LOG = LoggerFactory.getLogger(PropertyReader.class);
   private final String propertiesName;

   public PropertyReader(String propertiesName) {
      this.propertiesName = requireNonNull(propertiesName);
   }

   private static String readValue(String propKey, String propFile) {
      try (InputStream resourceStream = getInputStream(propFile)) {
         return readValue(resourceStream, propKey);
      } catch (IOException e) {
         LOG.error("Unable to read value for key '" + propKey + "' in property file '" + propFile + "'", e);
      }
      throw new IllegalStateException("No value read for key '" + propKey + "'");
   }

   private static String readValue(InputStream resourceStream, String propKey) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      return (String) prop.get(propKey);
   }

   private static InputStream getInputStream(String propertiesName) throws FileNotFoundException {
      InputStream resourceStreamFromResource = PropertyReader.class.getClassLoader().getResourceAsStream(propertiesName);
      if (isNull(resourceStreamFromResource)) {
         return new FileInputStream(propertiesName);
      }
      return resourceStreamFromResource;
   }

   /**
    * Reads the value for the given properties-key
    *
    * @param propKey the key of the properties to read
    * @return the value for the given properties-key
    */
   public String readValue(String propKey) {
      return readValue(propKey, propertiesName);
   }

   /**
    * Reads the value for the given properties-key
    *
    * @param propKey      the key of the properties to read
    * @param defaultValue the default value which is returned, when there is no value associated for the given key
    * @return the value for the given properties-key
    */
   public String readValueOrDefault(String propKey, String defaultValue) {
      String value = readValue(propKey, propertiesName);
      if (isNull(value)) {
         return defaultValue;
      }
      return value;
   }
}
