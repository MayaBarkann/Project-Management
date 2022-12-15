package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String title;


    @ManyToOne()
    @JoinColumn(nullable = false)
    private User admin;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items;

    public Board(String title, User admin) {
        this.title = title;
        this.admin = admin;
    }


//    @ElementCollection(targetClass=String.class)
//    private List<String> types;
//    @ElementCollection(targetClass=String.class)
//    private List<String> statuses;
}
