//package projectManagement.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import projectManagement.entities.Board;
//import projectManagement.entities.User;
//import projectManagement.entities.UserBoardPk;
//import projectManagement.entities.UserRoleInBoard;
//
//import java.util.Optional;
//
//public interface UserRoleInBoardRepo  extends JpaRepository<UserRoleInBoard, Long> {
//    Optional<UserRoleInBoard> findByUserAndBoard(User user, Board board);
//}
