package projectManagement.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectManagement.controller.BoardController;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
//import projectManagement.repository.UserRoleInBoardRepo;
//import projectManagement.repository.StatusRepo;
//import projectManagement.repository.TypeRepo;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());
    @Autowired
    BoardRepo boardRepo;
//    @Autowired
//    UserRoleInBoardRepo userRoleInBoardRepo;
//    @Autowired
//    StatusRepo statusRepo;
//    @Autowired
//    TypeRepo typeRepo;



    public Optional<Board> getBoardById(long boardId) {
        return boardRepo.findById(boardId);
    }

    /**
     * This function creates a new board with title and assigning the user to be the admin of this new board.
     * It saves the new board in BoardRepo and saving the given user with role Admin and the new board created in UserRoleInBoardRepo
     * @param title the title of the created board
     * @param admin the admin of the created board
     * @return the new created board
     */
    public Response<Board> createBoard(String title, User admin){
        System.out.println(1);
        Board newBoard = boardRepo.save(new Board(title, admin));
////        UserRoleInBoard userInBoard = new UserRoleInBoard();
//        userInBoard.setUserRole(UserRole.ADMIN);
//        userInBoard.setBoard(newBoard);
//        System.out.println(1);
//        userInBoard.setUser(admin);
//        Set<UserRoleInBoard> set = new HashSet<>();
//        set.add(userInBoard);
//        System.out.println(1);newBoard.getUserRoleInBoards().add(userInBoard);
//        boardRepo.save(newBoard);

//        userInBoard.setId(new UserBoardPk());
//        userInBoard.setBoard(newBoard);
//        userInBoard.setUser(admin);
//        userInBoard.setUserRole(UserRole.ADMIN);
//        userRoleInBoardRepo.save(userInBoard);
        System.out.println(1);
        return Response.createSuccessfulResponse(newBoard);
    }

    /**
     * creates new Status with the given name and adds it to the given board
     * @param boardId
     * @param status name of the new status created
     * @return the new status
     */
    public Response<String> addStatus(long boardId, String status) {
        if(status == null || status.isEmpty()){
            logger.error("In BoardService - failed to create new status since it is empty or null");
            return Response.createFailureResponse("Can not create empty status");
        }

        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            return Response.createFailureResponse("Can not add status - board does not exist");
        }
        Board newBoard = board.get();
        newBoard.addStatus(status);
        boardRepo.save(newBoard);
        //todo live update
        return Response.createSuccessfulResponse(status);
    }

    /**
     *
     * @param boardId
     * @param type
     * @return
     */
    public Response<String> addType(long boardId, String type){
        if(type == null || type.isEmpty()){
            logger.error("In BoardService - failed to create new type since it is empty or null");
            return Response.createFailureResponse("Can not create empty type");
        }

        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            logger.error("In BoardService - failed to create new type, board does not exist");
            return Response.createFailureResponse("Can not create new type - board does not exist");
        }

        Board newBoard = board.get();
        newBoard.addType(type);
        boardRepo.save(newBoard);
        return Response.createSuccessfulResponse(type);
    }

    /**
     *
     * @param status
     * @return
     */
    public Response<String> removeStatus(long boardId, String status){
        if(status == null || status.isEmpty()){
            logger.error("In BoardService - failed to create new status since it is empty or null");
            return Response.createFailureResponse("Can not create empty status");
        }

        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            return Response.createFailureResponse("Can not add status - board does not exist");
        }
        Board newBoard = board.get();
        newBoard.removeStatus(status);
        boardRepo.save(newBoard);
        //todo live update
        return Response.createSuccessfulResponse(status);
    }


    public Response<String> removeType(long boardId, String type){
        if(type == null || type.isEmpty()){
            logger.error("In BoardService - failed to create new type since it is empty or null");
            return Response.createFailureResponse("Can not create empty type");
        }

        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            return Response.createFailureResponse("Can not add type - board does not exist");
        }
        Board newBoard = board.get();
        //todo check if type does not exists then return failure response
        newBoard.removeType(type);
        boardRepo.save(newBoard);
        //todo live update
        return Response.createSuccessfulResponse(type);
    }

    /**
     *
     * @param boardId
     * @return
     */
    public Response<Board> getBoard(long boardId){
        Optional<Board> board = boardRepo.findById(boardId);
        if(!board.isPresent()) {
            return Response.createFailureResponse("Board does not exist");
        }

        return Response.createSuccessfulResponse(board.get());
    }

    //todo: check again
    public Response<String> typeExistsInBoard(Board board, String type){
        return board.getTypes().contains(type) ? Response.createSuccessfulResponse(type)
                : Response.createFailureResponse("Type does not exist");
    }

    public Response<String> statusExistsInBoard(Board board, String status){

        return board.getStatuses().contains(status) ? Response.createSuccessfulResponse(status)
                : Response.createFailureResponse("Status does not exist");
    }

    public Response<UserRole> userExistsInBoard(Board board, User user){

        return board.getUserRoleInBoards().containsKey(user) ? Response.createSuccessfulResponse(board.getUserRoleInBoards().get(user)) :
                Response.createFailureResponse("Can not assign this item to user - user does not exist in board");
    }
//todo check validation
    public Response<String> assignUserRole(long boardId, User user, UserRole role){
        Board board = boardRepo.findById(boardId).get();
        board.addUserRole(user, role);
        boardRepo.save(board);
        return Response.createSuccessfulResponse("Success");
    }

    public Response<String> removeUserRole(long boardId, User user){
        Board board = boardRepo.findById(boardId).get();
        board.removeUserRole(user);
        boardRepo.save(board);
        return Response.createSuccessfulResponse("Success");
    }






//    public Response<Board> delete(long boardId){
//        Optional<Board> board = boardRepo.findById(boardId);
//        if(board.isPresent()) {
//            boardRepo.deleteById(boardId);
//            return Response.createSuccessfulResponse(board.get());
//        }
//        return Response.createFailureResponse("no board id like that");
//    }

//    public Response<Board> rename(long boardId,String name){
//        Optional<Board> board = boardRepo.findById(boardId);
//        if(board.isPresent()) {
//            // TODO: add update title in repo
//            board.get().setTitle(name);
//            boardRepo.deleteById(boardId);
//            boardRepo.save(board.get());
//            return Response.createSuccessfulResponse(board.get());
//        }
//        return Response.createFailureResponse("no board id like that");
//    }


}
