package projectManagement.controller.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projectManagement.entities.UserRole;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUserRoleDTO {
    private String email;
    private UserRole role;
}
