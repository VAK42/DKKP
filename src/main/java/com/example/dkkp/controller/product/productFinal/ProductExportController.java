package com.example.dkkp.controller.product.productFinal;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
public class ProductExportController {
private ProductFinalController productFinalController;
@FXML
private MFXTextField fileToEx;
private Stage popupStage;
@FXML
public void initialize() throws Exception {
fileToEx.setOnKeyPressed(event -> {
try {
handleKeyPress(event);
} catch (Exception e) {
throw new RuntimeException(e);
}
});
}
private void handleKeyPress(KeyEvent event) throws Exception {
if (event.getCode() == KeyCode.ENTER) {
if (fileToEx.getText() == null || fileToEx.getText().isEmpty()) {
Alert alert = new Alert(Alert.AlertType.WARNING);
alert.setTitle("Warning");
alert.setHeaderText("Invalid Input");
alert.setContentText("Date of export cannot be empty!");
alert.showAndWait();
} else {
productFinalController.productExportName = fileToEx.getText();
productFinalController.exportToFile();
productFinalController.closePopup(popupStage);
}
}
}
public void setProductFinalController(ProductFinalController productFinalController) {
this.productFinalController = productFinalController;
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}