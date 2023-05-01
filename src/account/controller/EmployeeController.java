package account.controller;

import account.model.Employee;
import account.model.UserEntity;
import account.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

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
