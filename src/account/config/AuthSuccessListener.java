package account.config;

import account.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Configuration
@AllArgsConstructor
public class AuthSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LogService logService;


    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        if (username != null) {
            logService.loginSuccess(username);
        }
    }
}
