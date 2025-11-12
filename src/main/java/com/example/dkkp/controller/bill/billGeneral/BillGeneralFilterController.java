package com.example.dkkp.controller.bill.billGeneral;
import com.example.dkkp.model.Bill_Entity;
import com.example.dkkp.model.EnumType;
import com.example.dkkp.model.User_Entity;
import com.example.dkkp.service.UserService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class BillGeneralFilterController {
  EntityManager entityManager = entityManagerFactory.createEntityManager();
  EntityTransaction transaction = entityManager.getTransaction();
  @FXML
  private MFXTextField ID_BILL;
  @FXML
  private MFXTextField PHONE_BILL;
  @FXML
  private MFXTextField ADD_BILL;
  @FXML
  private MFXFilterComboBox < User_Entity > ID_USER;
  @FXML
  private MFXComboBox < String > totalPriceComboBox;
  @FXML
  private MFXComboBox < String > BILL_STATUS;
  @FXML
  private MFXComboBox < String > dateCombobox;
  @FXML
  private MFXTextField TOTAL_PRICE;
  @FXML
  private MFXDatePicker datePicker;
  @FXML
  private Spinner hourSpinner;
  @FXML
  private Spinner minuteSpinner;
  @FXML
  private Spinner secondSpinner;
  @FXML
  private MFXButton back;
  @FXML
  private MFXButton applyButton;
  private Stage popupStage;
  BillGeneralController billGeneralController;
  @FXML
  public void createFilter() throws Exception {
    LocalDateTime dateTime = null;
    int hour = 0;
    int minute = 0;
    int second = 0;
    if (datePicker.getValue() != null) {
      LocalDate date = datePicker.getValue();
      if (hourSpinner.getValue() != null) hour = (int) hourSpinner.getValue();
      if (minuteSpinner.getValue() != null) minute = (int) minuteSpinner.getValue();
      if (secondSpinner.getValue() != null) second = (int) secondSpinner.getValue();
      dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute, second));
    }
    String id = ID_BILL.getText().trim().isEmpty() ? null : ID_BILL.getText();
    String idUser = (ID_USER.getValue() != null) ? ID_USER.getValue().getID_USER() : null;
    if (totalPriceComboBox != null) {
      billGeneralController.typePrice = getValueOperator(totalPriceComboBox.getValue());
    }
    if (dateCombobox != null) {
      billGeneralController.typeDate = getValueOperator(dateCombobox.getValue());
    }
    String phone = null;
    String add = null;
    phone = (PHONE_BILL.getText().isEmpty()) ? null : PHONE_BILL.getText();
    add = (ADD_BILL.getText().isEmpty()) ? null : ADD_BILL.getText();
    String billS = (BILL_STATUS.getValue() != null) ? BILL_STATUS.getValue() : null;
    EnumType.Status_Bill billStatus =
      switch (billS) {
      case "Pending" -> EnumType.Status_Bill.PEN;
      case "Payed" -> EnumType.Status_Bill.CONF;
      case "Shipped" -> EnumType.Status_Bill.SHIP;
      case "Delivered" -> EnumType.Status_Bill.DELI;
      case "Cancel" -> EnumType.Status_Bill.CANC;
      case null -> null;
      default -> null;
      };
    Double pPrice = TOTAL_PRICE.getText().isEmpty() ? null : Double.parseDouble(TOTAL_PRICE.getText());
    billGeneralController.billEntity = new Bill_Entity(id, dateTime, phone, add, idUser, pPrice, null, billStatus, null);
    billGeneralController.setPage(1);
    billGeneralController.billController.setMainView("/com/example/dkkp/BillGeneral/BillGeneralView.fxml", billGeneralController);
    billGeneralController.closePopup(popupStage);
  }
  private String getValueOperator(String value) {
    if (value == null) return null;
    System.out.println(value + " day la");
    return switch (value) {
    case "Equal" -> "=";
    case "More" -> ">";
    case "Less" -> "<";
    case "Equal or More" -> "=>";
    case "Equal or Less" -> "<=";
    default -> null;
    };
  }
  public void setPopupStage(Stage popupStage) {
    this.popupStage = popupStage;
  }
  @FXML
  public void initialize() {
    hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
    minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    secondSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    setTextFormatter();
    applyButton.setOnAction(event -> {
      try {
        createFilter();
      } catch (Exception e) {
        System.out.println("loi " + e.getMessage());
        throw new RuntimeException(e);
      }
    });
    back.setOnMouseClicked(event -> {
      billGeneralController.closePopup(popupStage);
    });
    UserService userService = new UserService(entityManager);
    ID_USER.getItems().addAll(userService.getAllUser());
  }
  private void setTextFormatter() {
    Validator validator3 = new Validator();
    TOTAL_PRICE.delegateSetTextFormatter(validator3.formatterDouble);
  }
  public void setBillGeneralController(BillGeneralController billGeneralController) {
    this.billGeneralController = billGeneralController;
  }
}