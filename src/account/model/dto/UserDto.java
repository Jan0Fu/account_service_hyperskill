package account.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonPropertyOrder({"id", "name", "lastname", "email"})
public class UserDto {

    Long id;

    String name;

    String lastname;

    String email;
}
