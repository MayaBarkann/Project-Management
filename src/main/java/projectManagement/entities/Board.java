package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Column(nullable = false)
    @ElementCollection()
    @MapKeyJoinColumn(name="user")
    private Map<User, UserRole> userRole = new HashMap<>();

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
        this.userRole.put(user, userRole);

        return this.userRole;
    }

    public Map<User, UserRole> removeUserRole(User user){
        this.userRole.remove(user);

        return this.userRole;
    }

    public Set<String> addStatus(String status){
        this.statuses.add(status);

        return this.statuses;
    }

    public Set<String> removeStatus(String status){
        this.statuses.remove(status);
        items.removeIf(item -> item.getStatus().equals(status));

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

    public UserRole getUserRoleInBoard(User user){
        return this.userRole.get(user);
    }

}
