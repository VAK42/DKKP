package com.example.dkkp.controller.product.productBase;
import com.example.dkkp.controller.product.TableInterface;
import com.example.dkkp.model.Brand_Entity;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.service.BrandService;
import com.example.dkkp.service.CategoryService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductBaseFilterController implements TableInterface {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXFilterComboBox<Brand_Entity> brandComboBox;
@FXML
private MFXFilterComboBox<Category_Entity> categoryCombobox;
@FXML
private MFXComboBox<String> dateOperatorComboBox;
@FXML
private MFXComboBox<String> viewCountOperatorComboBox;
@FXML
private MFXComboBox<String> quantityOperatorComboBox;
@FXML
private MFXTextField idTextField;
@FXML
private MFXTextField nameTextField;
@FXML
private MFXTextField viewCountTextField;
@FXML
private MFXTextField quantityTextField;
@FXML
private MFXDatePicker datePicker;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductBaseController productBaseController;
@FXML
public void createFilter() {
String name = nameTextField.getText().trim().isEmpty() ? null : nameTextField.getText();
Integer id = idTextField.getText().trim().isEmpty() ? null : Integer.parseInt(idTextField.getText());
Integer view = viewCountTextField.getText().trim().isEmpty() ? null : Integer.parseInt(viewCountTextField.getText());
Integer quantity = quantityTextField.getText().trim().isEmpty() ? null : Integer.parseInt(quantityTextField.getText());
Integer brandId = brandComboBox.getValue() != null ? brandComboBox.getValue().getID_BRAND() : null;
Integer categoryId = categoryCombobox.getValue() != null ? categoryCombobox.getValue().getID_CATEGORY() : null;
LocalDate releaseDate = datePicker.getValue() != null ? datePicker.getValue() : null;
LocalTime time = LocalTime.MIDNIGHT;
LocalDateTime date = releaseDate != null ? releaseDate.atTime(time) : null;
if (dateOperatorComboBox != null) {productBaseController.typeDate = getValueOperator(dateOperatorComboBox.getValue());}
if (quantityOperatorComboBox!= null) {productBaseController.typeQuantity = getValueOperator(quantityOperatorComboBox.getValue());}
if (viewCountOperatorComboBox != null) {productBaseController.typeView = getValueOperator(viewCountOperatorComboBox.getValue());}
productBaseController.productBaseEntity = new Product_Base_Entity(id,name,quantity,date,null,view,categoryId,brandId);
productBaseController.setPage(1);
productBaseController.productController.setMainView("/com/example/dkkp/ProductBase/ProductBaseView.fxml",productBaseController);
productBaseController.closePopup(popupStage);
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
productBaseController.closePopup(popupStage);
});
CategoryService categoryService = new CategoryService(entityManager);
BrandService brandService = new BrandService(entityManager);
Brand_Entity brandEntity = new Brand_Entity();
Category_Entity categoryEntity = new Category_Entity();
brandComboBox.getItems().addAll(brandService.getFilteredBrand(brandEntity, null, null, null, null));
categoryCombobox.getItems().addAll(categoryService.getFilteredCategories(categoryEntity, null, null, null, null));
}
private void setTextFormatter(){
Validator validator1 = new Validator();
Validator validator2 = new Validator();
Validator validator3 = new Validator();
idTextField.delegateSetTextFormatter(validator1.formatterInteger);
viewCountTextField.delegateSetTextFormatter(validator2.formatterInteger);
quantityTextField.delegateSetTextFormatter(validator3.formatterInteger);
}
@Override
public void setWidth() {
}
public void setProductBaseController(ProductBaseController productBaseController) {
this.productBaseController = productBaseController;
}
}