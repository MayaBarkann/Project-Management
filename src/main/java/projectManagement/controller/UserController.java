package projectManagement.controller;

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

@RequestMapping("/user")
@CrossOrigin
@RestController
public class UserController {
    public UserController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void saveUser() {


    }


}
