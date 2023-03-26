package machels.ohmybox.dto;

import lombok.Data;
import machels.ohmybox.domain.User;

@Data
public class RequestPostCreateUserDto {

    private String email;

    private String password;

    private String name;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .cloudSize(32212254720L)
                .cloudUsage(0L)
                .rootDirectoryId(null)
                .build();
    }
}
