package projectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectManagement.entities.*;
import projectManagement.repository.BoardRepo;
import projectManagement.repository.UserRepo;

import java.util.Optional;

@Service

public class PermissionsService {
    UserRepo userRepo;
    BoardRepo boardRepo;

    @Autowired
    public PermissionsService(UserRepo userRepo, BoardRepo boardRepo) {
        this.userRepo = userRepo;
        this.boardRepo = boardRepo;
    }

    /**
     * This method checks if a user has the required permission to perform a certain action on a board.
     *
     * @param userId     The ID of the user to check the permissions for.
     * @param boardId    The ID of the board on which the action will be performed.
     * @param methodName The name of the method (i.e., action) that will be performed.
     *                   This should correspond to an Action enum value.
     * @return A Response object, which contains a Board object if the user has the required permission and a failure message otherwise.
     */
    @Transactional
    public Response<Board> checkPermission(long userId, long boardId, String methodName) {
        Optional<User> user = userRepo.findById(userId);
        Optional<Board> board = boardRepo.findById(boardId);
        Action action;
        try {
            action = Action.valueOf(methodName);
        } catch (IllegalArgumentException e) {
            return Response.createFailureResponse("Invalid method name");
        }
        if (!user.isPresent()) {
            return Response.createFailureResponse(String.format("User with id: %d does not exist", userId));
        }
        if (!board.isPresent()) {
            return Response.createFailureResponse(String.format("Board with id: %d does not exists", boardId));
        }
        UserRole userRole = board.get().getUserRoleInBoard(user.get());
        if (userRole == null) {
            return Response.createFailureResponse("User does not belong to this board");
        }
        if (PermissionsManager.hasPermission(userRole, action)) {
            return Response.createSuccessfulResponse(board.get());
        }
        return Response.createFailureResponse("User does not have the right permissions");
    }

}
