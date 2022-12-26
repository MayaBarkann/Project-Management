package projectManagement.utils;

import org.mockito.InjectMocks;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @InjectMocks
    BCryptPasswordEncoder bCryptPasswordEncoder;

}