package account.service;

import account.model.UserEntity;
import account.model.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserEntityService {

    ResponseEntity<Object> register(UserEntity user);
    UserDto getUser(UserEntity user);
}
