package account.config;

import account.model.SecurityEvent;
import account.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import javax.servlet.http.HttpServletRequest;

@Configuration
@AllArgsConstructor
public class AuthFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LogService logService;
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        String requestURI = request.getRequestURI();
        logService.saveLog(SecurityEvent.LOGIN_FAILED, username, requestURI, requestURI);
        if (username != null) {
            logService.loginFailure(username, requestURI);
        }
    }
}
