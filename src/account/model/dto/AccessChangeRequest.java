package account.model.dto;

import lombok.Data;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Data
public class AccessChangeRequest {

    @NotBlank
    private String user;
    @Enumerated
    private Access operation;

    public enum Access {
        LOCK, UNLOCK
    }
}
