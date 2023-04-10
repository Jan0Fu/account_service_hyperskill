package account.service;

import account.exceptions.UserAlreadyExistsException;
import account.model.UserEntity;
import account.model.dto.UserDto;

public interface UserEntityService {

    UserDto register(UserEntity user) throws UserAlreadyExistsException;
    boolean userEmailExists(String email);
}
