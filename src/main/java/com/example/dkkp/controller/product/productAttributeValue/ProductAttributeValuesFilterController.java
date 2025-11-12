package com.example.dkkp.controller.product.productAttributeValue;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.model.Product_Attribute_Entity;
import com.example.dkkp.model.Product_Attribute_Values_Entity;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.service.CategoryService;
import com.example.dkkp.service.ProductBaseService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductAttributeValuesFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID;
@FXML
private MFXTextField VALUE;
@FXML
private MFXFilterComboBox<Product_Base_Entity> baseProductField;
@FXML
private MFXFilterComboBox<Product_Attribute_Entity> attributeField;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductAttributeValuesController productAttributeValuesController;
@FXML
public void createFilter() {
Integer id = ID.getText().trim().isEmpty() ? null : Integer.parseInt(ID.getText());
String value = VALUE.getText().trim().isEmpty() ? null : VALUE.getText();
Integer baseProductId = (baseProductField.getValue() != null) ? baseProductField.getValue().getID_BASE_PRODUCT() : null;
Integer attributeId = (attributeField.getValue() != null) ? attributeField.getValue().getID_ATTRIBUTE() : null;
productAttributeValuesController.productAttributeValuesEntity = new Product_Attribute_Values_Entity(id,baseProductId,attributeId, value);
productAttributeValuesController.setPage(1);
productAttributeValuesController.productController.setMainView("/com/example/dkkp/ProductAttributeValue/ProductAttributeValueView.fxml", productAttributeValuesController);
productAttributeValuesController.closePopup(popupStage);
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
@FXML
public void initialize() {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
productAttributeValuesController.closePopup(popupStage);
});
ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Base_Entity product = new Product_Base_Entity();
baseProductField.getItems().addAll(productBaseService.getProductBaseByCombinedCondition(product,null,null,null,null,null,null,null));
Product_Attribute_Entity attribute = new Product_Attribute_Entity();
attributeField.getItems().addAll(productBaseService.getProductAttributeCombinedCondition(attribute,null,null,null,null));
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setProductAttributeValuesController(ProductAttributeValuesController productAttributeValuesController) {
this.productAttributeValuesController = productAttributeValuesController;
}
}