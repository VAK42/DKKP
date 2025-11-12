package com.example.dkkp.controller.product.productCategory;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductCategoryFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID_CATEGORY;
@FXML
private MFXTextField NAME_CATEGORY;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductCategoryController productCategoryController;
@FXML
public void createFilter() {
Integer id = ID_CATEGORY.getText().trim().isEmpty() ? null : Integer.parseInt(ID_CATEGORY.getText());
String name = NAME_CATEGORY.getText().trim().isEmpty() ? null : NAME_CATEGORY.getText();
productCategoryController.categoryEntity = new Category_Entity(id,name);
productCategoryController.setPage(1);
productCategoryController.productController.setMainView("/com/example/dkkp/Category/ProductCategoryView.fxml",productCategoryController);
productCategoryController.closePopup(popupStage);
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
@FXML
public void initialize() {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
productCategoryController.closePopup(popupStage);
});
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID_CATEGORY.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setProductCategoryController(ProductCategoryController productCategoryController) {
this.productCategoryController = productCategoryController;
}
}