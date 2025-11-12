package com.example.dkkp.controller.bill.billDetail;
import com.example.dkkp.model.Bill_Detail_Entity;
import com.example.dkkp.model.Bill_Entity;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.service.BillService;
import com.example.dkkp.service.ProductFinalService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class BillDetailFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID_BILL_DETAIL;
@FXML
private MFXTextField quantityField;
@FXML
private MFXTextField unitPriceField;
@FXML
private MFXTextField totalPriceField;
@FXML
private MFXComboBox<String> quantityCombobox;
@FXML
private MFXComboBox<String> unitPriceCombobox;
@FXML
private MFXComboBox<String> totalPriceCombobox;
@FXML
private MFXComboBox<String> isAvailableCombobox;
@FXML
private MFXFilterComboBox<Product_Final_Entity> finalProductCombobox;
@FXML
private MFXFilterComboBox<Bill_Entity> ID_BILL;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
BillDetailController billDetailController;
@FXML
public void createFilter() {
Integer id = ID_BILL_DETAIL.getText().trim().isEmpty() ? null : Integer.parseInt(ID_BILL_DETAIL.getText());
Boolean isAvailable = null;
if (isAvailableCombobox.getValue() != null) isAvailable = switch (isAvailableCombobox.getValue()) {
case "Both" -> null;
case "Yes" -> true;
case "No" -> false;
case null -> false;
default -> null;
};
Integer finalId = finalProductCombobox.getValue() != null ? finalProductCombobox.getValue().getID_SP() : null;
String idBill = ID_BILL.getValue() != null ? ID_BILL.getValue().getID_BILL() : null;
if (quantityCombobox != null) {billDetailController.typeQuantity = getValueOperator(quantityCombobox.getValue());}
if (unitPriceCombobox != null) {billDetailController.typeUPrice = getValueOperator(unitPriceCombobox.getValue());}
if (totalPriceCombobox != null) {billDetailController.typePPrice = getValueOperator(totalPriceCombobox.getValue());}
Integer quantity = quantityField.getText().trim().isEmpty() ? null : Integer.parseInt(quantityField.getText());
Double uPrice = unitPriceField.getText().trim().isEmpty() ? null : Double.parseDouble(unitPriceField.getText());
Double pPrice = totalPriceField.getText().trim().isEmpty() ? null : Double.parseDouble(totalPriceField.getText());
billDetailController.billDetailEntity = new Bill_Detail_Entity(id,idBill,quantity,pPrice,uPrice,finalId,isAvailable);
billDetailController.setPage(1);
billDetailController.billController.setMainView("/com/example/dkkp/BillDetail/BillDetailView.fxml",billDetailController);
billDetailController.closePopup(popupStage);
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
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
@FXML
public void initialize() throws Exception {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
billDetailController.closePopup(popupStage);
});
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Final_Entity productFinal = new Product_Final_Entity();
finalProductCombobox.getItems().addAll(productFinalService.getProductFinalByCombinedCondition(productFinal,null,null,null,null,null,null,null));
BillService billService = new BillService(entityManager);
Bill_Entity item = new Bill_Entity();
ID_BILL.getItems().addAll(billService.getBillByCombinedCondition(item,null,null,null,null,null,null));
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID_BILL_DETAIL.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setBillDetailController(BillDetailController billDetailController) {
this.billDetailController = billDetailController;
}
}