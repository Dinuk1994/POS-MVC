package Model.impl;

import Model.CustomerModel;
import com.jfoenix.controls.JFXButton;
import db.DBConnection;
import dto.CustomerDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModelImpl implements CustomerModel {
    @Override
    public boolean isSavedCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO Customer Values(?,?,?,?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,dto.getId());
        pstm.setString(2, dto.getName());
        pstm.setString(3,dto.getAddress());
        pstm.setDouble(4,dto.getSalary());
        int result = pstm.executeUpdate();

        return result>0 ;
    }

    @Override
    public boolean isUpdatedCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        String sql="UPDATE Customer SET name=?,address=?,salary=? WHERE id=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,dto.getName());
        pstm.setString(2,dto.getAddress());
        pstm.setDouble(3,dto.getSalary());
        pstm.setString(4,dto.getId());
        int result = pstm.executeUpdate();
        return result>0;
    }

    @Override
    public boolean isDeleteCustomer(String id) throws SQLException, ClassNotFoundException {
        String sql="DELETE FROM customer WHERE id=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,id);
        int result = pstm.executeUpdate();
        return result>0;
    }

    @Override
    public List<CustomerDto> allCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDto> list = new ArrayList<>();
        String sql="SELECT * FROM Customer";

        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            CustomerDto c =new CustomerDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
                    );
          list.add(c);
        }

        return list;
    }


    @Override
    public CustomerDto searchCustomer(String id) {
        return null;
    }
}
