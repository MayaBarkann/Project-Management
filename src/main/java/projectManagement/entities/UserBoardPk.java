package projectManagement.entities;

import lombok.Data;
import lombok.Setter;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
@Data
@Setter
public class UserBoardPk implements Serializable{
    private static final long serialVersionUID = 1L;
    private long userId;
    private long boardId;
}
