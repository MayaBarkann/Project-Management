package projectManagement.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectManagement.controller.entities.BoardDTO;
import projectManagement.controller.entities.LoginBoardDTO;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());
    @Autowired
    BoardRepo boardRepo;

    /**
     * Gets a board with a given ID.
     *
     * @param boardId The ID of the board to retrieve.
     * @return An optional containing the board with the given ID.
     */
    public Optional<Board> getBoardById(long boardId) {
        return boardRepo.findById(boardId);
    }

    /**
     * This function creates a new board with title and assigning the user to be the admin of this new board.
     * It saves the new board in BoardRepo and saving the given user with role Admin and the new board created in UserRoleInBoardRepo
     *
     * @param title the title of the created board
     * @param admin the admin of the created board
     * @return the new created board
     */
    public Response<Board> createBoard(String title, User admin) {
        if (title == null || title.equals("") || admin == null) {
            logger.error("In BoardService - failed to create new Board since title is empty or null");
            return Response.createFailureResponse("Can not create empty Board");
        }
        Board newBoard = boardRepo.save(new Board(title, admin));
        return Response.createSuccessfulResponse(newBoard);
    }

    /**
     * Adds a new status to a board.
     *
     * @param board  The board to which the status will be added.
     * @param status The new status to be added.
     * @return A response object indicating the success or failure of the request.
     * BadRequest if the status is empty or null, or if the board is null.
     */
    public Response<String> addStatus(Board board, String status) {
        if (status == null || status.isEmpty()) {
            logger.error("In BoardService - failed to create new status since it is empty or null");
            return Response.createFailureResponse("Can not create empty status");
        }
        if (board == null) {
            return Response.createFailureResponse("Can not create status- board is null");
        }
        board.addStatus(status);
        boardRepo.save(board);
        //todo live update
        return Response.createSuccessfulResponse(status);
    }

    /**
     * Adds a type to a board.
     *
     * @param board The board to which the type will be added.
     * @param type  The type to be added.
     * @return A response object indicating the success or failure of the request.
     * Bad response if the type is empty or null.
     */
    public Response<String> addType(Board board, String type) {
        if (type == null || type.isEmpty()) {
            logger.error("In BoardService - failed to create new type since it is empty or null");
            return Response.createFailureResponse("Can not create empty type");
        }
        if (board == null) {
            return Response.createFailureResponse("Board can not be null");
        }
        board.addType(type);
        boardRepo.save(board);
        return Response.createSuccessfulResponse(type);
    }

    /**
     * Removes a status from a board.
     *
     * @param board  The board from which the status will be removed.
     * @param status The status to be removed.
     * @return A response object indicating the success or failure of the request.
     * <p>
     * Bad response if the status is empty or null, or if the board is null.
     */
    public Response<String> removeStatus(Board board, String status) {
        if (status == null || status.isEmpty()) {
            logger.error("In BoardService - failed to delete status since it is empty or null");
            return Response.createFailureResponse("Can not delete empty status");
        }

        if (board == null) {
            return Response.createFailureResponse("Can not delete status- board is null");
        }
        if (board.getStatuses().contains(status)) {
            board.removeStatus(status);
            boardRepo.save(board);
            return Response.createSuccessfulResponse(status);
        }
        return Response.createFailureResponse("Can not remove status since it does not exist in board");
    }

    /**
     * Removes a type from a board.
     *
     * @param board The board from which the type will be removed.
     * @param type  The type to be removed.
     * @return A response object indicating the success or failure of the request.
     * <p>
     * Bad response if the type is empty or null, or if the board is null.
     */
    public Response<String> removeType(Board board, String type) {
        if (type == null || type.isEmpty()) {
            logger.error("In BoardService - failed to delete type since it is empty or null");
            return Response.createFailureResponse("Can not delete empty type");
        }
        if (board == null) {
            logger.error("In BoardService - failed to delete type - board is null");
            return Response.createFailureResponse("Can not delete type - board is null");
        }
        if (board.getTypes().contains(type)) {
            board.removeType(type);
            boardRepo.save(board);
            return Response.createSuccessfulResponse(type);
        }
        return Response.createFailureResponse("Can not remove type since it does not exist in board");
    }

    /**
     * Retrieves a board by its ID.
     *
     * @param user    The user requesting the board.
     * @param boardId The ID of the board to be retrieved.
     * @return A response object containing the requested board, or an error message indicating the failure of the request.
     */
    @Transactional
    public Response<Board> getBoard(User user, long boardId) {
        Optional<Board> board = boardRepo.findById(boardId);
        if (board.isPresent() && userExistsInBoard(board.get(), user).isSucceed()) {
            return Response.createSuccessfulResponse(board.get());
        }
        return Response.createFailureResponse("Can not get board");
    }

    /**
     * Determines whether a given type exists in a board.
     *
     * @param board The board to be checked for the type.
     * @param type  The type to be checked for in the board.
     * @return A response object indicating the success or failure of the request.
     */
    //todo: check again
    public Response<String> typeExistsInBoard(Board board, String type) {
        if (board == null) {
            return Response.createFailureResponse("Board is null");
        }
        return board.getTypes().contains(type) ? Response.createSuccessfulResponse(type)
                : Response.createFailureResponse("Type does not exist");
    }

    /**
     * Determines whether a given status exists in a board.
     *
     * @param board  The board to be checked for the status.
     * @param status The status to be checked for in the board.
     * @return A response object indicating the success or failure of the request.
     * BadRequest if the board is null.
     */
    public Response<String> statusExistsInBoard(Board board, String status) {
        if (board == null) {
            return Response.createFailureResponse("Board is null");
        }
        return board.getStatuses().contains(status) ? Response.createSuccessfulResponse(status)
                : Response.createFailureResponse("Status does not exist");
    }

    /**
     * Determines whether a given User exists in a board.
     *
     * @param board The board to be checked for the status.
     * @param user  The user to be checked for in the board.
     * @return A response object indicating the success or failure of the request.
     * BadRequest if the Board does not exist.
     */
    public Response<UserRole> userExistsInBoard(Board board, User user) {
        if (board == null || !boardRepo.findById(board.getId()).isPresent()) {
            return Response.createFailureResponse("Board does not exist");
        }
        UserRole userRole = board.getUserRoleInBoard(user);
        return board.getUserRoleInBoard(user) != null ? Response.createSuccessfulResponse(userRole) :
                Response.createFailureResponse("User does not exist in board");
    }

    /**
     * Assigns a user role to a user in a board.
     *
     * @param board The board to which the user is to be assigned a role.
     * @param user  The user to be assigned a role in the board.
     * @param role  The role to be assigned to the user in the board.
     * @return A response object indicating the success or failure of the request.
     * BadRequest if the board is null or the user is the admin (creator) of the board.
     */
    //todo check validation
    public Response<String> assignUserRole(Board board, User user, UserRole role) {
        if (board == null) {
            return Response.createFailureResponse("Can not assign user to board- board is null");
        }
        if (board.getAdmin().equals(user)) {
            return Response.createFailureResponse(String.format("%s is the admin (creator) of the board - can not change the role of this user", user.getName()));
        }
        board.addUserRole(user, role);
        boardRepo.save(board);
        return Response.createSuccessfulResponse(String.format("Assigned role %s to user %s", role.name(), user.getName()));
    }

    /**
     * Retrieves a set of items from a board.
     *
     * @param user    The user requesting the items.
     * @param boardId The ID of the board from which the items should be retrieved.
     * @return A response object containing the set of items.
     * BadRequest if the board does not exist or the user is not a member of the board.
     */
    public Response<Set<Item>> getItems(User user, long boardId) {
        Optional<Board> board = boardRepo.findById(boardId);
        if (!board.isPresent()) {
            return Response.createFailureResponse("Board does not exist");
        }
        Response<UserRole> userExistsInBoard = userExistsInBoard(board.get(), user);
        if (!userExistsInBoard.isSucceed()) {
            return Response.createFailureResponse(userExistsInBoard.getMessage());
        }
        return Response.createSuccessfulResponse(board.get().getItems());
    }

    /**
     * Removes the role of a user from a board.
     *
     * @param boardId The ID of the board from which the user's role should be removed.
     * @param user    The user whose role should be removed.
     * @return A response object indicating the success or failure of the request.
     * BadRequest if the board does not exist.
     */
    public Response<String> removeUserRole(long boardId, User user) {
        Optional<Board> board = boardRepo.findById(boardId);
        if (!board.isPresent()) {
            return Response.createFailureResponse("Board does not exist");
        }
        board.get().removeUserRole(user);
        boardRepo.save(board.get());
        return Response.createSuccessfulResponse("Success");
    }

    /**
     * Gets a set of user IDs for all users associated with a given board.
     *
     * @param boardId The ID of the board for which to retrieve the users.
     * @return A set of user IDs, representing the users associated with the given board.
     * If the board does not exist, null is returned.
     */
    //todo delete this
    public Set<Long> getAllUsersInBoardByBoardId(long boardId) {
        Optional<Board> boardOptional = getBoardById(boardId);
        return boardOptional.map(board -> board.getUserRole().keySet().stream().map(User::getId).collect(Collectors.toSet())).orElse(null);
    }

    /**
     * Gets a list of boards that the user is associated with.
     *
     * @param user The user for which to retrieve the list of boards. The user must not be null.
     * @return A response object containing the list of {@link LoginBoardDTO} that the user is associated with.
     * BadRequest the user is null.
     */
    public Response<List<LoginBoardDTO>> getBoards(User user) {
        List<LoginBoardDTO> ListBoards = new ArrayList<>();
        for (Board board : boardRepo.findAll()) {
            if (board.getUserRole().containsKey(user)) {
                ListBoards.add(LoginBoardDTO.createLoginBoardDTOFromBoard(board));
            }
        }
        return Response.createSuccessfulResponse(ListBoards);
    }

    public Response<Item> deleteItemFromBoard(Board board, Item item){
        if(!boardRepo.findById(board.getId()).isPresent()){
            return Response.createFailureResponse("Board does not exist");
        }
        board.removeItem(item);
        boardRepo.save(board);
        return Response.createSuccessfulResponse(item);
    }




}
