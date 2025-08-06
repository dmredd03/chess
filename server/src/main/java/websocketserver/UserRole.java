package websocketserver;

import java.util.Objects;

public class UserRole {
    public final String username;
    public final String role;
    
    public UserRole(String username, String color) {
        this.username = username;
        this.role = color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRole userRole = (UserRole) o;
        return Objects.equals(username, userRole.username) && Objects.equals(role, userRole.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role);
    }
}
