package account.controller;

import account.model.LogEntity;
import account.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class SecurityController {

    private final LogService logService;

    @GetMapping("/api/security/events")
    public List<LogEntity> getAllLogs() {
        return logService.getAllLogs();
    }
}
