package account.config;

import account.model.SecurityEvent;
import account.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private LogService logService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        String requestUri = request.getRequestURI();
        String subject = SecurityContextHolder.getContext().getAuthentication().getName();
        logService.saveLog(SecurityEvent.ACCESS_DENIED, subject, requestUri, requestUri);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getOutputStream().println(new ObjectMapper().writeValueAsString(Map.of(
                "timestamp", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden",
                "message", "Access Denied!",
                "path", request.getRequestURI()
        )));
    }
}
