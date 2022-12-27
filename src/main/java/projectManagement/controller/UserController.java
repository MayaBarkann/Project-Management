package projectManagement.controller;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.LoginBoardDTO;
import projectManagement.controller.entities.UserDTO;
import projectManagement.entities.Board;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.BoardService;

import java.util.List;

@RequestMapping(value = "/user")
@AllArgsConstructor
@CrossOrigin
@RestController
public class UserController {
    private static Logger logger = LogManager.getLogger(UserController.class.getName());

    @Autowired
    BoardService boardService;

    /**
     * when a user first time logged into the system, we want to show him all the board he linked with.
     *
     * @param user - the user who just logged in.
     * @return - list of board, show the name of the board and the id.
     */
    @GetMapping("/get_boards")
    public ResponseEntity<List<LoginBoardDTO>> getBoards(@RequestAttribute User user) {
        Response<List<LoginBoardDTO>> response = boardService.getBoards(user);
        return response.isSucceed() ? ResponseEntity.ok().body(response.getData()) : ResponseEntity.badRequest().body(null);
    }



    }
