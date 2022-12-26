package projectManagement.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import projectManagement.controller.entities.UserDTO;
import projectManagement.controller.entities.UserLoginDTO;
import projectManagement.controller.entities.UserRequest;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {
    @InjectMocks
    Validation validation;

    private UserRequest goodUser;
    private UserRequest badEmailUser;
    private UserRequest badPasswordUser;
    private UserRequest badNameUser;



    @BeforeEach
    @DisplayName("Make sure we have all necessary items, good as new, for each test when it's called")
    void setUp() {
        goodUser = new UserRequest("asaf396@gmail.com", "dvir1234", "dvir shaul");
        badEmailUser = new UserRequest("Dvgmai.com", "dvir1234567", "dvir shaul");
        badPasswordUser = new UserRequest("Dvgmai@gmail.com", "4", "dviros");
        badNameUser = new UserRequest("Dvgmai@gmail.com", "dvir1234567", "1");
    }

    @Test
    void validateInputRegister_goodUser_Success() {
        assertTrue(Validation.validateInputRegister(goodUser));
    }
    @Test
    void validateInputRegister_badUserEmail_fail() {
        assertFalse(Validation.validateInputRegister(badEmailUser));
    }
    @Test
    void validateInputRegister_badUserPassword_fail() {
        assertFalse(Validation.validateInputRegister(badPasswordUser));
    }
    @Test
    void validateInputRegister_badUserName_fail() {
        assertFalse(Validation.validateInputRegister(badNameUser));
    }

    @Test
    void validateInputLogin_goodUser_Success() {
        assertTrue(Validation.validateInputLogin(goodUser));
    }
    @Test
    void validateInputLogin_badUserEmail_fail() {
        assertFalse(Validation.validateInputLogin(badEmailUser));
    }
    @Test
    void validateInputLogin_badUserPassword_fail() {
        assertFalse(Validation.validateInputLogin(badPasswordUser));
    }


    @Test
    void validate_validEmail_success() {
        String validEmail = "john@example.com";
        assertTrue(Validation.validate(Regex.EMAIL.getRegex(), validEmail));
    }
    @Test
    void validate_invalidEmail_fail() {
        String invalidEmail  = "john@invalid";
        assertFalse(Validation.validate(Regex.EMAIL.getRegex(), invalidEmail ));
    }
    @Test
    void validate_validName_success() {
        String validName = "John Smith";
        assertTrue(Validation.validate(Regex.NAME.getRegex(), validName));
    }
    @Test
    void validate_invalidName_fail() {
        String invalidName = "John1 Smith";
        assertFalse(Validation.validate(Regex.NAME.getRegex(), invalidName ));
    }
    @Test
    void validate_validPassword_success() {
        String validPassword = "password1";
        assertTrue(Validation.validate(Regex.PASSWORD.getRegex(), validPassword));
    }
    @Test
    void validate_invalidPassword_failNoNumber() {
        String invalidPassword = "password";
        assertFalse(Validation.validate(Regex.PASSWORD.getRegex(), invalidPassword ));
    }
    @Test
    void validate_invalidPassword_failNoLetter() {
        String invalidPassword = "12341234";
        assertFalse(Validation.validate(Regex.PASSWORD.getRegex(), invalidPassword ));
    }
    @Test
    void validate_invalidPassword_failTooShort() {
        String invalidPassword = "pass";
        assertFalse(Validation.validate(Regex.PASSWORD.getRegex(), invalidPassword ));
    }
    @Test
    void validate_invalidPassword_failSpecialChar() {
        String invalidPassword = "password1!";
        assertFalse(Validation.validate(Regex.PASSWORD.getRegex(), invalidPassword ));
    }
    @Test
    void validate_invalidData_fail() {
        assertFalse(Validation.validate(Regex.PASSWORD.getRegex(), null ));
    }

}