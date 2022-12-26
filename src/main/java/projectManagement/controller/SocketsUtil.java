package projectManagement.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import projectManagement.entities.Item;
import projectManagement.entities.Response;

@Component
public class SocketsUtil {

    private final SimpMessagingTemplate template;

    public SocketsUtil(SimpMessagingTemplate template) {
        this.template = template;
    }


    public Response createItem(Response response, long boardId) {
        System.out.println("Sending item update" + response);
        template.convertAndSend("/topic/createItem/" + boardId, response.getData());
        return response;
    }

    public void updateItem(Item item, Long userId, Long BoardId) {
        System.out.println("Sending item update" + item);
        template.convertAndSend("/topic/updateItem/" + userId + "/" + BoardId, item);

    }

    //sub to notify in client
//    public void pushNotification(Long userId, String notificationContent) {
//        template.convertAndSend("/topic/notification/" + userId, notificationContent);
//
//    }


    public Response deleteItem(Response response) {
        System.out.println("Sending item update" + response);
        template.convertAndSend("/topic/deleteItem", response.getData());
        return response;
    }

}
