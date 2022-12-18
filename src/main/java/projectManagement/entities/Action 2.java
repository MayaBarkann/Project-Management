package projectManagement.entities;

public enum Action {
    CREATE_ITEM("create-item"),
    DELETE_ITEM("delete-item"),
    ASSIGN_ITEM("assign-item"),
    UPDATE_ITEM("update-item"),
    ADD_STATUSES("add-status"),
    REMOVE_STATUS("remove-status"),
    ADD_ITEM_TYPE("add-new-type"),
    REMOVE_ITEM_TYPE("remove-type"),
    CREATE_BOARD("create-board"),
    UPDATE_ITEM_STATUS("update-item-status"),
    ADD_COMMENT("add-comment"),
    FILTER("filter");

    private String methodName;

    Action(String name) {
        this.methodName = name;
    }

}
