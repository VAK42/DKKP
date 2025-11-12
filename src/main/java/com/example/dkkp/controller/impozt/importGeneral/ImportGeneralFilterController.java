package com.example.dkkp.controller.impozt.importGeneral;
import com.example.dkkp.model.Import_Entity;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ImportGeneralFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID_IMP;
@FXML
private MFXTextField ID_REPLACE;
@FXML
private MFXComboBox<String> totalPriceComboBox;
@FXML
private MFXComboBox<String> isAvailableComboBox;
@FXML
private MFXComboBox<String> dateCombobox;
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
ImportGeneralController importGeneralController;
@FXML
public void createFilter() {
LocalDateTime dateTime = LocalDateTime.now();
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
String id = ID_IMP.getText().trim().isEmpty() ? null : ID_IMP.getText();
String idReplace = ID_REPLACE.getText().trim().isEmpty() ? null : ID_REPLACE.getText();
if (totalPriceComboBox != null) {
importGeneralController.typePrice = getValueOperator(totalPriceComboBox.getValue());
}
if (dateCombobox != null) {
importGeneralController.typeDate = getValueOperator(dateCombobox.getValue());
}
Boolean isAvailable = null;
if (isAvailableComboBox.getValue() != null) isAvailable = switch (isAvailableComboBox.getValue()) {
case "Both" -> null;
case "Yes" -> true;
case "No" -> false;
case null -> false;
default -> null;
};
Double pPrice = TOTAL_PRICE.getText().isEmpty() ? null : Double.parseDouble(TOTAL_PRICE.getText());
importGeneralController.importEntity = new Import_Entity(id, dateTime, null, isAvailable, idReplace, pPrice);
importGeneralController.setPage(1);
importGeneralController.importController.setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralView.fxml", importGeneralController);
importGeneralController.closePopup(popupStage);
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
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
importGeneralController.closePopup(popupStage);
});
}
private void setTextFormatter() {
Validator validator3 = new Validator();
TOTAL_PRICE.delegateSetTextFormatter(validator3.formatterDouble);
}
public void setImportGeneralController(ImportGeneralController importGeneralController) {
this.importGeneralController = importGeneralController;
}
}