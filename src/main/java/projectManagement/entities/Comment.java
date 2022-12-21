package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(nullable = false)
    private String comment;


    @JsonIncludeProperties(value = {"id", "name"})
    @ManyToOne()
    @JoinColumn(nullable = false)
    private User commentedUser;


    @JsonIncludeProperties(value = {"id"})
    @ManyToOne()
    @JoinColumn(nullable = false)
    private Item item;


    private LocalDateTime dateTime;

    public Comment() {
    }

    public Comment( String comment, User commentedUser, Item item, LocalDateTime dateTime) {

        this.comment = comment;
        this.commentedUser = commentedUser;
        this.item = item;
        this.dateTime = dateTime;
    }
}
