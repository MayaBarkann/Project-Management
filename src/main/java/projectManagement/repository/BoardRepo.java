package projectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectManagement.entities.Board;
import projectManagement.entities.User;

import java.util.Optional;

@Repository
public interface BoardRepo extends JpaRepository<Board,Long> {

    Optional<Board> findById(Long id);

}
