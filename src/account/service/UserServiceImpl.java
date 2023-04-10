package account.service;

import account.exceptions.UserAlreadyExistsException;
import account.model.UserEntity;
import account.model.dto.UserDto;
import account.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public UserDto register(UserEntity user) throws UserAlreadyExistsException {
        if (userEmailExists(user.getEmail())) {
            throw new UserAlreadyExistsException();
        } else {
            userEntityRepository.save(user);
            return UserDto.builder()
                    .name(user.getName())
                    .lastname(user.getLastname())
                    .email(user.getEmail()).build();
        }
    }

    @Override
    public boolean userEmailExists(String email) {
        return userEntityRepository.existsByEmail(email);
    }
}
