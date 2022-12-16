package projectManagement.entities;

import javax.persistence.*;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;


    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Board> boards;

    @OneToMany(mappedBy = "assignedToUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> assignedItems;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> ItemsCreated;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) &&
                password.equals(user.password) &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name);
    }
}
