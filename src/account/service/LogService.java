package account.service;

import account.model.LogEntity;
import account.model.SecurityEvent;

import java.util.List;

public interface LogService {

    void saveLog(SecurityEvent action, String subject, String object, String path);

    List<LogEntity> getAllLogs();

    void loginSuccess(String username);

    void loginFailure(String username, String requestURI);
}
