package com.example.dkkp.controller.impozt.importGeneral;
import com.example.dkkp.controller.impozt.ImportController;
import com.example.dkkp.controller.impozt.importDetail.ImportDetailExportController;
import com.example.dkkp.model.Import_Entity;
import com.example.dkkp.service.ImportService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ImportGeneralController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTableView<Import_Entity> importTable;
@FXML
private MFXTableColumn<Import_Entity> ID_IMP;
@FXML
private MFXTableColumn<Import_Entity> DATE_IMP;
@FXML
private MFXTableColumn<Import_Entity> DESCRIPTION;
@FXML
private MFXTableColumn<Import_Entity> IS_AVAILABLE;
@FXML
private MFXTableColumn<Import_Entity> ID_REPLACE;
@FXML
private MFXTableColumn<Import_Entity> TOTAL_PRICE;
@FXML
private MFXButton searchFld;
@FXML
private MFXButton crtBtn;
@FXML
private MFXButton delBtn;
@FXML
private MFXButton refreshBtn;
@FXML
private MFXButton detailBtn;
@FXML
private MFXTextField setOffField;
@FXML
private MFXButton exportBtn;
@FXML
private Label totalRowLabel;
@FXML
private Label numberSetOff;
@FXML
private Label numberTotalPage;
@FXML
private StackPane main;
@FXML
private HBox paginationHBox;
private ObservableList<Import_Entity> observableList;
@FXML
private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
@FXML
private Label pageLabel1, pageLabel2, pageLabel3;
public String typeDate;
public String typePrice;
public int currentPage = 1;
private int totalPages = 5;
String sortField = "ID_IMP";
String sortOrder = "desc";
Integer setOff = 2;
Integer offSet = 0;
private static final Logger logger = LoggerFactory.getLogger(ImportController.class);
public ImportService importService = new ImportService(entityManager);
public Import_Entity importEntity = new Import_Entity();
public ImportController importController;
public ImportGeneralCreateController importGeneralCreateController = new ImportGeneralCreateController();
public ImportGeneralFilterController importGeneralFilterController = new ImportGeneralFilterController();
public ImportGeneralDetailController importGeneralDetailController = new ImportGeneralDetailController();
public String importExportName = null;
public ImportExportController importExportController = new ImportExportController();
@FXML
public void initialize() {
observableList = getImport();
importTable.setItems(observableList);
setCol();
setWidth();
updateTotalPage();
crt();
setSort();
Validator validator1 = new Validator();
setOffField.delegateSetTextFormatter(validator1.formatterInteger);
}
private void setCol() {
ID_IMP.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Entity::getID_IMP));
DATE_IMP.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Entity::getDATE_IMP));
DESCRIPTION.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Entity::getDESCRIPTION));
IS_AVAILABLE.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Entity::getIS_AVAILABLE));
ID_REPLACE.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Entity::getID_REPLACE));
TOTAL_PRICE.setRowCellFactory(_ -> new MFXTableRowCell<>(product -> {
Double price = product.getTOTAL_PRICE();
if (price != null) {
DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
return decimalFormat.format(price);
}
return "";
}));
}
public void setWidth() {
ID_IMP.prefWidthProperty().bind(importTable.widthProperty().multiply(0.2));
DATE_IMP.prefWidthProperty().bind(importTable.widthProperty().multiply(0.2));
DESCRIPTION.prefWidthProperty().bind(importTable.widthProperty().multiply(0.2));
IS_AVAILABLE.prefWidthProperty().bind(importTable.widthProperty().multiply(0.1));
ID_REPLACE.prefWidthProperty().bind(importTable.widthProperty().multiply(0.2));
TOTAL_PRICE.prefWidthProperty().bind(importTable.widthProperty().multiply(0.1));
}
private void setSort() {
ID_IMP.setOnMouseClicked(event -> handleSort("ID_IMP"));
DATE_IMP.setOnMouseClicked(event -> handleSort("DATE_IMP"));
DESCRIPTION.setOnMouseClicked(event -> handleSort("DESCRIPTION"));
IS_AVAILABLE.setOnMouseClicked(event -> handleSort("IS_AVAILABLE"));
ID_REPLACE.setOnMouseClicked(event -> handleSort("ID_REPLACE"));
TOTAL_PRICE.setOnMouseClicked(event -> handleSort("TOTAL_PRICE"));
}
private void handleSort(String columnName) {
if (sortField == null || !sortField.equals(columnName)) {
sortField = columnName;
sortOrder = "asc";
} else {
sortOrder = sortOrder.equals("asc") ? "desc" : "asc";
}
refreshProductTable();
}
public void refreshProductTable() {
observableList = getImport();
importTable.setItems(observableList);
}
private void updateTotalPage() {
Integer number = importService.getCountImportByCombinedCondition(importEntity,typeDate,typePrice);
totalPages = (int) Math.ceil((double) number / setOff);
totalRowLabel.setText("Total row : " +number);
numberSetOff.setText("Number row per page: " +setOff);
numberTotalPage.setText("Number pages: " + totalPages );
}
private void crt() {
exportBtn.setOnMouseClicked(event -> {
try {
importExportController.setImportGeneralController(this);
Stage popupStage = setPopView("/com/example/dkkp/ExportName.fxml", importExportController);
importExportController.setPopupStage(popupStage);
;
} catch (Exception e) {
throw new RuntimeException(e);
}
});
main.setOnMouseClicked(event -> {
importTable.getSelectionModel().clearSelection();
main.requestFocus();
});
setOffField.setOnKeyPressed(event -> handleKeyPress(event));
crtBtn.setOnAction(_ -> {
importGeneralCreateController.setImportGeneralController(this);
setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralCreate.fxml", importGeneralCreateController);
});
updatePagination();
refreshBtn.setOnMouseClicked(event -> {
ImportGeneralController importGeneralController = new ImportGeneralController();
importGeneralController.importController = this.importController;
importController.importGeneralController = importGeneralController;
importController.setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralView.fxml", importGeneralController);
});
searchFld.setOnMouseClicked(event -> {
importGeneralFilterController.setImportGeneralController(this);
Stage popupStage = setPopView("/com/example/dkkp/ImportGeneral/ImportGeneralFilter.fxml", importGeneralFilterController);
importGeneralFilterController.setPopupStage(popupStage);
});
delBtn.setOnMouseClicked(event -> del());
detailBtn.setOnMouseClicked(event ->detail() );
pageLabel1.setOnMouseClicked(event -> setPage(currentPage));
pageLabel2.setOnMouseClicked(event -> setPage(currentPage + 1));
pageLabel3.setOnMouseClicked(event -> setPage(currentPage + 2));
prevBtn.setOnAction(event -> setPage(1));
nextBtn.setOnAction(event -> setPage(totalPages));
prevPageBtn.setOnAction(event -> setPage(currentPage - 1));
nextPageBtn.setOnAction(event -> setPage(currentPage + 1));
}
private void detail() {
try{
List<Import_Entity> selectedItems = importTable.getSelectionModel().getSelectedValues();
if (selectedItems.size() == 1) {
importGeneralDetailController.setEntity(selectedItems.getFirst());
importGeneralDetailController.setImportGeneralController(this);
Stage popupStageUpdate = setPopView("/com/example/dkkp/ImportGeneral/ImportGeneralDetail.fxml", importGeneralDetailController);
importGeneralDetailController.setPopupStage(popupStageUpdate);
}
} catch (Exception e) {
System.out.println("co loi " +e.getMessage());
}
;
}
public void exportToFile() throws Exception {
Path currentDir = Path.of(System.getProperty("user.dir"));
Path destinationDir = currentDir.resolve("src/main/FILE/IMPORT_FILE/IMPORT_GENERAL");
List<Import_Entity> p = importService.getImportByCombinedCondition(importEntity, typeDate, typePrice, sortField, sortOrder, null, null);
Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("Imports");
String[] headers = {"ID_IMPORT", "ID_REPLACE", "IS_AVAILABLE", "DATE_IMP", "TOTAL_PRICE", "DESCRIPTION"};
Row headerRow = sheet.createRow(0);
for (int i = 0; i < headers.length; i++) {
Cell cell = headerRow.createCell(i);
cell.setCellValue(headers[i]);
CellStyle style = workbook.createCellStyle();
Font font = workbook.createFont();
font.setBold(true);
style.setFont(font);
cell.setCellStyle(style);
}
int rowNum = 1;
for (Import_Entity bill : p) {
Row row = sheet.createRow(rowNum++);
row.createCell(0).setCellValue(bill.getID_IMP() != null ? bill.getID_IMP().toString() : "X");
row.createCell(1).setCellValue(bill.getID_IMP() != null ? bill.getID_IMP().toString() : "X");
row.createCell(2).setCellValue(bill.getIS_AVAILABLE() != null ? bill.getIS_AVAILABLE().toString() : "X");
row.createCell(3).setCellValue(bill.getDATE_IMP() != null ? bill.getDATE_IMP().toString() : "");
row.createCell(4).setCellValue(bill.getTOTAL_PRICE() != null ? bill.getTOTAL_PRICE() : 0.0);
row.createCell(5).setCellValue(bill.getDESCRIPTION() != null ? bill.getDESCRIPTION() : "");
}
if (Files.notExists(destinationDir)) {
Files.createDirectories(destinationDir);
}
String fileName = "Import_General-"+ importExportName + ".xlsx";
Path filePath = destinationDir.resolve(fileName);
try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
workbook.write(fileOut);
}
workbook.close();
System.out.println("File đã được xuất ra: " + filePath.toAbsolutePath());
}
private void handleKeyPress(KeyEvent event) {
if (event.getCode() == KeyCode.ENTER) {
setOff = Integer.parseInt(setOffField.getText().trim());
updateTotalPage();
if (currentPage > totalPages) {
setPage(totalPages);
} else {
setPage(currentPage);
}
importController.setMainView("/com/example/dkkp/ImportGeneral/ImportGeneralDetail.fxml", this);
}
}
public void setPage(int page) {
if (page < 1 || page > totalPages || totalPages == 1) {
return;
}
offSet = ((page - 1) * setOff);
currentPage = page;
updatePagination();
refreshProductTable();
}
private void updatePagination() {
if (totalPages < 3) {
if (totalPages == 1) {
System.out.println("total Page" + totalPages);
pageLabel2.setManaged(false);
}
pageLabel3.setManaged(false);
if (totalPages == 0) {
paginationHBox.setVisible(false);
paginationHBox.setManaged(false);
}
}
if (currentPage == 1) {
pageLabel1.setText("1");
pageLabel2.setText("2");
pageLabel3.setText("3");
pageLabel1.setDisable(true);
pageLabel2.setDisable(false);
pageLabel3.setDisable(false);
pageLabel2.setOnMouseClicked(event -> setPage(2));
pageLabel3.setOnMouseClicked(event -> setPage(3));
} else if (currentPage == totalPages && totalPages == 2) {
pageLabel1.setText("1");
pageLabel2.setText("2");
pageLabel2.setDisable(true);
pageLabel1.setDisable(false);
pageLabel1.setOnMouseClicked(event -> setPage(1));
} else if (currentPage == totalPages) {
pageLabel1.setText(String.valueOf(totalPages - 2));
pageLabel2.setText(String.valueOf(totalPages - 1));
pageLabel3.setText(String.valueOf(totalPages));
pageLabel1.setDisable(false);
pageLabel2.setDisable(false);
pageLabel3.setDisable(true);
pageLabel1.setOnMouseClicked(event -> setPage(totalPages - 2));
pageLabel2.setOnMouseClicked(event -> setPage(totalPages - 1));
pageLabel3.setOnMouseClicked(event -> setPage(totalPages));
} else {
pageLabel1.setText(String.valueOf(currentPage - 1));
pageLabel2.setText(String.valueOf(currentPage));
pageLabel3.setText(String.valueOf(currentPage + 1));
pageLabel1.setDisable(false);
pageLabel2.setDisable(true);
pageLabel3.setDisable(false);
pageLabel1.setOnMouseClicked(event -> setPage(currentPage - 1));
pageLabel2.setOnMouseClicked(event -> setPage(currentPage));
pageLabel3.setOnMouseClicked(event -> setPage(currentPage + 1));
if (pageLabel1.isDisable()) {
pageLabel1.setStyle("-fx-text-fill: gray;");
} else {
pageLabel1.setStyle("-fx-text-fill: black;");
}
if (pageLabel2.isDisable()) {
pageLabel2.setStyle("-fx-text-fill: gray;");
} else {
pageLabel2.setStyle("-fx-text-fill: black;");
}
if (pageLabel3.isDisable()) {
pageLabel3.setStyle("-fx-text-fill: gray;");
} else {
pageLabel3.setStyle("-fx-text-fill: black;");
}
}
prevBtn.setDisable(currentPage == 1);
prevPageBtn.setDisable(currentPage == 1);
nextBtn.setDisable(currentPage == totalPages || totalPages == 3);
nextPageBtn.setDisable(currentPage == totalPages || totalPages == 3);
}
private void del() {
List<Import_Entity> selectedItems = importTable.getSelectionModel().getSelectedValues();
if (!selectedItems.isEmpty()) {
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.setTitle("Confirm Deletion");
alert.setHeaderText("Are you sure you want to delete this item?");
alert.setContentText("This action cannot be undone.");
ButtonType yesButton = new ButtonType("Yes");
ButtonType noButton = new ButtonType("No");
alert.getButtonTypes().setAll(yesButton, noButton);
Optional<ButtonType> result = alert.showAndWait();
if (result.isPresent() && result.get() == yesButton) {
try {
transaction.begin();
ImportService importService = new ImportService(entityManager);
for (Import_Entity item : selectedItems) {
importService.deleteImportAndDetail(item.getID_IMP());
}
transaction.commit();
} catch (PersistenceException e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR);
errorAlert.setTitle("Deletion Error");
errorAlert.setHeaderText("Unable to delete item(s).");
TextArea textArea = new TextArea("The selected item(s) cannot be deleted due to foreign key constraints. "
+ "Please ensure the item(s) are not referenced elsewhere before attempting to delete.");
textArea.setEditable(false);
textArea.setWrapText(true);
errorAlert.getDialogPane().setContent(textArea);
errorAlert.showAndWait();
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR);
errorAlert.setTitle("Error");
errorAlert.setHeaderText("An unexpected error occurred.");
errorAlert.setContentText("Please try again later.");
errorAlert.showAndWait();
}
updateTotalPage();
refreshProductTable();
}
}
}
private ObservableList<Import_Entity> getImport() {
List<Import_Entity> p = importService.getImportByCombinedCondition(importEntity,typeDate,typePrice,sortField,sortOrder,setOff,offSet);
return FXCollections.observableArrayList(p);
}
public void setImportController(ImportController importController) {
this.importController = importController;
}
public void setMainView(String fxmlPath, Object controller) {
try {
FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
loader.setController(controller);
main.getChildren().clear();
main.getChildren().add(loader.load());
} catch (IOException e) {
logger.error("Loading FXML Failed!", e.getMessage());
}
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