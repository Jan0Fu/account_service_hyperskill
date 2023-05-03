package account.service;

import account.model.UserEntity;
import account.model.dto.UserDto;
import account.model.dto.UserRoleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface UserEntityService {

    UserDto registerUser(UserEntity user);

    ResponseEntity<Object> changePassword(String password, String email);

    UserEntity findUserByEmail(String email) throws ResponseStatusException;

    List<UserDto> getUsersInfo();

    UserDto updateUserRole(UserRoleRequest roleRequest, UserEntity user);

    ResponseEntity<Object> deleteUser(String email, UserEntity user);
}
