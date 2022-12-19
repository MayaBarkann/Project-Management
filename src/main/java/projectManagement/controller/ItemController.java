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

    public ItemController() {
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Response<Item>> createItem(@RequestBody CreateItemDTO item) {
        if (item == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }
        if (item.boardId == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Board id could not be null"));
        }

        Optional<Board> optionalBoard = boardService.getBoardById(item.boardId);
        if (!optionalBoard.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("Board not found"));
        }
        Board board = optionalBoard.get();

        if (item.creatorId == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("user creator id could not be null"));

        }

        Optional<User> optionalCreator = userService.getUser(item.creatorId);
        if (!optionalCreator.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("user creator not found"));
        }
        User creator = optionalCreator.get();

        User assignedToUser = null;
        if (item.assignedToUserId != null) {
            Optional<User> optionalAssignedToUser = userService.getUser(item.assignedToUserId);
            if (!optionalAssignedToUser.isPresent()) {
                return ResponseEntity.badRequest().body(Response.createFailureResponse("user assignedTo not found"));
            }
            assignedToUser = optionalAssignedToUser.get();

        }
        Item parentItem = null;
        if (item.parentId != null) {
            Optional<Item> optionalParentItem = itemService.getItem(item.parentId);
            if (!optionalParentItem.isPresent()) {
                return ResponseEntity.badRequest().body(Response.createFailureResponse("item parent not found"));
            }
            parentItem = optionalParentItem.get();

        }


        Response<Item> res = itemService.createItem(item, creator, board, assignedToUser, parentItem);

        if (res.isSucceed()) {
            return ResponseEntity.ok().body(res);
        } else {
            return ResponseEntity.badRequest().body(res);
        }


    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Response<Long>> deleteItem(@RequestBody ItemIdDTO deleteItemId) {

        if (deleteItemId == null || deleteItemId.itemId == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Long itemId = deleteItemId.itemId;
        Response<Long> response = itemService.deleteItem(itemId);
        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


    }


    @RequestMapping(value = "/changeType", method = RequestMethod.PUT)
    public ResponseEntity<Response<Item>> addItemType(@RequestBody AddItemType addItemType) {

        if (addItemType == null || addItemType.itemId == null || addItemType.type == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Response<Item> response = itemService.changeType(addItemType);

        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public ResponseEntity<Response<Item>> changeItemStatus(@RequestBody ChangeStatusDTO changeStatusDTO) {

        if (changeStatusDTO == null || changeStatusDTO.itemId == null || changeStatusDTO.newStatus == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Response<Item> response = itemService.changeStatus(changeStatusDTO);

        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


    }

    @RequestMapping(value = "/changeDescription", method = RequestMethod.PUT)
    public ResponseEntity<Response<Item>> changeItemDescription(@RequestBody ChangeDescriptionDTO changeDescriptionDTO) {

        if (changeDescriptionDTO == null || changeDescriptionDTO.itemId == null || changeDescriptionDTO.description == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Response<Item> response = itemService.changeDescription(changeDescriptionDTO);

        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


    }

    @RequestMapping(value = "/assignToUser", method = RequestMethod.PUT)
    public ResponseEntity<Response<Item>> changeAssignedToUser(@RequestBody AssignToUserDTO assignToUserDTO) {

        if (assignToUserDTO == null || assignToUserDTO.itemId == null || assignToUserDTO.assignedToId == null) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("parameter could not be null"));
        }

        Optional<User> foundUser = userService.getUser(assignToUserDTO.assignedToId);
        if (!foundUser.isPresent()) {
            return ResponseEntity.badRequest().body(Response.createFailureResponse("the assigned to user could not be found"));
        }
        User assignedToUser = foundUser.get();
        Response<Item> response = itemService.changeAssignedToUser(assignToUserDTO.itemId, assignedToUser);

        if (response.isSucceed()) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }


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
