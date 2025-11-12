package com.example.dkkp.controller.product.productCategory;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.service.CategoryService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductCategoryUpdateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private Category_Entity categoryEntity;
private ProductCategoryController productCategoryController;
@FXML
private MFXTextField ID_CATEGORY;
@FXML
private MFXTextField NAME_CATEGORY;
@FXML
private MFXButton updateBtn;
@FXML
private MFXButton backBtn;
private Stage popupStage;
@FXML
public void initialize() {
pushEntity();
updateBtn.setOnAction(event -> updateCategory());
backBtn.setOnMouseClicked(event -> productCategoryController.closePopup(popupStage));
}
private void updateCategory() {
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.setTitle("Confirm Update");
alert.setHeaderText("Are you sure you want to update this item?");
alert.setContentText("This action cannot be undone.");
ButtonType yesButton = new ButtonType("Yes");
ButtonType noButton = new ButtonType("No");
alert.getButtonTypes().setAll(yesButton, noButton);
Optional<ButtonType> result = alert.showAndWait();
if (result.isPresent() && result.get() == yesButton) {
try {
transaction.begin();
categoryEntity.setNAME_CATEGORY(NAME_CATEGORY.getText());
CategoryService categoryService = new CategoryService(entityManager);
categoryService.updateCategory(categoryEntity);
transaction.commit();
productCategoryController.refreshProductTable();
productCategoryController.closePopup(popupStage);
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR);
errorAlert.setTitle("Update Error");
errorAlert.setHeaderText("Failed to update category.");
errorAlert.setContentText("An unexpected error occurred: " + e.getMessage());
errorAlert.showAndWait();
}
}
}
public void pushEntity() {
if (categoryEntity != null) {
ID_CATEGORY.setText(categoryEntity.getID_CATEGORY().toString());
NAME_CATEGORY.setText(categoryEntity.getNAME_CATEGORY());
}
}
public void setProductCategoryController(ProductCategoryController productCategoryController) {
this.productCategoryController = productCategoryController;
}
public void setEntity(Category_Entity category_Entity) {
this.categoryEntity = category_Entity;
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}