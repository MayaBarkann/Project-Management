package projectManagement.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projectManagement.controller.BoardController;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.TypeRepo;

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
        System.out.println(1);
        Board newBoard = boardRepo.save(new Board(title, admin));
        System.out.println(1);UserRoleInBoard userInBoard = new UserRoleInBoard();
        System.out.println(1);userInBoard.setUserRole(UserRole.ADMIN);
//        userInBoard.setBoard(newBoard);
        System.out.println(1);userInBoard.setUser(admin);
//        Set<UserRoleInBoard> set = new HashSet<>();
//        set.add(userInBoard);
        System.out.println(1);newBoard.getUserRoleInBoards().add(userInBoard);
        boardRepo.save(newBoard);

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
     * @param statusStr name of the new status created
     * @return the new status
     */
    public Response<Status> addStatus(long boardId, String statusStr) {
        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            logger.error("In BoardService - failed to add status, board does not exist");
            return Response.createFailureResponse("Can not add status - board does not exist");
        }

        Status status = new Status(statusStr);
        board.get().addStatus(status);
        boardRepo.save(board.get());
        //todo: add live update
        return Response.createSuccessfulResponse(status);
    }

    /**
     *
     * @param statusId
     * @return
     */
    public Response<Status> removeStatus(long boardId, long statusId){
        Optional<Board> board = getBoardById(boardId);
        if(!board.isPresent()){
            logger.error("In BoardService - failed to remove status, board does not exist");
            return Response.createFailureResponse("Can not remove status - board does not exist");
        }
        Optional<Status> status = getStatusById(board.get(), statusId);

        if(!status.isPresent()){
            logger.error("In BoardService - failed to remove status, status does not exist");
            return Response.createFailureResponse("Can not remove status - status does not exist");
        }
        //todo: add live update
        board.get().removeStatus(status.get());
        boardRepo.save(board.get());

        return Response.createSuccessfulResponse(status.get());
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

//    public Response<Status> statusExistsInBoard(Board board, long statusId){
//        Optional<Status> statusExist = statusRepo.findById(statusId);
//
//        return statusExist.isPresent() && board.equals(statusExist.get().getBoard()) ?
//                Response.createSuccessfulResponse(statusExist.get()) : Response.createFailureResponse("Status does not exist");
//    }

    public Response<String> userExistsInBoard(Board board, User user){
        Set<UserRoleInBoard> userRoleInBoardSet =board.getUserRoleInBoards();
        if(userRoleInBoardSet.stream().anyMatch(userRoleInBoard -> userRoleInBoard.getUser().equals(user))){
            return Response.createFailureResponse("Can not assign this item to user - user does not exist in board");
        }


//        Optional<UserRoleInBoard> userRoleInBoard = userRoleInBoardRepo.findByUserAndBoard(user, board);
//        if(!userRoleInBoard.isPresent()){
//            return Response.createFailureResponse("Can not assign this item to user - user does not exist in board");
//        }
        //todo
        return Response.createSuccessfulResponse("Success");
    }

    public Response<UserRoleInBoard> assignUserRole(long boardId, User user, UserRole role){
        Board board = boardRepo.findById(boardId).get();
        UserRoleInBoard userRoleInBoard = new UserRoleInBoard(user, role);
        board.addUserRole(userRoleInBoard);
        boardRepo.save(board);
        return Response.createSuccessfulResponse(userRoleInBoard);
    }

    public Response<UserRoleInBoard> removeUserRole(long boardId, User user, UserRole role){
        Board board = boardRepo.findById(boardId).get();
        UserRoleInBoard userRoleInBoard = new UserRoleInBoard(user, role);
        board.removeUserRole(userRoleInBoard);
        boardRepo.save(board);
        return Response.createSuccessfulResponse(userRoleInBoard);
    }

    public Optional<Status> getStatusById(Board board, long statusId){
        return board.getStatuses().stream().filter(status -> status.getId() == statusId).findFirst();
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
