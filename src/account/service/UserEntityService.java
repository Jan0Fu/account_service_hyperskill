package account.service;

import account.model.UserEntity;
import account.model.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserEntityService {

    ResponseEntity<Object> registerUser(UserEntity user);

    UserDto getUser(UserEntity user);

    ResponseEntity<Object> changePassword(String password, String email);

}
