package projectManagement.controller.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projectManagement.entities.NotificationType;
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UpdateUserNotificationDTO {
    public NotificationType ITEM_ASSIGNED_TO_ME;
    public NotificationType ITEM_STATUS_CHANGED;
    public NotificationType ITEM_COMMENT_ADDED;
    public NotificationType ITEM_DELETED;
    public NotificationType ITEM_DATA_CHANGED;
    public NotificationType USER_ADDED_TO_THE_SYSTEM;
}
