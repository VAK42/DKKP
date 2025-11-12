package com.example.dkkp.controller.impozt.importDetail;
import com.example.dkkp.model.*;
import com.example.dkkp.service.ImportService;
import com.example.dkkp.service.ProductBaseService;
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
public class ImportDetailFilterController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTextField ID_IMPD;
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
private MFXFilterComboBox<Product_Base_Entity> baseProductCombobox;
@FXML
private MFXFilterComboBox<Product_Final_Entity> finalProductCombobox;
@FXML
private MFXFilterComboBox<Import_Entity> ID_IMPORT;
@FXML
private MFXButton back;
@FXML
private MFXButton applyButton;
private Stage popupStage;
ImportDetailController importDetailController;
@FXML
public void createFilter() {
Integer id = ID_IMPD.getText().trim().isEmpty() ? null : Integer.parseInt(ID_IMPD.getText());
Boolean isAvailable = null;
if (isAvailableCombobox.getValue() != null) isAvailable = switch (isAvailableCombobox.getValue()) {
case "Both" -> null;
case "Yes" -> true;
case "No" -> false;
case null -> false;
default -> null;
};
Integer baseId = baseProductCombobox.getValue() != null ? baseProductCombobox.getValue().getID_BASE_PRODUCT() : null;
Integer finalId = finalProductCombobox.getValue() != null ? finalProductCombobox.getValue().getID_SP() : null;
String idIMP = ID_IMPORT.getValue() != null ? ID_IMPORT.getValue().getID_IMP() : null;
if (quantityCombobox != null) {importDetailController.typeQuantity = getValueOperator(quantityCombobox.getValue());}
if (unitPriceCombobox != null) {importDetailController.typeUPrice = getValueOperator(unitPriceCombobox.getValue());}
if (totalPriceCombobox != null) {importDetailController.typePPrice = getValueOperator(totalPriceCombobox.getValue());}
Integer quantity = quantityField.getText().trim().isEmpty() ? null : Integer.parseInt(quantityField.getText());
Double uPrice = unitPriceField.getText().trim().isEmpty() ? null : Double.parseDouble(unitPriceField.getText());
Double pPrice = totalPriceField.getText().trim().isEmpty() ? null : Double.parseDouble(totalPriceField.getText());
importDetailController.importDetailEntity = new Import_Detail_Entity(id,idIMP,isAvailable,baseId,finalId,quantity,uPrice,pPrice,null);
importDetailController.setPage(1);
importDetailController.importController.setMainView("/com/example/dkkp/ImportDetail/ImportDetailView.fxml",importDetailController);
importDetailController.closePopup(popupStage);
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
public void initialize() {
setTextFormatter();
applyButton.setOnAction(event -> createFilter());
back.setOnMouseClicked(event -> {
importDetailController.closePopup(popupStage);
});
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Final_Entity productFinal = new Product_Final_Entity();
finalProductCombobox.getItems().addAll(productFinalService.getProductFinalByCombinedCondition(productFinal,null,null,null,null,null,null,null));
ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Base_Entity productBase = new Product_Base_Entity();
baseProductCombobox.getItems().addAll(productBaseService.getProductBaseByCombinedCondition(productBase,null,null,null,null,null,null,null));
ImportService importService = new ImportService(entityManager);
Import_Entity item = new Import_Entity();
ID_IMPORT.getItems().addAll(importService.getImportByCombinedCondition(item,null,null,null,null,null,null));
}
private void setTextFormatter(){
Validator validator1 = new Validator();
ID_IMPD.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setImportDetailController(ImportDetailController importDetailController) {
this.importDetailController = importDetailController;
}
}