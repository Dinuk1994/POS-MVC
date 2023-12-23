package controller;

import Model.ItemModel;
import Model.impl.ItemModelImpl;
import com.google.protobuf.StringValue;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.CustomerDto;
import dto.ItemDto;
import dto.tm.ItemTm;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ItemFormController {

    public Label lblTime;
    @FXML
    private AnchorPane pane3;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTreeTableView<ItemTm> tblItem;

    @FXML
    private TreeTableColumn colCode;

    @FXML
    private TreeTableColumn colDesc;

    @FXML
    private TreeTableColumn colPrice;

    @FXML
    private TreeTableColumn colQty;

    @FXML
    private TreeTableColumn colOption;

    public ItemModel itemModel = new ItemModelImpl();

    public void initialize(){
        calculateTime();
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadItemTable();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, itemTmTreeItem, newValue) ->{
            setData(newValue);
        } );
    }

    private void calculateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void setData(TreeItem<ItemTm> newValue) {
        if (newValue!=null){
            txtCode.setEditable(false);
            txtCode.setText(newValue.getValue().getCode());
            txtDesc.setText(newValue.getValue().getDesc());
            txtPrice.setText(String.valueOf(newValue.getValue().getUnitPrice()));
            txtQty.setText(String.valueOf(newValue.getValue().getQty()));

        }

    }


    private void loadItemTable() {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();

        try {
            List<ItemDto> itemList = itemModel.allItems();
            for (ItemDto dto:itemList) {
                JFXButton btn=new JFXButton("Delete");
                btn.setStyle("-fx-background-color: #EF6262;");
                ItemTm item=new ItemTm(
                        dto.getCode(),
                        dto.getDesc(),
                        dto.getUnitPrice(),
                        dto.getQty(),
                        btn
                );
                btn.setOnAction(actionEvent -> {
                    deleteItem(item.getCode());
                });
                tmList.add(item);

                RecursiveTreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
                tblItem.setRoot(treeItem);
                tblItem.setShowRoot(false);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private void deleteItem(String code) {
        try {
            boolean isDelete = itemModel.isDeleteItem(code);
            if (isDelete){
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted!").show();
                loadItemTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something Went Wrong!").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    public void saveButtonOnAction(javafx.event.ActionEvent actionEvent) {


        try {
            ItemDto dto=new ItemDto(txtCode.getText(),txtDesc.getText(),Integer.parseInt(txtQty.getText()),Double.parseDouble(txtPrice.getText()));
            boolean isSaved = itemModel.isSavedItem(dto);
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Item Saved!").show();
                loadItemTable();
            }else {
                new Alert(Alert.AlertType.ERROR,"Something Went Wrong!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void updateButtonOnAction(javafx.event.ActionEvent actionEvent) {
        try {
            ItemDto item=new ItemDto(txtCode.getText(),txtDesc.getText(),Integer.parseInt(txtQty.getText()),Double.parseDouble(txtPrice.getText()));
            boolean isUpdate = itemModel.isUpdatedItem(item);
            if (isUpdate){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated").show();
                loadItemTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something Went Wrong!").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public void backButtonOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage)pane3.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashBoardForm.fxml"))));
            stage.show();
            stage.setTitle("Dashboard Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadButtonOnAction(ActionEvent actionEvent) {
        loadItemTable();
        clearFields();
    }

    private void clearFields() {
        txtCode.clear();
        txtDesc.clear();
        txtPrice.clear();
        txtQty.clear();
        txtSearch.clear();
    }
}



