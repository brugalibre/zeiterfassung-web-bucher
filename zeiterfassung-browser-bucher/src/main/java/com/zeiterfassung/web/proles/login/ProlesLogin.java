package com.zeiterfassung.web.proles.login;

import com.zeiterfassung.web.proles.constant.ProlesWebConst;
import com.zeiterfassung.web.proles.impl.navigate.ProlesWebNavigator;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class ProlesLogin extends ProlesWebNavigator {

   private ProlesLogin(String userName, char[] userPassword) {
      super(userName, userPassword, ProlesWebConst.PROLES_BOOKING_PROPERTIES_FILE_NAME);
   }

   /**
    * Creates and prepares a new {@link ProlesLogin}
    *
    * @param userName     the username
    * @param userPassword the user-password
    * @return a new {@link ProlesLogin}
    */
   public static ProlesLogin createProlesTicketExtractor(String userName, char[] userPassword) {
      ProlesLogin prolesLogin = new ProlesLogin(userName, userPassword);
      prolesLogin.initWebDriver();
      return prolesLogin;
   }

   /**
    * Tries a login.
    *
    * @return <code>true</code> if the login was successful or <code>false</code> if not
    */
   public boolean doLogin() {
      super.navigateToPageAndLogin();
      Optional<WebElement> buttonOpt = this.webNavigatorHelper.findWebElementById(ProlesWebConst.WEB_ELEMENT_ANMELDE_BUTTON);
      boolean isLoggedIn = buttonOpt.isEmpty(); // successful when button is not visible anymore!
      logout();
      return isLoggedIn;
   }
}
