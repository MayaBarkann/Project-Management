package projectManagement.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.CommentDTO;
import projectManagement.controller.entities.FilterItemDTO;
import projectManagement.controller.entities.StatusDTO;
import projectManagement.entities.*;
import projectManagement.repository.StatusRepo;
import projectManagement.repository.TypeRepo;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.service.UserService;
import projectManagement.utils.Validation;

import javax.swing.text.html.Option;
import java.time.LocalDate;
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
    public ResponseEntity<Response<Board>> createBoard(@RequestAttribute long userId, @RequestBody String title){
        Optional<User> admin = userService.getUser(userId);
        logger.info("In BoardController - creating new board");
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
    public ResponseEntity<Response<Type>> addType(@RequestParam long boardId, @RequestBody String type){
        Response<Type> response = boardService.addType(boardId, type);

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }

    /**
     * This method creates new status and add it to the given board
     * @param boardId
     * @param status
     * @return response entity with successful response containing the new Status created if the given board exists,
     * otherwise returns bad request containing failure Response with the reason for failure.
     */
    //todo: add live changes with sockets?
    @PostMapping("/add-status")
    public ResponseEntity<Response<Status>> addStatus(@RequestParam long boardId, @RequestBody String status){
        Response<Status> response = boardService.addStatus(boardId, status);

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }

    /**
     * This method removes the given status from board and deletes all items with this status
     * @param boardId
     * @param statusId id of the status we want to remove
     * @return
     */
    //todo: remove boardId since it seems redundant
    @DeleteMapping("/remove-status")
    public ResponseEntity<Response<Long>> removeStatus(@RequestParam long boardId, @RequestParam long statusId){
    //todo: need to remove all items from item table that has this status?
        Response<Long> response = boardService.removeStatus(statusId);

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }

    /**
     *
     * @param boardId
     * @param typeId
     * @return
     */
    @DeleteMapping("/remove-type")
    public ResponseEntity<Response<Long>> removeType(@RequestParam long boardId, @RequestParam long typeId){
        Response<Long> response = boardService.removeType(typeId);

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<Response<Board>> getItems(@RequestParam long boardId) {
        Response<Board> response = boardService.getBoard(boardId);

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }


//    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = "application/json")
//    public ResponseEntity<Response> create(@RequestBody Board board) {
//        // TODO: we need validation when create a board?
//            Response<Board> response = boardService.createBoard(board);
//            if (response.isSucceed()) {
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
//    }
//
//    @RequestMapping(value = "delete", method = RequestMethod.DELETE, consumes = "application/json")
//    public ResponseEntity<Response> delete(@RequestParam long boardId) {
//        if(Validation.validateBoard(boardId)) {
//            Response<Board> response = boardService.delete(boardId);
//            if (response.isSucceed()) {
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
//    }
//
//    @RequestMapping(value = "rename", method = RequestMethod.PATCH, consumes = "application/json")
//    public ResponseEntity<Response> rename(@RequestParam long boardId,@RequestParam String name) {
//        if(Validation.validateBoard(boardId)) {
//            Response<Board> response = boardService.rename(boardId,name);
//            if (response.isSucceed()) {
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
//    }
//
//    @RequestMapping(value = "addItem", method = RequestMethod.POST, consumes = "application/json")
//    public ResponseEntity<Response> addItem(@RequestBody Item item, @RequestParam long boardId) {
//        if(Validation.validateItem(item)) {
//            Response<Item> response = itemService.addItem(boardId,item);
//            if (response.isSucceed()) {
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
//    }
//
//    @RequestMapping(value = "removeItem", method = RequestMethod.DELETE, consumes = "application/json")
//    public ResponseEntity<Response> removeItem(@RequestBody Item item, @RequestParam long boardId) {
//        if(Validation.validateItem(item)) {
//            Response<Item> response = itemService.removeItem(boardId,item);
//            if (response.isSucceed()) {
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
//    }
//
//    @RequestMapping(value = "assignItemToUser", method = RequestMethod.POST, consumes = "application/json")
//    public ResponseEntity<Response> assignItemToUser(@RequestBody Item item, @RequestParam long boardId, @RequestParam long userId) {
//        if(Validation.validateItem(item)) {
//            Response<Item> response = itemService.assignItemToUser(boardId,userId,item);
//            if (response.isSucceed()) {
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(Response.createFailureResponse("bad input"), HttpStatus.BAD_REQUEST);
//    }


}
