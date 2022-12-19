package projectManagement.service;


import org.springframework.beans.factory.annotation.Autowired;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.UserRepo;
import projectManagement.repository.UserRoleInBoardRepo;

import java.util.Optional;

public class PermissionsService {
    UserRoleInBoardRepo userRoleInBoardRepo;
    UserRepo userRepo;
    BoardRepo boardRepo;

    @Autowired
    public PermissionsService(UserRoleInBoardRepo userRoleInBoardRepo, UserRepo userRepo, BoardRepo boardRepo) {
        this.userRoleInBoardRepo = userRoleInBoardRepo;
        this.userRepo = userRepo;
        this.boardRepo = boardRepo;
    }

    public Response<Boolean> checkPermission(long userId, long boardId, Action action) {
        Optional<User> user = userRepo.findById(userId);
        Optional<Board> board = boardRepo.findById(boardId);

        if (!user.isPresent()) {
            return Response.createFailureResponse(String.format("User with id: %d does not exist", userId));
        }

        if (!board.isPresent()){
            return Response.createFailureResponse(String.format("Board with id: %d does not exists", boardId));
        }

        Optional<UserRoleInBoard> userRoleInBoard = userRoleInBoardRepo.findByUserAndBoard(user.get(), board.get());

        if (!userRoleInBoard.isPresent()){
            return Response.createFailureResponse("User does not belong to this board");
        }

        if(PermissionsManager.hasPermission(userRoleInBoard.get().getUserRole() ,action)) {

            return Response.createSuccessfulResponse(true);

        }
        return Response.createSuccessfulResponse(false);
    }

}
