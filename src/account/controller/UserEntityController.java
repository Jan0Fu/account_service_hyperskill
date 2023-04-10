package account.controller;

import account.exceptions.UserAlreadyExistsException;
import account.model.UserEntity;
import account.model.dto.UserDto;
import account.service.UserEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserEntity user) throws UserAlreadyExistsException {
        UserDto register = userEntityService.register(user);
        return ResponseEntity.ok().body(register);
    }
}
