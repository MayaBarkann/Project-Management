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


    public void createItem(Response response, long boardId) {
        System.out.println("Sending item update" + response);
        template.convertAndSend("/topic/createItem/" + boardId, response.getData());

    }

    public void updateItem(Item item, Long BoardId) {
        System.out.println("Sending item update" + item);
        template.convertAndSend("/topic/updateItem/" + BoardId, item);

    }

    public void pushNotification(Long userId, String notificationContent) {
        template.convertAndSend("/topic/notification/" + userId, notificationContent);

    }


    public void deleteItem(Response response, long boardId) {
        System.out.println("Sending item update" + response);
        template.convertAndSend("/topic/deleteItem/" + boardId, response.getData());

    }

    public void addBoardType(Response response, long boardId) {
        System.out.println("board type added: " + response);
        template.convertAndSend("/topic/addBoardType/" + boardId, response.getData());

    }

    public void deleteBoardType(Response response, long boardId) {
        System.out.println("board type added: " + response);
        template.convertAndSend("/topic/deleteBoardType/" + boardId, response.getData());

    }

    public void addBoardStatus(Response response, long boardId) {
        System.out.println("board status added: " + response);
        template.convertAndSend("/topic/addBoardStatus/" + boardId, response.getData());

    }

    public void deleteBoardStatus(Response response, long boardId) {
        System.out.println("board type added: " + response);
        template.convertAndSend("/topic/deleteBoardStatus/" + boardId, response.getData());

    }

}
