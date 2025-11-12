package com.example.dkkp.controller.bill.billGeneral;
import com.example.dkkp.controller.bill.BillController;
import com.example.dkkp.controller.bill.billDetail.BillDetailCreateController;
import com.example.dkkp.model.*;
import com.example.dkkp.service.BillService;
import com.example.dkkp.service.UserService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class BillGeneralCreateController {
  EntityManager entityManager = entityManagerFactory.createEntityManager();
  EntityTransaction transaction = entityManager.getTransaction();
  @FXML
  private MFXTableView < Bill_Detail_Entity > billDetailTable;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > ID_FINAL_PRODUCT;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > QUANTITY_SP;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > UNIT_PRICE;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > TOTAL_DETAIL_PRICE;
  @FXML
  private MFXTableColumn < Bill_Detail_Entity > AVAILABLE;
  @FXML
  private MFXTextField ADD_BILL;
  @FXML
  private MFXTextField PHONE_BILL;
  @FXML
  private MFXTextField DESCRIPTION;
  @FXML
  private MFXFilterComboBox < User_Entity > ID_USER;
  @FXML
  private MFXDatePicker datePicker;
  @FXML
  private Spinner hourSpinner;
  @FXML
  private Spinner minuteSpinner;
  @FXML
  private Spinner secondSpinner;
  @FXML
  private MFXButton createBtn;
  @FXML
  private MFXButton back;
  @FXML
  private MFXButton createBillDetail;
  @FXML
  private MFXButton deleteBillDetail;
  public ObservableList < Bill_Detail_Entity > observableList;
  private static final Logger logger = LoggerFactory.getLogger(BillController.class);
  BillGeneralController billGeneralController;
  BillDetailCreateController importDetailCreateController = new BillDetailCreateController();
  public List < Bill_Detail_Entity > listBillDetail = new ArrayList < > ();
  @FXML
  public void createItem() throws Exception {
    System.out.println("Dang trong create");
    LocalDateTime dateTime;
    int hour = 0;
    int minute = 0;
    int second = 0;
    System.out.println("Dang trong create");
    if (datePicker.getValue() != null) {
      System.out.println("dang khong null");
      LocalDate date = datePicker.getValue();
      if (hourSpinner.getValue() != null) hour = (int) hourSpinner.getValue();
      if (minuteSpinner.getValue() != null) minute = (int) minuteSpinner.getValue();
      if (secondSpinner.getValue() != null) second = (int) secondSpinner.getValue();
      dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute, second));
    } else {
      System.out.println("duma nullll");
      dateTime = LocalDateTime.now();
    }
    System.out.println("Dang trong create " + dateTime);
    String des = (DESCRIPTION.getText().isEmpty()) ? null : DESCRIPTION.getText();
    String idUser = (ID_USER.getValue() != null) ? ID_USER.getValue().getID_USER() : null;
    String phone = null;
    String add = null;
    if (idUser != null) {
      UserService userService = new UserService(entityManager);
      phone = userService.getUsersByID(idUser).getPHONE_ACC();
      add = userService.getUsersByID(idUser).getADDRESS();
    }
    if (!PHONE_BILL.getText().isEmpty()) {
      System.out.println("dang khong null");
      phone = PHONE_BILL.getText();
      try {
        Validator.normalizePhoneNumber(phone);
      } catch (Exception e) {
        showAlert("Error", "Please Enter right phone format");
        return;
      }
    }
    if (!ADD_BILL.getText().isEmpty()) add = ADD_BILL.getText();
    EnumType.Status_Bill billStatus = EnumType.Status_Bill.PEN;
    transaction.begin();
    try {
      Bill_Entity billEntity = new Bill_Entity(dateTime, add, phone, idUser, null, des, billStatus);
      BillService billService = new BillService(entityManager);
      billService.registerNewBill(billEntity, phone, add);
      if (!listBillDetail.isEmpty()) {
        for (Bill_Detail_Entity item: listBillDetail) {
          System.out.println("dang trong for");
          item.setID_BILL(billEntity.getID_BILL());
          item.setAVAILABLE(true);
        }
      };
      System.out.println("kiem tra phat cuoi " + billEntity.getDATE_EXP());
      if (!listBillDetail.isEmpty()) billService.registerNewBillDetail(listBillDetail);
      billGeneralController.billController.setMainView("/com/example/dkkp/BillGeneral/BillGeneralView.fxml", billGeneralController);
      transaction.commit();
      observableList.clear();
      listBillDetail.clear();
    } catch (Exception e) {
      transaction.rollback();
    }
  }
  private void showAlert(String title, String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  @FXML
  public void initialize() {
    setItem();
    setCol();
    setWidth();
    crt();
    del();
    hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
    minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    secondSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    UserService userService = new UserService(entityManager);
    ID_USER.getItems().addAll(userService.getAllUser());
  }
  private void del() {
    deleteBillDetail.setOnMouseClicked(event -> {
      List < Bill_Detail_Entity > selectedItems = billDetailTable.getSelectionModel().getSelectedValues();
      if (!selectedItems.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("This action cannot be undone.");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional < ButtonType > result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
          listBillDetail.removeAll(selectedItems);
          observableList.removeAll(selectedItems);
          setItem();
        }
      }
    });
  }
  private void crt() {
    createBtn.setOnAction(event -> {
      try {
        createItem();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    back.setOnMouseClicked(event -> {
      observableList.clear();
      listBillDetail.clear();
      billGeneralController.billController.setMainView("/com/example/dkkp/BillGeneral/BillGeneralView.fxml", billGeneralController);
    });
    createBillDetail.setOnMouseClicked(event -> {
      try {
        importDetailCreateController.setBillGeneralCreateController(this);
        Stage popupStage = setPopView("/com/example/dkkp/BillDetail/BillDetailCreate.fxml", importDetailCreateController);
        importDetailCreateController.setPopupStage(popupStage);
      } catch (Exception e) {
        System.out.println("coi loi " + e.getMessage());
      }
    });
  }
  public void setItem() {
    observableList = FXCollections.observableArrayList(listBillDetail);
    billDetailTable.setItems(observableList);
  }
  private void setCol() {
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
    AVAILABLE.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Detail_Entity::getAVAILABLE));
  }
  private void setWidth() {
    ID_FINAL_PRODUCT.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.2));
    QUANTITY_SP.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.2));
    UNIT_PRICE.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.2));
    TOTAL_DETAIL_PRICE.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.2));
    AVAILABLE.prefWidthProperty().bind(billDetailTable.widthProperty().multiply(0.2));
  }
  public void setBillGeneralController(BillGeneralController billGeneralController) {
    this.billGeneralController = billGeneralController;
  }
  public Stage setPopView(String fxmlPath, Object controller) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      loader.setController(controller);
      Stage popupStage = new Stage();
      popupStage.initModality(Modality.APPLICATION_MODAL);
      popupStage.setTitle("Base product filter");
      Scene scene = new Scene(loader.load());
      popupStage.setScene(scene);
      double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
      double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
      popupStage.setWidth(screenWidth * 0.8);
      popupStage.setHeight(screenHeight * 0.8);
      popupStage.show();
      return popupStage;
    } catch (IOException e) {
      logger.error(e.getMessage());
      return null;
    }
  }
  public void closePopup(Stage popupStage) {
    if (popupStage != null) {
      popupStage.close();
    }
  }
}