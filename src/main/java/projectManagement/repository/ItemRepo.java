package projectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectManagement.entities.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
}
