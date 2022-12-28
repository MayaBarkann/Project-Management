package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.controller.entities.FilterItemDTO;
import projectManagement.entities.*;
import projectManagement.repository.CommentRepo;
import projectManagement.repository.ItemRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ItemService {
    @Autowired
    ItemRepo itemRepo;
    @Autowired
    CommentRepo commentRepo;


    public Response<Item> createItem(String title, String status, User creator, Board board) {
        if (creator == null) {
            return Response.createFailureResponse("Can not create item- creator user is null");
        }
        if (board == null) {
            return Response.createFailureResponse("Can not create item- board  is null");
        }
        return Response.createSuccessfulResponse(itemRepo.save(new Item(title, status, board, creator)));
    }

    public Response<Item> deleteItem(long itemId, Board board) {
        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(itemId, board, "delete");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }

        itemRepo.deleteById(itemId);
        return Response.createSuccessfulResponse(itemExistsInBoardResponse.getData());
    }

    public Response<Item> changeType(long itemId, String type, Board board) {
        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(itemId, board, "change type");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }

        Item item = itemExistsInBoardResponse.getData();
        item.setType(type);
        itemRepo.save(item);

        return Response.createSuccessfulResponse(item);
    }

    public Response<Item> changeStatus(long itemId, String status, Board board) {
        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(itemId, board, "change status");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }

        Item item = itemExistsInBoardResponse.getData();
        item.setStatus(status);
        itemRepo.save(item);
        for (Item subItem : item.getChildren()) {
            subItem.setStatus(item.getStatus());
            itemRepo.save(subItem);
        }
        //todo: add live update
        return Response.createSuccessfulResponse(item);
    }

    public Response<Item> changeDescription(long itemId, String description, Board board) {
        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(itemId, board, "change description");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }

        Item item = itemExistsInBoardResponse.getData();
        item.setDescription(description);

        return Response.createSuccessfulResponse(itemRepo.save(item));
    }


    public Response<Item> changeAssignedToUser(long itemId, User assignedToUser, Board board) {
        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(itemId, board, "change assigned user");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }

        Item item = itemExistsInBoardResponse.getData();
        item.setAssignedToUser(assignedToUser);
        item = itemRepo.save(item);

        return Response.createSuccessfulResponse(item);
    }

    /**
     * This method filters items of the given board by given properties and their values.
     * It creates ItemSpecification object
     *
     * @param filter  - properties and their values we want to filter by
     * @param boardId
     * @return Response containing the list of items that match all of the given properties
     */
    public Response<List<Item>> filterItems(FilterItemDTO filter, Long boardId) {
        ItemSpecification specification = new ItemSpecification(filter, boardId);
        return Response.createSuccessfulResponse(itemRepo.findAll(specification));
    }


    public Response<Item> addComment(long itemId, Board board, User user, String commentStr) {
        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(itemId, board, "add comment");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }

        Item item = itemExistsInBoardResponse.getData();
        Comment comment = new Comment(commentStr, user, item, LocalDateTime.now());
        commentRepo.save(comment);

        return Response.createSuccessfulResponse(item);

    }

    public Response<Item> deleteComment(Board board, User user, long commentId) {
        Optional<Comment> comment = commentRepo.findById(commentId);
        if (!comment.isPresent()) {
            return Response.createFailureResponse("Comment does not exist");
        }

        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(comment.get().getItem().getId(), board, "delete comment");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }
        Item item = itemRepo.findById(itemExistsInBoardResponse.getData().getId()).get();
        item.removeComment(comment.get());
        commentRepo.deleteById(commentId);
        //todo check again if it returns the item without the comment
        return Response.createSuccessfulResponse(item);
    }

    public Response<Item> updateImportance(Board board, User user, long itemId, ItemImportance importance) {
        if (importance == null) {
            return Response.createFailureResponse("can not update item importance");
        }

        Response<Item> itemExistsInBoardResponse = itemExistsInBoard(itemId, board, "update importance");
        if(!itemExistsInBoardResponse.isSucceed()){
            return itemExistsInBoardResponse;
        }

        Item item = itemExistsInBoardResponse.getData();
        item.setImportance(importance);
        return Response.createSuccessfulResponse(itemRepo.save(item));
    }


    public Response<Item> createSubItem(String title, User creator, Board board, long parentId) {
        Response<Item> parentExistsInBoardResponse = itemExistsInBoard(parentId, board, "create sub item");
        if(!parentExistsInBoardResponse.isSucceed()){
            return parentExistsInBoardResponse;
        }

        Item parent = parentExistsInBoardResponse.getData();
        Item subItem = itemRepo.save(Item.createItemFromParent(title, parent.getStatus(), board, creator, parent));
        //todo: check if this lines are necessary
//        parent.getChildren().add(subItem);
//        itemRepo.save(parent);
        return Response.createSuccessfulResponse(subItem);
    }

    public Optional<Item> getItem(long itemId) {
        return itemRepo.findById(itemId);
    }

    public Response<List<Item>> getAll() {
        return Response.createSuccessfulResponse(itemRepo.findAll());
    }

    public Response<List<Item>> getBoardItems(Long boardId) {
        return Response.createSuccessfulResponse(itemRepo.findByBoardId(boardId));
    }

    private Response<Item> itemExistsInBoard(long itemId, Board board, String action){
        Optional<Item> optItem = itemRepo.findById(itemId);

        if (!optItem.isPresent()) {
            return Response.createFailureResponse(String.format("Can not %s - Item does not exist", action));
        }

        if (!optItem.get().getBoard().equals(board)) {
            return Response.createFailureResponse(String.format("Can not %s - Item does not exist in board", action));
        }

        return Response.createSuccessfulResponse(optItem.get());
    }

}
