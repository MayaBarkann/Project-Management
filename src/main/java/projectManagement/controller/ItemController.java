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
    //TODO change to auth service
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

    /**
     * Creates an item for a given board.
     *
     * @param board The board for which the item will be created.
     * @param user  The user who is creating the item.
     * @param item  The item to be created, containing the following properties:
     *              title: The title of the item.
     *              status: The status of the item.
     * @return ResponseEntity object with a body containing a message about the success or failure of the operation.
     */
    @PostMapping(value = "/create_item")
    public ResponseEntity<String> createItem(@RequestAttribute Board board, @RequestAttribute User user, @RequestBody CreateItem item) {
        if (item == null) {
            return ResponseEntity.badRequest().body("parameter could not be null");
        }
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
    }

    /**
     * Deletes an item for a given board.
     *
     * @param board  The board for which the item will be deleted.
     * @param itemId The ID of the item to be deleted.
     * @return ResponseEntity object with a body containing a message about the success or failure of the operation.
     */
    @DeleteMapping(value = "/delete_item")
    public ResponseEntity<String> deleteItem(@RequestAttribute Board board, @RequestParam long itemId) {
        //TODO check if the item is exist before deleting.
        Response<Item> response = itemService.deleteItem(itemId, board);
        if (response.isSucceed()) {
            socketsUtil.deleteItem(response, board.getId());

            Set<Long> allUsersInBoard = boardService.getAllUsersInBoardByBoardId(board.getId());
            String notificationContent = "Item deleted" + response.getData().getTitle();
            notificationService.sendNotification(allUsersInBoard, notificationContent, NotifyWhen.ITEM_DELETED);
            return ResponseEntity.ok().body("Item was deleted successfully");

        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    /**
     * Changes the type of item for a given board.
     *
     * @param board  The board for which the item's type will be changed.
     * @param itemId The ID of the item whose type will be changed.
     * @param type   The new type for the item.
     * @return ResponseEntity object with a body containing a message about the success or failure of the operation.
     */
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
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            return ResponseEntity.ok().body("Type changed successfully");
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    /**
     * Changes the status of an item for a given board.
     *
     * @param board  The board for which the item's type will be changed.
     * @param itemId The ID of the item whose status will be changed.
     * @param status The new status for the item.
     * @return ResponseEntity object with a body containing a message about the success or failure of the operation.
     */
    @PutMapping("/change_item_status")
    public ResponseEntity<String> changeStatus(@RequestAttribute Board board, @RequestParam long itemId, @RequestBody String status) {
        if (!boardService.statusExistsInBoard(board, status).isSucceed()) {
            return ResponseEntity.badRequest().body("Status does not exist in board");
        }
        Response<Item> response = itemService.changeStatus(itemId, status, board);
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            Set<Long> allUsersInBoard = boardService.getAllUsersInBoardByBoardId(board.getId());
            String notificationContent = "the status is changed in item" + response.getData().getTitle() + " new status is " + response.getData().getStatus();
            notificationService.sendNotification(allUsersInBoard, notificationContent, NotifyWhen.ITEM_STATUS_CHANGED);
            return ResponseEntity.ok().body("Status changed successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    /**
     * Changes the description of an item.
     *
     * @param board       The board that the item belongs to.
     * @param itemId      The ID of the item to change the description for.
     * @param description The new description for the item.
     * @return An HTTP response with a message indicating whether the operation was successful or not.
     */
    @PutMapping("/change_item_description")
    public ResponseEntity<String> changeItemDescription(@RequestAttribute Board board, @RequestParam long itemId, @RequestBody String description) {
        Response<Item> response = itemService.changeDescription(itemId, description, board);
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            Set<Long> allUsersInBoard = boardService.getAllUsersInBoardByBoardId(board.getId());
            String notificationContent = "the data is changed in item" + response.getData().getTitle() + " new data is " + response.getData().getDescription();
            notificationService.sendNotification(allUsersInBoard, notificationContent, NotifyWhen.ITEM_DATA_CHANGED);

            return ResponseEntity.ok().body("Description has changed successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    /**
     * Changes the user that an item is assigned to.
     *
     * @param board  The board that the item belongs to.
     * @param itemId The ID of the item to change the assigned user for.
     * @param userId The ID of the user to assign the item to.
     * @return An HTTP response with a message indicating whether the operation was successful or not.
     */
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
            //TODO check this function it the user got notification
            Set<Long> allUsersInBoard = boardService.getAllUsersInBoardByBoardId(board.getId());
            String notificationContent = "the status is changed in item" + response.getData().getTitle() + " new status is " + response.getData().getStatus();
            notificationService.sendNotification(allUsersInBoard, notificationContent, NotifyWhen.ITEM_ASSIGNED_TO_ME);
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
     * Adds a comment to an item.
     *
     * @param user       The user adding the comment.
     * @param board      The board that the item belongs to.
     * @param itemId     The ID of the item to add the comment to.
     * @param commentStr The content of the comment.
     * @return An HTTP response with a message indicating whether the operation was successful or not.
     */
    @PostMapping("/add_comment")
    public ResponseEntity<String> addComment(@RequestAttribute User user, @RequestAttribute Board board, @RequestParam long itemId, @RequestBody String commentStr) {
        Response<Item> response = itemService.addComment(itemId, board, user, commentStr);
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            Set<Long> allUsersInBoard = boardService.getAllUsersInBoardByBoardId(board.getId());
            String notificationContent = "add comment" + response.getData().getTitle() + " new comment is added " + commentStr;
            notificationService.sendNotification(allUsersInBoard, notificationContent, NotifyWhen.ITEM_COMMENT_ADDED);
            return ResponseEntity.ok().body("Added comment successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    /**
     * Deletes a comment from an item.
     * The method deletes the comment with the specified ID and updates the item.
     *
     * @param user      The user deleting the comment. This is a request attribute that represents the user deleting the comment.
     * @param board     The board that the item belongs to. This is a request attribute that represents the board that the item belongs to.
     * @param commentId The ID of the comment to delete.
     * @return ResponseEntity object with a body containing a message about the success or failure of the operation.
     */
    @DeleteMapping("delete_comment")
    public ResponseEntity<String> deleteComment(@RequestAttribute User user, @RequestAttribute Board board, @RequestParam long commentId) {
        Response<Item> response = itemService.deleteComment(board, user, commentId);
        if (response.isSucceed()) {
            socketsUtil.updateItem(response.getData(), response.getData().getBoard().getId());
            return ResponseEntity.ok().body("Comment was deleted successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    /**
     * Updates the importance of an item.
     * The method updates the importance of the item with the specified ID and updates the item.
     *
     * @param user       The user updating the item's importance. This is a request attribute that represents the user updating the item's importance.
     * @param board      The board that the item belongs to. This is a request attribute that represents the board that the item belongs to.
     * @param itemId     The ID of the item to update the importance for.
     * @param importance The new importance for the item.
     * @return ResponseEntity object with a body containing a message about the success or failure of the operation.
     */
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

    /**
     * Creates a sub item for a parent item.
     *
     * @param user         The user creating the sub item. This is a request attribute that represents the user creating the sub item.
     * @param board        The board that the parent item belongs to. This is a request attribute that represents the board that the parent item belongs to.
     * @param parentItemId The ID of the parent item to create the sub item for.
     * @param title        The title of the sub item.
     * @return ResponseEntity object with a body containing a message about the success or failure of the operation.
     */
    @PostMapping("create_sub_item")
    public ResponseEntity<String> createSubItem(@RequestAttribute User user, @RequestAttribute Board board, @RequestParam long parentItemId, @RequestBody String title) {
        Response<Item> response = itemService.createSubItem(title, user, board, parentItemId);
        if (response.isSucceed()) {
            socketsUtil.createItem(response, response.getData().getBoard().getId());
            return ResponseEntity.ok("Sub Item was created successfully");
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

}
