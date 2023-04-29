package account.service;

import account.model.Role;
import account.model.UserEntity;
import account.model.dto.UserDto;
import account.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> registerUser(UserEntity user) throws ResponseStatusException {
        if (userEntityRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        user.setRole(Role.USER);
        UserEntity newUser = userEntityRepository.save(user);
        return ResponseEntity.ok().body(UserDto.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .lastname(newUser.getLastname())
                .email(newUser.getEmail()).build());
    }

    @Override
    public ResponseEntity<Object> changePassword(String password, String email) throws ResponseStatusException {
        Optional<UserEntity> user = userEntityRepository.findByEmailIgnoreCase(email);
        savePassword(user.get(), password);
        return ResponseEntity.ok().body(Map.of("status", "The password has been updated successfully",
                "email", user.get().getEmail()));

    }

    @Override
    public UserDto getUser(UserEntity user) {
        return UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .lastname(user.getLastname())
                    .email(user.getEmail()).build();
    }

    public void savePassword(UserEntity user, String password) throws ResponseStatusException {
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
        user.setPassword(passwordEncoder.encode(password));
        userEntityRepository.save(user);
    }
}
