package projectManagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.UserService;
import projectManagement.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projectManagement.entities.Board;
import projectManagement.entities.Item;
import projectManagement.entities.ItemImportance;
import projectManagement.entities.User;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.ItemRepo;
import projectManagement.repository.UserRepo;
import projectManagement.service.UserService;

@RequestMapping(value = "/user/auth")
@AllArgsConstructor
@CrossOrigin
@RestController
public class UserController {
    public UserController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void saveUser() {
    }

    @Autowired
    UserService userService;

    @RequestMapping(value = "register", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> register(@RequestBody UserRequest user1) {
        User user = new User(user1.getName(), user1.getEmail(), user1.getPassword());
        if(Validation.validateInputRegister(user)) {
            Response<User> response = userService.register(user);
            if (response.isSucceed()) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response> login(@RequestBody User user) {
        if(Validation.validateInputLogin(user)) {
            Response<String> response = userService.login(user);
            if (response.isSucceed()) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
    }

}
