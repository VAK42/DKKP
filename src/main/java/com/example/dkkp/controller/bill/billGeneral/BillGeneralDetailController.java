package com.example.dkkp.controller.bill.billGeneral;
import com.example.dkkp.controller.product.TableInterface;
import com.example.dkkp.model.Bill_Detail_Entity;
import com.example.dkkp.model.Bill_Entity;
import com.example.dkkp.model.EnumType;
import com.example.dkkp.service.BillService;
import com.example.dkkp.service.SecurityFunction;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class BillGeneralDetailController implements TableInterface {
  EntityManager entityManager = entityManagerFactory.createEntityManager();
  EntityTransaction transaction = entityManager.getTransaction();
  private Bill_Entity billEntity;
  private BillGeneralController billGeneralController;
  @FXML
  private MFXTableView < Bill_Detail_Entity > billDetailTable;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > ID_BILL_DETAIL;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > ID_BILLD;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > AVAILABLE;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > ID_FINAL_PRODUCT;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > QUANTITY_SP;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > UNIT_PRICE;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > TOTAL_DETAIL_PRICE;
  @FXML
  private MFXTextField ID_BILL;
  @FXML
  private MFXTextField ID_USER;
  @FXML
  private MFXComboBox < String > BILL_STATUS;
  @FXML
  private MFXTextField PHONE_BILL;
  @FXML
  private MFXTextField ADD_BILL;
  @FXML
  private MFXTextField DESCRIPTION;
  @FXML
  private MFXTextField DATE_EXP;
  @FXML
  private MFXButton backBtn;
  @FXML
  private MFXButton updateBtn;
  private Stage popupStage;
  private ObservableList < Bill_Detail_Entity > list;
  @FXML
  public void initialize() throws Exception {
    list = getBillDetail();
    billDetailTable.setItems(list);
    setCol();
    setWidth();
    updateBtn.setOnMouseClicked(event -> updateBill());
    pushEntity();
    backBtn.setOnMouseClicked(event -> billGeneralController.closePopup(popupStage));
  }
  private void updateBill() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Deletion");
    alert.setHeaderText("Are you sure you want to update this item?");
    alert.setContentText("This action cannot be undone.");
    ButtonType yesButton = new ButtonType("Yes");
    ButtonType noButton = new ButtonType("No");
    alert.getButtonTypes().setAll(yesButton, noButton);
    Optional < ButtonType > result = alert.showAndWait();
    if (result.isPresent() && result.get() == yesButton) {
      try {
        transaction.begin();
        String billS = (BILL_STATUS.getValue() != null) ? BILL_STATUS.getValue() : null;
        System.out.println("status cua bill " + billS);
        EnumType.Status_Bill billStatus =
          switch (billS) {
          case "Pending" -> EnumType.Status_Bill.PEN;
          case "Payed" -> EnumType.Status_Bill.CONF;
          case "Shipped" -> EnumType.Status_Bill.SHIP;
          case "Delivered" -> EnumType.Status_Bill.DELI;
          case "Cancel" -> EnumType.Status_Bill.CANC;
          default -> null;
          };
        BillService billService = new BillService(entityManager);
        billService.changeBillStatus(billEntity.getID_BILL(), billStatus);
        transaction.commit();
        billGeneralController.closePopup(popupStage);
      } catch (Exception e) {
        transaction.rollback();
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Error");
        alert1.setHeaderText("Transaction Failed");
        alert1.setContentText(e.getMessage());
        alert1.showAndWait();
      }
    }
  }
  public ObservableList < Bill_Detail_Entity > getBillDetail() {
    List < Bill_Detail_Entity > p = null;
    try {
      BillService importService = new BillService(entityManager);
      Bill_Detail_Entity importDetailEntity = new Bill_Detail_Entity();
      importDetailEntity.setID_BILL(billEntity.getID_BILL());
      p = importService.getBillDetailByCombinedCondition(importDetailEntity, null, null, null, null, null, null, null);
      for (Bill_Detail_Entity i: p) {
        System.out.println("dcm " + i.getID_BILL_DETAIL());
      }
    } catch (Exception _) {} finally {
      return FXCollections.observableArrayList(p);
    }
  }
  @Override
  public void setWidth() {
    ID_BILL_DETAIL.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.13));
    ID_BILLD.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.13));
    AVAILABLE.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.37));
    ID_FINAL_PRODUCT.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.37));
    QUANTITY_SP.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.37));
    UNIT_PRICE.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.37));
    TOTAL_DETAIL_PRICE.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.37));
  }
  private void setCol() {
    ID_BILL_DETAIL.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Detail_Entity::getID_BILL_DETAIL));
    ID_BILLD.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Detail_Entity::getID_BILL));
    AVAILABLE.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Detail_Entity::getAVAILABLE));
    ID_FINAL_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Detail_Entity::getID_FINAL_PRODUCT));
    QUANTITY_SP.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Detail_Entity::getQUANTITY_BILL));
    UNIT_PRICE.setRowCellFactory(_ -> new MFXTableRowCell < > (product -> {
      Double price = product.getUNIT_PRICE();
      if (price != null) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(price);
      }
      return "";
    }));
    TOTAL_DETAIL_PRICE.setRowCellFactory(_ -> new MFXTableRowCell < > (product -> {
      Double price = product.getTOTAL_DETAIL_PRICE();
      if (price != null) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(price);
      }
      return "";
    }));
  }
  public void setBillGeneralController(BillGeneralController billGeneralController) {
    this.billGeneralController = billGeneralController;
  }
  public void setEntity(Bill_Entity import_Entity) {
    this.billEntity = import_Entity;
  }
  public void pushEntity() throws Exception {
    if (billEntity != null) {
      ID_BILL.setText(billEntity.getID_BILL());
      ID_USER.setText(SecurityFunction.decrypt(billEntity.getEMAIL_ACC()));
      BILL_STATUS.setText(billEntity.getBILL_STATUS().getDescription());
      PHONE_BILL.setText((billEntity.getPHONE_BILL()));
      ADD_BILL.setText((billEntity.getADD_BILL()));
      DESCRIPTION.setText(billEntity.getDESCRIPTION());
      DATE_EXP.setText(billEntity.getDATE_EXP().toString());
    }
  }
  public void setPopupStage(Stage popupStage) {
    this.popupStage = popupStage;
  }
}