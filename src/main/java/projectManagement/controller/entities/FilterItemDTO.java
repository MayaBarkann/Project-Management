package projectManagement.controller.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projectManagement.entities.ItemImportance;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterItemDTO {
    private String type;
    private Long creatorId;
    private Long assignedToId;
    private LocalDate dueDate;
    private ItemImportance importance;
}
