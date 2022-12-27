package projectManagement.controller.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projectManagement.entities.Board;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
