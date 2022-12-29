package projectManagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import projectManagement.controller.entities.UserDTO;
import projectManagement.controller.entities.UserLoginDTO;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.AuthService;
import projectManagement.utils.Provider;
import projectManagement.utils.Validation;

import java.net.URI;
import java.sql.SQLDataException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private UserRequest goodUser;
    private UserRequest badEmailUser;
    private UserRequest badPasswordUser;
    private UserRequest badNameUser;
    private UserDTO userDTO;
    private UserLoginDTO userLoginDTO;


    @BeforeEach
    @DisplayName("Make sure we have all necessary items, good as new, for each test when it's called")
    void setUp() {
        goodUser = new UserRequest("asaf396@gmail.com", "dvir1234", "dvir");

        badEmailUser = new UserRequest("Dvgmai.com", "dvir1234567", "dvir");
        badPasswordUser = new UserRequest("Dvgmai@gmail.com", "4", "dviros");
        badNameUser = new UserRequest("Dvgmai@gmail.com", "dvir1234567", "1");
    }


    @Test
    void register_badEmailUser_createFailureResponse(){
        assertEquals( 400, authController.register(badEmailUser).getStatusCodeValue(), "register with bad email user parameters did not return 400");
    }
    @Test
    void register_badPasswordUser_createFailureResponse(){
        assertEquals( 400, authController.register(badPasswordUser).getStatusCodeValue(), "register with bad email user parameters did not return 400");
    }

    @Test
    void login_goodUser_Successfully() {
        given(authService.login(goodUser)).willReturn(Response.createSuccessfulResponse(userLoginDTO));
        assertEquals(ResponseEntity.ok(Response.createSuccessfulResponse(userDTO)).getStatusCode(),
                authController.login(goodUser).getStatusCode(), "login with good user parameters did not return createSuccessfulResponse");
    }
    @Test
    void login_badUserEmail_createFailureResponse(){
        assertEquals( 400, authController.login(badEmailUser).getStatusCodeValue(), "test to login with a wrong password did not return errorCode 400");
    }
    @Test
    void login_badPasswordUser_createFailureResponse(){
        assertEquals( 400, authController.login(badPasswordUser).getStatusCodeValue(), "test to login with a wrong password did not return errorCode 400");
    }

    

}