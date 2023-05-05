package account.controller;

import account.model.UserEntity;
import account.model.dto.AccessChangeRequest;
import account.model.dto.UserDto;
import account.model.dto.UserRoleRequest;
import account.service.UserEntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserEntityService userService;

    public AdminController(UserEntityService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<UserDto> getUsersInfo() {
        return userService.getUsersInfo();
    }

    @PutMapping("/user/role")
    public UserDto updateUserRole(@RequestBody UserRoleRequest roleRequest, @AuthenticationPrincipal UserEntity user) {
        return userService.updateUserRole(roleRequest, user);
    }

    @DeleteMapping("/user/{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email, @AuthenticationPrincipal UserEntity user) {
        return userService.deleteUser(email, user);
    }

    @PutMapping("/user/access")
    public ResponseEntity<Object> setAccess(@RequestBody AccessChangeRequest access, @AuthenticationPrincipal UserEntity user) {
        return userService.setAccess(access, user);
    }
}
