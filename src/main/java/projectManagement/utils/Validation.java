package projectManagement.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Board;
import projectManagement.entities.Item;
import projectManagement.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {


    private static Logger logger = LogManager.getLogger(Validation.class.getName());

    /**
     * validateWrongInputRegister gets called when a new user is register to the app
     * checks if we got all the parameters we need to make register.
     *
     * @param user - a user who trying register to the app.
     * @return - true if one of the parameters is wrong
     */
    public static boolean validateInputRegister(UserRequest user) {
        logger.info("in Validations -> validateInputRegister" + user.toString());
        return validate(Regex.EMAIL.getRegex(), user.getEmail()) &&
                validate(Regex.NAME.getRegex(), user.getName()) &&
                validate(Regex.PASSWORD.getRegex(), user.getPassword());
    }

    public static boolean validateInputLogin(UserRequest user) {
        logger.info("in Validations -> validateInputRegister" + user.toString());
        return validate(Regex.EMAIL.getRegex(), user.getEmail()) &&
                validate(Regex.PASSWORD.getRegex(), user.getPassword());
    }

    /**
     * validate is inner class function that called when we need to validate a given input such as email or password,
     * the validation process is according to enum regex we created.
     *
     * @param regex - the type we check on from email or password.
     * @param data  - the input to check on.
     */
    static boolean validate(String regex, String data) {
        logger.info("in Validations -> validate");
        if (data == null) {
            logger.error("in Validations -> validate -> data == null " + regex);
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    public static boolean validateItem(Item item) {
        return true;
    }

    public static boolean validateBoard(long boardId) {
        return true;
    }
}
