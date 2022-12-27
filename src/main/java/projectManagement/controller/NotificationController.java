package projectManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import projectManagement.controller.entities.ChangeStatusDTO;
import projectManagement.entities.Item;
import projectManagement.entities.Notification;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.NotificationService;
import projectManagement.service.UserService;
import projectManagement.utils.Email;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

@RequestMapping("/notification")
@CrossOrigin
@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    /**
     * This method retrieves a notification for a given user.
     *
     * @param user The user to get the notification for.
     * @return A ResponseEntity object, which contains a Response object
     * with a Notification object if the request was successful and a failure message otherwise.
     */
    @RequestMapping(value = "/get_user_notification", method = RequestMethod.GET)
    public ResponseEntity<Response<Notification>> getNotificationByUser(@RequestAttribute User user) {
        if(user == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("User can not be null"));
        }
        Response<Notification> res = notificationService.getNotificationsByUser(user);
        if (res.isSucceed()) {
            return ResponseEntity.ok().body(res);
        } else {
            return ResponseEntity.badRequest().body(res);
        }
    }

}
