package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
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

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @Column(nullable = false)
//    @ElementCollection()
//    private Set<UserRoleInBoard> userRoleInBoards = new HashSet<>();
    @Column(nullable = false)
    @ElementCollection()
    private Map<User, UserRole> userRoleInBoards = new HashMap<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> items = new HashSet<>();

    @Column(nullable = false)
    @ElementCollection()
    private Set<String> statuses = new HashSet<>();

    @Column(nullable = false)
    @ElementCollection()
    private Set<String> types = new HashSet<>();

    public Board(String title, User admin) {
        this.title = title;
        this.admin = admin;
        this.addUserRole(admin, UserRole.ADMIN);
    }

    public Map<User, UserRole> addUserRole(User user,  UserRole userRole){
        this.userRoleInBoards.put(user, userRole);
        return this.userRoleInBoards;
    }

    public Map<User, UserRole> removeUserRole(User user){
        this.userRoleInBoards.remove(user);
        return this.userRoleInBoards;
    }

    public Set<String> addStatus(String status){
        this.statuses.add(status);
        return this.statuses;
    }

    public Set<String> removeStatus(String status){
        this.statuses.remove(status);
        items.removeIf(item -> item.getStatus().equals(status));
//        for (Item item: items) {
//            if(item.getStatus() != null && item.getStatus().equals(status)){
//                item.setStatus("");
//            }
//        }
        return this.statuses;
    }

    public Set<String> addType(String type){
        this.types.add(type);
        return this.types;
    }

    public Set<String> removeType(String type){
        this.types.remove(type);
        for (Item item: items) {
            if(item.getType() != null && item.getType().equals(type)){
                item.setType("");
            }
        }

        return this.types;
    }

}
