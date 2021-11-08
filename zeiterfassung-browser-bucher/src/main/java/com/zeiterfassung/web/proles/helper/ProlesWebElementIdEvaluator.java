package com.zeiterfassung.web.proles.helper;

import com.zeiterfassung.web.common.exception.NoSuchIdException;
import org.openqa.selenium.WebElement;

public interface ProlesWebElementIdEvaluator {

   /**
    * Evaluates the id of an unique row, defined by a customer, project-name and an activity
    *
    * @param searchContext the {@link WebElement} in which the id is searched.
    * @param customer      the name of the customer
    * @param projectName   the name of the project
    * @param activity      the name of the activity
    * @return the found row-id
    * @throws NoSuchIdException if there is no id found for the given parameters
    */
   String evalRowIdByCustomerProjectAndActivity(WebElement searchContext, String customer, String projectName, String activity);
}
