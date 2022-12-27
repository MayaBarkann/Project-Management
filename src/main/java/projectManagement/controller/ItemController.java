package projectManagement.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectManagement.controller.entities.*;
import projectManagement.entities.*;
import projectManagement.repository.UserRepo;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.service.AuthService;
import projectManagement.service.NotificationService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/item")
@CrossOrigin
@RestController
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    AuthService userService;
    @Autowired
    BoardService boardService;
    @Autowired
    SocketsUtil socketsUtil;
    //TODO delete this
    @Autowired
    UserRepo userRepo;
    @Autowired
    NotificationService notificationService;

    @PostMapping(value = "/create_item")
    public ResponseEntity<String> createItem(@RequestAttribute Board board, @RequestAttribute User user, @RequestBody CreateItem item) {
        if (item == null) {
            return ResponseEntity.badRequest().body("parameter could not be null");
        }

//        if (item.getBoardId() == null) {
//            return ResponseEntity.badRequest().body("Board id could not be null");
//        }
//
//        Optional<Board> optionalBoard = boardService.getBoardById(item.getBoardId());
//        if (!optionalBoard.isPresent()) {
//            return ResponseEntity.badRequest().body("Board not found");
//        }
//
//        Optional<User> creator = userService.getUser(userId);
//        if (!creator.isPresent()) {
//            return ResponseEntity.badRequest().body("User does not exist");
//        }

        Response<String> responseStatus = boardService.statusExistsInBoard(board, item.getStatus());
        if (!responseStatus.isSucceed()) {
            return ResponseEntity.badRequest().body(responseStatus.getMessage());
        }

        Response<Item> createdItemResponse = itemService.createItem(item.getTitle(), item.getStatus(), user, board);
        if (createdItemResponse.isSucceed()) {
            socketsUtil.createItem(createdItemResponse, board.getId());
            return ResponseEntity.ok().body(createdItemResponse.getMessage());
        }

        return ResponseEntity.badRequest().body(createdItemResponse.getMessage());
//todo: add live update

    }


    @DeleteMapping(value = "/delete_item")
    public ResponseEntity<String> deleteItem(@RequestAttribute Board board, @RequestParam long itemId) {
        Response<Item> response = itemService.deleteItem(itemId, board);
        if (response.isSucceed()) {
            socketsUtil.deleteItem(response, board.getId());
            return ResponseEntity.ok().body("Item was deleted successfully");

        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }


    @PutMapping("/change_item_type")
    public ResponseEntity<String> changeType(@RequestAttribute Board board, @RequestParam long itemId, @RequestBody String type) {
        if (type == null) {
            return ResponseEntity.badRequest().body("Can not update type - type is null");
        }

        Response<String> responseType = boardService.typeExistsInBoard(board, type);

        if (!responseType.isSucceed()) {
            return ResponseEntity.badRequest().body(responseType.getMessage());
        }

        Response<Item> response = itemService.changeType(itemId, type, board);
        if (response.isSucceed()) {
            //TODO check this one
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            return ResponseEntity.ok().body("Type changed successfully");
        }

        return ResponseEntity.badRequest().body(response.getMessage());
    }


    @PutMapping("/change_item_status")
    public ResponseEntity<String> changeStatus(@RequestAttribute Board board, @RequestParam long itemId, @RequestBody String status) {

        if (!boardService.statusExistsInBoard(board, status).isSucceed()) {
            return ResponseEntity.badRequest().body("Status does not exist in board");
        }

        Response<Item> response = itemService.changeStatus(itemId, status,board);
        if (response.isSucceed()) {

            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            //TODO this should be changed to user in board.

            Set<Long> allUsersInBoard = boardService.getAllUsersInBoardByBoardId(board.getId());
            String notificationContent = "the status is changed in item" + response.getData().getTitle() + " new status is " + response.getData().getStatus();
            notificationService.sendNotification(allUsersInBoard, notificationContent, response.getData().getBoard().getId(), NotifyWhen.ITEM_STATUS_CHANGED);

            return ResponseEntity.ok().body("Status changed successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }

    }

    /**
     * @param itemId
     * @param description
     * @return
     */
    @PutMapping("/change_item_description")
    public ResponseEntity<String> changeItemDescription(@RequestAttribute Board board, @RequestParam long itemId, @RequestBody String description) {

        Response<Item> response = itemService.changeDescription(itemId, description, board);
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            return ResponseEntity.ok().body("Description has changed successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }

    }

    //todo
    @PutMapping("/change_item_assign_to_user")
    public ResponseEntity<Response<Item>> changeAssignToUser(@RequestAttribute Board board, @RequestParam long itemId, @RequestBody long userId) {
        Optional<User> assignedUser = userService.getUser(userId);
        if (!assignedUser.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("User does not exist"));
        }

        Response<UserRole> userRoleInBoardResponse = boardService.userExistsInBoard(board, assignedUser.get());
        if (!userRoleInBoardResponse.isSucceed()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse(userRoleInBoardResponse.getMessage()));
        }

        Response<Item> response = itemService.changeAssignedToUser(itemId, assignedUser.get(), board);
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


//    @RequestMapping(value = "/getItem/{itemId}", method = RequestMethod.GET)
//    public ResponseEntity<Response<Item>> getItem(@PathVariable Long itemId) {
//
//        Optional<Item> item = itemService.getItem(itemId);
//        Response<Item> response = Response.createSuccessfulResponse(item.get());
//        if (response.isSucceed()) {
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//
//
//    }

    /**
     * @param user
     * @param board
     * @param itemId
     * @param commentStr
     * @return
     */
    @PostMapping("/add_comment")
    public ResponseEntity<String> addComment(@RequestAttribute User user, @RequestAttribute Board board, @RequestParam long itemId, @RequestBody String commentStr) {
        Response<Item> response = itemService.addComment(itemId, board, user, commentStr);
        //todo: add live update
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            Set<Long> allUsersInBoard = boardService.getAllUsersInBoardByBoardId(board.getId());
            String notificationContent = "add comment" + response.getData().getTitle() + " new comment is added " + commentStr;
            notificationService.sendNotification(allUsersInBoard, notificationContent, response.getData().getBoard().getId(), NotifyWhen.ITEM_COMMENT_ADDED);
            return ResponseEntity.ok().body("Added comment successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }


    }

    /**
     * @param user
     * @param board
     * @param commentId
     * @return
     */
    @DeleteMapping("delete_comment")
    public ResponseEntity<String> deleteComment(@RequestAttribute User user, @RequestAttribute Board board, @RequestParam long commentId) {
        Response<Item> response = itemService.deleteComment(board, user, commentId);
        //todo: add live update
        return response.isSucceed() ? ResponseEntity.ok().body("Comment was deleted successfully") : ResponseEntity.badRequest().body(response.getMessage());
    }


    @PutMapping("update_importance")
    public ResponseEntity<String> updateItemImportance(@RequestAttribute User user, @RequestAttribute Board board, @RequestParam long itemId, @RequestBody ItemImportance importance) {
        Response<Item> response = itemService.updateImportance(board, user, itemId, importance);
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            return ResponseEntity.ok("Item's importance was updated successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }


    @PostMapping("create_sub_item")
    public ResponseEntity<String> createSubItem(@RequestAttribute User user, @RequestAttribute Board board, @RequestParam long parentItemId, @RequestBody String title) {
//        Optional<Item> optionalItem = itemService.getItem(parentItemId);
//        if(!optionalItem.isPresent()){
//            return ResponseEntity.badRequest().body("parentItemId does not exist");
//        }
//        Item parentItem = optionalItem.get();
        Response<Item> response = itemService.createSubItem(title, user, board, parentItemId);
        //todo: add live update
        return response.isSucceed() ? ResponseEntity.ok("Sub Item was created successfully") : ResponseEntity.badRequest().body(response.getMessage());
    }

}
