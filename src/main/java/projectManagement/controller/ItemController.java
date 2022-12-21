package projectManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.*;
import projectManagement.entities.*;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.service.UserService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/item")
@CrossOrigin
@RestController
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BoardService boardService;

    @PostMapping(value = "/create")
    public ResponseEntity<Response<Item>> createItem(@RequestParam long userId, @RequestBody CreateItem item) {
        if (item == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        if (item.getBoardId() == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Board id could not be null"));
        }

        Optional<Board> optionalBoard = boardService.getBoardById(item.getBoardId());
        if (!optionalBoard.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Board not found"));
        }

        Optional<User> creator = userService.getUser(userId);
        if(!creator.isPresent()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("User does not exist"));
        }
        Response<Item> response = itemService.createItem(item.getTitle(), item.getStatusId(), creator.get(), optionalBoard.get());

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }


    @DeleteMapping(value = "/delete")
    public ResponseEntity<Response<Long>> deleteItem(@RequestParam long itemId) {
        Response<Long> response = itemService.deleteItem(itemId);

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }


    @PutMapping("/change-type")
    public  ResponseEntity<Response<Item>> changeType(@RequestParam long itemId, @RequestBody long typeId){
        Optional<Item> item = itemService.getItem(itemId);
        if(!item.isPresent()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Can not update type - Item does not exist"));
        }

        Response<Type> type = boardService.typeExistsInBoard(item.get().getBoard(),typeId);

        if (!type.isSucceed()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Type does not exist in board"));
        }

        Response<Item> response = itemService.changeType(itemId, type.getData());

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }


    @PutMapping("/change-status")
    public  ResponseEntity<Response<Item>> changeStatus(@RequestParam long itemId, @RequestBody long statusId) {
        Optional<Item> item = itemService.getItem(itemId);
        if(!item.isPresent()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Can not update status - Item does not exist"));
        }

        Response<Status> statusResponse = boardService.statusExistsInBoard(item.get().getBoard(), statusId);

        if (!statusResponse.isSucceed()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Status does not exist in board"));
        }

        Response<Item> response = itemService.changeStatus(itemId, statusResponse.getData());

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }


    @PutMapping("/change-description")
    public ResponseEntity<Response<Item>> changeItemDescription(@RequestParam long itemId, @RequestBody String description){
        Response<Item> response = itemService.changeDescription(itemId, description);

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);

    }

    @PutMapping("/assignToUser")
    public ResponseEntity<Response<Item>> changeAssignToUser(@RequestParam long itemId, @RequestBody long userId){
        Optional<User> assignedUser = userService.getUser(userId);

        if(!assignedUser.isPresent()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("User does not exist"));
        }

        Optional<Item> item = itemService.getItem(itemId);
        if(!item.isPresent()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Item does not exist"));
        }

        Response<UserRoleInBoard> userRoleInBoardResponse = boardService.userExistsInBoard(item.get().getBoard(), assignedUser.get());

        if(!userRoleInBoardResponse.isSucceed()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse(userRoleInBoardResponse.getMessage()));
        }

        Response<Item> response = itemService.changeAssignedToUser(itemId, assignedUser.get());

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }

//    @RequestMapping(value = "/assignToUser", method = RequestMethod.PUT)
//    public ResponseEntity<Response<Item>> changeAssignedToUser(@RequestBody AssignToUserDTO assignToUserDTO) {
//
//        if (assignToUserDTO == null || assignToUserDTO.itemId == null || assignToUserDTO.assignedToId == null) {
//            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
//        }
//
//        Optional<User> foundUser = userService.getUser(assignToUserDTO.assignedToId);
//        if (!foundUser.isPresent()) {
//            return ResponseEntity.badRequest().body(Response.createFailureResponse("the assigned to user could not be found"));
//        }
//        User assignedToUser = foundUser.get();
//        Response<Item> response = itemService.changeAssignedToUser(assignToUserDTO.itemId, assignedToUser);
//
//        if (response.isSucceed()) {
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//
//
//    }

    @RequestMapping(value = "/getItems/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Item>>> getItems(@PathVariable Long boardId) {
        if (boardId == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Response<List<Item>> response = itemService.getBoardItems(boardId);

        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


    }

    @RequestMapping(value = "/getItem/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Response<Item>> getItem(@PathVariable Long itemId) {

        Optional<Item> item = itemService.getItem(itemId);
        Response<Item> response = Response.createSuccessfulResponse(item.get());
        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<Response<Comment>> addComment(@RequestBody CommentDTO commentDTO) {

        if (commentDTO == null || commentDTO.userId == null || commentDTO.itemId == null || commentDTO.comment == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }


        Optional<User> userFound = userService.getUser(commentDTO.userId);
        if (!userFound.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("the commented user could not be found"));
        }
        User user = userFound.get();

        Optional<Item> itemFound = itemService.getItem(commentDTO.itemId);
        if (!itemFound.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("the item you want to comment on could not be found"));
        }
        Item item = itemFound.get();

        Response<Comment> response = itemService.addComment(item, user, commentDTO.comment);
        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(value = "/deleteComment", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Long>> deleteComment(@RequestBody DeleteCommentDTO deleteCommentDTO) {

        if (deleteCommentDTO == null || deleteCommentDTO.commentId == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Response<Long> response = itemService.deleteComment(deleteCommentDTO.commentId);
        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
