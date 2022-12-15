package projectManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import projectManagement.entities.Item;
import projectManagement.entities.Response;
import projectManagement.service.ItemService;

@RequestMapping("/item")
@CrossOrigin
@RestController
public class ItemController {
    @Autowired
    ItemService itemService;

    public ItemController() {
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Response<Item>> createItem(@RequestBody Item item) {

        Response<Item> res = itemService.createItem(item);
        if (res.isSucceed()) {
            return ResponseEntity.ok().body(res);
        } else {
            return ResponseEntity.badRequest().body(res);
        }

    }


}
