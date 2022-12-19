package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.UserRoleInBoardRepo;

import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepo boardRepo;
    @Autowired
    UserRoleInBoardRepo userRoleInBoardRepo;


    public Optional<Board> getBoard(long boardId) {
        return boardRepo.findById(boardId);
    }

    public Response<Board> create(String title, User admin){
        Board newBoard = boardRepo.save(new Board(title, admin));
        UserRoleInBoard userInBoard = new UserRoleInBoard();
        userInBoard.setId(new UserBoardPk());
        userInBoard.setBoard(newBoard);
        userInBoard.setUser(admin);
        userInBoard.setUserRole(UserRole.ADMIN);
        userRoleInBoardRepo.save(userInBoard);
        return Response.createSuccessfulResponse(newBoard);
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
