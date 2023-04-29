package account.service;

import account.model.Role;
import account.model.UserEntity;
import account.model.dto.CreateUserResponse;
import account.model.dto.UserDto;
import account.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<String> breachedPass = List.of("PasswordForJanuary", "PasswordForFebruary",
            "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    @Override
    public ResponseEntity<Object> register(UserEntity user) {
        if (breachedPass.contains(user.getPassword())) {
            CreateUserResponse response = CreateUserResponse.builder()
                    .status(400)
                    .error("Bad Request")
                    .message("The password is in the hacker's database!")
                    .path("/api/auth/signup")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        if (userEntityRepository.existsByEmailIgnoreCase(user.getEmail())) {
            CreateUserResponse response = CreateUserResponse.builder()
                    .status(400)
                    .error("Bad Request")
                    .message("User exist!")
                    .path("/api/auth/signup")
                    .build();
            return ResponseEntity.badRequest().body(response);
        } else {
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
    }

    @Override
    public ResponseEntity<Object> changePassword(String password, String email) {
        if (breachedPass.contains(password)) {
            CreateUserResponse response = CreateUserResponse.builder()
                    .status(400)
                    .error("Bad Request")
                    .message("The password is in the hacker's database!")
                    .path("/api/auth/changepass")
                    .build();
            return ResponseEntity.badRequest().body(response);
        } else {
            Optional<UserEntity> user = userEntityRepository.findByEmailIgnoreCase(email);
            savePassword(user.get(), password);
            return ResponseEntity.ok().body(Map.of("status", "The password has been updated successfully",
                    "email", user.get().getEmail()));
        }
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
