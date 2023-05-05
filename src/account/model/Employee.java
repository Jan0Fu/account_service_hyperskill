package account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @NotBlank(message = "Employee cannot be empty")
    private String employee;

    @NotBlank(message = "Period cannot be empty")
    private String period;

    @NotNull
    @Min(value = 0, message = "Salary must be non negative!")
    private Long salary;
}
