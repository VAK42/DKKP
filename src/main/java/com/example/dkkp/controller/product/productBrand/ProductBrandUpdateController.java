package com.example.dkkp.controller.product.productBrand;
import com.example.dkkp.model.Brand_Entity;
import com.example.dkkp.service.BrandService;
import io.github.palexdev.materialfx.controls.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductBrandUpdateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private Brand_Entity brandEntity;
private ProductBrandController productBrandController;
@FXML
private MFXTextField ID_BRAND;
@FXML
private MFXTextField NAME_BRAND;
@FXML
private MFXTextField DETAIL;
@FXML
private MFXButton updateBtn;
@FXML
private MFXButton backBtn;
private Stage popupStage;
@FXML
public void initialize() {
pushEntity();
updateBtn.setOnAction(event -> updateBrand());
backBtn.setOnMouseClicked(event -> productBrandController.closePopup(popupStage));
}
private void updateBrand() {
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.setTitle("Confirm Update");
alert.setHeaderText("Are you sure you want to update this item?");
alert.setContentText("This action cannot be undone.");
ButtonType yesButton = new ButtonType("Yes");
ButtonType noButton = new ButtonType("No");
alert.getButtonTypes().setAll(yesButton, noButton);
Optional<ButtonType> result = alert.showAndWait();
if (result.isPresent() && result.get() == yesButton) {
try {
transaction.begin();
brandEntity.setNAME_BRAND(NAME_BRAND.getText());
brandEntity.setDETAIL(DETAIL.getText());
BrandService brandService = new BrandService(entityManager);
brandService.updateBrand(brandEntity);
transaction.commit();
productBrandController.refreshProductTable();
productBrandController.closePopup(popupStage);
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR);
errorAlert.setTitle("Update Error");
errorAlert.setHeaderText("Failed to update brand.");
errorAlert.setContentText("An unexpected error occurred: " + e.getMessage());
errorAlert.showAndWait();
}
}
}
public void setProductBrandController(ProductBrandController productBrandController) {
this.productBrandController = productBrandController;
}
public void pushEntity() {
if (brandEntity != null) {
ID_BRAND.setText(brandEntity.getID_BRAND().toString());
NAME_BRAND.setText(brandEntity.getNAME_BRAND());
DETAIL.setText(brandEntity.getDETAIL());
}
}
public void setEntity(Brand_Entity brand_Entity) {
this.brandEntity = brand_Entity;
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}