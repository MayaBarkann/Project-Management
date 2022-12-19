package projectManagement.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    public Status(Board board, String status) {
        this.status = status;
        this.board = board;
    }
}
