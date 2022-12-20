package projectManagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
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

    /**
     * Register controller
     * @param user - mail,password,name
     * @return l
     */
    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<UserDTO>> register(@RequestBody UserRequest user) {
        if(Validation.validateInputRegister(user)) {
            Response<UserDTO> response = authService.register(user);
            if (response.isSucceed()) {
                return ResponseEntity.ok().body(response);
            }
        }
        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
    }

    /**
     * login controller
     * @param user -
     * @return -
     */
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

    /**
     * Github register
     * @param code - after authorize we get a code as a param
     * @return -
     */
    @RequestMapping(method = RequestMethod.GET, path = "/loginGithub")
    public ResponseEntity<Response<UserLoginDTO>> loginGithub(@RequestParam String code){
        Response<UserLoginDTO> response = authService.loginGithub(code);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:3000"));
        System.out.println("good luck");
        return new ResponseEntity<Response<UserLoginDTO>>(response,headers,HttpStatus.MOVED_PERMANENTLY);
    }
}
