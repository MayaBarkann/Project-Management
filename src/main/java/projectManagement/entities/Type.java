package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String type;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne()
    @JoinColumn(nullable = false)
    private Board board;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "type")
    private Set<Item> items;

    public Type(Board board, String type) {
        this.type = type;
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type1 = (Type) o;
        return id == type1.id &&
                Objects.equals(type, type1.type) &&
                Objects.equals(board, type1.board) &&
                Objects.equals(items, type1.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, board, items);
    }
}
