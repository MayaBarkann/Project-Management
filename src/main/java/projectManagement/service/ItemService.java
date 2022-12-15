package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.repository.ItemRepo;

@Service
public class ItemService {
    @Autowired
    ItemRepo itemRepo;

    public ItemService() {
    }

    public Response<Item> createItem(Item item) {
        Item savedItem = itemRepo.save(item);
        if (savedItem == null) {
            Response.createFailureResponse("can't crate a new item");
        }
        return Response.createSuccessfulResponse(savedItem);


    }
}
