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
import projectManagement.controller.entities.AddUserRoleDTO;
import projectManagement.controller.entities.BoardDTO;
import projectManagement.entities.Board;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.entities.UserRole;
import projectManagement.service.BoardService;
import projectManagement.service.ItemService;
import projectManagement.service.UserService;

import java.util.Optional;

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
    public void addType_InvalidInput_returnsBadRequest() {
        Board board = new Board();
        String type = "";
        Response<String> expectedResponse = Response.createFailureResponse("Type cant be empty");
        given(boardService.addType(board, type)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.addType(board, type);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void filterItems() {
    }


    @Test
    void addStatus_valid_returnOk() {
        Board board = new Board();
        String status = "status";
        Response<String> expectedResponse = Response.createSuccessfulResponse("Status added successfully");
        given(boardService.addStatus(board, status)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.addStatus(board, status);
        verify(socketsUtil).addBoardStatus(expectedResponse, board.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void addStatus_Invalid_returnBadRequest() {
        Board board = new Board();
        String status = "";
        Response<String> expectedResponse = Response.createFailureResponse("Status cannot be empty");
        given(boardService.addStatus(board, status)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.addStatus(board, status);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    void removeStatus_valid_returnOk() {
        Board board = new Board();
        String status = "status";
        Response<String> expectedResponse = Response.createSuccessfulResponse("Status added successfully");
        given(boardService.removeStatus(board, status)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.removeStatus(board, status);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void removeStatus_Invalid_returnBadRequest() {
        Board board = new Board();
        String status = "";
        Response<String> expectedResponse = Response.createFailureResponse("Status cannot be empty");
        given(boardService.removeStatus(board, status)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.removeStatus(board, status);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void removeType_valid_returnOk() {
        Board board = new Board();
        String type = "type";
        Response<String> expectedResponse = Response.createSuccessfulResponse("Status added successfully");
        given(boardService.removeType(board, type)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.removeType(board, type);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void removeType_Invalid_returnBadRequest() {
        Board board = new Board();
        String type = "type";
        Response<String> expectedResponse = Response.createFailureResponse("Status cannot be empty");
        given(boardService.removeType(board, type)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.removeType(board, type);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getBoard_validBoardId_returnBoard() {
        Board board = new Board();
        board.setId(1);
        User user = new User();
        Response<Board> expectedResponse = Response.createSuccessfulResponse(board);
        given(boardService.getBoard(user,board.getId())).willReturn(expectedResponse);
        ResponseEntity<BoardDTO> responseEntity = boardController.getBoard(user,board.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void getBoard_InvalidBoardId_returnBadRequest() {
        Board board = new Board();
        board.setId(1);
        User user = new User();
        Response<Board> expectedResponse = Response.createFailureResponse("no board id");
        given(boardService.getBoard(user,board.getId())).willReturn(expectedResponse);
        ResponseEntity<BoardDTO> responseEntity = boardController.getBoard(user,board.getId());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    void assignRoleToUser_wrongBoardId_returnBadRequest() {
        Board board = new Board();
        User user = new User();
        ResponseEntity<String> responseEntity = boardController.assignRoleToUser(board,new AddUserRoleDTO("dvir@gmail.com", UserRole.USER));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    void assignRoleToUser_BoardId_returnOk() {
        Board board = new Board();
        User user = new User();
        Response<String> expectedResponse = Response.createSuccessfulResponse("no board id");
        given(userService.getUserByEmail("dvir@gmail.com")).willReturn(Optional.of(user));
        given(boardService.assignUserRole(board,user, UserRole.USER)).willReturn(expectedResponse);
        ResponseEntity<String> responseEntity = boardController.assignRoleToUser(board,new AddUserRoleDTO("dvir@gmail.com", UserRole.USER));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}