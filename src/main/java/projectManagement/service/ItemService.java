package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.ItemRepo;

@Service
public class ItemService {
    @Autowired
    ItemRepo itemRepo;
    @Autowired
    BoardRepo boardRepo;
    public ItemService() {
    }

    public Response<Item> createItem(Item item) {
        Item savedItem = itemRepo.save(item);
        if (savedItem == null) {
            Response.createFailureResponse("can't crate a new item");
        }
        return Response.createSuccessfulResponse(savedItem);
    }

    public Response<Item> addItem(long boardId,Item item) {
        if(boardRepo.findById(boardId).isPresent()){
            //TODO: add item to board
            return Response.createSuccessfulResponse(item);
        }
        return Response.createFailureResponse("no board id like that");
    }
    public Response<Item> removeItem(long boardId,Item item) {
        if(boardRepo.findById(boardId).isPresent()){
            //TODO: removeItem from board
            return Response.createSuccessfulResponse(item);
        }
        return Response.createFailureResponse("no board id like that");
    }

    public Response<Item> assignItemToUser(long boardId,long userId,Item item) {
        if(boardRepo.findById(boardId).isPresent()){
            //TODO: assignItemToUser
            return Response.createSuccessfulResponse(item);
        }
        return Response.createFailureResponse("no board id like that");
    }
}
