package account.controller;

import account.model.UserEntity;
import account.model.dto.ChangePasswordRequest;
import account.model.dto.UserDto;
import account.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")

public class UserEntityController {

    private final UserEntityService userEntityService;

    public UserEntityController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @PostMapping(value = {"/auth/signup", "/auth/signup/"})
    public UserDto registerUser(@Valid @RequestBody UserEntity user) {
        return userEntityService.registerUser(user);
    }

    @PostMapping("/auth/changepass")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequest password,
                                                 @AuthenticationPrincipal UserEntity user) {
        return userEntityService.changePassword(password.getNew_password(), user.getEmail());
    }
}
