package com.example.dkkp.controller.product.productBrand;
import com.example.dkkp.model.Brand_Entity;
import com.example.dkkp.service.BrandService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductBrandCreateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField NAME_BRAND;
@FXML
private MFXTextField DETAIL;
@FXML
private MFXButton createBtn;
@FXML
private MFXButton back;
ProductBrandController productBrandController;
@FXML
public void createProduct() {
String name = (NAME_BRAND.getText().isEmpty()) ? null : NAME_BRAND.getText();
String des = (DETAIL.getText().isEmpty()) ? null : DETAIL.getText();
if (name == null ) {
Alert alert = new Alert(Alert.AlertType.WARNING, "Please do not leave the Name field empty.", ButtonType.OK);
alert.setTitle("Input Warning");
alert.setHeaderText("Invalid Input");
alert.showAndWait();
return;
}
transaction.begin();
try {
Brand_Entity brandEntity = new Brand_Entity(name, des);
BrandService brandService = new BrandService(entityManager);
brandService.createNewBrand(brandEntity);
transaction.commit();
productBrandController.productController.setMainView("/com/example/dkkp/Brand/ProductBrandView.fxml", productBrandController);
} catch (Exception e) {
transaction.rollback();
}
}
@FXML
public void initialize() {
createBtn.setOnAction(event -> createProduct());
back.setOnMouseClicked(event -> {
productBrandController.productController.setMainView("/com/example/dkkp/Brand/ProductBrandView.fxml", productBrandController);
});
}
public void setProductBrandController(ProductBrandController productBrandController) {
this.productBrandController = productBrandController;
}
}