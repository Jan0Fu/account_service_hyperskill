package account.repository;

import account.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByEmployeeIgnoreCaseAndPeriod(String employee, String period);

    List<Employee> findEmployeeByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);
}
