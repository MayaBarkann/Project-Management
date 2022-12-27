package projectManagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import projectManagement.controller.entities.LoginBoardDTO;
import projectManagement.entities.Board;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.BoardService;
import projectManagement.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    BoardService boardService;

    private User user;
    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void getBoards_returnListOfBoards() {
        given(boardService.getBoards(user)).willReturn(Response.createSuccessfulResponse(
                Arrays.asList(new LoginBoardDTO( "Board 1",1),
                        new LoginBoardDTO("Board 2",2))));
        ResponseEntity<List<LoginBoardDTO>> response = userController.getBoards(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).size(),2);
    }
    @Test
    void getBoards_returnListOfBoardsMadeFromBoard() {
        Board b = new Board();
        given(boardService.getBoards(user)).willReturn(Response.createSuccessfulResponse(
                Arrays.asList(LoginBoardDTO.createLoginBoardDTOFromBoard(b),
                        new LoginBoardDTO("Board 2",2))));
        ResponseEntity<List<LoginBoardDTO>> response = userController.getBoards(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).size(),2);
    }
    @Test
    void getBoards_returnEmptyListOfBoards() {
        given(boardService.getBoards(user)).willReturn(Response.createSuccessfulResponse(
                List.of()));
        ResponseEntity<List<LoginBoardDTO>> response = userController.getBoards(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).size(),0);
    }


}