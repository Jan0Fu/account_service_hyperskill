package account.service;

import account.model.LogEntity;
import account.model.Role;
import account.model.SecurityEvent;
import account.model.UserEntity;
import account.repository.LogRepository;
import account.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {

    private final static int ATTEMPTS = 4;
    private final LogRepository logRepository;
    private final UserEntityRepository userEntityRepository;

    public void saveLog(SecurityEvent action, String subject, String object, String path) {
        logRepository.save(LogEntity.builder().action(action).subject(subject).object(object).path(path)
                .build());
    }

    @Override
    public List<LogEntity> getAllLogs() {
        return logRepository.findAll();
    }

    @Override
    public void loginSuccess(String email) {
        UserEntity user = userEntityRepository.findByEmailIgnoreCase(email).orElseGet(UserEntity::new);
        user.setFailedLogins(0);
        userEntityRepository.save(user);
    }

    @Override
    public void loginFailure(String email, String requestURI) {
        Optional<UserEntity> userOptional = userEntityRepository.findByEmailIgnoreCase(email);
        if (userOptional.isEmpty()) {
            return;
        }
        UserEntity user = userOptional.get();

        if(user.getRoles().contains(Role.ROLE_ADMINISTRATOR)) {
            return;
        }
        user.setFailedLogins(user.getFailedLogins() + 1);

        if (user.getFailedLogins() > ATTEMPTS) {
            user.setAccountNonLocked(false);
            saveLog(SecurityEvent.BRUTE_FORCE, email, requestURI, requestURI);
            saveLog(SecurityEvent.LOCK_USER, email, "Lock user " + email, requestURI);
        }
        userEntityRepository.save(user);
    }
}
