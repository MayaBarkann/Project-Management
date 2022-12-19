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
     * This function filters item of the given board by given properties and values.
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
     * This function creates a new board with the given user as the admin.
     * @param userId the admin of the new board
     * @param title the title of the new board
     * @return Response entity with successful response containing the new created board if the given user exists,
     * otherwise returns bad request with the reason.
     */
    //todo: change userId to requestAttribute
    @PostMapping("/create-board")
    public ResponseEntity<Response<Board>> createBoard(@RequestAttribute long userId, @RequestBody String title){
        Optional<User> admin = userService.getUser(userId);
        logger.info("In BoardController - creating new board");
        if (!admin.isPresent()){
            logger.error("In BoardController - can not create new board since this user does not exist");
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Can not create board - user does not exist"));
        }
        return ResponseEntity.status(201).body(boardService.createBoard(title, admin.get()));
    }

    @PostMapping("/add-type")
    public ResponseEntity<Response<Type>> addType(@RequestParam long boardId, @RequestBody String status){
        if (status == null || status.isEmpty()){
            logger.error("In BoardController - failed to create new status since it is empty or null");
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Can not create empty status"));
        }

        Optional<Board> board = boardService.getBoardById(boardId);
        if(!board.isPresent()){
            logger.error("In BoardController - failed to create new status since it is empty or null");
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Can not create status - board does not exist"));
        }

    }



    @RequestMapping(value = "/addStatus", method = RequestMethod.POST)
    public ResponseEntity<Response<Status>> addStatus(@RequestBody StatusDTO statusDTO) {

        if (statusDTO == null || statusDTO.boardId == null || statusDTO.status == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }


        Optional<Board> boardFound = boardService.getBoardById(statusDTO.boardId);
        if (!boardFound.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("the board could not be found"));
        }
        Board board = boardFound.get();

        Response<Status> response = boardService.addStatus(board, statusDTO.status);
        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


    }

    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<Response<Board>> getItems(@PathVariable Long boardId) {
        if (boardId == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Optional<Board> board = boardService.getBoardById(boardId);
        if (!board.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("the board could not be found"));
        }
        Response<Board> response = Response.createSuccessfulResponse(board.get());

        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


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
