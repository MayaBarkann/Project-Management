package projectManagement.utils;

import com.google.api.client.auth.oauth2.TokenResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


import javax.mail.MessagingException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class EmailTest {
    @InjectMocks
    Email email = new Email();
    EmailTest() throws Exception {
    }

//    @Test
//    void send(){
//        assertThrows(TokenResponseException.class,()-> Email.send("ddd","hi","d"));
//    }
}