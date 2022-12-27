package projectManagement.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import projectManagement.service.NotificationService;
import projectManagement.service.UserService;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {
    @InjectMocks
    private NotificationController notificationController;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserService userService;
}
