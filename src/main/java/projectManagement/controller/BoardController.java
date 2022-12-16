package projectManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projectManagement.entities.Board;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.utils.Validation;

@RequestMapping(value = "/board")
@RestController
public class BoardController {


    @Autowired
    ItemService itemService;
    @Autowired
    BoardService boardService;
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
