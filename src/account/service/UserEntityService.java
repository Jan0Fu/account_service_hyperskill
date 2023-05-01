package account.service;

import account.model.UserEntity;
import account.model.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public interface UserEntityService {

    UserDto registerUser(UserEntity user);

    ResponseEntity<Object> changePassword(String password, String email);

    UserEntity findUserByEmail(String email) throws ResponseStatusException;
}
