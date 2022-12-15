package projectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectManagement.entities.Board;
import projectManagement.entities.UserBoardPk;
import projectManagement.entities.UserRoleInBoard;

public interface UserRoleInBoardRepo  extends JpaRepository<UserRoleInBoard, Long> {

}
