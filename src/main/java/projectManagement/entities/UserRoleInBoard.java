package projectManagement.entities;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserRoleInBoard {

    @EmbeddedId
    private UserBoardPk id = new UserBoardPk();

    @ManyToOne()
    @MapsId("userId")
    @Cascade(CascadeType.ALL)
    private User user;

    @ManyToOne()
    @MapsId("boardId")
    @Cascade(CascadeType.ALL)
    private Board board;

    @Enumerated
    @Column(name = "user_role", nullable = false)
    @Cascade(CascadeType.ALL)
    private UserRole userRole;


}


