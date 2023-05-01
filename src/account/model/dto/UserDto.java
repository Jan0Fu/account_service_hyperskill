package account.model.dto;

import account.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserDto {

    private final Long id;
    private final String name;
    private final String lastname;
    private final String email;
    @JsonIgnore
    private final List<String> roles;

    public UserDto(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.roles = new ArrayList<>(user.getRoles());
    }
}
