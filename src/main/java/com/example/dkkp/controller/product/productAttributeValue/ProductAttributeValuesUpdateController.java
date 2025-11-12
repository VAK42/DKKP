package com.example.dkkp.controller.product.productAttributeValue;
import com.example.dkkp.model.Product_Attribute_Entity;
import com.example.dkkp.model.Product_Attribute_Values_Entity;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.service.ProductBaseService;
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
public class ProductAttributeValuesUpdateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private Product_Attribute_Values_Entity productAttributeValuesEntity;
private ProductAttributeValuesController productAttributeValuesController;
@FXML
private MFXTextField ID;
@FXML
private MFXTextField VALUE;
@FXML
private MFXFilterComboBox<Product_Base_Entity> baseProductField;
@FXML
private MFXFilterComboBox<Product_Attribute_Entity> attributeField;
@FXML
private MFXButton updateBtn;
@FXML
private MFXButton backBtn;
private Stage popupStage;
@FXML
public void initialize() {
pushEntity();
updateBtn.setOnAction(event -> updateCategory());
backBtn.setOnMouseClicked(event -> productAttributeValuesController.closePopup(popupStage));
ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Base_Entity product = new Product_Base_Entity();
Product_Attribute_Entity attribute = new Product_Attribute_Entity();
baseProductField.getItems().addAll(productBaseService.getProductBaseByCombinedCondition(product, null, null, null, null, null, null, null));
attributeField.getItems().addAll(productBaseService.getProductAttributeCombinedCondition(attribute, null, null, null, null));
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
if (baseProductField.getSelectionModel().getSelectedItem() != null) {
productAttributeValuesEntity.setID_BASE_PRODUCT(baseProductField.getSelectionModel().getSelectedItem().getID_BASE_PRODUCT());
}
if (attributeField.getSelectionModel().getSelectedItem() != null) {
productAttributeValuesEntity.setID_ATTRIBUTE(attributeField.getSelectionModel().getSelectedItem().getID_ATTRIBUTE());
}
productAttributeValuesEntity.setVALUE(VALUE.getText());
ProductBaseService productBaseService = new ProductBaseService(entityManager);
productBaseService.updateProductAttributeValues(productAttributeValuesEntity);
transaction.commit();
productAttributeValuesController.setMainView("/com/example/dkkp/ProductAttributeValue/ProductAttributeValueView.fxml", productAttributeValuesController);
productAttributeValuesController.closePopup(popupStage);
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR);
errorAlert.setTitle("Update Error");
errorAlert.setHeaderText("Transaction Failed");
errorAlert.setContentText("Failed to update attribute value: " + e.getMessage());
errorAlert.showAndWait();
}
}
}
public void pushEntity() {
if (productAttributeValuesEntity != null) {
ID.setText(productAttributeValuesEntity.getID().toString());
VALUE.setText(productAttributeValuesEntity.getVALUE());
ProductBaseService productBaseService = new ProductBaseService(entityManager);
// Find and set default Base Product
Product_Base_Entity productQuery = new Product_Base_Entity();
productQuery.setID_BASE_PRODUCT(productAttributeValuesEntity.getID_BASE_PRODUCT());
List<Product_Base_Entity> baseProducts = productBaseService.getProductBaseByCombinedCondition(productQuery, null, null, null, null, null, null, null);
if (!baseProducts.isEmpty()) {
Product_Base_Entity defaultBaseProduct = baseProducts.getFirst();
baseProductField.setText(defaultBaseProduct.toString());
}
// Find and set default Attribute
Product_Attribute_Entity attributeQuery = new Product_Attribute_Entity();
attributeQuery.setID_ATTRIBUTE(productAttributeValuesEntity.getID_ATTRIBUTE());
List<Product_Attribute_Entity> attributes = productBaseService.getProductAttributeCombinedCondition(attributeQuery, null, null, null, null);
if (!attributes.isEmpty()) {
Product_Attribute_Entity defaultAttribute = attributes.getFirst();
attributeField.setText(defaultAttribute.toString());
}
}
}
public void setProductAttributeValuesController(ProductAttributeValuesController productAttributeValuesController) {
this.productAttributeValuesController = productAttributeValuesController;
}
public void setEntity(Product_Attribute_Values_Entity productAttributeValuesEntity) {
this.productAttributeValuesEntity = productAttributeValuesEntity;
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}