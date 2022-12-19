package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.UserRoleInBoardRepo;
import projectManagement.repository.StatusRepo;
import projectManagement.repository.TypeRepo;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BoardService {

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

    public Response<Status> addStatus(Board board, String status) {
        Status statusObj = new Status(board, status);
        Status savedStatus = statusRepo.save(statusObj);

        return Response.createSuccessfulResponse(savedStatus);
    }

    /**
     *
     * @param board
     * @param type
     * @return
     */

    public Response<Type> addType(Board board, String type){
        Type newType = new Type()
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


//    public Response<Item> addItem(long boardId, Item item) {
//        if(boardRepo.findById(boardId).isPresent()){
//            //TODO: add item to board
//            return Response.createSuccessfulResponse(item);
//        }
//        return Response.createFailureResponse("no board id like that");
//    }
}
