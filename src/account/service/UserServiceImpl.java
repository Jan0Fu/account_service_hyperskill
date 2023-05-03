package account.service;

import account.model.Role;
import account.model.UserEntity;
import account.model.dto.UserDto;
import account.model.dto.UserRoleRequest;
import account.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto registerUser(UserEntity user) {
        if (userEntityRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        if (userEntityRepository.count() == 0) {
            user.setRoles(Collections.singletonList(Role.ROLE_ADMINISTRATOR));
        } else {
            user.setRoles(Collections.singletonList(Role.ROLE_USER));
        }
        return new UserDto(userEntityRepository.save(user));
    }

    @Override
    public ResponseEntity<Object> changePassword(String password, String email) {
        Optional<UserEntity> user = userEntityRepository.findByEmailIgnoreCase(email);
        savePassword(user.get(), password);
        return ResponseEntity.ok().body(Map.of("status", "The password has been updated successfully",
                "email", user.get().getEmail()));
    }

    public void savePassword(UserEntity user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }
        user.setPassword(passwordEncoder.encode(password));
        userEntityRepository.save(user);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userEntityRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
    }

    @Override
    public List<UserDto> getUsersInfo() {
        return userEntityRepository.findAll().stream().map(UserDto::new).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional
    public UserDto updateUserRole(UserRoleRequest roleRequest, UserEntity user) {
        UserEntity theUser = userEntityRepository.findByEmailIgnoreCase(roleRequest.getUser()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        Role role;
        try {
            role = Role.valueOf("ROLE_" + roleRequest.getRole());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }

        if (roleRequest.getOperation().toString().equals("REMOVE")) {
            if (role.equals(Role.ROLE_ADMINISTRATOR)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            } else if (!theUser.getRoles().contains(role)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            } else if (theUser.getRoles().size() < 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            }
            theUser.removeRole(role);
            return new UserDto(userEntityRepository.save(theUser));

        } else {
            if (role.equals(Role.ROLE_ADMINISTRATOR)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            } else if (theUser.getRoles().contains(Role.ROLE_ADMINISTRATOR) && (role.equals(Role.ROLE_USER) || role.equals(Role.ROLE_ACCOUNTANT))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            }
            theUser.addRole(role);
            return new UserDto(userEntityRepository.save(theUser));
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteUser(String email, UserEntity user) {
        UserEntity oldUser = userEntityRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        if (oldUser.getRoles().contains(Role.ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        userEntityRepository.delete(oldUser);
        return ResponseEntity.ok(Map.of("user", email, "status", "Deleted successfully!"));
    }
}
