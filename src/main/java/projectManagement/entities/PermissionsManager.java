package projectManagement.entities;

import java.util.Map;
import java.util.Set;

public class PermissionsManager {
    private static final Map<UserRole, Set<Action>> permissions=Map.of(
            UserRole.ADMIN, Set.of(Action.values()),
            UserRole.LEADER,Set.of(Action.CREATE_ITEM, Action.ASSIGN_ITEM, Action.UPDATE_ITEM_STATUS),
            UserRole.USER,Set.of(Action.UPDATE_ITEM_STATUS, Action.ADD_COMMENT)
    );

    /**
     * Checks if given user type has permissions to perform given action.
     *
     * @param userRole - userRole Enum, represents the role of user in the system.
     * @param action - Action Enum, represents an action a user can perform in the system.
     * @return true - if the given user type can perform the action, false - otherwise.
     */
    public static boolean hasPermission(UserRole userRole,Action action) {
        return permissions.get(userRole).contains(action);
    }

}
