package projectManagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import projectManagement.controller.SocketsUtil;
import projectManagement.entities.Notification;
import projectManagement.entities.NotificationType;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.NotificationRepo;
import projectManagement.repository.UserRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    NotificationRepo notificationRepo;
    UserRepo userRepo;
    BoardRepo boardRepo;
    SocketsUtil socketsUtil;

    @Test
    public void testInitNotificationsSuccess() {
        User user = new User();
        assertTrue(notificationService.initNotifications(user).isSucceed());
    }

    @Test
    void getNotificationsByUser() {
    }

    @Test
    void checkEmailNotification() {
    }

    @Test
    void sendMails() {
    }

    @Test
    void checkPop() {
    }

    @Test
    void sendNotification() {
    }
}