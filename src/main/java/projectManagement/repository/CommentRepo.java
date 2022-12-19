package projectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectManagement.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {

}
