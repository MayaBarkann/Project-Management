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
        if(title== null || title.equals("") || admin ==null ){
            logger.error("In BoardService - failed to create new Board since title is empty or null");
            return Response.createFailureResponse("Can not create empty Board");
        }
        Board newBoard = boardRepo.save(new Board(title, admin));

        return Response.createSuccessfulResponse(newBoard);
    }

    /**
     * creates new Status with the given name and adds it to the given board
     *
     * @param board
     * @param status  name of the new status created
     * @return the new status
     */
    public Response<String> addStatus(Board board, String status) {
        if (status == null || status.isEmpty()) {
            logger.error("In BoardService - failed to create new status since it is empty or null");
            return Response.createFailureResponse("Can not create empty status");
        }

        if(board == null){
            return Response.createFailureResponse("Can not create status- board is null");
        }

//        Optional<Board> board = getBoardById(boardId);
//        if (!board.isPresent()) {
//            return Response.createFailureResponse("Can not add status - board does not exist");
//        }
//        Board newBoard = board.get();
        board.addStatus(status);
        boardRepo.save(board);
        //todo live update
        return Response.createSuccessfulResponse(status);
    }

    /**
     * @param board
     * @param type
     * @return
     */
    public Response<String> addType(Board board, String type) {
        if (type == null || type.isEmpty()) {
            logger.error("In BoardService - failed to create new type since it is empty or null");
            return Response.createFailureResponse("Can not create empty type");
        }
        if(board == null){
            return Response.createFailureResponse("Board can not be null");
        }
//        Optional<Board> board = getBoardById(boardId);
//        if (!board.isPresent()) {
//            logger.error("In BoardService - failed to create new type, board does not exist");
//            return Response.createFailureResponse("Can not create new type - board does not exist");
//        }

//        Board newBoard = board.get();
        board.addType(type);
        boardRepo.save(board);
        return Response.createSuccessfulResponse(type);
    }

    /**
     *
     * @param status
     * @return
     */
    public Response<String> removeStatus(Board board, String status) {
        if (status == null || status.isEmpty()) {
            logger.error("In BoardService - failed to delete status since it is empty or null");
            return Response.createFailureResponse("Can not delete empty status");
        }

        if(board == null){
            return Response.createFailureResponse("Can not delete status- board is null");
        }

//        Optional<Board> board = getBoardById(boardId);
//        if (!board.isPresent()) {
//            return Response.createFailureResponse("Can not add status - board does not exist");
//        }
//        Board newBoard = board.get();
        if(board.getStatuses().contains(status)){
            board.removeStatus(status);
            boardRepo.save(board);
            return Response.createSuccessfulResponse(status);
        }
        return Response.createFailureResponse("Can not remove status since it does not exist in board");
    }


    public Response<String> removeType(Board board, String type) {
        if (type == null || type.isEmpty()) {
            logger.error("In BoardService - failed to delete type since it is empty or null");
            return Response.createFailureResponse("Can not delete empty type");
        }

        if(board == null){
            logger.error("In BoardService - failed to delete type - board is null");
            return Response.createFailureResponse("Can not delete type - board is null");
        }

//        Optional<Board> board = getBoardById(boardId);
//        if (!board.isPresent()) {
//            return Response.createFailureResponse("Can not add type - board does not exist");
//        }
//        Board newBoard = board.get();

        if(board.getTypes().contains(type)){
            board.removeType(type);
            boardRepo.save(board);
            return Response.createSuccessfulResponse(type);
        }

        return Response.createFailureResponse("Can not remove type since it does not exist in board");

    }

    /**
     *
     * @param user
     * @param boardId
     * @return
     */
    @Transactional
    public Response<Board> getBoard(User user, long boardId) {
        Optional<Board> board = boardRepo.findById(boardId);
        if(board.isPresent() && userExistsInBoard(board.get(), user).isSucceed()){
            return Response.createSuccessfulResponse(board.get());
        }
        return Response.createFailureResponse("Can not get board");
    }

    //todo: check again
    public Response<String> typeExistsInBoard(Board board, String type) {
        return board.getTypes().contains(type) ? Response.createSuccessfulResponse(type)
                : Response.createFailureResponse("Type does not exist");
    }

    public Response<String> statusExistsInBoard(Board board, String status) {
        if(board == null) {
            return Response.createFailureResponse("Board is null");
        }
        return board.getStatuses().contains(status) ? Response.createSuccessfulResponse(status)
                : Response.createFailureResponse("Status does not exist");
    }

    public Response<UserRole> userExistsInBoard(Board board, User user) {
        if (board == null || !boardRepo.findById(board.getId()).isPresent()) {
            return Response.createFailureResponse("Board does not exist");
        }

        UserRole userRole = board.getUserRoleInBoard(user);
        return board.getUserRoleInBoard(user) != null ? Response.createSuccessfulResponse(userRole) :
                Response.createFailureResponse("User does not exist in board");
    }

    //todo check validation
    public Response<String> assignUserRole(Board board, User user, UserRole role) {
        if(board == null){
            return Response.createFailureResponse("Can not assign user to board- board is null");
        }

        if (board.getAdmin().equals(user)) {
            return Response.createFailureResponse(String.format("%s is the admin (creator) of the board - can not change the role of this user", user.getName()));
        }

        board.addUserRole(user, role);
        boardRepo.save(board);

        return Response.createSuccessfulResponse(String.format("Assigned role %s to user %s", role.name(), user.getName()));
    }
    public Response<Set<Item>> getItems(User user, long boardId){
        Optional<Board> board = boardRepo.findById(boardId);
        if(!board.isPresent()){
            return Response.createFailureResponse("Board does not exist");
        }

        Response<UserRole> userExistsInBoard = userExistsInBoard(board.get(), user);
        if(!userExistsInBoard.isSucceed()){
            return Response.createFailureResponse(userExistsInBoard.getMessage());
        }

        return Response.createSuccessfulResponse(board.get().getItems());
    }

    public Response<String> removeUserRole(long boardId, User user) {
        Optional<Board> board = boardRepo.findById(boardId);
        if(!board.isPresent()){
            return Response.createFailureResponse("Board does not exist");
        }
        board.get().removeUserRole(user);
        boardRepo.save(board.get());
        return Response.createSuccessfulResponse("Success");
    }

//todo delete this
    public Set<Long> getAllUsersInBoardByBoardId(long boardId) {
        Optional<Board> boardOptional = getBoardById(boardId);
        return boardOptional.map(board -> board.getUserRole().keySet().stream().map(User::getId).collect(Collectors.toSet())).orElse(null);
    }


    public Response<List<LoginBoardDTO>> getBoards(User user){
        List<LoginBoardDTO> ListBoards = new ArrayList<>();
        for ( Board board: boardRepo.findAll()) {
            if(board.getUserRole().containsKey(user)){
                ListBoards.add(LoginBoardDTO.createLoginBoardDTOFromBoard(board));
            }
        }
        return Response.createSuccessfulResponse(ListBoards);
    }








}
