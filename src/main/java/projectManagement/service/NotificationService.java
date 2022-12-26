package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import projectManagement.controller.entities.CreateItemDTO;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.NotificationRepo;
import projectManagement.repository.UserRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepo notificationRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BoardRepo boardRepo;

    public Response<Void> initNotifications(User user) {

        Notification notification = new Notification(user, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING, NotificationType.NOTHING);
        Notification savedNotification = notificationRepo.save(notification);

        return Response.createSuccessfulResponse(null);
    }

    public Response<Notification> getNotificationsByUser(User user) {

        Notification notification = notificationRepo.findByUser(user);
        if (notification == null) {
            Response.createFailureResponse("can't find a notifications");
        }
        return Response.createSuccessfulResponse(notification);

    }

    public List<String> checkEmailNotification(NotifyWhen notifyWhen, Long boardId) {
        List<String> emails = new ArrayList<>();

        List<User> userInBoard = userRepo.findAll();
//TODO this should be done for userInBoard
        for (User user : userInBoard) {
            Notification userNotification = notificationRepo.findByUser(user);
            switch (notifyWhen) {
                case ITEM_ASSIGNED_TO_ME:
                    if (userNotification.getITEM_ASSIGNED_TO_ME() == NotificationType.EMAIL || userNotification.getITEM_ASSIGNED_TO_ME() == NotificationType.EMAIL_POP) {
                        emails.add(user.getEmail());
                    }
                    break;
                case ITEM_STATUS_CHANGED:
                    if (userNotification.getITEM_STATUS_CHANGED() == NotificationType.EMAIL || userNotification.getITEM_STATUS_CHANGED() == NotificationType.EMAIL_POP) {
                        emails.add(user.getEmail());
                    }
                    break;
                case ITEM_COMMENT_ADDED:
                    if (userNotification.getITEM_COMMENT_ADDED() == NotificationType.EMAIL || userNotification.getITEM_COMMENT_ADDED() == NotificationType.EMAIL_POP) {
                        emails.add(user.getEmail());
                    }
                    break;
                case ITEM_DELETED:
                    if (userNotification.getITEM_DELETED() == NotificationType.EMAIL || userNotification.getITEM_DELETED() == NotificationType.EMAIL_POP) {
                        emails.add(user.getEmail());
                    }
                    break;
                case ITEM_DATA_CHANGED:
                    if (userNotification.getITEM_DATA_CHANGED() == NotificationType.EMAIL || userNotification.getITEM_DATA_CHANGED() == NotificationType.EMAIL_POP) {
                        emails.add(user.getEmail());
                    }
                    break;
                case USER_ADDED_TO_THE_SYSTEM:
                    if (userNotification.getUSER_ADDED_TO_THE_SYSTEM() == NotificationType.EMAIL || userNotification.getUSER_ADDED_TO_THE_SYSTEM() == NotificationType.EMAIL_POP) {
                        emails.add(user.getEmail());
                    }
                    break;
            }


        }
        return emails;
    }

    public void sendMails(Long boardId, NotifyWhen notifyWhen, String content) {
        List<String> emails = checkEmailNotification(notifyWhen, boardId);
        System.out.println(Arrays.toString(emails.toArray()));


    }

    //TODO
    public boolean checkPop(User user, NotifyWhen notifyWhen) {
//        List<Notification> all = notificationRepo.findAll();

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

}
