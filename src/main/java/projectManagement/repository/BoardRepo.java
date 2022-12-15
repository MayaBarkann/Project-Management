package projectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectManagement.entities.Board;

public interface BoardRepo extends JpaRepository<Board,Long> {

}
