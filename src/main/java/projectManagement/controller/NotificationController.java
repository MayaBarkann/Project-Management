package projectManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import projectManagement.controller.entities.ChangeStatusDTO;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.utils.Email;

import javax.mail.MessagingException;
import java.io.IOException;

@RequestMapping("/notification")
@CrossOrigin
@RestController
public class NotificationController {

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public ResponseEntity<Response<String>> changeItemStatus(@RequestParam String email){
        try {
            Email.send(email, "hello first try", "project management");
            return ResponseEntity.ok(Response.createSuccessfulResponse("work ok"));
        }catch (MessagingException | IOException e ){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("did not sent sent"));
        }
    }
}
