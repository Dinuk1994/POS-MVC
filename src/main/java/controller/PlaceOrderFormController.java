package controller;

import dao.CustomerModel;
import dao.ItemModel;
import dao.OrderModel;
import dao.impl.CustomerModelImpl;
import dao.impl.ItemModelImpl;
import dao.impl.OrderModelImpl;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDto;
import dto.OrderDetailsDto;
import dto.tm.OrderTm;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderFormController {
    public AnchorPane pane4;
    public Label lblTime;
    public JFXComboBox cmbCode;
    public JFXComboBox cmbId;
    public JFXTextField txtName;
    public JFXTextField txtDesc;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public JFXTreeTableView<OrderTm> tblOrder;
    public TreeTableColumn colCode;
    public TreeTableColumn colDesc;
    public TreeTableColumn colQty;
    public TreeTableColumn colAmount;
    public TreeTableColumn colOption;
    public Label lblTotal;
    public Label lblOrderId;

    private List<CustomerDto> customers;
    private List<ItemDto> items;

    CustomerModel customerModel=new CustomerModelImpl();
    ItemModel itemModel=new ItemModelImpl();
    OrderModel orderModel=new OrderModelImpl();

    ObservableList<OrderTm> orderList = FXCollections.observableArrayList();


    public void initialize(){
        calculateTime();
        loadCustomerIds();
        loadItemCodes();
        generateID();

        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("Qty"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("Amount"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        cmbId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, id) ->{
            setCustomerData((String) id);
        } );

        cmbCode.getSelectionModel().selectedItemProperty().addListener((observableValue, OldValue, code) ->{
            setItemData(code);


        } );
    }

    private void setItemData(Object code) {
        for (ItemDto dto:items) {
            if (dto.getCode().equals(code)){
                txtDesc.setText(dto.getDesc());
                txtUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
                txtQty.setText(String.valueOf(dto.getQty()));

            }
        }
    }

    private void setCustomerData(String id) {
        for (CustomerDto dto:customers) {
            if (dto.getId().equals(id)){
                txtName.setText(dto.getName());
            }
        }
    }

    private void loadCustomerIds() {
        try {
            customers=customerModel.allCustomers();
            ObservableList list = FXCollections.observableArrayList();
            for (CustomerDto dto:customers) {
                list.add(dto.getId());
            }
            cmbId.setItems(list);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadItemCodes() {
        try {
            items=itemModel.allItems();
            ObservableList list = FXCollections.observableArrayList();
            for (ItemDto dto:items) {
                list.add(dto.getCode());
            }
            cmbCode.setItems(list);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void calculateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> lblTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage= (Stage)pane4.getScene().getWindow();
        stage.setTitle("Dashboard Form");
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashBoardForm.fxml"))));
        stage.show();
    }

    public void addToCartButtonOnAction(ActionEvent actionEvent) {

        double amount= Integer.parseInt(txtQty.getText()) * Double.parseDouble(txtUnitPrice.getText());
        JFXButton btn=new JFXButton("Delete");
        OrderTm orderTm = new OrderTm(
                cmbCode.getValue().toString(),
                txtDesc.getText(),
                Integer.parseInt(txtQty.getText()),
                amount,
                btn
        );

        btn.setOnAction(actionEvent1 -> {
            deleteOrder(orderTm);
        });
        orderList.add(orderTm);

        lblTotal.setText(calculateTotal());

        RecursiveTreeItem<OrderTm> treeItem = new RecursiveTreeItem<>(orderList, RecursiveTreeObject::getChildren);
        tblOrder.setRoot(treeItem);
        tblOrder.setShowRoot(false);
    }

    private String calculateTotal() {
        double total=0;
        for (OrderTm order:orderList) {
            total+=order.getAmount();
        }
        return String.format("%.2f",total);
    }

    private void deleteOrder(OrderTm orderTm) {
        orderList.remove(orderTm);
        lblTotal.setText(calculateTotal());
    }

    public void generateID()  {
        try {
            OrderDto dto= orderModel.lastOrder();
            if (dto!=null){
                String id= dto.getId();
                int num=Integer.parseInt(id.split("[D]")[1]);
                num++;
                lblOrderId.setText(String.format("D%03d",num));
            }else{
              lblOrderId.setText("D001");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void placeOrderButtonOnAction(ActionEvent actionEvent) {
        List<OrderDetailsDto> list=new ArrayList<>();

        for (OrderTm tm:orderList) {
            OrderDetailsDto orderDetailsDto = new OrderDetailsDto(
                    lblOrderId.getText(),
                    tm.getCode(),
                    tm.getQty(),
                    tm.getAmount()/tm.getQty()
            );
            list.add(orderDetailsDto);
        }

        if (!orderList.isEmpty()){
            boolean saved=false;
            try {
                saved = orderModel.isSaved(new OrderDto(
                        lblOrderId.getText(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")).toString(),
                        cmbId.getValue().toString(),
                        list
                ));
                if (saved){
                    new Alert(Alert.AlertType.INFORMATION,"Order Saved!").show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
