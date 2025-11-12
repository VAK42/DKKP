package com.example.dkkp.controller.impozt.importDetail;
import com.example.dkkp.controller.impozt.ImportController;
import com.example.dkkp.model.Import_Detail_Entity;
import com.example.dkkp.service.ImportService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ImportDetailController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTableView<Import_Detail_Entity> importTable;
@FXML
private MFXTableColumn<Import_Detail_Entity> ID_IMPD;
@FXML
private MFXTableColumn<Import_Detail_Entity> ID_IMPORT;
@FXML
private MFXTableColumn<Import_Detail_Entity> IS_AVAILABLE;
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
private MFXTableColumn<Import_Detail_Entity> DESCRIPTION;
@FXML
private MFXButton searchFld;
@FXML
private MFXButton crtBtn;
@FXML
private MFXButton delBtn;
@FXML
private MFXButton refreshBtn;
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
private ObservableList<Import_Detail_Entity> observableList;
@FXML
private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
@FXML
private Label pageLabel1, pageLabel2, pageLabel3;
public String typePPrice;
public String typeUPrice;
public String typeQuantity;
public int currentPage = 1;
private int totalPages = 5;
String sortField = "ID_IMPD";
String sortOrder = "desc";
Integer setOff = 2;
Integer offSet = 0;
private static final Logger logger = LoggerFactory.getLogger(ImportController.class);
public ImportService importService = new ImportService(entityManager);
public Import_Detail_Entity importDetailEntity = new Import_Detail_Entity();
public ImportController importController;
public ImportDetailFilterController importDetailFilterController = new ImportDetailFilterController();
public ImportDetailExportController importDetailExportController = new ImportDetailExportController();
public String importDetailExportName = null;
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
ID_IMPD.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getID_IMPD));
ID_IMPORT.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getID_IMPORT));
IS_AVAILABLE.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getIS_AVAILABLE));
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
TOTAL_PRICE.setRowCellFactory(_ -> new MFXTableRowCell<>(product -> {
Double price = product.getTOTAL_PRICE();
if (price != null) {
DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
return decimalFormat.format(price);
}
return "";
}));
DESCRIPTION.setRowCellFactory(_ -> new MFXTableRowCell<>(Import_Detail_Entity::getDESCRIPTION));
}
public void setWidth() {
ID_IMPD.prefWidthProperty().bind(importTable.widthProperty().multiply(0.1));
ID_IMPORT.prefWidthProperty().bind(importTable.widthProperty().multiply(0.15));
IS_AVAILABLE.prefWidthProperty().bind(importTable.widthProperty().multiply(0.1));
ID_BASE_PRODUCT.prefWidthProperty().bind(importTable.widthProperty().multiply(0.1));
ID_FINAL_PRODUCT.prefWidthProperty().bind(importTable.widthProperty().multiply(0.1));
QUANTITY.prefWidthProperty().bind(importTable.widthProperty().multiply(0.1));
UNIT_PRICE.prefWidthProperty().bind(importTable.widthProperty().multiply(0.15));
TOTAL_PRICE.prefWidthProperty().bind(importTable.widthProperty().multiply(0.15));
DESCRIPTION.prefWidthProperty().bind(importTable.widthProperty().multiply(0.15));
}
private void setSort() {
ID_IMPD.setOnMouseClicked(event -> handleSort("ID_IMPD"));
ID_IMPORT.setOnMouseClicked(event -> handleSort("ID_IMPORT"));
IS_AVAILABLE.setOnMouseClicked(event -> handleSort("IS_AVAILABLE"));
ID_BASE_PRODUCT.setOnMouseClicked(event -> handleSort("ID_BASE_PRODUCT"));
ID_FINAL_PRODUCT.setOnMouseClicked(event -> handleSort("ID_FINAL_PRODUCT"));
QUANTITY.setOnMouseClicked(event -> handleSort("QUANTITY"));
UNIT_PRICE.setOnMouseClicked(event -> handleSort("UNIT_PRICE"));
TOTAL_PRICE.setOnMouseClicked(event -> handleSort("TOTAL_PRICE"));
DESCRIPTION.setOnMouseClicked(event -> handleSort("DESCRIPTION"));
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
Integer number = importService.getCountImportDetailByCombinedConditionCount(importDetailEntity, typeUPrice,typeQuantity, typePPrice);
totalPages = (int) Math.ceil((double) number / setOff);
totalRowLabel.setText("Total row : " +number);
numberSetOff.setText("Number row per page: " +setOff);
numberTotalPage.setText("Number pages: " + totalPages );
}
private void crt() {
exportBtn.setOnMouseClicked(event -> {
try {
importDetailExportController.setImportDetailController(this);
Stage popupStage = setPopView("/com/example/dkkp/ExportName.fxml", importDetailExportController);
importDetailExportController.setPopupStage(popupStage);
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
updatePagination();
refreshBtn.setOnMouseClicked(event -> {
ImportDetailController importDetailController = new ImportDetailController();
importDetailController.importController = this.importController;
importController.importDetailController = importDetailController;
importController.setMainView("/com/example/dkkp/ImportDetail/ImportDetailView.fxml", importDetailController);
});
searchFld.setOnMouseClicked(event -> {
importDetailFilterController.setImportDetailController(this);
Stage popupStage = setPopView("/com/example/dkkp/ImportDetail/ImportDetailFilter.fxml", importDetailFilterController);
importDetailFilterController.setPopupStage(popupStage);
});
pageLabel1.setOnMouseClicked(event -> setPage(currentPage));
pageLabel2.setOnMouseClicked(event -> setPage(currentPage + 1));
pageLabel3.setOnMouseClicked(event -> setPage(currentPage + 2));
prevBtn.setOnAction(event -> setPage(1));
nextBtn.setOnAction(event -> setPage(totalPages));
prevPageBtn.setOnAction(event -> setPage(currentPage - 1));
nextPageBtn.setOnAction(event -> setPage(currentPage + 1));
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
importController.setMainView("/com/example/dkkp/ImportDetail/ImportDetailView.fxml", this);
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
public void exportToFile() throws Exception {
Path currentDir = Path.of(System.getProperty("user.dir"));
Path destinationDir = currentDir.resolve("src/main/FILE/IMPORT_FILE/IMPORT_DETAIL");
List<Import_Detail_Entity> p = importService.getImportDetailByCombinedCondition(importDetailEntity, typeUPrice,typeQuantity, typePPrice,sortField,sortOrder,null,null);
Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("Imports");
String[] headers = {"ID_IMPORT_DETAIL", "ID_IMPORT", "IS_AVAILABLE", "ID_BASE_PRODUCT", "NAME_BASE_PRODUCT", "ID_FINAL_PRODUCT", "NAME_FINAL_PRODUCT", "QUANTITY", "UNIT_PRICE", "TOTAL_PRICE", "DESCRIPTION"};
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
for (Import_Detail_Entity importDetail : p) {
Row row = sheet.createRow(rowNum++);
row.createCell(0).setCellValue(importDetail.getID_IMPD() != null ? importDetail.getID_IMPD().toString() : "X");
row.createCell(1).setCellValue(importDetail.getID_IMPORT() != null ? importDetail.getID_IMPORT().toString() : "X");
row.createCell(2).setCellValue(importDetail.getIS_AVAILABLE() != null ? importDetail.getIS_AVAILABLE().toString() : "X");
row.createCell(3).setCellValue(importDetail.getID_BASE_PRODUCT() != null ? importDetail.getID_BASE_PRODUCT().toString() : "X");
row.createCell(4).setCellValue(importDetail.getNAME_PRODUCT_BASE() != null ? importDetail.getNAME_PRODUCT_BASE() : "X");
row.createCell(5).setCellValue(importDetail.getID_FINAL_PRODUCT() != null ? importDetail.getID_FINAL_PRODUCT().toString() : "X");
row.createCell(6).setCellValue(importDetail.getNAME_PRODUCT_FINAL() != null ? importDetail.getNAME_PRODUCT_FINAL() : "X");
row.createCell(7).setCellValue(importDetail.getQUANTITY() != null ? importDetail.getQUANTITY() : 0);
row.createCell(8).setCellValue(importDetail.getUNIT_PRICE() != null ? importDetail.getUNIT_PRICE() : 0.0);
row.createCell(9).setCellValue(importDetail.getTOTAL_PRICE() != null ? importDetail.getTOTAL_PRICE() : 0.0);
row.createCell(10).setCellValue(importDetail.getDESCRIPTION() != null ? importDetail.getDESCRIPTION() : "");
}
if (Files.notExists(destinationDir)) {
Files.createDirectories(destinationDir);
}
String fileName = "Import_Detail-"+ importDetailExportName + ".xlsx";
Path filePath = destinationDir.resolve(fileName);
try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
workbook.write(fileOut);
}
workbook.close();
System.out.println("File đã được xuất ra: " + filePath.toAbsolutePath());
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
private ObservableList<Import_Detail_Entity> getImport() {
List<Import_Detail_Entity> p = importService.getImportDetailByCombinedCondition(importDetailEntity, typeUPrice,typeQuantity, typePPrice,sortField,sortOrder,setOff,offSet);
for(Import_Detail_Entity item : p) {
System.out.println("San pham " + item.getID_IMPD());
}
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