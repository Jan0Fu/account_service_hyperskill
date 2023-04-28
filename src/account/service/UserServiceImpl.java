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

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> register(UserEntity user) {
        if (userEntityRepository.existsByEmailIgnoreCase(user.getEmail())) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalTime.now());
            body.put("status", 400);
            body.put("error", "Bad Request");
            body.put("message", "User exist!");
            body.put("path", "/api/auth/signup");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public UserDto getUser(UserEntity user) {
        return UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .lastname(user.getLastname())
                    .email(user.getEmail()).build();
    }
}
