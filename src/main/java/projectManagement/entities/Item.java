package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    private ItemImportance importance;
    private String description;


    private String type;
    private LocalDate dueDate;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.REMOVE)//remove
    private Set<Item> children;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()

    private Item parent;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne()
    @JoinColumn(nullable = false)

    private Board board;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne()
    @JoinColumn()

    private User assignedToUser;

    @JsonIncludeProperties(value = {"id" , "name"})

    @ManyToOne()
    @JoinColumn(nullable = false)
    private User creator;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    public Item(String title, String status, ItemImportance importance, String type, Item parent, Board board, User creator, User assignedToUser) {
        this.title = title;
        this.status = status;
        this.importance = importance;
        this.type = type;
        this.parent = parent;
        this.board = board;
        this.creator = creator;
        this.assignedToUser = assignedToUser;
    }

    //    @OneToMany(mappedBy = "",cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn()
//    private List<ItemComment> comment;

//    private Set<String> comments;


}
