package com.example.dkkp.controller.impozt.importGeneral;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
public class ImportExportController {
private ImportGeneralController importGeneralController;
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
importGeneralController.importExportName = fileToEx.getText();
importGeneralController.exportToFile();
importGeneralController.closePopup(popupStage);
}
}
}
public void setImportGeneralController(ImportGeneralController importGeneralController) {
this.importGeneralController = importGeneralController;
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}