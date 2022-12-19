package projectManagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.UserDTO;
import projectManagement.controller.entities.UserLoginDTO;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.AuthService;
import projectManagement.utils.Validation;

import java.util.Optional;

@RequestMapping(value = "/auth")
@AllArgsConstructor
@CrossOrigin
@RestController
public class AuthController {
    public AuthController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void saveUser() {
    }

    @Autowired
    AuthService authService;

    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<UserDTO>> register(@RequestBody UserRequest user) {
        System.out.println(user.getEmail());
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        if(Validation.validateInputRegister(user)) {
            Response<UserDTO> response = authService.register(user);
            if (response.isSucceed()) {
                return ResponseEntity.ok().body(response);
            }
        }
        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<UserLoginDTO>> login(@RequestBody UserRequest user) {
        if(Validation.validateInputLogin(user)) {
            Response<UserLoginDTO> response = authService.login(user);
            if (response.isSucceed()) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/loginGithub")
    public ResponseEntity<Response<UserLoginDTO>> loginGithub(@RequestParam String code){
        Response<UserLoginDTO> response = authService.loginGithub(code);
        return ResponseEntity.ok().body(response);
    }

}
