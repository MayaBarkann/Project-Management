//package projectManagement.entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.*;
//import org.hibernate.annotations.Cascade;
//import org.hibernate.annotations.CascadeType;
//
//import javax.persistence.*;
//import java.util.Objects;
//
////@Entity
////@AllArgsConstructor
//@Getter
//@Setter
//@NoArgsConstructor
//public class UserRoleInBoard {
////
////    @EmbeddedId
////    private UserBoardPk id = new UserBoardPk();
////    @Id
////    @GeneratedValue(strategy = GenerationType.AUTO)
////    private long id;
//
////    @ManyToOne()
////    @MapsId("userId")
////    @Cascade(CascadeType.ALL)
////    private User user;
//
////    @MapsId("userId")
////    @JsonIgnore
////    @OneToOne()
//    private User user;
////
////    @ManyToOne()
////    @MapsId("boardId")
////    @Cascade(CascadeType.ALL)
////    private Board board;
//
////    @Enumerated(EnumType.STRING)
////    @Column(name = "user_role", nullable = false)
////    @Cascade(CascadeType.ALL)
//    private UserRole userRole;
//
//    public UserRoleInBoard(User user, UserRole userRole){
//        this.user = user;
//        this.userRole = userRole;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UserRoleInBoard that = (UserRoleInBoard) o;
//        return user.equals(that.user);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(user);
//    }
//}
//
//
