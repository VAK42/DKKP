package com.example.dkkp.controller.product.productOptionValue;
import com.example.dkkp.model.Product_Option_Entity;
import com.example.dkkp.model.Product_Option_Values_Entity;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.service.ProductFinalService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductOptionValuesUpdateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private Product_Option_Values_Entity productOptionValuesEntity;
private ProductOptionValuesController productOptionValuesController;
@FXML
private MFXTextField ID;
@FXML
private MFXTextField VALUE;
@FXML
private MFXFilterComboBox<Product_Option_Entity> optionField;
@FXML
private MFXFilterComboBox<Product_Final_Entity> finalField;
@FXML
private MFXButton updateBtn;
@FXML
private MFXButton backBtn;
private Stage popupStage;
@FXML
public void initialize() {
pushEntity();
updateBtn.setOnAction(event -> updateCategory());
backBtn.setOnMouseClicked(event -> productOptionValuesController.closePopup(popupStage));
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Final_Entity product = new Product_Final_Entity();
Product_Option_Entity option = new Product_Option_Entity();
finalField.getItems().addAll(productFinalService.getProductFinalByCombinedCondition(product, null, null, null, null, null, null, null));
optionField.getItems().addAll(productFinalService.getProductOptionCombinedCondition(option, null, null, null, null));
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
ProductFinalService productFinalService = new ProductFinalService(entityManager);
if (finalField.getSelectionModel().getSelectedItem() != null) {
productOptionValuesEntity.setID_FINAL_PRODUCT(finalField.getSelectionModel().getSelectedItem().getID_SP());
}
if (optionField.getSelectionModel().getSelectedItem() != null) {
productOptionValuesEntity.setID_OPTION(optionField.getSelectionModel().getSelectedItem().getID_OPTION());
}
if (VALUE != null) productOptionValuesEntity.setVALUE(VALUE.getText());
productFinalService.updateProductOptionValues(productOptionValuesEntity);
transaction.commit();
productOptionValuesController.refreshProductTable();
productOptionValuesController.closePopup(popupStage);
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to update option value: " + e.getMessage(), ButtonType.OK);
errorAlert.setTitle("Update Error");
errorAlert.setHeaderText("Transaction Failed");
errorAlert.showAndWait();
}
}
}
public void pushEntity() {
if (productOptionValuesEntity != null) {
ID.setText(productOptionValuesEntity.getID().toString());
VALUE.setText(productOptionValuesEntity.getVALUE());
ProductFinalService productFinalService = new ProductFinalService(entityManager);
// Find and set default final product entity in the dropdown
Product_Final_Entity productQuery = new Product_Final_Entity();
productQuery.setID_SP(productOptionValuesEntity.getID_FINAL_PRODUCT());
List<Product_Final_Entity> finalProducts = productFinalService.getProductFinalByCombinedCondition(productQuery, null, null, null, null, null, null, null);
if (!finalProducts.isEmpty()) {
finalField.setText(finalProducts.getFirst().toString());
}
// Find and set default option entity in the dropdown
Product_Option_Entity optionQuery = new Product_Option_Entity();
optionQuery.setID_OPTION(productOptionValuesEntity.getID_OPTION());
List<Product_Option_Entity> options = productFinalService.getProductOptionCombinedCondition(optionQuery, null, null, null, null);
if (!options.isEmpty()) {
optionField.setText(options.getFirst().toString());
}
}
}
public void setProductOptionValuesController(ProductOptionValuesController productOptionValuesController) {
this.productOptionValuesController = productOptionValuesController;
}
public void setEntity(Product_Option_Values_Entity productOptionValuesEntity) {
this.productOptionValuesEntity = productOptionValuesEntity;
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}