package dto;


import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ItemDto {
    private String code;
    private String desc;
    private int qty;
    private double unitPrice;
}