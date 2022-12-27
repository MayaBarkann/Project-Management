package projectManagement.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import projectManagement.utils.Provider;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class UserTest {

    @InjectMocks
    User user;

    private User testingUser;
    @BeforeEach
    void setup(){
        testingUser = new User();
    }
    @Test
    public void testSetPassword() {
        testingUser.setPassword("password123");
        assertEquals("password123",testingUser.getPassword());
    }

    @Test
    void testSetProvider_githubProvider() {
        testingUser.setProvider(Provider.GITHUB);
        assertEquals(Provider.GITHUB,testingUser.getProvider());
    }
    @Test
    void testSetProvider_LOCALProvider() {
        testingUser.setProvider(Provider.LOCAL);
        assertEquals(Provider.LOCAL,testingUser.getProvider());
    }

    @Test
    public void testCreateUser() {
        User user = User.CreateUser("divir", "divir@example.com", "password123", Provider.GITHUB);
        assertEquals("divir", user.getName());
        assertEquals("divir@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(Provider.GITHUB, user.getProvider());
    }


    @Test
    void getId() {
        User user = new User("dvir", "dvir@example.com", "password123");
        assertEquals(0, user.getId());
    }

    @Test
    void getName() {
        User user = new User("dvir", "dvir@example.com", "password123");
        assertEquals("dvir",user.getName());
    }

    @Test
    void getEmail() {
        User user = new User("dvir", "dvir@example.com", "password123");
        assertEquals("dvir@example.com",user.getEmail());
    }

    @Test
    void getPassword() {
        User user = new User("dvir", "dvir@example.com", "password123");
        assertEquals("password123",user.getPassword());
    }

    @Test
    void getProvider() {
        testingUser.setProvider(Provider.LOCAL);
        assertEquals(Provider.LOCAL,testingUser.getProvider());
    }
}