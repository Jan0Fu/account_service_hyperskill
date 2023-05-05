package account.model.dto;

import account.validator.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @NotBlank
    @ValidPassword
    private String new_password;
}
