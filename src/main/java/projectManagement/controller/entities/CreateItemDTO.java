package projectManagement.controller.entities;

import lombok.NoArgsConstructor;
import projectManagement.entities.ItemImportance;

import java.time.LocalDateTime;


@NoArgsConstructor
public class CreateItemDTO {
//todo: goback
    public String title;
    public Long status;
    public ItemImportance importance;
//    public String description;
    public Long type;
//    public LocalDateTime dueDate;
    public Long parentId;
    public Long boardId;
    public Long assignedToUserId;
    public Long creatorId;


}
