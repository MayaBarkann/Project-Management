package projectManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import projectManagement.controller.AuthController;
import projectManagement.controller.entities.UserDTO;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.utils.Provider;

import java.sql.SQLDataException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class AuthServiceTest {


    @Test
    void save() {
    }

    @Test
    void login() {
    }

    @Test
    void checkTokenToUserInDB() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void loginGithub() {
    }

    @Test
    void getGithubToken() {
    }

    @Test
    void getGithubUser() {
    }
}