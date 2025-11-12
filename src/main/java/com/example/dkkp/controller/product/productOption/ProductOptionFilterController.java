package com.example.dkkp.controller.product.productOption;
import com.example.dkkp.model.Product_Option_Entity;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductOptionFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID_OPTION;
@FXML
private MFXTextField NAME_OPTION;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ProductOptionController productOptionController;
@FXML
public void createFilter() {
Integer id = ID_OPTION.getText().trim().isEmpty() ? null : Integer.parseInt(ID_OPTION.getText());
String name = NAME_OPTION.getText().trim().isEmpty() ? null : NAME_OPTION.getText();
productOptionController.productOptionEntity = new Product_Option_Entity(id, name);
productOptionController.setPage(1);
productOptionController.productController.setMainView("/com/example/dkkp/ProductOption/ProductOptionView.fxml",productOptionController);
productOptionController.closePopup(popupStage);
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
@FXML
public void initialize() {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
productOptionController.closePopup(popupStage);
});
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID_OPTION.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setProductOptionController(ProductOptionController productOptionController) {
this.productOptionController = productOptionController;
}
}