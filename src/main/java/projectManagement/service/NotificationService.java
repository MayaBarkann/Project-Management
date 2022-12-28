package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import projectManagement.controller.entities.CreateItemDTO;
import projectManagement.controller.SocketsUtil;
import projectManagement.controller.entities.UpdateUserNotificationDTO;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.NotificationRepo;
import projectManagement.repository.UserRepo;
import projectManagement.utils.Email;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

@Service
public class NotificationService {
    @Autowired
    NotificationRepo notificationRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BoardRepo boardRepo;
    @Autowired
    SocketsUtil socketsUtil;

    public Response<Void> initNotifications(User user) {

        Notification notification = new Notification(user, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING);
        Notification savedNotification = notificationRepo.save(notification);

        return Response.createSuccessfulResponse(null);
    }

    public Response<Notification> getNotificationsByUser(User user) {

        Notification notification = notificationRepo.findByUser(user);
        if (notification == null) {
            return Response.createFailureResponse("can't find a notifications");
        }
        return Response.createSuccessfulResponse(notification);

    }

    public String checkEmailNotification(User user, NotifyWhen notifyWhen) {

        Notification userNotification = notificationRepo.findByUser(user);
        switch (notifyWhen) {
            case ITEM_ASSIGNED_TO_ME:
                if (userNotification.getITEM_ASSIGNED_TO_ME() == NotificationType.EMAIL || userNotification.getITEM_ASSIGNED_TO_ME() == NotificationType.EMAIL_POP) {
                    return (user.getEmail());
                }
                break;
            case ITEM_STATUS_CHANGED:
                if (userNotification.getITEM_STATUS_CHANGED() == NotificationType.EMAIL || userNotification.getITEM_STATUS_CHANGED() == NotificationType.EMAIL_POP) {
                    return (user.getEmail());
                }
                break;
            case ITEM_COMMENT_ADDED:
                if (userNotification.getITEM_COMMENT_ADDED() == NotificationType.EMAIL || userNotification.getITEM_COMMENT_ADDED() == NotificationType.EMAIL_POP) {
                    return (user.getEmail());
                }
                break;
            case ITEM_DELETED:
                if (userNotification.getITEM_DELETED() == NotificationType.EMAIL || userNotification.getITEM_DELETED() == NotificationType.EMAIL_POP) {
                    return (user.getEmail());
                }
                break;
            case ITEM_DATA_CHANGED:
                if (userNotification.getITEM_DATA_CHANGED() == NotificationType.EMAIL || userNotification.getITEM_DATA_CHANGED() == NotificationType.EMAIL_POP) {
                    return (user.getEmail());
                }
                break;
            case USER_ADDED_TO_THE_SYSTEM:
                if (userNotification.getUSER_ADDED_TO_THE_SYSTEM() == NotificationType.EMAIL || userNotification.getUSER_ADDED_TO_THE_SYSTEM() == NotificationType.EMAIL_POP) {
                    return (user.getEmail());
                }
                break;
        }


        return "";
    }

    public void sendMails(String mail, User user, NotifyWhen notifyWhen, String content) {
        String email = checkEmailNotification(user, notifyWhen);
        try {
            Email.send(email, content, "project management");
        } catch (Exception e) {

        }


    }

    public boolean checkPop(User user, NotifyWhen notifyWhen) {

        Notification userNotification = notificationRepo.findByUser(user);
        switch (notifyWhen) {
            case ITEM_ASSIGNED_TO_ME:
                if (userNotification.getITEM_ASSIGNED_TO_ME() == NotificationType.POP || userNotification.getITEM_ASSIGNED_TO_ME() == NotificationType.EMAIL_POP) {
                    return true;
                }
                break;
            case ITEM_STATUS_CHANGED:
                if (userNotification.getITEM_STATUS_CHANGED() == NotificationType.POP || userNotification.getITEM_STATUS_CHANGED() == NotificationType.EMAIL_POP) {
                    return true;
                }
                break;
            case ITEM_COMMENT_ADDED:
                if (userNotification.getITEM_COMMENT_ADDED() == NotificationType.POP || userNotification.getITEM_COMMENT_ADDED() == NotificationType.EMAIL_POP) {
                    return true;
                }
                break;
            case ITEM_DELETED:
                if (userNotification.getITEM_DELETED() == NotificationType.POP || userNotification.getITEM_DELETED() == NotificationType.EMAIL_POP) {
                    return true;
                }
                break;
            case ITEM_DATA_CHANGED:
                if (userNotification.getITEM_DATA_CHANGED() == NotificationType.POP || userNotification.getITEM_DATA_CHANGED() == NotificationType.EMAIL_POP) {
                    return true;
                }
                break;
            case USER_ADDED_TO_THE_SYSTEM:
                if (userNotification.getUSER_ADDED_TO_THE_SYSTEM() == NotificationType.POP || userNotification.getUSER_ADDED_TO_THE_SYSTEM() == NotificationType.EMAIL_POP) {
                    return true;
                }
                break;
        }
        return false;

    }

    public void sendNotification(Set<Long> allUsersInBoard, String notificationContent, NotifyWhen notifyWhen) {
        for (long userId : allUsersInBoard) {
            Optional<User> user = userRepo.findById(userId);
            if (checkPop(user.get(), notifyWhen)) {
                socketsUtil.pushNotification(user.get().getId(), notificationContent);
            }
            String mail = checkEmailNotification(user.get(), notifyWhen);
            if (mail != "") {
                sendMails(mail, user.get(), notifyWhen, notificationContent);
            }
        }

    }

    public Response<Notification> updateUserNotification(User user, UpdateUserNotificationDTO updateUserNotificationDTO) {

        Notification notificationByUser = notificationRepo.findByUser(user);
        notificationByUser.setITEM_DELETED(updateUserNotificationDTO.getITEM_DELETED());
        notificationByUser.setITEM_ASSIGNED_TO_ME(updateUserNotificationDTO.getITEM_ASSIGNED_TO_ME());
        notificationByUser.setITEM_STATUS_CHANGED(updateUserNotificationDTO.getITEM_STATUS_CHANGED());
        notificationByUser.setITEM_COMMENT_ADDED(updateUserNotificationDTO.getITEM_COMMENT_ADDED());
        notificationByUser.setUSER_ADDED_TO_THE_SYSTEM(updateUserNotificationDTO.getUSER_ADDED_TO_THE_SYSTEM());
        Notification savedNotification = notificationRepo.save(notificationByUser);

        return Response.createSuccessfulResponse(savedNotification);


    }
}
