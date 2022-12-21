package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.controller.entities.*;
import projectManagement.entities.*;
import projectManagement.repository.CommentRepo;
import projectManagement.repository.ItemRepo;
import projectManagement.repository.StatusRepo;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    ItemRepo itemRepo;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    StatusRepo statusRepo;


    public ItemService() {
    }

//    public Response<Item> createItem(CreateItemDTO item, User creator, Board board, User assignedToUser, Item parentItem) {
//
//        Item newItem = new Item(item.title, item.status, item.importance, item.type, parentItem, board, creator, assignedToUser);
//        Item savedItem = itemRepo.save(newItem);
//        if (savedItem == null) {
//            Response.createFailureResponse("can't crate a new item");
//        }
//        return Response.createSuccessfulResponse(savedItem);
//
//
//    }

    public Response<Item> createItem(String title, long statusId, User creator, Board board){
        Optional<Status> status = statusRepo.findById(statusId);

        if(!status.isPresent()){
            return Response.createFailureResponse("Can not create item - status does not exist");
        }

        if(!status.get().getBoard().equals(board)){
            return Response.createFailureResponse("Can not create item - invalid status");
        }

        return Response.createSuccessfulResponse(itemRepo.save(new Item(title, status.get(), board, creator)));
    }


    public Response<Long> deleteItem(long itemId) {
        Optional<Item> itemFound = itemRepo.findById(itemId);

        if (!itemFound.isPresent()) {
            return Response.createFailureResponse("Item doesn't exist");
        }

        itemRepo.deleteById(itemId);
        return Response.createSuccessfulResponse(itemId);
    }

//    public Response<Item> changeType(AddItemType addItemType) {
//        Optional<Item> itemFound = itemRepo.findById(addItemType.itemId);
//        if (!itemFound.isPresent()) {
//            return Response.createFailureResponse("the item doesn't exist");
//        }
//        Item item = itemFound.get();
//        item.setType(addItemType.type);
//
//        Item savedItem = itemRepo.save(item);
//
//
//        return Response.createSuccessfulResponse(savedItem);
//    }

//    public Response<Item> changeStatus(ChangeStatusDTO changeStatusDTO) {
//        Optional<Item> itemFound = itemRepo.findById(changeStatusDTO.itemId);
//        if (!itemFound.isPresent()) {
//            return Response.createFailureResponse("the item doesn't exist");
//        }
//        Item item = itemFound.get();
//        item.setStatus(changeStatusDTO.newStatus);
//
//        Item savedItem = itemRepo.save(item);
//
//
//        return Response.createSuccessfulResponse(savedItem);
//    }

    public Response<Item> changeDescription(ChangeDescriptionDTO changeDescriptionDTO) {
        Optional<Item> itemFound = itemRepo.findById(changeDescriptionDTO.itemId);
        if (!itemFound.isPresent()) {
            return Response.createFailureResponse("the item doesn't exist");
        }
        Item item = itemFound.get();
        item.setDescription(changeDescriptionDTO.description);

        Item savedItem = itemRepo.save(item);


        return Response.createSuccessfulResponse(savedItem);
    }

    public Response<Item> changeAssignedToUser(Long itemId, User assignedToUser) {
        Optional<Item> itemFound = itemRepo.findById(itemId);
        if (!itemFound.isPresent()) {
            return Response.createFailureResponse("the item doesn't exist");
        }
        Item item = itemFound.get();


        item.setAssignedToUser(assignedToUser);

        Item savedItem = itemRepo.save(item);


        return Response.createSuccessfulResponse(savedItem);
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

    public Response<Comment> addComment(Item item, User user, String comment) {
        Comment commentObj = new Comment(comment, user, item, LocalDateTime.now());
        Comment savedComment = commentRepo.save(commentObj);


        return Response.createSuccessfulResponse(savedComment);
    }

    public Response<Long> deleteComment(Long commentId) {
        Optional<Comment> commentFound = commentRepo.findById(commentId);

        if (!commentFound.isPresent()) {
            return Response.createFailureResponse("the comment doesn't exist");
        }

        commentRepo.deleteById(commentId);
        return Response.createSuccessfulResponse(commentId);
    }
}
