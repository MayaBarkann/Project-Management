package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.controller.entities.CreateItemDTO;
import projectManagement.entities.Board;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.repository.ItemRepo;

import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    ItemRepo itemRepo;

    public ItemService() {
    }

    public Response<Item> createItem(CreateItemDTO item, User creator, Board board, User assignedToUser, Item parentItem) {

        Item newItem = new Item(item.title, item.status, item.importance, item.type, parentItem, board, creator, assignedToUser);
        Item savedItem = itemRepo.save(newItem);
        if (savedItem == null) {
            Response.createFailureResponse("can't crate a new item");
        }

        return Response.createSuccessfulResponse(savedItem);

    }

    public Response<Long> deleteItem(Long itemId) {
        Optional<Item> itemFound = itemRepo.findById(itemId);

        if (!itemFound.isPresent()) {
            return Response.createFailureResponse("the item you want to delete doesn't exists");
        }

        itemRepo.deleteById(itemId);
        return Response.createSuccessfulResponse(itemId);
    }

    public Optional<Item> getItem(long itemId) {
        return itemRepo.findById(itemId);
    }
}
