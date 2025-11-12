package com.example.dkkp.controller.product.productBase;
import com.example.dkkp.controller.product.ProductController;
import com.example.dkkp.controller.product.TableInterface;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.service.ProductBaseService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.HomeController.numberOr;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductBaseController implements TableInterface {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
@FXML
private MFXTableView<Product_Base_Entity> productTable;
@FXML
private MFXTableColumn<Product_Base_Entity> ID_BASE_PRODUCT;
@FXML
private MFXTableColumn<Product_Base_Entity> NAME_PRODUCT;
@FXML
private MFXTableColumn<Product_Base_Entity> TOTAL_QUANTITY;
@FXML
private MFXTableColumn<Product_Base_Entity> DATE_RELEASE;
@FXML
private MFXTableColumn<Product_Base_Entity> DES_PRODUCT;
@FXML
private MFXTableColumn<Product_Base_Entity> VIEW_COUNT;
@FXML
private MFXTableColumn<Product_Base_Entity> NAME_CATEGORY;
@FXML
private Label totalRowLabel;
@FXML
private Label numberTotalPage;
@FXML
private Label numberSetOff;
@FXML
private MFXTableColumn<Product_Base_Entity> NAME_BRAND;
@FXML
private Button searchFld;
@FXML
private Button crtBtn;
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
private ObservableList<Product_Base_Entity> observableList;
public ProductController productController;
private ProductBaseCreateController productBaseCreateController = new ProductBaseCreateController();
private ProductBaseUpdateController productBaseUpdateController = new ProductBaseUpdateController();
private ProductBaseFilterController productBaseFilterController = new ProductBaseFilterController();
public ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Base_Entity productBaseEntity = new Product_Base_Entity();
String sortField = "ID_BASE_PRODUCT";
String sortOrder = "desc";
String typeDate = null;
String typeQuantity = null;
String typeView = null;
Integer setOff = numberOr;
Integer offSet = 0;
@FXML
private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
@FXML
private Label pageLabel1, pageLabel2, pageLabel3;
public int currentPage = 1;
private int totalPages = 5;
@FXML
public void initialize() {
setCol();
setWidth();
observableList = getProducts();
productTable.setItems(observableList);
updateTotalPage();
crt();
setSort();
Validator validator1 = new Validator();
setOffField.delegateSetTextFormatter(validator1.formatterInteger);
}
public void setProductController(ProductController productController) {
this.productController = productController;
}
private void setSort() {
ID_BASE_PRODUCT.setOnMouseClicked(event -> handleSort("ID_BASE_PRODUCT"));
NAME_PRODUCT.setOnMouseClicked(event -> handleSort("NAME_PRODUCT"));
TOTAL_QUANTITY.setOnMouseClicked(event -> handleSort("TOTAL_QUANTITY"));
DATE_RELEASE.setOnMouseClicked(event -> handleSort("DATE_RELEASE"));
DES_PRODUCT.setOnMouseClicked(event -> handleSort("DES_PRODUCT"));
VIEW_COUNT.setOnMouseClicked(event -> handleSort("VIEW_COUNT"));
NAME_CATEGORY.setOnMouseClicked(event -> handleSort("NAME_CATEGORY"));
NAME_BRAND.setOnMouseClicked(event -> handleSort("NAME_BRAND"));
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
public void setPage(int page) {
if (page < 1 || page > totalPages) {
return;
}
System.out.println("set page " + page);
currentPage = page;
offSet = ((page - 1) * setOff);
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
System.out.println("dcm loi hoai");
pageLabel1.setText(String.valueOf(totalPages - 2));
pageLabel2.setText(String.valueOf(totalPages - 1));
pageLabel3.setText(String.valueOf(totalPages));
pageLabel1.setDisable(false);
pageLabel2.setDisable(false);
pageLabel3.setDisable(true);
pageLabel1.setOnMouseClicked(event -> {
System.out.println("bam roi");
setPage(totalPages - 2);
});
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
private void setCol() {
ID_BASE_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getID_BASE_PRODUCT));
NAME_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getNAME_PRODUCT));
DES_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getDES_PRODUCT));
DATE_RELEASE.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getDATE_RELEASE));
VIEW_COUNT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getVIEW_COUNT));
TOTAL_QUANTITY.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getTOTAL_QUANTITY));
NAME_CATEGORY.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getNAME_CATEGORY));
NAME_BRAND.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Base_Entity::getNAME_BRAND));
}
public void setWidth() {
ID_BASE_PRODUCT.setMinWidth(50);
ID_BASE_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
NAME_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.24));
DES_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
DATE_RELEASE.prefWidthProperty().bind(productTable.widthProperty().multiply(0.2));
VIEW_COUNT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
VIEW_COUNT.setMinWidth(30);
TOTAL_QUANTITY.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
TOTAL_QUANTITY.setMinWidth(30);
NAME_CATEGORY.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
NAME_BRAND.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
}
private void crt() {
main.setOnMouseClicked(event -> {
productTable.getSelectionModel().clearSelection();
main.requestFocus();
});
setOffField.setOnKeyPressed(event -> handleKeyPress(event));
crtBtn.setOnAction(_ -> {
productBaseCreateController.setProductBaseController(this);
setMainView("/com/example/dkkp/ProductBase/ProductBaseCreate.fxml", productBaseCreateController);
});
updatePagination();
refreshBtn.setOnMouseClicked(event -> {
ProductBaseController productBaseControllerNew = new ProductBaseController();
productBaseControllerNew.productController = this.productController;
productController.productBaseController = productBaseControllerNew;
productController.setMainView("/com/example/dkkp/ProductBase/ProductBaseView.fxml", productBaseControllerNew);
});
searchFld.setOnMouseClicked(event -> {
productBaseFilterController.setProductBaseController(this);
Stage popupStage = setPopView("/com/example/dkkp/ProductBase/ProductBaseFilter.fxml", productBaseFilterController);
productBaseFilterController.setPopupStage(popupStage);
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
private void upd() {
List<Product_Base_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
if (selectedItems.size() == 1) {
productBaseUpdateController.setEntity(selectedItems.getFirst());
productBaseUpdateController.setProductBaseController(this);
Stage popupStageUpdate = setPopView("/com/example/dkkp/ProductBase/ProductBaseUpdate.fxml", productBaseUpdateController);
productBaseUpdateController.setPopupStage(popupStageUpdate);
}
;
}
private void handleKeyPress(KeyEvent event) {
if (event.getCode() == KeyCode.ENTER) {
setOff = Integer.parseInt(setOffField.getText().trim());
updateTotalPage();
productController.setMainView("/com/example/dkkp/ProductBase/ProductBaseView.fxml", this);
if (currentPage > totalPages) {
setPage(totalPages);
} else {
setPage(currentPage);
}
}
}
private void del() {
List<Product_Base_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
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
ProductBaseService productBaseService = new ProductBaseService(entityManager);
for (Product_Base_Entity item : selectedItems) {
productBaseService.deleteProductBase(item.getID_BASE_PRODUCT());
}
transaction.commit();
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR);
errorAlert.setTitle("Deletion Error");
errorAlert.setHeaderText("Unable to delete item(s).");
errorAlert.setContentText("The selected item(s) cannot be deleted due to foreign key constraints, or another unexpected error occurred: " + e.getMessage());
errorAlert.showAndWait();
}
updatePagination();
refreshProductTable();
}
}
}
public void refreshProductTable() {
updateTotalPage();
observableList = getProducts();
productTable.setItems(observableList);
}
private void setColumnResizableForAllColumns(boolean resizable) {
ID_BASE_PRODUCT.setColumnResizable(resizable);
ID_BASE_PRODUCT.setMaxWidth(300);
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
public Stage setPopView(String fxmlPath, TableInterface controller) {
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
popupStage.setWidth(screenWidth * 0.8);
popupStage.setHeight(screenHeight * 0.8);
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
private ObservableList<Product_Base_Entity> getProducts() {
List<Product_Base_Entity> p = productBaseService.getProductBaseByCombinedCondition(productBaseEntity, sortField, sortOrder, typeDate, typeQuantity, typeView, setOff, offSet);
return FXCollections.observableArrayList(p);
}
private void updateTotalPage() {
Integer number = productBaseService.getCountProductBase(productBaseEntity, typeDate, typeQuantity, typeView);
totalPages = (int) Math.ceil((double) number / setOff);
totalRowLabel.setText("Total row : " + number);
numberSetOff.setText("Number row per page: " + setOff);
numberTotalPage.setText("Number pages: " + totalPages );
}
}