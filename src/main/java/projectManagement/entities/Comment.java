package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

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


    private LocalDateTime dueDate;

    public Comment() {
    }
}
