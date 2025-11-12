package com.example.dkkp.controller.product.productBrand;
import com.example.dkkp.model.Brand_Entity;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductBrandFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID_BRAND;
@FXML
private MFXTextField NAME_BRAND;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductBrandController productBrandController;
@FXML
public void createFilter() {
Integer id = ID_BRAND.getText().trim().isEmpty() ? null : Integer.parseInt(ID_BRAND.getText());
String name = NAME_BRAND.getText().trim().isEmpty() ? null : NAME_BRAND.getText();
productBrandController.brandEntity = new Brand_Entity(id,name,null);
productBrandController.setPage(1);
productBrandController.productController.setMainView("/com/example/dkkp/Brand/ProductBrandView.fxml",productBrandController);
productBrandController.closePopup(popupStage);
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
productBrandController.closePopup(popupStage);
});
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID_BRAND.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setProductBrandController(ProductBrandController productBrandController) {
this.productBrandController = productBrandController;
}
}