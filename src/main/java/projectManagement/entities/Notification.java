package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@ToString
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;





    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Column(name = "ITEM_ASSIGNED_TO_ME")
    @Enumerated(EnumType.STRING)
    private NotificationType ITEM_ASSIGNED_TO_ME;

    @Column(name = "ITEM_STATUS_CHANGED")
    @Enumerated(EnumType.STRING)
    private NotificationType ITEM_STATUS_CHANGED;

    @Column(name = "ITEM_COMMENT_ADDED")
    @Enumerated(EnumType.STRING)
    private NotificationType ITEM_COMMENT_ADDED;

    @Column(name = "ITEM_DELETED")
    @Enumerated(EnumType.STRING)
    private NotificationType ITEM_DELETED;

    @Column(name = "ITEM_DATA_CHANGED")
    @Enumerated(EnumType.STRING)
    private NotificationType ITEM_DATA_CHANGED;

    @Column(name = "USER_ADDED_TO_THE_SYSTEM")
    @Enumerated(EnumType.STRING)
    private NotificationType USER_ADDED_TO_THE_SYSTEM;


    public Notification(User user, NotificationType ITEM_ASSIGNED_TO_ME, NotificationType ITEM_STATUS_CHANGED, NotificationType ITEM_COMMENT_ADDED, NotificationType ITEM_DELETED, NotificationType ITEM_DATA_CHANGED, NotificationType USER_ADDED_TO_THE_SYSTEM) {
        this.user = user;
        this.ITEM_ASSIGNED_TO_ME = ITEM_ASSIGNED_TO_ME;
        this.ITEM_STATUS_CHANGED = ITEM_STATUS_CHANGED;
        this.ITEM_COMMENT_ADDED = ITEM_COMMENT_ADDED;
        this.ITEM_DELETED = ITEM_DELETED;
        this.ITEM_DATA_CHANGED = ITEM_DATA_CHANGED;
        this.USER_ADDED_TO_THE_SYSTEM = USER_ADDED_TO_THE_SYSTEM;
    }
}
