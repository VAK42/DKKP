package com.example.dkkp.controller.product.productAttribute;
import com.example.dkkp.controller.product.ProductController;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.model.Product_Attribute_Entity;
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
public class ProductAttributeCreateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField NAME_ATTRIBUTE;
@FXML
private MFXFilterComboBox<Category_Entity> cateField;
@FXML
private MFXButton createBtn;
@FXML
private MFXButton back;
ProductAttributeController productAttributeController;
@FXML
public void createProduct() {
String name = (NAME_ATTRIBUTE.getText().isEmpty()) ? null : NAME_ATTRIBUTE.getText();
Integer categoryId = (cateField.getValue() != null) ? cateField.getValue().getID_CATEGORY() : null;
if (name == null || categoryId == null) {
Alert alert = new Alert(Alert.AlertType.WARNING, "Please do not leave the Name or Category fields empty.", ButtonType.OK);
alert.setTitle("Input Warning");
alert.setHeaderText("Invalid Input");
alert.showAndWait();
return;
}
transaction.begin();
try {
Product_Attribute_Entity productAttributeEntity = new Product_Attribute_Entity(null, name,categoryId);
ProductBaseService productBaseService = new ProductBaseService(entityManager);
productBaseService.createProductAttribute(productAttributeEntity);
transaction.commit();
productAttributeController.productController.setMainView("/com/example/dkkp/ProductAttribute/ProductAttributeView.fxml", productAttributeController);
} catch (Exception e) {
transaction.rollback();
}
}
@FXML
public void initialize() {
createBtn.setOnAction(event -> createProduct());
back.setOnMouseClicked(event -> {
productAttributeController.productController.setMainView("/com/example/dkkp/ProductAttribute/ProductAttributeView.fxml", productAttributeController);
});
CategoryService categoryService = new CategoryService(entityManager);
Category_Entity categoryEntity = new Category_Entity();
cateField.getItems().addAll(categoryService.getFilteredCategories(categoryEntity, null, null, null, null));
}
public void setProductAttributeController(ProductAttributeController productAttributeController) {
this.productAttributeController = productAttributeController;
}
}