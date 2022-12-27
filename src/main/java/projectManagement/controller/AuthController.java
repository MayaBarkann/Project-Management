package projectManagement.controller;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.UserDTO;
import projectManagement.controller.entities.UserLoginDTO;
import projectManagement.controller.entities.UserRequest;
import projectManagement.entities.Response;
import projectManagement.service.AuthService;
import projectManagement.utils.Validation;

import java.net.URI;


@RequestMapping(value = "/auth")
@AllArgsConstructor
@CrossOrigin
@RestController
public class AuthController {
    private static Logger logger = LogManager.getLogger(AuthController.class.getName());



    @RequestMapping(value = "", method = RequestMethod.GET)
    public void saveUser() {
    }

    @Autowired
    AuthService authService;

    /**
     * Register function is responsible for creating new users and adding them to the database.
     * Users will use their personal information to create a new account: email, password, name.
     *
     * @param user - mail,password,name
     * @return l
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<UserDTO>> register(@RequestBody UserRequest user) {
        logger.info("in AuthController -> register");
        if (Validation.validateInputRegister(user)) {
            Response<UserDTO> response = authService.register(user);
            if (response.isSucceed()) {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.badRequest().body(response);
        }
        logger.error("in AuthController -> register -> Wrong input");
        return ResponseEntity.badRequest().body(Response.createFailureResponse("bad input"));
    }

    /**
     * login controller, get an UserRequest with user login details. (email,password).
     *
     * @param user - (email,password)
     * @return - UserLoginDTO => userId; token;
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<UserLoginDTO>> login(@RequestBody UserRequest user) {
        logger.info("in AuthController -> login");
        if (Validation.validateInputLogin(user)) {
            Response<UserLoginDTO> response = authService.login(user);
            if (response.isSucceed()) {
                return ResponseEntity.ok().body(response);
            }
            return ResponseEntity.badRequest().body(response);
        }
        logger.error("in AuthController -> login -> Wrong input");
        return ResponseEntity.badRequest().body(Response.createFailureResponse("bad input"));
    }

    /**
     * GitHub register - after client authenticate with gitHub, he gets a code as a parameter.
     * we send the parameter from the client to the controller to here.
     * then we need to take 3 actions with this code to get the user email and name
     *
     * @param code - after authorize we get a code as a param
     * @return - UserLoginDTO => userId; token;
     */
    @RequestMapping(method = RequestMethod.GET, path = "/loginGithub")
    public ResponseEntity<Response<UserLoginDTO>> loginGithub(@RequestParam String code) {
        logger.info("in AuthController -> loginGithub");
        Response<UserLoginDTO> response = authService.loginGithub(code);
        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }
}
