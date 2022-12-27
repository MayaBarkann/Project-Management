package projectManagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Notification;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.NotificationService;
import projectManagement.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {
    @InjectMocks
    private NotificationController notificationController;
    @Mock
    private NotificationService notificationService;

    @BeforeEach
    @DisplayName("Make sure we have all necessary items, good as new, for each test when it's called")
    void setUp() {

    }

    @Test
    void getNotification_valid_ok(){
        Notification notification = new Notification();
        User user = new User();
        given(notificationService.getNotificationsByUser(user)).willReturn(Response.createSuccessfulResponse(notification));
        assertEquals(HttpStatus.OK, notificationController.getNotificationByUser(user).getStatusCode());
    }

    @Test
    void getNotification_nullUser_badRequest(){
        Notification notification = new Notification();
        assertEquals(HttpStatus.BAD_REQUEST, notificationController.getNotificationByUser(null).getStatusCode());

    }
    

}
