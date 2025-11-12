package com.example.dkkp.controller.bill.billDetail;
import com.example.dkkp.controller.bill.BillController;
import com.example.dkkp.controller.bill.billGeneral.BillExportController;
import com.example.dkkp.model.Bill_Detail_Entity;
import com.example.dkkp.model.Bill_Entity;
import com.example.dkkp.service.BillService;
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
public class BillDetailController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTableView<Bill_Detail_Entity> billTable;
@FXML
private MFXTableColumn<Bill_Detail_Entity> ID_BILL_DETAIL;
@FXML
private MFXTableColumn<Bill_Detail_Entity> ID_BILL;
@FXML
private MFXTableColumn<Bill_Detail_Entity> AVAILABLE;
@FXML
private MFXTableColumn<Bill_Detail_Entity> ID_FINAL_PRODUCT;
@FXML
private MFXTableColumn<Bill_Detail_Entity> QUANTITY_SP;
@FXML
private MFXTableColumn<Bill_Detail_Entity> UNIT_PRICE;
@FXML
private MFXTableColumn<Bill_Detail_Entity> TOTAL_DETAIL_PRICE;
@FXML
private MFXButton searchFld;
@FXML
private MFXButton crtBtn;
@FXML
private MFXButton delBtn;
@FXML
private MFXButton refreshBtn;
@FXML
private MFXButton exportBtn;
@FXML
private MFXTextField setOffField;
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
private ObservableList<Bill_Detail_Entity> observableList;
@FXML
private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
@FXML
private Label pageLabel1, pageLabel2, pageLabel3;
public String typePPrice;
public String typeUPrice;
public String typeQuantity;
public int currentPage = 1;
private int totalPages = 5;
String sortField = "ID_BILL_DETAIL";
String sortOrder = "desc";
Integer setOff = 2;
Integer offSet = 0;
private static final Logger logger = LoggerFactory.getLogger(BillController.class);
public BillService billService = new BillService(entityManager);
public Bill_Detail_Entity billDetailEntity = new Bill_Detail_Entity();
public BillController billController;
public BillDetailExportController billDetailExportController = new BillDetailExportController();
public BillDetailFilterController billDetailFilterController = new BillDetailFilterController();
public String billDetailExportName = null;
@FXML
public void initialize() {
observableList = getBill();
billTable.setItems(observableList);
setCol();
setWidth();
updateTotalPage();
crt();
setSort();
Validator validator1 = new Validator();
setOffField.delegateSetTextFormatter(validator1.formatterInteger);
}
private void setCol() {
ID_BILL_DETAIL.setRowCellFactory(_ -> new MFXTableRowCell<>(Bill_Detail_Entity::getID_BILL_DETAIL));
ID_BILL.setRowCellFactory(_ -> new MFXTableRowCell<>(Bill_Detail_Entity::getID_BILL));
AVAILABLE.setRowCellFactory(_ -> new MFXTableRowCell<>(Bill_Detail_Entity::getAVAILABLE));
ID_FINAL_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Bill_Detail_Entity::getID_FINAL_PRODUCT));
QUANTITY_SP.setRowCellFactory(_ -> new MFXTableRowCell<>(Bill_Detail_Entity::getQUANTITY_BILL));
UNIT_PRICE.setRowCellFactory(_ -> new MFXTableRowCell<>(product -> {
Double price = product.getUNIT_PRICE();
if (price != null) {
DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
return decimalFormat.format(price);
}
return "";
}));
TOTAL_DETAIL_PRICE.setRowCellFactory(_ -> new MFXTableRowCell<>(product -> {
Double price = product.getTOTAL_DETAIL_PRICE();
if (price != null) {
DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
return decimalFormat.format(price);
}
return "";
}));
}
public void setWidth() {
ID_BILL_DETAIL.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
ID_BILL.prefWidthProperty().bind(billTable.widthProperty().multiply(0.15));
AVAILABLE.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
ID_FINAL_PRODUCT.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
QUANTITY_SP.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
UNIT_PRICE.prefWidthProperty().bind(billTable.widthProperty().multiply(0.15));
TOTAL_DETAIL_PRICE.prefWidthProperty().bind(billTable.widthProperty().multiply(0.15));
}
private void setSort() {
ID_BILL_DETAIL.setOnMouseClicked(event -> handleSort("ID_BILL_DETAIL"));
ID_BILL.setOnMouseClicked(event -> handleSort("ID_BILL"));
AVAILABLE.setOnMouseClicked(event -> handleSort("AVAILABLE"));
ID_FINAL_PRODUCT.setOnMouseClicked(event -> handleSort("ID_FINAL_PRODUCT"));
QUANTITY_SP.setOnMouseClicked(event -> handleSort("QUANTITY_SP"));
UNIT_PRICE.setOnMouseClicked(event -> handleSort("UNIT_PRICE"));
TOTAL_DETAIL_PRICE.setOnMouseClicked(event -> handleSort("TOTAL_DETAIL_PRICE"));
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
observableList = getBill();
billTable.setItems(observableList);
}
private void updateTotalPage() {
Integer number = billService.getCountBillDetailByCombinedCondition(billDetailEntity, typeUPrice,typeQuantity, typePPrice);
totalPages = (int) Math.ceil((double) number / setOff);
totalRowLabel.setText("Total row : " +number);
numberSetOff.setText("Number row per page: " +setOff);
numberTotalPage.setText("Number pages: " + totalPages );
}
private void crt() {
exportBtn.setOnMouseClicked(event -> {
try {
billDetailExportController.setBillDetailController(this);
Stage popupStage = setPopView("/com/example/dkkp/ExportName.fxml", billDetailExportController);
billDetailExportController.setPopupStage(popupStage);
;
} catch (Exception e) {
throw new RuntimeException(e);
}
});
main.setOnMouseClicked(event -> {
billTable.getSelectionModel().clearSelection();
main.requestFocus();
});
setOffField.setOnKeyPressed(event -> handleKeyPress(event));
updatePagination();
refreshBtn.setOnMouseClicked(event -> {
BillDetailController billDetailController = new BillDetailController();
billDetailController.billController = this.billController;
billController.billDetailController = billDetailController;
billController.setMainView("/com/example/dkkp/BillDetail/BillDetailView.fxml", billDetailController);
});
searchFld.setOnMouseClicked(event -> {
billDetailFilterController.setBillDetailController(this);
Stage popupStage = setPopView("/com/example/dkkp/BillDetail/BillDetailFilter.fxml", billDetailFilterController);
billDetailFilterController.setPopupStage(popupStage);
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
billController.setMainView("/com/example/dkkp/BillDetail/BillDetailView.fxml", this);
}
}
public void exportToFile() throws Exception {
Path currentDir = Path.of(System.getProperty("user.dir"));
Path destinationDir = currentDir.resolve("src/main/FILE/BILL_FILE/BILL_DETAIL");
List<Bill_Detail_Entity> p = billService.getBillDetailByCombinedCondition(billDetailEntity, typeUPrice,typeQuantity, typePPrice,sortField,sortOrder,null,null);
Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("Bills");
String[] headers = {"ID_BILL_DETAIL", "ID_BILL", "AVAILABLE", "ID_FINAL_PRODUCT", "NAME_FINAL_PRODUCT", "QUANTITY_SP", "UNIT_PRICE", "TOTAL_DETAIL_PRICE"};
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
for (Bill_Detail_Entity bill : p) {
Row row = sheet.createRow(rowNum++);
row.createCell(0).setCellValue(bill.getID_BILL_DETAIL() != null ? bill.getID_BILL_DETAIL().toString() : "X");
row.createCell(1).setCellValue(bill.getID_BILL() != null ? bill.getID_BILL() : "X");
row.createCell(2).setCellValue(bill.getAVAILABLE() != null ? bill.getAVAILABLE().toString() : "N/A");
row.createCell(3).setCellValue(bill.getID_FINAL_PRODUCT() != null ? bill.getID_FINAL_PRODUCT().toString() : "N/A");
row.createCell(4).setCellValue(bill.getNAME_FINAL_PRODUCT() != null ? bill.getNAME_FINAL_PRODUCT() : "Unknown");
row.createCell(5).setCellValue(bill.getQUANTITY_BILL() != null ? bill.getQUANTITY_BILL() : 0.0);
row.createCell(6).setCellValue(bill.getUNIT_PRICE() != null ? bill.getUNIT_PRICE() : 0.0);
row.createCell(7).setCellValue(bill.getTOTAL_DETAIL_PRICE() != null ? bill.getTOTAL_DETAIL_PRICE() : 0.0);
}
if (Files.notExists(destinationDir)) {
Files.createDirectories(destinationDir);
}
String fileName = "Bill_Detail-"+ billDetailExportName + ".xlsx";
Path filePath = destinationDir.resolve(fileName);
try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
workbook.write(fileOut);
}
workbook.close();
System.out.println("File đã được xuất ra: " + filePath.toAbsolutePath());
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
private ObservableList<Bill_Detail_Entity> getBill() {
List<Bill_Detail_Entity> p = billService.getBillDetailByCombinedCondition(billDetailEntity, typeUPrice,typeQuantity, typePPrice,sortField,sortOrder,setOff,offSet);
for(Bill_Detail_Entity item : p) {
System.out.println("San pham " + item.getID_BILL_DETAIL());
}
return FXCollections.observableArrayList(p);
}
public void setBillController(BillController billController) {
this.billController = billController;
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