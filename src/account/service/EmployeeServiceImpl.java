package account.service;

import account.model.Employee;
import account.model.UserEntity;
import account.model.dto.EmployeeDto;
import account.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.DateFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserEntityService userService;

    @Override
    @Transactional
    public ResponseEntity<Object> addPayrolls(List<Employee> employees) throws ResponseStatusException {
        String error = saveValidPayrolls(employees);
        if (error != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
        return ResponseEntity.ok().body(Map.of("status", "Added successfully!"));
    }

    @Override
    public ResponseEntity<Object> updateSalary(Employee employee) throws ResponseStatusException {
        Employee updatedEmployee = employeeRepository.findEmployeeByEmployeeIgnoreCaseAndPeriod(
                employee.getEmployee(), employee.getPeriod())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payroll doesn't exist"));
        updatedEmployee.setSalary(employee.getSalary());
        employeeRepository.save(updatedEmployee);
        return ResponseEntity.ok().body(Map.of("status", "Updated successfully!"));
    }

    @Override
    public ResponseEntity<Object> getPayrolls(String email, Optional<String> period) {
        return period.<ResponseEntity<Object>>map(p -> ResponseEntity.ok(getEmployeeByEmailAndPeriod(email, p)))
                .orElseGet(() -> ResponseEntity.ok(getEmployeesByEmail(email)));
    }

    public List<EmployeeDto> getEmployeesByEmail(String email) {
        List<Employee> employeess = employeeRepository.
                findEmployeeByEmployeeIgnoreCaseOrderByPeriodDesc(email);
        return employeess.stream()
                .map(this::getEmployeeDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public EmployeeDto getEmployeeByEmailAndPeriod(String email, String period) throws ResponseStatusException {
        Optional<Employee> payroll = employeeRepository
                .findEmployeeByEmployeeIgnoreCaseAndPeriod(email, period);
        return payroll
                .map(employee -> getEmployeeDto(payroll.get()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Payroll Found"));
    }

    public EmployeeDto getEmployeeDto(Employee employee) {
        UserEntity user = userService.findUserByEmail(employee.getEmployee());
        return new EmployeeDto(
                user.getName(), user.getLastname(),
                formatPeriod(employee.getPeriod()), formatSalary(employee.getSalary()));
    }

    public String formatPeriod(String period) {
        String[] periodArray = period.split("-");
        try {
            String month = new DateFormatSymbols().getMonths()[Integer.parseInt(periodArray[0]) - 1];
            return String.format("%s-%s", month, periodArray[1]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Date");
        }
    }

    public String formatSalary(Long salary) {
        return String.format("%d dollar(s) %d cent(s)", salary / 100, salary % 100);
    }

    public boolean isPayrollUnique(String employee, String period) {
        return employeeRepository.findEmployeeByEmployeeIgnoreCaseAndPeriod(employee, period).isEmpty();
    }

    @Transactional
    public String saveValidPayrolls(List<Employee> employees) {
        for (var employee : employees) {
            if (!isPayrollUnique(employee.getEmployee(), employee.getPeriod())) {
                return "Payroll with same employee and period already exists";
            }
            int month = Integer.parseInt(employee.getPeriod().split("-")[0]);
            if (month < 0 || month > 12) {
                return "Invalid date";
            }
            employeeRepository.save(employee);
        }
        return null;
    }
}
