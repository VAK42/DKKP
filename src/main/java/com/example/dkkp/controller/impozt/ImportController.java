package com.example.dkkp.controller.impozt;
import com.example.dkkp.controller.impozt.importDetail.ImportDetailController;
import com.example.dkkp.controller.impozt.importGeneral.ImportGeneralController;
import com.example.dkkp.controller.product.ProductController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
public class ImportController {
@FXML
public StackPane main;
@FXML
private MFXButton importGeneral;
@FXML
private MFXButton importDetail;
private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
public ImportGeneralController importGeneralController = new ImportGeneralController();
public ImportDetailController importDetailController = new ImportDetailController();
@FXML
public void initialize() {
importGeneralController.setImportController(this);
getButton();
setActiveTab(importGeneral);
setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralView.fxml",importGeneralController);
}
private void getButton(){
importGeneral.setOnAction(event -> {
importGeneralController.setImportController(this);
setActiveTab(importGeneral);
setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralView.fxml",importGeneralController);
});
importDetail.setOnMouseClicked(event -> {
importDetailController.setImportController(this);
setActiveTab(importDetail);
setMainView("/com/example/dkkp/ImportDetail/ImportDetailView.fxml",importDetailController);
});
}
public void setMainView(String fxmlPath, Object controller) {
try {
FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
loader.setController(controller);
main.getChildren().clear();
main.getChildren().add(loader.load());
} catch (IOException e) {
System.out.printf("Loi " +e.getMessage());
}
}
private void setActiveTab(Button activeTab) {
importDetail.getStyleClass().remove("activeProductBtn");
importGeneral.getStyleClass().remove("activeProductBtn");
activeTab.getStyleClass().add("activeProductBtn");
}
}