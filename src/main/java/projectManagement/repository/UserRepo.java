package projectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectManagement.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
}
