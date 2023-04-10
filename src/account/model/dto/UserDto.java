package account.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({"name", "lastname", "email"})
public class UserDto {

    private String name;

    private String lastname;

    private String email;
}
