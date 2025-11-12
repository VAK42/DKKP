package com.example.dkkp.controller.product.productFinal;
import com.example.dkkp.controller.product.ProductController;
import com.example.dkkp.controller.product.TableInterface;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.service.ProductFinalService;
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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import static com.example.dkkp.controller.HomeController.numberOr;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductFinalController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
@FXML
private MFXTableView<Product_Final_Entity> productTable;
@FXML
private MFXTableColumn<Product_Final_Entity> ID_FINAL_PRODUCT;
@FXML
private MFXTableColumn<Product_Final_Entity> ID_BASE_PRODUCT;
@FXML
private MFXTableColumn<Product_Final_Entity> NAME_PRODUCT;
@FXML
private MFXTableColumn<Product_Final_Entity> NAME_BASE_PRODUCT;
@FXML
private MFXTableColumn<Product_Final_Entity> QUANTITY;
@FXML
private MFXTableColumn<Product_Final_Entity> DES_PRODUCT;
@FXML
private MFXTableColumn<Product_Final_Entity> PRICE_SP;
@FXML
private MFXTableColumn<Product_Final_Entity> DISCOUNT;
Product_Final_Entity productFinalEntity = new Product_Final_Entity();
ProductFinalService productFinalService = new ProductFinalService(entityManager);
@FXML
private Label totalRowLabel;
@FXML
private Label numberSetOff;
@FXML
private Label numberTotalPage;
@FXML
private Button searchFld;
@FXML
private Button crtBtn;
@FXML
private MFXButton exportBtn;
@FXML
private Button updBtn;
@FXML
private Button delBtn;
@FXML
private Button refreshBtn;
@FXML
private StackPane main;
@FXML
private HBox paginationHBox;
@FXML
private MFXTextField setOffField;
public ProductController productController;
public ProductFinalCreateController productFinalCreateController = new ProductFinalCreateController() ;
public ProductFinalFilterController productFinalFilterController = new ProductFinalFilterController() ;
public ProductFinalUpdateController productFinalUpdateController = new ProductFinalUpdateController() ;
String sortField = "ID_SP";
String sortOrder = "desc";
Integer setOff = numberOr;
Integer offSet = 0;
String typePrice = null;
String typeDiscount = null;
String typeQuantity = null;
@FXML
private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
@FXML
private Label pageLabel1, pageLabel2, pageLabel3;
public int currentPage = 1;
private int totalPages = 5;
private ObservableList<Product_Final_Entity> observableList;
public String productExportName = null;
public ProductExportController productExportController = new ProductExportController() ;
@FXML
public void initialize() {
observableList = getProducts();
productTable.setItems(observableList);
setCol();
setWidth();
updateTotalPage();
crt();
setSort();
Validator validator1 = new Validator();
setOffField.delegateSetTextFormatter(validator1.formatterInteger);
}
private void crt() {
exportBtn.setOnMouseClicked(event -> {
try {
productExportController.setProductFinalController(this);
Stage popupStage = setPopView("/com/example/dkkp/ExportName.fxml", productExportController);
productExportController.setPopupStage(popupStage);
;
} catch (Exception e) {
throw new RuntimeException(e);
}
});
main.setOnMouseClicked(event -> {
productTable.getSelectionModel().clearSelection();
main.requestFocus();
});
setOffField.setOnKeyPressed(event -> handleKeyPress(event));
crtBtn.setOnAction(_ -> {
productFinalCreateController.setProductFinalController(this);
setMainView("/com/example/dkkp/ProductFinal/ProductFinalCreate.fxml", productFinalCreateController);
});
updatePagination();
refreshBtn.setOnMouseClicked(event -> {
ProductFinalController productFinalControllerNew = new ProductFinalController();
productFinalControllerNew.productController = this.productController;
productController.productFinalController = productFinalControllerNew;
productController.setMainView("/com/example/dkkp/ProductFinal/ProductFinalView.fxml", productFinalControllerNew);
});
searchFld.setOnMouseClicked(event -> {
productFinalFilterController.setProductFinalController(this);
Stage popupStage = setPopView("/com/example/dkkp/ProductFinal/ProductFinalFilter.fxml", productFinalFilterController);
productFinalFilterController.setPopupStage(popupStage);
});
updBtn.setOnMouseClicked(event -> upd());
delBtn.setOnMouseClicked(event -> del());
pageLabel1.setOnMouseClicked(event -> setPage(currentPage));
pageLabel2.setOnMouseClicked(event -> setPage(currentPage + 1));
pageLabel3.setOnMouseClicked(event -> setPage(currentPage + 2));
prevBtn.setOnAction(event -> setPage(1));
nextBtn.setOnAction(event -> setPage(totalPages));
prevPageBtn.setOnAction(event -> setPage(currentPage - 1));
nextPageBtn.setOnAction(event -> setPage(currentPage + 1));
}
public void exportToFile() throws Exception {
Path currentDir = Path.of(System.getProperty("user.dir"));
Path destinationDir = currentDir.resolve("src/main/FILE/PRODUCT_FINAL");
List<Product_Final_Entity> p = productFinalService.getProductFinalByCombinedCondition(productFinalEntity,typePrice, typeDiscount, typeQuantity, sortField, sortOrder, null, null);
Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("Imports");
String[] headers = {"ID_FINAL_PRODUCT", "NAME_FINAL_PRODUCT", "ID_BASE_PRODUCT", "NAME_BASE_PRODUCT", "QUANTITY","DISCOUNT", "BASE_PRICE" ,"LAST_PRICE", "DES_PRODUCT"};
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
for (Product_Final_Entity product : p) {
Row row = sheet.createRow(rowNum++);
row.createCell(0).setCellValue(product.getID_SP() != null ? product.getID_SP().toString() : "X");
row.createCell(1).setCellValue(product.getNAME_PRODUCT() != null ? product.getNAME_PRODUCT() : "X");
row.createCell(2).setCellValue(product.getID_BASE_PRODUCT() != null ? product.getID_BASE_PRODUCT().toString() : "X");
row.createCell(3).setCellValue(product.getNAME_PRODUCT_BASE() != null ? product.getNAME_PRODUCT_BASE() : "X");
row.createCell(4).setCellValue(product.getQUANTITY() != null ? product.getQUANTITY() : 0.0);
row.createCell(5).setCellValue(product.getDISCOUNT()!= null ? product.getDISCOUNT().toString() : "X");
row.createCell(6).setCellValue(product.getPRICE_SP() != null ? product.getPRICE_SP().toString() : "X");
row.createCell(7).setCellValue(((product.getPRICE_SP()!= null) && (product.getDISCOUNT() != null))
?String.valueOf (product.getPRICE_SP() *(1-product.getDISCOUNT() /100)) :
(product.getPRICE_SP()!= null ? product.getPRICE_SP().toString() : " " ));
row.createCell(8).setCellValue(product.getDES_PRODUCT() != null ? product.getDES_PRODUCT() : "X");
}
if (Files.notExists(destinationDir)) {
Files.createDirectories(destinationDir);
}
String fileName = "FINAL_PRODUCT-"+ productExportName + ".xlsx";
Path filePath = destinationDir.resolve(fileName);
try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
workbook.write(fileOut);
}
workbook.close();
System.out.println("File đã được xuất ra: " + filePath.toAbsolutePath());
}
public void setProductController(ProductController productController) {
this.productController = productController;
}
private void setWidth() {
ID_FINAL_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
ID_BASE_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
NAME_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.15));
NAME_BASE_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.15));
QUANTITY.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
DES_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
PRICE_SP.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
DISCOUNT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
}
private void setCol() {
ID_FINAL_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getID_SP));
ID_BASE_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getID_BASE_PRODUCT));
NAME_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getNAME_PRODUCT));
NAME_BASE_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getNAME_PRODUCT_BASE));
QUANTITY.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getQUANTITY));
DES_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getDES_PRODUCT));
PRICE_SP.setRowCellFactory(cell -> new MFXTableRowCell<>(product -> {
Double price = product.getPRICE_SP();
if (price != null) {
DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
return decimalFormat.format(price);
}
return "";
}));
DISCOUNT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getDISCOUNT));
}
private void setSort() {
ID_FINAL_PRODUCT.setOnMouseClicked(event -> handleSort("ID_SP"));
ID_BASE_PRODUCT.setOnMouseClicked(event -> handleSort("ID_BASE_PRODUCT"));
NAME_PRODUCT.setOnMouseClicked(event -> handleSort("NAME_PRODUCT"));
NAME_BASE_PRODUCT.setOnMouseClicked(event -> handleSort("NAME_BASE_PRODUCT"));
QUANTITY.setOnMouseClicked(event -> handleSort("QUANTITY"));
DES_PRODUCT.setOnMouseClicked(event -> handleSort("DES_PRODUCT"));
PRICE_SP.setOnMouseClicked(event -> handleSort("PRICE_SP"));
DISCOUNT.setOnMouseClicked(event -> handleSort("DISCOUNT"));
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
private void handleKeyPress(KeyEvent event) {
if (event.getCode() == KeyCode.ENTER) {
setOff = Integer.parseInt(setOffField.getText().trim());
updateTotalPage();
productController.setMainView("/com/example/dkkp/ProductFinal/ProductFinalView.fxml", this);
if (currentPage > totalPages) {
setPage(totalPages);
} else {
setPage(currentPage);
}
}
}
public void refreshProductTable() {
observableList = getProducts();
productTable.setItems(observableList);
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
List<Product_Final_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
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
ProductFinalService productFinalService = new ProductFinalService(entityManager);
for (Product_Final_Entity item : selectedItems) {
productFinalService.deleteProductFinal(item.getID_SP());
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
private void upd() {
List<Product_Final_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
if (selectedItems.size() == 1) {
productFinalUpdateController.setEntity(selectedItems.getFirst());
productFinalUpdateController.setProductFinalController(this);
Stage popupStageUpdate = setPopView("/com/example/dkkp/ProductFinal/ProductFinalUpdate.fxml", productFinalUpdateController);
productFinalUpdateController.setImageView();
productFinalUpdateController.setPopupStage(popupStageUpdate);
}
;
}
private ObservableList<Product_Final_Entity> getProducts() {
List<Product_Final_Entity> p = productFinalService.getProductFinalByCombinedCondition(productFinalEntity,typePrice, typeDiscount, typeQuantity, sortField, sortOrder, setOff, offSet);
return FXCollections.observableArrayList(p);
}
private void updateTotalPage() {
Integer number = productFinalService.getCountProductFinalByCombinedCondition(productFinalEntity, typePrice, typeQuantity, typeDiscount);
totalPages = (int) Math.ceil((double) number / setOff);
totalRowLabel.setText("Total row : " +number);
numberSetOff.setText("Number row per page: " +setOff);
numberTotalPage.setText("Number pages: " + totalPages );
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
logger.error("Loading FXML Failed!", e.getMessage());
return null;
}
}
public void closePopup(Stage popupStage) {
if (popupStage != null) {
popupStage.close();
}
}
}