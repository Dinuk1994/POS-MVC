package dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class CustomerTm {
    private String id;
    private String name;
    private String address;
    private Double salary;
    private JFXButton btn;


}
