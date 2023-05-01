package account.service;

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

    private static final Map<String, String> ROLES = Map.of(
            "USER", "ROLE_USER",
            "ADMINISTRATOR", "ROLE_ADMINISTRATOR",
            "ACCOUNTANT", "ROLE_ACCOUNTANT",
            "AUDITOR", "ROLE_AUDITOR"
    );
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserEntity user) throws ResponseStatusException {
        if (userEntityRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        return new UserDto(userEntityRepository.save(createUser(userEntityRepository.findAll().isEmpty(), user)));
    }

    @Override
    public ResponseEntity<Object> changePassword(String password, String email) throws ResponseStatusException {
        Optional<UserEntity> user = userEntityRepository.findByEmailIgnoreCase(email);
        savePassword(user.get(), password);
        return ResponseEntity.ok().body(Map.of("status", "The password has been updated successfully",
                "email", user.get().getEmail()));
    }

    public UserEntity createUser(boolean isFirst, UserEntity user) {
        user.addRole(isFirst ? ROLES.get("ADMINISTRATOR") : ROLES.get("USER"));
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    public void savePassword(UserEntity user, String password) throws ResponseStatusException {
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
        user.setPassword(passwordEncoder.encode(password));
        userEntityRepository.save(user);
    }

    @Override
    public UserEntity findUserByEmail(String email) throws ResponseStatusException {
        return userEntityRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
    }
}
