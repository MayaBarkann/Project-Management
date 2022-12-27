package projectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectManagement.entities.Notification;
import projectManagement.entities.User;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    Notification findByUser(User user);

}
