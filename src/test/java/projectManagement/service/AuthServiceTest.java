package projectManagement.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import projectManagement.controller.AuthController;
import projectManagement.controller.entities.UserDTO;
import projectManagement.controller.entities.UserLoginDTO;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;
import projectManagement.utils.Provider;
import projectManagement.utils.Token;

import java.sql.SQLDataException;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepo userRepo;

    private User goodUser;
    private User badUser;

    private UserRequest userReq;

    @BeforeEach
    public void createUser() {
        goodUser = new User("test user","test@test.com", "abcd1234");
        badUser = new User("test user","test.com", "abcd1234");
    }

    @Test
    public void givenExistingEmail_whenRegisterUser_thenFailRegister() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        userReq = new UserRequest(goodUser.getEmail(),goodUser.getPassword());
        assertFalse(authService.register(userReq).isSucceed());
    }
    //    @Test
//    public void givenNewEmail_whenRegisterUser_thenRegister() {
//        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.empty());
//        userReq = new UserRequest(goodUser.getEmail(),goodUser.getPassword());
//        given(userRepo.save(goodUser)).willReturn(goodUser);
//        given(goodUser.getId()).willReturn(1L);
//        given(authService.createUser(userReq, Provider.LOCAL)).willReturn(new UserDTO(goodUser));
//        assertTrue(authService.register(userReq).isSucceed());
//    }
    @Test
    public void givenNewEmail_whenRegisterUser_thenFailRegisterDuringCreation() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.empty());
        userReq = new UserRequest(goodUser.getEmail(),goodUser.getPassword());
        assertFalse(authService.register(userReq).isSucceed());
    }

    @Test
    public void givenExistingEmail_whenLoginUser_thenLogin() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        userReq = new UserRequest(goodUser.getEmail(),goodUser.getPassword());
        assertTrue(authService.login(userReq).isSucceed());
    }
    @Test
    public void givenNotExistingEmail_whenLoginUser_thenDoNotLogin() {
        given(userRepo.findByEmail(badUser.getEmail())).willReturn(Optional.empty());
        userReq = new UserRequest(badUser.getEmail(),badUser.getPassword());
        assertFalse(authService.login(userReq).isSucceed());
    }

    @Test
    public void givenWrongPassword_whenLoginUser_thenDoNotLogin() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        goodUser.setProvider(Provider.LOCAL);
        userReq = new UserRequest(goodUser.getEmail(),"12341234");
        assertFalse(authService.login(userReq).isSucceed());
    }

    @Test
    public void givenExistingEmail_whenCreateUser_thenDoNotCreate(){
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        userReq = new UserRequest(goodUser.getEmail(),"12341234");
        assertThrows(IllegalArgumentException.class, () ->
                        authService.createUser(userReq,Provider.LOCAL),
                "Existing email didn't Throw error");
    }
//    @Test
//    public void givenUserId_whenGenerateToken_thenGenerate(){
//        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
//        userReq = new UserRequest(goodUser.getEmail(),"12341234");
//        given(Token.createJWT("1","hi","hi", Instant.now().toEpochMilli())).willReturn("1");
//        assertEquals("1",authService.generateToken(1));
//    }

}