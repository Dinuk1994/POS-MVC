package dao.impl;

import dao.ItemModel;
import db.DBConnection;
import dto.ItemDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemModelImpl implements ItemModel {

    @Override
    public boolean isSavedItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO Item VALUE(?,?,?,?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,dto.getCode());
        pstm.setString(2,dto.getDesc());
        pstm.setInt(3,dto.getQty());
        pstm.setDouble(4,dto.getUnitPrice());

        int result = pstm.executeUpdate();
        return result>0;
    }

    @Override
    public boolean isUpdatedItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        String sql="UPDATE Item SET Description=?,unitPrice=?,qtyOnHand=? WHERE code=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, dto.getDesc());
        pstm.setInt(2,dto.getQty());
        pstm.setDouble(3,dto.getUnitPrice());
        pstm.setString(4,dto.getCode());
        int result = pstm.executeUpdate();

        return result>0;
    }

    @Override
    public boolean isDeleteItem(String code) throws SQLException, ClassNotFoundException {
        String sql="DELETE FROM Item WHERE Code=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,code);
        int result = pstm.executeUpdate();
        return result>0;
    }

    @Override
    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDto> itemList = new ArrayList<>();
        String sql="SELECT * FROM Item";
        Statement stm = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultSet = stm.executeQuery(sql);
        while (resultSet.next()){
            ItemDto item=new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            );
            itemList.add(item);

        }
        return itemList;
    }

    @Override
    public ItemDto searchItem(String code) {
        return null;
    }
}
