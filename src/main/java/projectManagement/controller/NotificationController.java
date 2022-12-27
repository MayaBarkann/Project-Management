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
    @Autowired
    UserService userService;


    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> changeItemStatus(@RequestParam String email) {
        try {
            Email.send(email, "hello first try", "project management");
            return ResponseEntity.ok(Response.createSuccessfulResponse("work ok"));
        } catch (MessagingException | IOException e) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("did not sent sent"));
        }
    }

    @RequestMapping(value = "/initUserNotifications/{userId}", method = RequestMethod.POST)
    public ResponseEntity<Response<Notification>> initUserNotifications(@PathVariable Long userId) {
        Optional<User> user = userService.getUser(userId);
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("user not found"));
        }

        Response<Void> res = notificationService.initNotifications(user.get());

        if (res.isSucceed()) {
            return ResponseEntity.ok().body(null);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @RequestMapping(value = "/get_user_notification", method = RequestMethod.GET)
    public ResponseEntity<Response<Notification>> getNotificationByUser(@RequestParam Long userId) {

        Optional<User> user = userService.getUser(userId);
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("user not found"));
        }
        Response<Notification> res = notificationService.getNotificationsByUser(user.get());
        if (res.isSucceed()) {
            return ResponseEntity.ok().body(res);
        } else {
            return ResponseEntity.badRequest().body(res);
        }
    }

}
