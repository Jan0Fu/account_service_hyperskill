package account.model.dto;

import account.model.Role;
import account.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Data
public class UserDto {

    @Min(1)
    private Long id;

    @NotBlank(message = "The name should not be empty")
    private String name;

    @NotBlank(message = "The lastname should not be empty")
    private String lastname;

    @NotBlank
    @Email
    @Pattern(regexp = ".+@acme\\.com", message = "Email should ends with @acme.com")
    private String email;

    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    public UserDto() {
    }

    public UserDto(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail().toLowerCase();
        this.roles = new LinkedList<>(user.getRoles());
        this.roles.sort(Comparator.naturalOrder());
    }
}
