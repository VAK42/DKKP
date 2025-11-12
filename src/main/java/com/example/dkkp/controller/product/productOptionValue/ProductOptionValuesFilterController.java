package com.example.dkkp.controller.product.productOptionValue;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.model.Product_Option_Entity;
import com.example.dkkp.model.Product_Option_Values_Entity;
import com.example.dkkp.service.ProductFinalService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductOptionValuesFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID;
@FXML
private MFXTextField VALUE;
@FXML
private MFXFilterComboBox<Product_Option_Entity> optionField;
@FXML
private MFXFilterComboBox<Product_Final_Entity> finalProductField;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductOptionValuesController productOptionValuesController;
@FXML
public void createFilter() {
Integer id = ID.getText().trim().isEmpty() ? null : Integer.parseInt(ID.getText());
String value = VALUE.getText().trim().isEmpty() ? null : VALUE.getText();
Integer optionId = (optionField.getValue() != null) ? optionField.getValue().getID_OPTION() : null;
Integer finalId = (finalProductField.getValue() != null) ? finalProductField.getValue().getID_SP() : null;
productOptionValuesController.productOptionValuesEntity = new Product_Option_Values_Entity(id, optionId ,value,finalId);
productOptionValuesController.setPage(1);
productOptionValuesController.productController.setMainView("/com/example/dkkp/ProductOptionValue/ProductOptionValueView.fxml",productOptionValuesController);
productOptionValuesController.closePopup(popupStage);
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
@FXML
public void initialize() {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
productOptionValuesController.closePopup(popupStage);
});
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Final_Entity product = new Product_Final_Entity();
finalProductField.getItems().addAll(productFinalService.getProductFinalByCombinedCondition(product,null,null,null,null,null,null,null));
Product_Option_Entity option = new Product_Option_Entity();
optionField.getItems().addAll(productFinalService.getProductOptionCombinedCondition(option,null,null,null,null));
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setProductOptionValuesController(ProductOptionValuesController productOptionValuesController) {
this.productOptionValuesController = productOptionValuesController;
}
}