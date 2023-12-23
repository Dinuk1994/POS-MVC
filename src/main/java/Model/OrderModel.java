package Model;

import dto.OrderDto;

import java.sql.SQLException;

public interface OrderModel {
    boolean isSaved(OrderDto dto) throws SQLException, ClassNotFoundException;
    OrderDto lastOrder() throws SQLException, ClassNotFoundException;

}
