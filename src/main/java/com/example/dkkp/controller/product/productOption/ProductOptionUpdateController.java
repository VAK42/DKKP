package com.example.dkkp.controller.product.productOption;
import com.example.dkkp.model.Product_Option_Entity;
import com.example.dkkp.service.ProductFinalService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductOptionUpdateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private Product_Option_Entity productOptionEntity;
private ProductOptionController productOptionController;
@FXML
private MFXTextField ID_OPTION;
@FXML
private MFXTextField NAME_OPTION;
@FXML
private MFXButton updateBtn;
@FXML
private MFXButton backBtn;
private Stage popupStage;
@FXML
public void initialize() {
pushEntity();
updateBtn.setOnAction(event -> updateCategory());
backBtn.setOnMouseClicked(event -> productOptionController.closePopup(popupStage));
}
private void updateCategory() {
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
productOptionEntity.setNAME_OPTION(NAME_OPTION.getText());
ProductFinalService productFinalService = new ProductFinalService(entityManager);
productFinalService.updateProductOption(productOptionEntity);
transaction.commit();
productOptionController.refreshProductTable();
productOptionController.closePopup(popupStage);
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to update product option: " + e.getMessage(), ButtonType.OK);
errorAlert.setTitle("Update Error");
errorAlert.setHeaderText("Transaction Failed");
errorAlert.showAndWait();
}
}
}
public void pushEntity() {
if (productOptionEntity != null) {
ID_OPTION.setText(productOptionEntity.getID_OPTION().toString());
NAME_OPTION.setText(productOptionEntity.getNAME_OPTION());
}
}
public void setProductOptionController(ProductOptionController productOptionController) {
this.productOptionController = productOptionController;
}
public void setEntity(Product_Option_Entity productOptionEntity) {
this.productOptionEntity = productOptionEntity;
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}