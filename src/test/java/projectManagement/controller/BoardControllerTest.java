package projectManagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import projectManagement.entities.Board;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {
    @InjectMocks
    BoardController boardController;
    @Mock
    ItemService itemService;
    @Mock
    BoardService boardService;
    @Mock
    UserService userService;
    @Mock
    SocketsUtil socketsUtil;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }


    @Test
    public void createBoard_validInput_returnsSuccessResponse() {
        String title = "My Board";
        Response<Board> expectedResponse = Response.createSuccessfulResponse(new Board());
        given(boardService.createBoard(title, user)).willReturn(expectedResponse);
        ResponseEntity<Response<Board>> responseEntity = boardController.createBoard(user, title);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
    @Test
    public void createBoard_emptyTitle_returnsBadRequestResponse() {
        String title = "";
        ResponseEntity<Response<Board>> responseEntity = boardController.createBoard(user, title);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Title must not be empty", responseEntity.getBody().getMessage());
    }

    @Test
    public void addType_validInput_returnsSuccessResponse() {
        Board board = new Board();
        String type = "My Type";
        Response<String> expectedResponse = Response.createSuccessfulResponse("Type added successfully");
        given(boardService.addType(board, type)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.addType(board, type);
        verify(socketsUtil).addBoardType(expectedResponse, board.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }




    @Test
    void filterItems() {
    }


    @Test
    void addStatus() {
    }

    @Test
    void removeStatus() {
    }

    @Test
    void removeType() {
    }

    @Test
    void getBoard() {
    }

    @Test
    void assignRoleToUser() {
    }
}