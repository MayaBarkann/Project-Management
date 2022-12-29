package projectManagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import projectManagement.controller.AuthController;
import projectManagement.controller.entities.UserDTO;
import projectManagement.controller.entities.UserLoginDTO;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;
import projectManagement.utils.Provider;
import projectManagement.utils.Token;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.SQLDataException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserRepo userRepo;

    private User goodUser;
    private User badUser;

    private UserRequest userReq;

    @BeforeEach
    public void createUser() {
        goodUser = new User("test user", "test@test.com", "abcd1234");
        badUser = new User("test user", "test.com", "abcd1234");
        authService.userToToken = new HashMap<>();
    }

    @Test
    public void givenExistingEmail_whenRegisterUser_thenFailRegister() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        userReq = new UserRequest(goodUser.getEmail(), goodUser.getPassword());
        assertFalse(authService.register(userReq).isSucceed());
    }


    @Test
    public void givenNewEmail_whenRegisterUser_thenFailRegisterDuringCreation() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.empty());
        userReq = new UserRequest(goodUser.getEmail(), goodUser.getPassword());
        assertFalse(authService.register(userReq).isSucceed());
    }

    @Test
    public void givenExistingEmail_whenLoginUser_thenLogin() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        userReq = new UserRequest(goodUser.getEmail(), goodUser.getPassword());
        given(bCryptPasswordEncoder.matches(userReq.getPassword(),goodUser.getPassword())).willReturn(true);
        assertTrue(authService.login(userReq).isSucceed());
    }

    @Test
    public void givenNotExistingEmail_whenLoginUser_thenDoNotLogin() {
        given(userRepo.findByEmail(badUser.getEmail())).willReturn(Optional.empty());
        userReq = new UserRequest(badUser.getEmail(), badUser.getPassword());
        assertFalse(authService.login(userReq).isSucceed());
    }

    @Test
    public void givenWrongPassword_whenLoginUser_thenDoNotLogin() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        goodUser.setProvider(Provider.LOCAL);
        userReq = new UserRequest(goodUser.getEmail(), "12341234");
        given(bCryptPasswordEncoder.matches(userReq.getPassword(),goodUser.getPassword())).willReturn(false);
        assertFalse(authService.login(userReq).isSucceed());
    }

    @Test
    public void givenExistingEmailGithubProvider_whenLoginUser_thenDoNotLogin() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        goodUser.setProvider(Provider.GITHUB);
        userReq = new UserRequest(goodUser.getEmail(), "12341234");
        assertFalse(authService.login(userReq).isSucceed());
    }

    @Test
    public void givenExistingEmail_whenCreateUser_thenDoNotCreate() {
        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        userReq = new UserRequest(goodUser.getEmail(), "12341234");
        assertThrows(IllegalArgumentException.class, () ->
                        authService.createUser(userReq, Provider.LOCAL),
                "Existing email didn't Throw error");
    }
    @Test
    public void givenExistingEmail_whenGetUserByEmail_thenGetUser() {
        given(authService.getUserByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
        assertNotNull(authService.getUserByEmail(goodUser.getEmail()));
    }
    @Test
    public void givenNoneExistingEmail_whenGetUserByEmail_thenGetNull() {
        given(authService.getUserByEmail(goodUser.getEmail())).willReturn(Optional.empty());
        assertFalse(authService.getUserByEmail(goodUser.getEmail()).isPresent());
    }
    @Test
    public void givenBadToken_whenCheckTokenToUserInDB_thenThrow() {
        String token = "1234";
        assertThrows(IllegalAccessError.class, () ->
                        authService.checkTokenToUserInDB(token),
                "bad token input didn't Throw error");
    }

    @Test
    public void givenGoodLengthTokenNoUser_whenCheckTokenToUserInDB_thenReturnUserId() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjcxOTU4NzU5LCJzdWIiOiJsb2dpbiIsImlzcyI6IlByb2plY3QgTWFuYWdlbWVudCIsImV4cCI6MzM0MzkxNzUxOH0.vCzaRYXcTQ9S9yXRAfP1XoJUiz8QiQGItRnBiw2SEmk";
        assertThrows(AccountNotFoundException.class, () ->
                        authService.checkTokenToUserInDB(token),
                "token for no user in DB didn't Throw error");
    }

    @Test
    public void checkTokenIsReal_whenTokenExist_Success(){
        authService.userToToken.put(1L,"key");
        assertTrue(authService.checkTokenIsReal(1L,"key"));
    }
    @Test
    public void checkTokenIsReal_whenTokenDoNotExist_assertFalse(){
//        authService.userToToken.put(1L,"key");
        assertFalse(authService.checkTokenIsReal(1L,"key"));
    }
//    @Test
//    public void givenGoodLengthToken_whenCheckTokenToUserInDB_thenReturnUserId() {
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjcxOTU4NzU5LCJzdWIiOiJsb2dpbiIsImlzcyI6IlByb2plY3QgTWFuYWdlbWVudCIsImV4cCI6MzM0MzkxNzUxOH0.vCzaRYXcTQ9S9yXRAfP1XoJUiz8QiQGItRnBiw2SEmk";
//        given(authService.getUser(1L)).willReturn(Optional.ofNullable(goodUser));
//        assertThrows(AccountNotFoundException.class, () ->
//                        authService.checkTokenToUserInDB(token),
//                "token for no user in DB didn't Throw error");
//    }
//    @Test
//    public void givenGoournUserId() {
//        authService.addTokenToUser();"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjcxOTU4NzU5LCJzdWIiOiJsb2dpbiIsImlzcyI6IlByb2plY3QgTWFuYWdlbWVudCIsImV4cCI6MzM0MzkxNzUxOH0.vCzaRYXcTQ9S9yXRAfP1XoJUiz8QiQGItRnBiw2SEmk";
//        given(authService.getUser(1L)).willReturn(Optional.ofNullable(goodUser));
//        assertThrows(AccountNotFoundException.class, () ->
//                        authService.checkTokenToUserInDB(token),
//                "token for no user in DB didn't Throw error");
//    }

//    @Test
//    public void givenUserId_whenGenerateToken_thenGenerate(){
//        given(userRepo.findByEmail(goodUser.getEmail())).willReturn(Optional.of(goodUser));
//        userReq = new UserRequest(goodUser.getEmail(),"12341234");
//        given(Token.createJWT("1","hi","hi", Instant.now().toEpochMilli())).willReturn("1");
//        assertEquals("1",authService.generateToken(1));
//    }

}