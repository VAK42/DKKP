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
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductOptionValuesCreateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField VALUE;
@FXML
private MFXFilterComboBox<Product_Option_Entity> optionField;
@FXML
private MFXFilterComboBox<Product_Final_Entity> finalProductField;
@FXML
private MFXButton createBtn;
@FXML
private MFXButton back;
ProductOptionValuesController productOptionValuesController;
@FXML
public void createProduct() {
String value = (VALUE.getText().isEmpty()) ? null : VALUE.getText();
Integer optionId = (optionField.getValue() != null) ? optionField.getValue().getID_OPTION() : null;
Integer finalId = (finalProductField.getValue() != null) ? finalProductField.getValue().getID_SP() : null;
if (value == null || optionId == null || finalId == null) {
Alert alert = new Alert(Alert.AlertType.WARNING, "Please do not leave the **Value**, **Option**, or **Final Product** fields empty.", ButtonType.OK);
alert.setTitle("Input Warning");
alert.setHeaderText("Invalid Input");
alert.showAndWait();
return;
}
transaction.begin();
try {
Product_Option_Values_Entity productOptionValuesEntity = new Product_Option_Values_Entity(null, optionId,value,finalId);
ProductFinalService productFinalService = new ProductFinalService(entityManager);
productFinalService.createProductOptionValues(productOptionValuesEntity);
transaction.commit();
productOptionValuesController.productController.setMainView("/com/example/dkkp/ProductOptionValue/ProductOptionValueView.fxml", productOptionValuesController);
} catch (Exception e) {
transaction.rollback();
Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create Option Value: " + e.getMessage(), ButtonType.OK);
alert.setTitle("Database Error");
alert.setHeaderText("Transaction Failed");
alert.showAndWait();
}
}
@FXML
public void initialize() {
createBtn.setOnAction(event -> createProduct());
back.setOnMouseClicked(event -> {
productOptionValuesController.productController.setMainView("/com/example/dkkp/ProductOptionValue/ProductOptionValueView.fxml", productOptionValuesController);
});
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Final_Entity product = new Product_Final_Entity();
Product_Option_Entity option = new Product_Option_Entity();
finalProductField.getItems().addAll(productFinalService.getProductFinalByCombinedCondition(product,null,null,null,null,null,null,null));
optionField.getItems().addAll(productFinalService.getProductOptionCombinedCondition(option,null,null,null,null));
}
public void setProductOptionValuesController(ProductOptionValuesController productOptionValuesController) {
this.productOptionValuesController = productOptionValuesController;
}
}