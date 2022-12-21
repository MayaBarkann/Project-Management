package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
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

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne()
    @JoinColumn(nullable = false)
    private Board board;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL)
    private Set<Item> items;

    public Status(Board board, String status) {
        this.status = status;
        this.board = board;
    }

}
