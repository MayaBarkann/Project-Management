package projectManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projectManagement.entities.User;
import projectManagement.repository.UserRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepo userRepo;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("dvir","dvir@example.com","dvir");
    }

    @Test
    void getUser_validUserId_returnUser() {
        given(userRepo.findById(100L)).willReturn(Optional.of(user));
        Optional<User> user = userService.getUser(100);
        assertTrue(user.isPresent());
        assertEquals("dvir", user.get().getName());
    }

    @Test
    void getUser_InvalidUserId_returnEmpty() {
        given(userRepo.findById(100L)).willReturn(Optional.empty());
        Optional<User> user = userService.getUser(100);
        assertFalse(user.isPresent());
    }

    @Test
    void getUser_negativeUserId_returnEmpty() {
        given(userRepo.findById(-100L)).willReturn(Optional.empty());
        Optional<User> user = userService.getUser(-100);
        assertFalse(user.isPresent());
    }

    @Test
    void getUser_MaxValueUserId_returnEmpty() {
        given(userRepo.findById(Long.MAX_VALUE)).willReturn(Optional.empty());
        Optional<User> user = userService.getUser(Long.MAX_VALUE);
        assertFalse(user.isPresent());
    }
    @Test
    void getUser_MinValueUserId_returnEmpty() {
        given(userRepo.findById(Long.MIN_VALUE)).willReturn(Optional.empty());
        Optional<User> user = userService.getUser(Long.MIN_VALUE);
        assertFalse(user.isPresent());
    }

    @Test
    void getUserByEmail_validEmail_returnUser() {
        given(userRepo.findByEmail("dvir@example.com")).willReturn(Optional.of(user));
        Optional<User> user = userService.getUserByEmail("dvir@example.com");
        assertTrue(user.isPresent());
        assertEquals("dvir", user.get().getName());
    }
    @Test
    void getUserByEmail_InvalidEmail_returnEmpty() {
        given(userRepo.findByEmail("dvir@example.com")).willReturn(Optional.empty());
        Optional<User> user = userService.getUserByEmail("dvir@example.com");
        assertFalse(user.isPresent());
    }
    @Test
    void getUserByEmail_caseSensitive_returnEmpty() {
        given(userRepo.findByEmail("dvir@example.com")).willReturn(Optional.of(user));
        given(userRepo.findByEmail("Dvir@example.com")).willReturn(Optional.empty());
        Optional<User> user = userService.getUserByEmail("dvir@example.com");
        Optional<User> user2 = userService.getUserByEmail("Dvir@example.com");
        assertTrue(user.isPresent());
        assertFalse(user2.isPresent());
    }
    @Test
    void getUserByEmail_nullMail_returnEmpty() {
        given(userRepo.findByEmail(null)).willReturn(Optional.empty());
        Optional<User> user = userService.getUserByEmail(null);
        assertFalse(user.isPresent());
    }

}