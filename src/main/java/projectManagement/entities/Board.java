package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
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
    @JsonIgnore
    private User admin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleInBoard> userRoleInBoards = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

//    @JsonIncludeProperties(value = {"id", "status"})
//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = false)
    @ElementCollection()
    private Set<String> statuses = new HashSet<>();

    @JsonIncludeProperties(value = {"id", "type"})
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Type> types = new HashSet<>();

    public Board(String title, User admin) {
        this.title = title;
        this.admin = admin;
    }

    public Set<UserRoleInBoard> addUserRole(UserRoleInBoard userRoleInBoard){
        this.userRoleInBoards.add(userRoleInBoard);
        return this.userRoleInBoards;
    }

    public Set<UserRoleInBoard> removeUserRole(UserRoleInBoard userRoleInBoard){
        this.userRoleInBoards.remove(userRoleInBoard);
        return this.userRoleInBoards;
    }

    public Set<String> addStatus(String status){
        this.statuses.add(status);
        return this.statuses;
    }

    //todo - remove all items that belongs to this status
    public Set<String> removeStatus(String status){
        this.statuses.remove(status);
        this.items = this.items.stream().filter(item -> !item.getStatus().equals(status)).collect(Collectors.toSet());
        return this.statuses;
    }

//    public boolean statusExistsInBoard(String status){
//        return this.statuses.contains(status);
//    }



}
