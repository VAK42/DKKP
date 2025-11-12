package com.example.dkkp.controller.impozt.importGeneral;
import com.example.dkkp.controller.impozt.ImportController;
import com.example.dkkp.controller.impozt.importDetail.ImportDetailCreateController;
import com.example.dkkp.model.Import_Detail_Entity;
import com.example.dkkp.model.Import_Entity;
import com.example.dkkp.service.ImportService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ImportGeneralCreateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTableView<Import_Detail_Entity> importDetailTable;
@FXML
private MFXTableColumn<Import_Detail_Entity> ID_BASE_PRODUCT;
@FXML
private MFXTableColumn<Import_Detail_Entity> ID_FINAL_PRODUCT;
@FXML
private MFXTableColumn<Import_Detail_Entity> QUANTITY;
@FXML
private MFXTableColumn<Import_Detail_Entity> UNIT_PRICE;
@FXML
private MFXTableColumn<Import_Detail_Entity> TOTAL_PRICE;
@FXML
private MFXTableColumn<Import_Detail_Entity> DESCRIPTIOND;
@FXML
private MFXTextField DESCRIPTION;
@FXML
private MFXFilterComboBox<Import_Entity> ID_REPLACE;
@FXML
private MFXDatePicker datePicker;
@FXML
private Spinner hourSpinner;
@FXML
private Spinner minuteSpinner;
@FXML
private Spinner secondSpinner;
@FXML
private MFXButton createBtn;
@FXML
private MFXButton back;
@FXML
private MFXButton createImportDetail;
@FXML
private MFXButton deleteImportDetail;
public ObservableList<Import_Detail_Entity> observableList;
private static final Logger logger = LoggerFactory.getLogger(ImportController.class);
ImportGeneralController importGeneralController;
ImportDetailCreateController importDetailCreateController = new ImportDetailCreateController();
public List<Import_Detail_Entity> listImportDetail = new ArrayList<>();
@FXML
public void createItem() {
LocalDateTime dateTime;
int hour = 0;
int minute = 0;
int second = 0;
if (datePicker.getValue() != null) {
LocalDate date = datePicker.getValue();
if (hourSpinner.getValue() != null) hour = (int) hourSpinner.getValue();
if (minuteSpinner.getValue() != null) minute = (int) minuteSpinner.getValue();
if (secondSpinner.getValue() != null) second = (int) secondSpinner.getValue();
dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute, second));
}else{
dateTime = LocalDateTime.now();
}
String des = (DESCRIPTION.getText().isEmpty()) ? null : DESCRIPTION.getText();
String idReplace = (ID_REPLACE.getValue() != null) ? ID_REPLACE.getValue().getID_IMP() : null;
transaction.begin();
while (true){
try {
Import_Entity importEntity = new Import_Entity(dateTime, des,true,idReplace,null);
ImportService importService = new ImportService(entityManager);
importService.registerNewImport(importEntity);
for(Import_Detail_Entity item : observableList){
item.setID_IMPORT(importEntity.getID_IMP());
}
importService.registerNewImportDetail(observableList);
importGeneralController.importController.setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralView.fxml", importGeneralController);
transaction.commit();
observableList.clear();
listImportDetail.clear();
return ;
} catch (Exception e) {
transaction.rollback();
}
}
}
@FXML
public void initialize() {
setItem();
setCol();
crt();
hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
secondSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
ImportService importService = new ImportService(entityManager);
Import_Entity importEntity = new Import_Entity();
ID_REPLACE.getItems().addAll(importService.getImportByCombinedCondition(importEntity, null, null, null, null, null, null));
}
private void crt() {
createBtn.setOnAction(event -> createItem());
back.setOnMouseClicked(event -> {
importGeneralController.importController.setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralView.fxml", importGeneralController);
});
createImportDetail.setOnMouseClicked(event -> {
try{
importDetailCreateController.setImportGeneralCreateController(this);
Stage popupStage = setPopView("/com/example/dkkp/ImportDetail/ImportDetailCreate.fxml", importDetailCreateController);
importDetailCreateController.setPopupStage(popupStage);
}catch (Exception e){
System.out.println("coi loi " + e.getMessage());
}
});
}
public void setItem() {
Import_Detail_Entity import_entity = new Import_Detail_Entity(1,"1",false,1,1,1,1.0,"2");
observableList = FXCollections.observableArrayList(listImportDetail);
importDetailTable.setItems(observableList);
}
private void setCol() {
ID_BASE_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getID_BASE_PRODUCT));
ID_FINAL_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getID_FINAL_PRODUCT));
QUANTITY.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getQUANTITY));
UNIT_PRICE.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getUNIT_PRICE));
TOTAL_PRICE.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getTOTAL_PRICE));
DESCRIPTIOND.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getDESCRIPTION));
}
public void setImportGeneralController(ImportGeneralController importGeneralController) {
this.importGeneralController = importGeneralController;
}
public Stage setPopView(String fxmlPath, Object controller) {
try {
FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
loader.setController(controller);
Stage popupStage = new Stage();
popupStage.initModality(Modality.APPLICATION_MODAL);
popupStage.setTitle("Base product filter");
Scene scene = new Scene(loader.load());
popupStage.setScene(scene);
double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
popupStage.setWidth(screenWidth *0.8);
popupStage.setHeight(screenHeight *0.8);
popupStage.show();
return popupStage;
} catch (IOException e) {
logger.error( e.getMessage());
return null;
}
}
public void closePopup(Stage popupStage) {
if (popupStage != null) {
popupStage.close();
}
}
}