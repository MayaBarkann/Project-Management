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
    public ResponseEntity<String> createItem(@RequestParam long userId, @RequestParam long boardId, @RequestBody CreateItem item) {
        if (item == null) {
            return ResponseEntity.badRequest().body("parameter could not be null");
        }

        if (item.getBoardId() == null) {
            return ResponseEntity.badRequest().body("Board id could not be null");
        }

        Optional<Board> optionalBoard = boardService.getBoardById(item.getBoardId());
        if (!optionalBoard.isPresent()) {
            return ResponseEntity.badRequest().body("Board not found");
        }

        Optional<User> creator = userService.getUser(userId);
        if(!creator.isPresent()){
            return ResponseEntity.badRequest().body("User does not exist");
        }
        Response<Item> response = itemService.createItem(item.getTitle(), item.getStatusId(), creator.get(), optionalBoard.get());

        return response.isSucceed() ? ResponseEntity.ok().body("Item was created successfully") : ResponseEntity.badRequest().body(response.getMessage());
    }


    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteItem(@RequestParam long itemId) {
        Response<Item> response = itemService.deleteItem(itemId);

        return response.isSucceed() ? ResponseEntity.ok().body("Item was deleted successfully") : ResponseEntity.badRequest().body(response.getMessage());
    }


    @PutMapping("/change-type")
    public  ResponseEntity<String> changeType(@RequestParam long itemId, @RequestBody long typeId){
        Optional<Item> item = itemService.getItem(itemId);
        if(!item.isPresent()){
            return ResponseEntity.badRequest().body("Can not update type - Item does not exist");
        }

        Response<Type> type = boardService.typeExistsInBoard(item.get().getBoard(),typeId);

        if (!type.isSucceed()){
            return ResponseEntity.badRequest().body("Type does not exist in board");
        }

        Response<Item> response = itemService.changeType(itemId, type.getData());

        return response.isSucceed() ? ResponseEntity.ok().body("Type changed successfully") : ResponseEntity.badRequest().body(response.getMessage());
    }


    @PutMapping("/change-status")
    public  ResponseEntity<String> changeStatus(@RequestParam long itemId, @RequestBody long statusId) {
        Optional<Item> item = itemService.getItem(itemId);
        if(!item.isPresent()){
            return ResponseEntity.badRequest().body("Can not update status - Item does not exist");
        }

        Response<Status> statusResponse = boardService.statusExistsInBoard(item.get().getBoard(), statusId);

        if (!statusResponse.isSucceed()){
            return ResponseEntity.badRequest().body("Status does not exist in board");
        }

        Response<Item> response = itemService.changeStatus(itemId, statusResponse.getData());

        return response.isSucceed() ? ResponseEntity.ok().body("Status changed successfully") : ResponseEntity.badRequest().body(response.getMessage());
    }

    /**
     *
     * @param itemId
     * @param description
     * @return
     */
    @PutMapping("/change-description")
    public ResponseEntity<String> changeItemDescription(@RequestParam long itemId, @RequestBody String description){
        Response<Item> response = itemService.changeDescription(itemId, description);

        return response.isSucceed() ? ResponseEntity.ok().body("Description has changed successfully") : ResponseEntity.badRequest().body(response.getMessage());

    }

    /**
     *
     * @param itemId
     * @param userId
     * @return
     */
    @PutMapping("/change-assign-to-user")
    public ResponseEntity<Response<Item>> changeAssignToUser(@RequestParam long itemId, @RequestBody long userId){
        Optional<User> assignedUser = userService.getUser(userId);

        if(!assignedUser.isPresent()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("User does not exist"));
        }

        Optional<Item> item = itemService.getItem(itemId);
        if(!item.isPresent()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Item does not exist"));
        }

        Response<String> userRoleInBoardResponse = boardService.userExistsInBoard(item.get().getBoard(), assignedUser.get());

        if(!userRoleInBoardResponse.isSucceed()){
            return ResponseEntity.badRequest().body(Response.createFailureResponse(userRoleInBoardResponse.getMessage()));
        }

        Response<Item> response = itemService.changeAssignedToUser(itemId, assignedUser.get());

        return response.isSucceed() ? ResponseEntity.ok().body(response) : ResponseEntity.badRequest().body(response);
    }


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

    /**
     *
     * @param userId
     * @param boardId
     * @param itemId
     * @param commentStr
     * @return
     */
    @PutMapping("/add-comment")
    public ResponseEntity<String> addComment(@RequestAttribute long userId, @RequestParam long boardId, @RequestParam long itemId, @RequestBody String commentStr){
        Optional<User> user = userService.getUser(userId);
        if(!user.isPresent()){
            return ResponseEntity.badRequest().body("User does exist");
        }
        Response<Item> response = itemService.addComment(itemId, boardId ,user.get(), commentStr );
        //todo: add live update
        return response.isSucceed() ? ResponseEntity.ok().body("Added comment successfully") : ResponseEntity.badRequest().body(response.getMessage());

    }

    /**
     *
     * @param userId
     * @param boardId
     * @param commentId
     * @return
     */
    @DeleteMapping("delete-comment")
    public ResponseEntity<String> deleteComment(@RequestAttribute long userId, @RequestParam long boardId, @RequestBody long commentId){
        Optional<User> user = userService.getUser(userId);
        if(!user.isPresent()){
            return ResponseEntity.badRequest().body("User does not exist");
        }
        Response<Item> response = itemService.deleteComment(boardId ,user.get(), commentId);
        //todo: add live update
        return response.isSucceed() ? ResponseEntity.ok().body("Comment was deleted successfully") : ResponseEntity.badRequest().body(response.getMessage());
    }


//    @RequestMapping(value = "/deleteComment", method = RequestMethod.DELETE)
//    public ResponseEntity<Response<Long>> deleteComment(@RequestBody DeleteCommentDTO deleteCommentDTO) {
//
//        if (deleteCommentDTO == null || deleteCommentDTO.commentId == null) {
//            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
//        }
//
//        Response<Long> response = itemService.deleteComment(deleteCommentDTO.commentId);
//        if (response.isSucceed()) {
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//    }


}
