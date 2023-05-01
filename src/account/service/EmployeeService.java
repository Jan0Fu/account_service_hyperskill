package account.service;

import account.model.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    ResponseEntity<Object> addPayrolls(List<Employee> employees);

    ResponseEntity<Object> updateSalary(Employee employee);

    ResponseEntity<Object> getPayrolls(String email, Optional<String> period);
}
