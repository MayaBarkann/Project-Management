package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String status;

//    @JsonIncludeProperties(value = {"id"})
//    @ManyToOne()
//    @JoinColumn(nullable = false)
//    private Board board;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<Item> items;

    public Status(String status) {
        this.status = status;
    }

    public Set<Item> addItem(Item item){
        this.items.add(item);
        return this.items;
    }

    public Set<Item> removeItem(Item item){
        this.items.remove(item);
        return this.items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return id == status.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
