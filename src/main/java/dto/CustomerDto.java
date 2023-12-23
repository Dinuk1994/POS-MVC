package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class CustomerDto {
    private String id;
    private String name;
    private String Address;
    private double Salary;
}
