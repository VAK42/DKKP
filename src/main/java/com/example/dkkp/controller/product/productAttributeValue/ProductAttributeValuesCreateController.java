package com.example.dkkp.controller.product.productAttributeValue;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.model.Product_Attribute_Entity;
import com.example.dkkp.model.Product_Attribute_Values_Entity;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.service.CategoryService;
import com.example.dkkp.service.ProductBaseService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductAttributeValuesCreateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField VALUE;
@FXML
private MFXFilterComboBox<Product_Attribute_Entity> attributeField;
@FXML
private MFXFilterComboBox<Product_Base_Entity> baseProductField;
@FXML
private MFXButton createBtn;
@FXML
private MFXButton back;
ProductAttributeValuesController productAttributeValuesController;
@FXML
public void createProduct() {
String value = (VALUE.getText().isEmpty()) ? null : VALUE.getText();
Integer attributeId = (attributeField.getValue() != null) ? attributeField.getValue().getID_ATTRIBUTE() : null;
Integer baseProductId = (baseProductField.getValue() != null) ? baseProductField.getValue().getID_BASE_PRODUCT() : null;
if (value == null || attributeId == null || baseProductId == null) {
Alert alert = new Alert(Alert.AlertType.WARNING, "Please do not leave the Value, Attribute, or Base Product fields empty.", ButtonType.OK);
alert.setTitle("Input Warning");
alert.setHeaderText("Invalid Input");
alert.showAndWait();
return;
}
transaction.begin();
try {
Product_Attribute_Values_Entity productAttributeValuesEntity = new Product_Attribute_Values_Entity(null, baseProductId,attributeId, value);
ProductBaseService productBaseService = new ProductBaseService(entityManager);
productBaseService.createProductAttributeValues(productAttributeValuesEntity);
transaction.commit();
productAttributeValuesController.productController.setMainView("/com/example/dkkp/ProductAttributeValue/ProductAttributeValueView.fxml", productAttributeValuesController);
} catch (Exception e) {
transaction.rollback();
Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create Attribute Value: " + e.getMessage(), ButtonType.OK);
alert.setTitle("Database Error");
alert.setHeaderText("Transaction Failed");
alert.showAndWait();
}
}
@FXML
public void initialize() {
createBtn.setOnAction(event -> createProduct());
back.setOnMouseClicked(event -> {
productAttributeValuesController.productController.setMainView("/com/example/dkkp/ProductAttributeValue/ProductAttributeValueView.fxml", productAttributeValuesController);
});
ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Base_Entity product = new Product_Base_Entity();
Product_Attribute_Entity attribute = new Product_Attribute_Entity();
baseProductField.getItems().addAll(productBaseService.getProductBaseByCombinedCondition(product,null,null,null,null,null,null,null));
attributeField.getItems().addAll(productBaseService.getProductAttributeCombinedCondition(attribute,null,null,null,null));
}
public void setProductAttributeValuesController(ProductAttributeValuesController productAttributeValuesController) {
this.productAttributeValuesController = productAttributeValuesController;
}
}