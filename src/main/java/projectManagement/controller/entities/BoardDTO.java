package projectManagement.controller.entities;

import lombok.Getter;
import lombok.Setter;
import projectManagement.entities.Board;
import projectManagement.entities.Item;
import projectManagement.entities.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class BoardDTO {
    private String title;
    private Set<UserDTO> users = new HashSet<>();
    private List<Item> items = new ArrayList<>();
    private Set<String> statuses = new HashSet<>();
    private Set<String> types = new HashSet<>();

    public static BoardDTO createBoardDTOFromBoard(Board board){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.title = board.getTitle();
        boardDTO.statuses.addAll(board.getStatuses());
        boardDTO.types.addAll(board.getTypes());
        boardDTO.users = board.getUserRole().keySet().stream().map(UserDTO::new).collect(Collectors.toSet());
        boardDTO.items.addAll(board.getItems());
        return boardDTO;
    }
}
