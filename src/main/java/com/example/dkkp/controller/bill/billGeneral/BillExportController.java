package com.example.dkkp.controller.bill.billGeneral;
import io.github.palexdev.materialfx.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
public class BillExportController {
  private BillGeneralController billGeneralController;
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
        billGeneralController.billExportName = fileToEx.getText();
        billGeneralController.exportToFile();
        billGeneralController.closePopup(popupStage);
      }
    }
  }
  public void setBillGeneralController(BillGeneralController billGeneralController) {
    this.billGeneralController = billGeneralController;
  }
  public void setPopupStage(Stage popupStage) {
    this.popupStage = popupStage;
  }
}