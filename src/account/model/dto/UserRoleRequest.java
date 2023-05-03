package account.model.dto;

import lombok.Data;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Data
public class UserRoleRequest {

    @NotBlank
    private String user;
    @NotBlank
    private String role;
    @Enumerated
    private Operation operation;

    public enum Operation {
        GRANT, REMOVE
    }
}
