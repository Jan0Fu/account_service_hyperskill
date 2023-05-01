package account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeDto {

    String name;
    String lastname;
    String period;
    String salary;
}
