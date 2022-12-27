package projectManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import projectManagement.controller.entities.LoginBoardDTO;
import projectManagement.entities.Board;
import projectManagement.entities.Response;
import projectManagement.entities.User;
import projectManagement.entities.UserRole;
import projectManagement.repository.BoardRepo;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    BoardRepo boardRepo;

    private Board board;
    private long boardId;

    @BeforeEach
    public void createBoardSetup() {
        boardId = 1;
        board = new Board();
        board.setId(boardId);
    }

        @Test
    void testGetBoardById_withValidBoardId_returnsCorrectBoard() {
        given(boardRepo.findById(boardId)).willReturn(Optional.of(board));
        Optional<Board> result = boardService.getBoardById(boardId);
        assertTrue(result.isPresent());
        assertEquals(board, result.get());
        }
    @Test
    void testGetBoardById_withInValidBoardId_returnsnNoBoard() {
        given(boardRepo.findById(2L)).willReturn(Optional.empty());
        Optional<Board> result = boardService.getBoardById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateBoard_withValidTitleAndAdmin_createsBoardAndReturnsSuccessfulResponse() {
        String title = "Test Board";
        User admin = new User();
        Board newBoard = new Board(title, admin);
        given(boardRepo.save(newBoard)).willReturn(newBoard);
        Response<Board> result = boardService.createBoard(title, admin);
        assertTrue(result.isSucceed());
        assertEquals(newBoard, result.getData());
    }
    @Test
    public void testCreateBoard_withEmptyTitle_returnsUnsuccessfulResponse() {
        String title = "";
        User admin = new User();
        Response<Board> result = boardService.createBoard(title, admin);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }

    @Test
    public void testCreateBoard_withNullAdmin_returnsUnsuccessfulResponse() {
        String title = "Test Board";
        User admin = null;
        Response<Board> result = boardService.createBoard(title, admin);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }

    @Test
    public void testAddType_withValidBoardAndType_addsTypeToBoardAndReturnsSuccessfulResponse() {
        String type = "Test Type";
        given(boardRepo.save(board)).willReturn(board);
        Response<String> result = boardService.addType(board, type);
        assertTrue(result.isSucceed());
        assertEquals(type, result.getData());
        assertTrue(board.getTypes().contains(type));
    }


    @Test
    public void testAddType_withEmptyType_returnsUnsuccessfulResponse() {
        String type = "";
        Response<String> result = boardService.addType(board, type);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
        assertFalse(board.getTypes().contains(type));
    }

    @Test
    public void testAddType_withNullType_returnsUnsuccessfulResponse() {
        String type = null;
        Response<String> result = boardService.addType(board, type);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
        assertFalse(board.getTypes().contains(type));
    }


    @Test
    public void testAddType_withNullBoard_returnsUnsuccessfulResponse() {
        board = null;
        String type = "Test Type";
        Response<String> result = boardService.addType(board, type);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }


    @Test
    public void testAddStatus_withValidBoardAndStatus_addsStatusToBoardAndReturnsSuccessfulResponse() {
        String status = "Test Status";
        given(boardRepo.save(board)).willReturn(board);
        Response<String> result = boardService.addStatus(board, status);
        assertTrue(result.isSucceed());
        assertEquals(status, result.getData());
        assertTrue(board.getStatuses().contains(status));
    }

    @Test
    public void testAddStatus_withEmptyStatus_returnsUnsuccessfulResponse() {
        String status = "";
        Response<String> result = boardService.addStatus(board, status);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
        assertFalse(board.getStatuses().contains(status));
    }

    @Test
    public void testAddStatus_withNullStatus_returnsUnsuccessfulResponse() {
        String status = null;
        Response<String> result = boardService.addStatus(board, status);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
        assertFalse(board.getStatuses().contains(status));
    }

    @Test
    public void testAddStatus_withNullBoard_returnsUnsuccessfulResponse() {
        board = null;
        String status = "Test Status";
        Response<String> result = boardService.addStatus(board, status);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }

    @Test
    public void testRemoveStatus_withValidBoardAndStatus_removesStatusFromBoardAndReturnsSuccessfulResponse() {
        String status = "Test Status";
        board.addStatus(status);
        given(boardRepo.save(board)).willReturn(board);
        Response<String> result = boardService.removeStatus(board, status);
        assertTrue(result.isSucceed());
        assertEquals(status, result.getData());
        assertFalse(board.getStatuses().contains(status));
    }

    @Test
    public void testRemoveStatus_withEmptyStatus_returnsUnsuccessfulResponse() {
        String status = "";
        Response<String> result = boardService.removeStatus(board, status);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }

    @Test
    public void testRemoveStatus_withNullStatus_returnsUnsuccessfulResponse() {
        String status = null;
        Response<String> result = boardService.removeStatus(board, status);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }

    @Test
    public void testRemoveStatus_withNullBoard_returnsUnsuccessfulResponse() {
        board = null;
        String status = "Test Status";
        Response<String> result = boardService.removeStatus(board, status);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }
    @Test
    public void testRemoveStatus_withStatusNotInBoard_returnsUnsuccessfulResponse() {
        String status = "Test Status";
        Response<String> result = boardService.removeStatus(board, status);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
        assertFalse(board.getStatuses().contains(status));
    }


    @Test
    public void testRemoveType_withValidBoardAndType_removesTypeFromBoardAndReturnsSuccessfulResponse() {
        String type = "Test Type";
        board.addType(type);
        given(boardRepo.save(board)).willReturn(board);
        Response<String> result = boardService.removeType(board, type);
        assertTrue(result.isSucceed());
        assertEquals(type, result.getData());
        assertFalse(board.getTypes().contains(type));
    }
    @Test
    public void testRemoveType_withEmptyType_returnsUnsuccessfulResponse() {
        String type = "";
        Response<String> result = boardService.removeType(board, type);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }
    @Test
    public void testRemoveType_withNullType_returnsUnsuccessfulResponse() {
        String type = null;
        Response<String> result = boardService.removeType(board, type);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }
    @Test
    public void testRemoveType_withNullBoard_returnsUnsuccessfulResponse() {
        board = null;
        String type = "Test Type";
        Response<String> result = boardService.removeType(board, type);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }

    @Test
    public void testGetBoard_withValidUserAndBoardId_returnsSuccessfulResponse() {
        User user = new User();
        long boardId = 1L;
        board.getUserRole().put(user, UserRole.USER);
        given(boardRepo.findById(boardId)).willReturn(Optional.of(board));
        Response<Board> result = boardService.getBoard(user, boardId);
        assertTrue(result.isSucceed());
        assertEquals(board, result.getData());
    }
    @Test
    public void testGetBoard_withInvalidBoardId_returnsUnsuccessfulResponse() {
        User user = new User();
        given(boardRepo.findById(boardId)).willReturn(Optional.empty());
        Response<Board> result = boardService.getBoard(user, boardId);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }
    @Test
    public void testGetBoard_withNullUser_returnsUnsuccessfulResponse() {
        User user = null;
        Board board = new Board();
        given(boardRepo.findById(boardId)).willReturn(Optional.of(board));
        Response<Board> result = boardService.getBoard(user, boardId);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }
    @Test
    public void testGetBoard_withUserNotPartOfBoard_returnsUnsuccessfulResponse() {
        User user = new User();
        given(boardRepo.findById(boardId)).willReturn(Optional.of(board));
        Response<Board> result = boardService.getBoard(user, boardId);
        assertFalse(result.isSucceed());
        assertNull(result.getData());
    }
    @Test
    public void testTypeExistsInBoard_Success() {
        Set<String> types = new HashSet<>();
        types.add("type1");types.add("type2");types.add("type3");
        board.setTypes(types);
        String type = "type2";
        Response<String> response = boardService.typeExistsInBoard(board, type);
        assertTrue(response.isSucceed());
        assertEquals(type, response.getData());
    }
    @Test
    public void testTypeExistsInBoard_Failure() {
        Set<String> types = new HashSet<>();
        types.add("type1");types.add("type2");types.add("type3");
        board.setTypes(types);
        String type = "type4";
        Response<String> response = boardService.typeExistsInBoard(board, type);
        assertFalse(response.isSucceed());
        assertEquals("Type does not exist", response.getMessage());
    }
    @Test
    public void testTypeExistsInBoard_EmptyTypesList() {
        String type = "type1";
        Response<String> response = boardService.typeExistsInBoard(board, type);
        assertFalse(response.isSucceed());
        assertEquals("Type does not exist", response.getMessage());
    }

    @Test
    public void testStatusExistsInBoard_Success() {
        Set<String> statuses = new HashSet<>();
        statuses.add("status1");statuses.add("status2");statuses.add("status3");
        String status = "status2";
        board.setStatuses(statuses);
        Response<String> response = boardService.statusExistsInBoard(board, status);
        assertTrue(response.isSucceed());
        assertEquals(status, response.getData());
    }
    @Test
    public void testStatusExistsInBoard_NullBoard() {
        board = null;
        String status = "status1";
        Response<String> response = boardService.statusExistsInBoard(board, status);
        assertFalse(response.isSucceed());
        assertEquals("Board is null", response.getMessage());
    }
    @Test
    public void testStatusExistsInBoard_Failure() {
        Set<String> statuses = new HashSet<>();
        statuses.add("status1");statuses.add("status2");statuses.add("status3");
        String status = "status4";
        board.setStatuses(statuses);
        Response<String> response = boardService.statusExistsInBoard(board, status);
        assertFalse(response.isSucceed());
        assertEquals("Status does not exist", response.getMessage());
    }
    @Test
    public void testStatusExistsInBoard_emptyStatusSet_Failure() {
        String status = "status4";
        Response<String> response = boardService.statusExistsInBoard(board, status);
        assertFalse(response.isSucceed());
        assertEquals("Status does not exist", response.getMessage());
    }

    @Test
    public void testUserExistsInBoard_Success() {
        User user = new User();
        given(boardRepo.findById(board.getId())).willReturn(Optional.of(board));
        board.getUserRole().put(user, UserRole.USER);
        Response<UserRole> response = boardService.userExistsInBoard(board, user);
        assertTrue(response.isSucceed());
        assertEquals(UserRole.USER, response.getData());
    }
    @Test
    public void testUserExistsInBoard_NullBoard() {
        board = null;
        User user = new User();
        Response<UserRole> response = boardService.userExistsInBoard(board, user);
        assertFalse(response.isSucceed());
        assertEquals("Board does not exist", response.getMessage());
    }
    @Test
    public void testUserExistsInBoard_BoardNotInRepo() {
        User user = new User();
        given(boardRepo.findById(board.getId())).willReturn(Optional.empty());
        Response<UserRole> response = boardService.userExistsInBoard(board, user);
        assertFalse(response.isSucceed());
        assertEquals("Board does not exist", response.getMessage());
    }

    @Test
    public void testUserExistsInBoard_UserNotInBoard() {
        User user = new User();
        given(boardRepo.findById(board.getId())).willReturn(Optional.of(board));
        Response<UserRole> response = boardService.userExistsInBoard(board, user);
        assertFalse(response.isSucceed());
        assertEquals("User does not exist in board", response.getMessage());
    }

    @Test
    public void testAssignUserRole_Success() {
        User admin = new User("3","3","3");
        User user =  new User("4","4","3");
        UserRole role = UserRole.USER;
        board.setAdmin(admin);
        Response<String> response = boardService.assignUserRole(board, user, role);
        assertTrue(response.isSucceed());
    }
    @Test
    public void testAssignUserRole_NullBoard() {
        board = null;
        User user = new User();
        UserRole role = UserRole.USER;
        Response<String> response = boardService.assignUserRole(board, user, role);
        assertFalse(response.isSucceed());
    }


    @Test
    public void testRemoveUserRole_Success() {
        User user = new User();
        Response<String> response = boardService.removeUserRole(board, user);
        assertTrue(response.isSucceed());
        assertEquals("Success", response.getData());
    }
    @Test
    public void testRemoveUserRole_BoardNotInRepo() {
        User user = new User();
        board = null;
        Response<String> response = boardService.removeUserRole(board, user);
        assertFalse(response.isSucceed());
        assertEquals("Can not remove user to board- board is null", response.getMessage());
    }



    @Test
    public void testGetBoards_Success() {
        board.setTitle("hi");
        User user = new User();
        Board board2 = new Board();
        List<Board> boardList = Arrays.asList(board, board2);
        given(boardRepo.findAll()).willReturn(boardList);
        board.addUserRole(user, UserRole.USER);
        Response<List<LoginBoardDTO>> response = boardService.getBoards(user);
        assertTrue(response.isSucceed());
        List<LoginBoardDTO> result = response.getData();
        assertEquals(1, result.size());
        assertEquals(board.getTitle(), result.get(0).getTitle());
    }


    @Test
    public void testGetBoards_EmptyBoardList() {
        User user = new User();
        given(boardRepo.findAll()).willReturn(Collections.emptyList());
        Response<List<LoginBoardDTO>> response = boardService.getBoards(user);
        assertTrue(response.isSucceed());
        List<LoginBoardDTO> result = response.getData();
        assertEquals(0, result.size());
    }


}