package projectManagement.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.AddUserRoleDTO;
import projectManagement.controller.entities.BoardDTO;
import projectManagement.controller.entities.FilterItemDTO;
import projectManagement.entities.*;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@RequestMapping(value = "/board")
@CrossOrigin
@RestController
public class BoardController {
    private static Logger logger = LogManager.getLogger(BoardController.class.getName());
    @Autowired
    ItemService itemService;
    @Autowired
    BoardService boardService;
    @Autowired
    UserService userService;
    @Autowired
    SocketsUtil socketsUtil;


    /***
     * This method filters item of the given board by given properties and values.
     * It returns all the items with exact match to all properties and their values (if the value is not null) in the given filter.
     *
     * @param boardId - the id of the board we want to perform the filter on
     * @param filter - FilterItemDTO object containing the values of fields we want to perform the filter on
     * @return response entity containing the items that match the filter
     */
    //todo: change boardId to requestAttribute
    @PostMapping("/filter")
    public ResponseEntity<Response<List<Item>>> filterItems(@RequestAttribute User user, @RequestParam long boardId, @RequestBody FilterItemDTO filter) {
        Optional<Board> board = boardService.getBoardById(boardId);
        logger.info("In BoardController - trying to filter items by properties");
        if (!board.isPresent()) {
            logger.error("In BoardController - can not perform the filter since the board does not exist");
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Board does not exist"));
        }
        if (filter == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Null pointer- can not perform filter"));
        }
        Response<UserRole> userExistsInBoard = boardService.userExistsInBoard(board.get(), user);
        if (!userExistsInBoard.isSucceed()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse(userExistsInBoard.getMessage()));
        }
        //todo: go back and change the response
        return ResponseEntity.ok(itemService.filterItems(filter, boardId));
    }

    /**
     * This method creates a new board with the given user as the admin.
     *
     * @param user  the admin of the new board
     * @param title the title of the new board
     * @return Response entity with successful response containing the new created board if the given user exists,
     * otherwise returns bad request with the reason.
     */
    @PostMapping("/create_board")
    public ResponseEntity<Response<Board>> createBoard(@RequestAttribute User user, @RequestBody String title) {
        logger.info("In BoardController - creating new board");
        if (title == null || title.equals("")) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Title must not be empty"));
        }
        return ResponseEntity.ok().body(boardService.createBoard(title, user));
    }

    /**
     * This method creates new type and add it to the given board
     *
     * @param board
     * @param type
     * @return response entity with successful response containing the new Type created if the given board exists,
     * otherwise returns bad request containing failure Response with the reason for failure.
     */
    //todo: add live changes
    @PostMapping("/add_type")
    public ResponseEntity<String> addType(@RequestAttribute Board board, @RequestBody String type) {
        Response<String> response = boardService.addType(board, type);
        if (response.isSucceed()) {
            socketsUtil.addBoardType(response, board.getId());
            return ResponseEntity.ok().body(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    /**
     * This method creates new status and add it to the given board
     *
     * @param board
     * @param status
     * @return response entity with successful response containing the new Status created if the given board exists,
     * otherwise returns bad request containing failure Response with the reason for failure.
     */

    @PostMapping("/add_status")
    public ResponseEntity<String> addStatus(@RequestAttribute Board board, @RequestBody String status) {
        logger.info("in board controller - trying to add_status");

        Response<String> response = boardService.addStatus(board, status);
        if (response.isSucceed()) {
            socketsUtil.addBoardStatus(response, board.getId());
            return ResponseEntity.ok().body(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }

    }

    /**
     * This method removes the given status from board and deletes all items with this status
     *
     * @param board
     * @param status the status we want to remove
     * @return
     */
    //todo: remove boardId since it seems redundant
    @DeleteMapping("/remove_status")
    public ResponseEntity<String> removeStatus(@RequestAttribute Board board, @RequestParam String status) {
        //todo: need to remove all items from item table that has this status?
        Response<String> response = boardService.removeStatus(board, status);
        if (response.isSucceed()) {
            socketsUtil.deleteBoardStatus(response, board.getId());
            //TODO remove all items that has this status through the socket
            return ResponseEntity.ok().body(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }

    }

    /**
     * Removes a type from a board.
     *
     * @param board The board from which the type will be removed.
     * @param type  The type to be removed from the board.
     * @return A ResponseEntity object with a body containing a message about the success or failure of the operation. If the operation is successful, the ResponseEntity will have an HTTP status of 200 OK. If the operation fails, the ResponseEntity will have an HTTP status of 400 Bad Request.
     */
    @DeleteMapping("/remove_type")
    public ResponseEntity<String> removeType(@RequestAttribute Board board, @RequestParam String type) {
        Response<String> response = boardService.removeType(board, type);

        if (response.isSucceed()) {
            socketsUtil.deleteBoardType(response, board.getId());
            //TODO update all the that has these item in live
            return ResponseEntity.ok().body(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    /**
     * This method retrieves a board for a given user.
     *
     * @param user    This is a request attribute that represents the user for whom the board will be retrieved.
     * @param boardId This is a request parameter that represents the ID of the board to be retrieved.
     * @return ResponseEntity object with a body that contains the requested board as a BoardDTO object
     */
    @GetMapping(value = "/get_board")
    public ResponseEntity<BoardDTO> getBoard(@RequestAttribute User user, @RequestParam long boardId) {
        Response<Board> response = boardService.getBoard(user, boardId);
        logger.info("in board controller - trying to get board");
        if (response.isSucceed()) {
            BoardDTO boardDTO = BoardDTO.createBoardDTOFromBoard(response.getData());
            return ResponseEntity.ok().body(boardDTO);
        }
        return ResponseEntity.badRequest().body(null);
    }

    /**
     * This method assigns a role to a user for a given board.
     *
     * @param board       This is a request attribute that represents the board for which the user's role will be assigned.
     * @param userRoleDTO This is a request body object that contains the following properties:
     *                    email: The email of the user for whom the role will be assigned.
     *                    role: The role to be assigned to the user.
     * @return ResponseEntity object with a body that contains a message about the success or failure of the operation.
     */
    @PostMapping(value = "/assign_role_to_user")
    public ResponseEntity<String> assignRoleToUser(@RequestAttribute Board board, @RequestBody AddUserRoleDTO userRoleDTO) {
        Optional<User> user = userService.getUserByEmail(userRoleDTO.getEmail());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("Can not assign role to user - user does not exist");
        }
        Response<String> response = boardService.assignUserRole(board, user.get(), userRoleDTO.getRole());
        return response.isSucceed() ? ResponseEntity.ok().body(response.getMessage()) : ResponseEntity.badRequest().body(response.getMessage());
    }

    /**
     * Retrieves the items for a given board.
     *
     * @param user    The user for whom the items will be retrieved.
     * @param boardId The ID of the board for which the items will be retrieved.
     * @return ResponseEntity object with a body containing the requested items as a Set of Item objects.
     */
    @RequestMapping(value = "/get_items", method = RequestMethod.GET)
    public ResponseEntity<Response<Set<Item>>> getItems(@RequestAttribute User user, @RequestParam long boardId) {
        Response<Set<Item>> responseGetItems = boardService.getItems(user, boardId);
        return responseGetItems.isSucceed() ? ResponseEntity.ok().body(responseGetItems) : ResponseEntity.badRequest().body(responseGetItems);
    }


//    @DeleteMapping(value = "/remove-user-role")
//    public ResponseEntity<Response<UserRoleInBoard>> removeUserRole(@RequestParam long boardId, @RequestBody AddUserRoleDTO userRoleDTO) {
//        Optional<User> user = userService.getUserByEmail(userRoleDTO.getEmail());
//        Response<UserRoleInBoard> response = boardService.removeUserRole(boardId, user.get(), userRoleDTO.getRole());
//        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
//    }

}
