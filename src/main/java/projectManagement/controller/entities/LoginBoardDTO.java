package projectManagement.controller.entities;

import lombok.Getter;
import lombok.Setter;
import projectManagement.entities.Board;

import java.util.stream.Collectors;

@Setter
@Getter
public class LoginBoardDTO {
    private String title;
    private long id;

    public static LoginBoardDTO createLoginBoardDTOFromBoard(Board board){
        LoginBoardDTO boardDTO = new LoginBoardDTO();
        boardDTO.title = board.getTitle();
        boardDTO.id = board.getId();
        return boardDTO;
    }

}
