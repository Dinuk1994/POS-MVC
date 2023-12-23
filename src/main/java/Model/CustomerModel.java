package Model;

import dto.CustomerDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerModel {
   boolean isSavedCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
   boolean isUpdatedCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
   boolean isDeleteCustomer(String id) throws SQLException, ClassNotFoundException;
   List<CustomerDto> allCustomers() throws SQLException, ClassNotFoundException;
   CustomerDto searchCustomer(String id);
}
