package shop.mtcoding.securityapp.dto;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    @Getter
    @Setter
    public static class JoinDTO {
        private Long id;
        private String username;
        private String password;
        private String role;
    }
}
