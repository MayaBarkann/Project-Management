package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.StatusRepo;
import projectManagement.repository.TypeRepo;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepo boardRepo;
    @Autowired
    StatusRepo statusRepo;
    @Autowired
    TypeRepo typeRepo;

    public BoardService() {
    }

    public Optional<Board> getBoardById(long boardId) {
        return boardRepo.findById(boardId);
    }

    public Response<Status> addStatus(Board board, String status) {
        Status statusObj = new Status(board, status);
        Status savedStatus = statusRepo.save(statusObj);


        return Response.createSuccessfulResponse(savedStatus);
    }


//    public Response<Board> createBoard(Board board){
//        //TODO: add item to board
//        return Response.createSuccessfulResponse(boardRepo.save(board));
////        return Response.createFailureResponse("no board id like that");
//    }


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
