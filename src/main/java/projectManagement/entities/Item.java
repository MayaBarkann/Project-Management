package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String title;

//    @JsonIncludeProperties(value = {"id", "status"})
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
    @Column(nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    private ItemImportance importance;

    private String description;

    @Column(nullable = false)
    private String type = "";

    private LocalDate dueDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.REMOVE)//remove
    private Set<Item> children;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Item parent;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne()
    @JoinColumn(nullable = false)
    private Board board;

    @JsonIncludeProperties(value = {"id", "name"})
    @ManyToOne()
    @JoinColumn()
    private User assignedToUser;

    @JsonIncludeProperties(value = {"id" , "name"})
    @ManyToOne()
    @JoinColumn(nullable = false)
    private User creator;

    @JsonIncludeProperties(value = {"id","comment"})
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    public Item(String title, String status, Board board, User creator) {
        this.title = title;
        this.status = status;
        this.board = board;
        this.creator = creator;
    }








}
