package projectManagement.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectManagement.controller.BoardController;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.UserRoleInBoardRepo;
import projectManagement.repository.StatusRepo;
import projectManagement.repository.TypeRepo;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BoardService {

    private static Logger logger = LogManager.getLogger(BoardService.class.getName());
    @Autowired
    BoardRepo boardRepo;
    @Autowired
    UserRoleInBoardRepo userRoleInBoardRepo;
    @Autowired
    StatusRepo statusRepo;
    @Autowired
    TypeRepo typeRepo;



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
        Board newBoard = boardRepo.save(new Board(title, admin));
        UserRoleInBoard userInBoard = new UserRoleInBoard();
        userInBoard.setId(new UserBoardPk());
        userInBoard.setBoard(newBoard);
        userInBoard.setUser(admin);
        userInBoard.setUserRole(UserRole.ADMIN);
        userRoleInBoardRepo.save(userInBoard);

        return Response.createSuccessfulResponse(newBoard);
    }

    /**
     * creates new Status with the given name and adds it to the given board
     * @param boardId
     * @param status name of the new status created
     * @return the new status
     */
    public Response<Status> addStatus(long boardId, String status) {
        if(status == null || status.isEmpty()){
            logger.error("In BoardService - failed to create new status since it is empty or null");
            return Response.createFailureResponse("Can not create empty status");
        }

        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            return Response.createFailureResponse("Can not add status - board does not exist");
        }

        return Response.createSuccessfulResponse(statusRepo.save(new Status(board.get(), status)));
    }

    /**
     *
     * @param boardId
     * @param type
     * @return
     */
    public Response<Type> addType(long boardId, String type){
        if(type == null || type.isEmpty()){
            logger.error("In BoardService - failed to create new type since it is empty or null");
            return Response.createFailureResponse("Can not create empty type");
        }

        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            logger.error("In BoardService - failed to create new type, board does not exist");
            return Response.createFailureResponse("Can not create new type - board does not exist");
        }

        return Response.createSuccessfulResponse(typeRepo.save(new Type(board.get(), type)));
    }

    /**
     *
     * @param statusId
     * @return
     */
    public Response<Long> removeStatus(long statusId){
        Optional<Status> status = statusRepo.findById(statusId);
        if(!status.isPresent()){
            return Response.createFailureResponse("Status does not exist");
        }
        statusRepo.delete(status.get());
        //todo: update live
        return Response.createSuccessfulResponse(statusId);

    }

    /**
     *
     * @param typeId
     * @return
     */
    public Response<Long> removeType(long typeId){
        Optional<Type> type = typeRepo.findById(typeId);
        if(!type.isPresent()){
            return Response.createFailureResponse("Type does not exist");
        }
        typeRepo.delete(type.get());
        //todo: update live
        return Response.createSuccessfulResponse(typeId);

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
    public Response<Type> typeExistsInBoard(Board board, long typeId){
        Optional<Type> typeExist = typeRepo.findById(typeId);

        return typeExist.isPresent() && board.equals(typeExist.get().getBoard()) ?
                Response.createSuccessfulResponse(typeExist.get()) : Response.createFailureResponse("Type does not exist");
    }

    public Response<Status> statusExistsInBoard(Board board, long statusId){
        Optional<Status> statusExist = statusRepo.findById(statusId);

        return statusExist.isPresent() && board.equals(statusExist.get().getBoard()) ?
                Response.createSuccessfulResponse(statusExist.get()) : Response.createFailureResponse("Status does not exist");
    }

    public Response<UserRoleInBoard> userExistsInBoard(Board board, User user){
        Optional<UserRoleInBoard> userRoleInBoard = userRoleInBoardRepo.findByUserAndBoard(user, board);
        if(!userRoleInBoard.isPresent()){
            return Response.createFailureResponse("Can not assign this item to user - user does not exist in board");
        }
        return Response.createSuccessfulResponse(userRoleInBoard.get());
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
