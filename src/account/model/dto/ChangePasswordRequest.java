package account.model.dto;

import account.validator.LengthConstraint;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @NotBlank
    @LengthConstraint
    private String new_password;
}
