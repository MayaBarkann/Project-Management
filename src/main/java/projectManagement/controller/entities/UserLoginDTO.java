package projectManagement.controller.entities;

public class UserLoginDTO {
    private long userId;
    private String token;

    public UserLoginDTO(long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}