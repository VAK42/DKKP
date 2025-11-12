package com.example.dkkp.controller.impozt.importGeneral;
import com.example.dkkp.controller.product.TableInterface;
import com.example.dkkp.model.*;
import com.example.dkkp.service.ImportService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.util.List;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ImportGeneralDetailController implements TableInterface {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private Import_Entity importEntity;
private ImportGeneralController importGeneralController;
@FXML
private MFXTableView<Import_Detail_Entity> importDetailTable;
@FXML
private MFXTableColumn<Import_Detail_Entity> ID_IMPD;
@FXML
private MFXTableColumn<Import_Detail_Entity> ID_BASE_PRODUCT;
@FXML
private MFXTableColumn<Import_Detail_Entity> ID_FINAL_PRODUCT;
@FXML
private MFXTableColumn<Import_Detail_Entity> QUANTITY;
@FXML
private MFXTableColumn<Import_Detail_Entity> UNIT_PRICE;
@FXML
private MFXTableColumn<Import_Detail_Entity> TOTAL_PRICED;
@FXML
private MFXTableColumn<Import_Detail_Entity> DESCRIPTIOND;
@FXML
private MFXTextField ID_IMP;
@FXML
private MFXTextField ID_REPLACE;
@FXML
private MFXTextField TOTAL_PRICE;
@FXML
private MFXTextField IS_AVAILABLE;
@FXML
private MFXTextField DESCRIPTION;
@FXML
private MFXTextField DATE_IMP;
@FXML
private MFXButton backBtn;
private Stage popupStage;
private ObservableList<Import_Detail_Entity> list;
@FXML
public void initialize() {
list = getImportDetail();
importDetailTable.setItems(list);
setCol();
setWidth();
pushEntity();
backBtn.setOnMouseClicked(event -> importGeneralController.closePopup(popupStage));
}
public ObservableList<Import_Detail_Entity> getImportDetail() {
ImportService importService = new ImportService(entityManager);
Import_Detail_Entity importDetailEntity = new Import_Detail_Entity(null,importEntity.getID_IMP(),null,null,null,null,null,null,null);
List<Import_Detail_Entity> p = importService.getImportDetailByCombinedCondition(importDetailEntity,null,null,null,null,null,null,null);
for (Import_Detail_Entity i : p) {
System.out.println("dcm " +i.getID_IMPORT());
}
return FXCollections.observableArrayList(p);
}
@Override
public void setWidth() {
ID_IMPD.prefWidthProperty().bind(importDetailTable.widthProperty().multiply(0.13));
ID_BASE_PRODUCT.prefWidthProperty().bind(importDetailTable.widthProperty().multiply(0.13));
ID_FINAL_PRODUCT.prefWidthProperty().bind(importDetailTable.widthProperty().multiply(0.37));
QUANTITY.prefWidthProperty().bind(importDetailTable.widthProperty().multiply(0.37));
UNIT_PRICE.prefWidthProperty().bind(importDetailTable.widthProperty().multiply(0.37));
TOTAL_PRICED.prefWidthProperty().bind(importDetailTable.widthProperty().multiply(0.37));
DESCRIPTIOND.prefWidthProperty().bind(importDetailTable.widthProperty().multiply(0.37));
}
private void setCol() {
ID_IMPD.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getID_IMPD));
ID_BASE_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getID_BASE_PRODUCT));
ID_FINAL_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getID_FINAL_PRODUCT));
QUANTITY.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getQUANTITY));
UNIT_PRICE.setRowCellFactory(_ -> new MFXTableRowCell<>(product -> {
Double price = product.getUNIT_PRICE();
if (price != null) {
DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
return decimalFormat.format(price);
}
return "";
}));
TOTAL_PRICED.setRowCellFactory(_ -> new MFXTableRowCell<>(product -> {
Double price = product.getTOTAL_PRICE();
if (price != null) {
DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
return decimalFormat.format(price);
}
return "";
}));
DESCRIPTIOND.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getDESCRIPTION));
}
public void setImportGeneralController(ImportGeneralController importGeneralController) {
this.importGeneralController = importGeneralController;
}
public void setEntity(Import_Entity import_Entity) {
this.importEntity = import_Entity;
}
public void pushEntity() {
if (importEntity != null) {
ID_IMP.setText(importEntity.getID_IMP());
ID_REPLACE.setText(importEntity.getID_REPLACE());
TOTAL_PRICE.setText(importEntity.getTOTAL_PRICE().toString());
IS_AVAILABLE.setText(importEntity.getIS_AVAILABLE().toString());
DATE_IMP.setText(importEntity.getDATE_IMP().toString());
DESCRIPTION.setText(String.valueOf(importEntity.getDESCRIPTION()));
}
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}