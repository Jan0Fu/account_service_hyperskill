package account.controller;

import account.model.Employee;
import account.model.UserEntity;
import account.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @ExceptionHandler({ConstraintViolationException.class, org.hibernate.exception.ConstraintViolationException.class})
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @GetMapping("/empl/payment")
    public ResponseEntity<Object> getPayrollByPeriod(@AuthenticationPrincipal UserEntity user,
                                                     @RequestParam Optional<String> period) {
        return employeeService.getPayrolls(user.getEmail(), period);
    }

    @PostMapping("/acct/payments")
    public ResponseEntity<Object> uploadPayrolls(@RequestBody List<@Valid Employee> employees) {
        return employeeService.addPayrolls(employees);
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<Object> changeSalary(@Valid @RequestBody Employee employee) {
        return employeeService.updateSalary(employee);
    }
}
