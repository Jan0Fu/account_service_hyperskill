package account.controller;

import account.model.UserEntity;
import account.model.dto.ChangePasswordRequest;
import account.model.dto.UserDto;
import account.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

    @PostMapping(value = {"/auth/signup", "/auth/signup/"})
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserEntity user) {
        return userEntityService.registerUser(user);
    }

    @PostMapping("/auth/changepass")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequest password,
                                                 @AuthenticationPrincipal UserEntity user) {
        return userEntityService.changePassword(password.getNew_password(), user.getEmail());
    }

    @GetMapping("/empl/payment")
    public UserDto getUserInfo(@AuthenticationPrincipal UserEntity user) {
        return userEntityService.getUser(user);
    }
}
