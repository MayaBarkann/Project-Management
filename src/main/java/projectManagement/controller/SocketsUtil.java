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


    /**
     *
     * @param response response to send to other users
     * @param boardId board id
     */
    public void createItem(Response response, long boardId) {
        System.out.println("Sending item update" + response);
        template.convertAndSend("/topic/createItem/" + boardId, response.getData());

    }
    /**
     *
     * @param item response to send to other users
     * @param BoardId board id
     */
    public void updateItem(Item item, Long BoardId) {
        System.out.println("Sending item update" + item);
        template.convertAndSend("/topic/updateItem/" + BoardId, item);

    }
    /**
     *
     * @param userId user id to send the notification to
     * @param notificationContent notification string
     */
    public void pushNotification(Long userId, String notificationContent) {
        template.convertAndSend("/topic/notification/" + userId, notificationContent);

    }

    /**
     *
     * @param response response to send to other users
     * @param boardId board id
     */
    public void deleteItem(Response response, long boardId) {
        System.out.println("Sending item update" + response);
        template.convertAndSend("/topic/deleteItem/" + boardId, response.getData());

    }

    /**
     *
     * @param response response to send to other users
     * @param boardId board id
     */
    public void addBoardType(Response response, long boardId) {
        System.out.println("board type added: " + response);
        template.convertAndSend("/topic/addBoardType/" + boardId, response.getData());

    }

    /**
     *
     * @param response response to send to other users
     * @param boardId board id
     */
    public void deleteBoardType(Response response, long boardId) {
        System.out.println("board type added: " + response);
        template.convertAndSend("/topic/deleteBoardType/" + boardId, response.getData());

    }

    /**
     *
     * @param response response to send to other users
     * @param boardId board id
     */
    public void addBoardStatus(Response response, long boardId) {
        System.out.println("board status added: " + response);
        template.convertAndSend("/topic/addBoardStatus/" + boardId, response.getData());

    }

    /**
     *
     * @param response response to send to other users
     * @param boardId board id
     */
    public void deleteBoardStatus(Response response, long boardId) {
        System.out.println("board type added: " + response);
        template.convertAndSend("/topic/deleteBoardStatus/" + boardId, response.getData());

    }

}
