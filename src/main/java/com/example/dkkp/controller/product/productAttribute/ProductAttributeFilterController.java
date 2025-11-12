package com.example.dkkp.controller.product.productAttribute;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.model.Product_Attribute_Entity;
import com.example.dkkp.service.CategoryService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductAttributeFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID_ATTRIBUTE;
@FXML
private MFXTextField NAME_ATTRIBUTE;
@FXML
private MFXFilterComboBox<Category_Entity> cateField;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductAttributeController productAttributeController;
@FXML
public void createFilter() {
Integer id = ID_ATTRIBUTE.getText().trim().isEmpty() ? null : Integer.parseInt(ID_ATTRIBUTE.getText());
String name = NAME_ATTRIBUTE.getText().trim().isEmpty() ? null : NAME_ATTRIBUTE.getText();
Integer categoryId = (cateField.getValue() != null) ? cateField.getValue().getID_CATEGORY() : null;
productAttributeController.productAttributeEntity = new Product_Attribute_Entity(id, name, categoryId);
productAttributeController.setPage(1);
productAttributeController.productController.setMainView("/com/example/dkkp/ProductAttribute/ProductAttributeView.fxml",productAttributeController);
productAttributeController.closePopup(popupStage);
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
@FXML
public void initialize() {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
productAttributeController.closePopup(popupStage);
});
CategoryService categoryService = new CategoryService(entityManager);
Category_Entity categoryEntity = new Category_Entity();
cateField.getItems().addAll(categoryService.getFilteredCategories(categoryEntity, null, null, null, null));
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID_ATTRIBUTE.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setProductAttributeController(ProductAttributeController productAttributeController) {
this.productAttributeController = productAttributeController;
}
}