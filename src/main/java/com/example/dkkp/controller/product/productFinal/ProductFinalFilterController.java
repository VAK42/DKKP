package com.example.dkkp.controller.product.productFinal;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.service.ProductBaseService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductFinalFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXFilterComboBox<Product_Base_Entity> productBase;
@FXML
private MFXComboBox<String> priceField;
@FXML
private MFXComboBox<String> discountField;
@FXML
private MFXComboBox<String> quantityField;
@FXML
private MFXTextField idTextField;
@FXML
private MFXTextField nameTextField;
@FXML
private MFXTextField priceTextField;
@FXML
private MFXTextField discountTextField;
@FXML
private MFXTextField quantityTextField;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductFinalController productFinalController;
@FXML
public void createFilter() {
Integer id = idTextField.getText().trim().isEmpty() ? null : Integer.parseInt(idTextField.getText());
Integer productBaseID = productBase.getValue() != null ? productBase.getValue().getID_BASE_PRODUCT() : null;
String name = nameTextField.getText().trim().isEmpty() ? null : nameTextField.getText();
Double price = priceTextField.getText().trim().isEmpty() ? null : Double.parseDouble(priceTextField.getText());
Integer quantity = quantityTextField.getText().trim().isEmpty() ? null : Integer.parseInt(quantityTextField.getText());
Double discount = discountTextField.getText().trim().isEmpty() ? null : Double.parseDouble(discountTextField.getText());
if (discountField != null) {productFinalController.typeDiscount = getValueOperator(discountField.getValue());}
if (quantityField!= null) {productFinalController.typeQuantity = getValueOperator(quantityField.getValue());}
if (priceField != null) {productFinalController.typePrice = getValueOperator(priceField.getValue());}
productFinalController.productFinalEntity = new Product_Final_Entity(id,productBaseID,name,quantity,price,discount,null,null,null);
productFinalController.setPage(1);
productFinalController.productController.setMainView("/com/example/dkkp/ProductFinal/ProductFinalView.fxml",productFinalController);
productFinalController.closePopup(popupStage);
}
private String getValueOperator(String value) {
if(value == null) return null;
System.out.println(value + " day la");
return switch (value) {
case "Equal" -> "=";
case "More" -> ">";
case "Less" -> "<";
case "Equal or More" -> "=>";
case "Equal or Less" -> "<=";
default -> null;
};
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
@FXML
public void initialize() {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
productFinalController.closePopup(popupStage);
});
ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Base_Entity productBaseEntity = new Product_Base_Entity();
productBase.getItems().addAll(
productBaseService.getProductBaseByCombinedCondition(productBaseEntity,null,null,null,null,null,null,null)
);
}
private void setTextFormatter(){
Validator validator1 = new Validator();
Validator validator2 = new Validator();
Validator validator3 = new Validator();
Validator validator4 = new Validator();
idTextField.delegateSetTextFormatter(validator1.formatterInteger);
quantityTextField.delegateSetTextFormatter(validator2.formatterInteger);
discountTextField.delegateSetTextFormatter(validator3.formatterDouble);
priceTextField.delegateSetTextFormatter(validator4.formatterDouble);
}
public void setProductFinalController(ProductFinalController productFinalController) {
this.productFinalController = productFinalController;
}
}