package projectManagement.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.AddUserRoleDTO;
import projectManagement.controller.entities.BoardDTO;
import projectManagement.controller.entities.FilterItemDTO;
import projectManagement.entities.Board;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.service.UserService;
import java.util.List;
import java.util.Optional;


@RequestMapping(value = "/board")
@RestController
public class BoardController {
    private static Logger logger = LogManager.getLogger(BoardController.class.getName());
    @Autowired
    ItemService itemService;
    @Autowired
    BoardService boardService;
    @Autowired
    UserService userService;


    /***
     * This method filters item of the given board by given properties and values.
     * It returns all the items with exact match to all properties and their values (if the value is not null) in the given filter.
     * @param boardId - the id of the board we want to perform the filter on
     * @param filter - FilterItemDTO object containing the values of fields we want to perform the filter on
     * @return response entity containing the items that match the filter
     */
    //todo: change boardId to requestAttribute
    @GetMapping("/filter")
    public ResponseEntity<Response<List<Item>>> filterItems(@RequestParam long boardId, @RequestBody FilterItemDTO filter) {
        Optional<Board> board = boardService.getBoardById(boardId);
        logger.info("In BoardController - trying to filter items by properties");
        if (!board.isPresent()) {
            logger.error("In BoardController - can not perform the filter since the board does not exist");
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Board does not exist"));
        }

        if (filter == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Null pointer- can not perform filter"));
        }

        //todo: go back and change the response
        return ResponseEntity.ok(itemService.filterItems(filter, boardId));
    }

    /**
     * This method creates a new board with the given user as the admin.
     * @param userId the admin of the new board
     * @param title the title of the new board
     * @return Response entity with successful response containing the new created board if the given user exists,
     * otherwise returns bad request with the reason.
     */
    @PostMapping("/create-board")
    public ResponseEntity<Response<Board>> createBoard(@RequestParam long userId, @RequestBody String title){
        System.out.println(userId +"ff "+ title);
        Optional<User> admin = userService.getUser(userId);
        logger.info("In BoardController - creating new board");
        logger.info(admin.isPresent());
        if (!admin.isPresent()){
            logger.error("In BoardController - can not create new board since this user does not exist");
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Can not create board - user does not exist"));
        }
        return ResponseEntity.ok().body(boardService.createBoard(title, admin.get()));
    }

    /**
     * This method creates new type and add it to the given board
     * @param boardId
     * @param type
     * @return response entity with successful response containing the new Type created if the given board exists,
     * otherwise returns bad request containing failure Response with the reason for failure.
     */
    //todo: add live changes
    @PostMapping("/add-type")
    public ResponseEntity<String> addType(@RequestParam long boardId, @RequestBody String type){
        Response<String> response = boardService.addType(boardId, type);

        return response.isSucceed() ? ResponseEntity.ok().body(response.getMessage()) : ResponseEntity.badRequest().body(response.getMessage());
    }

    /**
     * This method creates new status and add it to the given board
     * @param boardId
     * @param status
     * @return response entity with successful response containing the new Status created if the given board exists,
     * otherwise returns bad request containing failure Response with the reason for failure.
     */

    @PostMapping("/add-status")
    public ResponseEntity<String> addStatus(@RequestParam long boardId, @RequestBody String status){
        Response<String> response = boardService.addStatus(boardId, status);

        return response.isSucceed() ? ResponseEntity.ok().body(response.getMessage()) : ResponseEntity.badRequest().body(response.getMessage());
    }

    /**
     * This method removes the given status from board and deletes all items with this status
     * @param boardId
     * @param status the status we want to remove
     * @return
     */
    //todo: remove boardId since it seems redundant
    @DeleteMapping("/remove-status")
    public ResponseEntity<String> removeStatus(@RequestParam long boardId, @RequestParam String status){
    //todo: need to remove all items from item table that has this status?
        Response<String> response = boardService.removeStatus(boardId, status);

        return response.isSucceed() ? ResponseEntity.ok().body(response.getMessage()) : ResponseEntity.badRequest().body(response.getMessage());
    }


    @DeleteMapping("/remove-type")
    public ResponseEntity<String> removeType(@RequestParam long boardId, @RequestParam String type){
        Response<String> response = boardService.removeType(boardId, type);

        return response.isSucceed() ? ResponseEntity.ok().body(response.getMessage()) : ResponseEntity.badRequest().body(response.getMessage());
    }

    @GetMapping(value = "/get-board")
    public ResponseEntity<BoardDTO> getBoard(@RequestParam long boardId) {
        Response<Board> response = boardService.getBoard(boardId);
        if(response.isSucceed()){
            return ResponseEntity.ok().body(BoardDTO.createBoardDTOFromBoard(response.getData()));
        }
        
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping(value = "/assign-role-to-user")
    public ResponseEntity<String> assignRoleToUser(@RequestParam long boardId, @RequestBody AddUserRoleDTO userRoleDTO) {
//        Response<Board> response = boardService.getBoard(boardId);
        Optional<User> user = userService.getUserByEmail(userRoleDTO.getEmail());
        if(!user.isPresent()){
            return ResponseEntity.badRequest().body("Can not assign role to user - user does not exist");
        }
        Response<String> response = boardService.assignUserRole(boardId, user.get(), userRoleDTO.getRole());
        return response.isSucceed() ? ResponseEntity.ok().body(response.getMessage()) : ResponseEntity.badRequest().body(response.getMessage());
    }

//    @DeleteMapping(value = "/remove-user-role")
//    public ResponseEntity<Response<UserRoleInBoard>> removeUserRole(@RequestParam long boardId, @RequestBody AddUserRoleDTO userRoleDTO) {
//        Optional<User> user = userService.getUserByEmail(userRoleDTO.getEmail());
//        Response<UserRoleInBoard> response = boardService.removeUserRole(boardId, user.get(), userRoleDTO.getRole());
//        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
//    }

}
