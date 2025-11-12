package com.example.dkkp.controller.bill.billDetail;
import com.example.dkkp.controller.bill.billGeneral.BillGeneralCreateController;
import com.example.dkkp.model.Bill_Detail_Entity;
import com.example.dkkp.model.Product_Final_Entity;
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
public class BillDetailCreateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField QUANTITY_SP;
@FXML
private MFXFilterComboBox<Product_Final_Entity> ID_FINAL_PRODUCT;
@FXML
private MFXButton createBtn;
@FXML
private MFXButton back;
BillGeneralCreateController billGenralCreateController;
private Stage popupStage;
@FXML
public void createProduct() {
Integer quantity = (QUANTITY_SP.getText().isEmpty()) ? null :Integer.parseInt(QUANTITY_SP.getText()) ;
Integer finalProductId = (ID_FINAL_PRODUCT.getValue() != null) ? ID_FINAL_PRODUCT.getValue().getID_SP() : null;
if(ID_FINAL_PRODUCT.getValue() == null){
showAlert("Error", "You must chose Final Product");
return;
}
if(quantity == null){
showAlert("Error", "You must chose Quantity");
return;
}
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Double basePrice = productFinalService.getProductByID(finalProductId).getPRICE_SP();
Double discount = productFinalService.getProductByID(finalProductId).getDISCOUNT();
System.out.println("base price " + basePrice);
Double unitPrice = basePrice *(1 - discount/100) ;
System.out.println("final price " + unitPrice);
Bill_Detail_Entity bill_Detail_Entity = new Bill_Detail_Entity();
bill_Detail_Entity.setID_FINAL_PRODUCT(finalProductId);
bill_Detail_Entity.setQUANTITY_BILL(quantity);
bill_Detail_Entity.setUNIT_PRICE(unitPrice);
bill_Detail_Entity.setTOTAL_DETAIL_PRICE(unitPrice * quantity);
billGenralCreateController.listBillDetail.add(bill_Detail_Entity);
billGenralCreateController.setItem();
billGenralCreateController.closePopup(popupStage);
}
@FXML
public void initialize() {
createBtn.setOnAction(event -> createProduct());
back.setOnMouseClicked(event -> {
billGenralCreateController.closePopup(popupStage);
});
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Final_Entity productFinal = new Product_Final_Entity();
ID_FINAL_PRODUCT.getItems().addAll(productFinalService.getProductFinalByCombinedCondition(productFinal, null, null, null, null, null, null, null));
Validator validator1 = new Validator();
QUANTITY_SP.delegateSetTextFormatter(validator1.formatterInteger);
}
private void showAlert(String title, String message) {
javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
alert.setTitle(title);
alert.setHeaderText(null);
alert.setContentText(message);
alert.showAndWait();
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
public void setBillGeneralCreateController(BillGeneralCreateController billGenralCreateController) {
this.billGenralCreateController = billGenralCreateController;
}
}