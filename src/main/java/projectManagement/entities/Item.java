package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private LocalDateTime dueDate;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.REMOVE)//remove
    private Set<Item> children;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    @JsonIgnore
    private Item parent;

    @ManyToOne()
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Board board;

    @ManyToOne()
    @JoinColumn()
    @JsonIgnore
    private User assignedToUser;

    @ManyToOne()
    @JoinColumn(nullable = false)
    @JsonIgnore
    private User creator;

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
