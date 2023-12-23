package dao;

import dto.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemModel {
    boolean isSavedItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    boolean isUpdatedItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    boolean isDeleteItem(String code) throws SQLException, ClassNotFoundException;
    List<ItemDto> allItems() throws SQLException, ClassNotFoundException;
    ItemDto searchItem(String code);
}
