package com.example.dkkp.controller.product.productBrand;
import com.example.dkkp.controller.product.ProductController;
import com.example.dkkp.model.Brand_Entity;
import com.example.dkkp.service.BrandService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.HomeController.numberOr;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductBrandController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTableView<Brand_Entity> productTable;
@FXML
private MFXTableColumn<Brand_Entity> ID_BRAND;
@FXML
private MFXTableColumn<Brand_Entity> NAME_BRAND;
@FXML
private MFXTableColumn<Brand_Entity> DETAIL;
@FXML
private MFXButton searchFld;
@FXML
private MFXButton crtBtn;
@FXML
private MFXButton updBtn;
@FXML
private MFXButton delBtn;
@FXML
private MFXButton refreshBtn;
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
private ObservableList<Brand_Entity> observableList;
@FXML
private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
@FXML
private Label pageLabel1, pageLabel2, pageLabel3;
public int currentPage = 1;
private int totalPages = 5;
String sortField = "ID_BRAND";
String sortOrder = "desc";
Integer setOff = numberOr;
Integer offSet = 0;
private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
public BrandService brandService = new BrandService(entityManager);
Brand_Entity brandEntity = new Brand_Entity();
public ProductController productController;
public ProductBrandCreateController productBrandCreateController = new ProductBrandCreateController();
public ProductBrandFilterController productBrandFilterController = new ProductBrandFilterController();
public ProductBrandUpdateController productBrandUpdateController = new ProductBrandUpdateController();
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
private void setCol() {
ID_BRAND.setRowCellFactory(_ -> new MFXTableRowCell<>(Brand_Entity::getID_BRAND));
NAME_BRAND.setRowCellFactory(_ -> new MFXTableRowCell<>(Brand_Entity::getNAME_BRAND));
DETAIL.setRowCellFactory(_ -> new MFXTableRowCell<>(Brand_Entity::getDETAIL));
}
public void setWidth() {
ID_BRAND.prefWidthProperty().bind(productTable.widthProperty().multiply(0.15));
NAME_BRAND.prefWidthProperty().bind(productTable.widthProperty().multiply(0.4));
DETAIL.prefWidthProperty().bind(productTable.widthProperty().multiply(0.45));
}
private void setSort() {
ID_BRAND.setOnMouseClicked(event -> handleSort("ID_BRAND"));
NAME_BRAND.setOnMouseClicked(event -> handleSort("NAME_BRAND"));
DETAIL.setOnMouseClicked(event -> handleSort("DETAIL"));
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
observableList = getProducts();
productTable.setItems(observableList);
}
private void updateTotalPage() {
Integer number = brandService.getCountFilteredBrand(brandEntity);
totalPages = (int) Math.ceil((double) number / setOff);
totalRowLabel.setText("Total row : " +number);
numberSetOff.setText("Number row per page: " +setOff);
numberTotalPage.setText("Number pages: " + totalPages );
}
private void crt() {
main.setOnMouseClicked(event -> {
productTable.getSelectionModel().clearSelection();
main.requestFocus();
});
setOffField.setOnKeyPressed(event -> handleKeyPress(event));
crtBtn.setOnAction(_ -> {
productBrandCreateController.setProductBrandController(this);
setMainView("/com/example/dkkp/Brand/ProductBrandCreate.fxml", productBrandCreateController);
});
updatePagination();
refreshBtn.setOnMouseClicked(event -> {
ProductBrandController productBrandController = new ProductBrandController();
productBrandController.productController = this.productController;
productController.productBrandController = productBrandController;
productController.setMainView("/com/example/dkkp/Brand/ProductBrandView.fxml", productBrandController);
});
searchFld.setOnMouseClicked(event -> {
productBrandFilterController.setProductBrandController(this);
Stage popupStage = setPopView("/com/example/dkkp/Brand/ProductBrandFilter.fxml", productBrandFilterController);
productBrandFilterController.setPopupStage(popupStage);
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
private void handleKeyPress(KeyEvent event) {
if (event.getCode() == KeyCode.ENTER) {
setOff = Integer.parseInt(setOffField.getText().trim());
updateTotalPage();
productController.setMainView("/com/example/dkkp/Brand/ProductBrandView.fxml", this);
if (currentPage > totalPages) {
setPage(totalPages);
} else {
setPage(currentPage);
}
}
}
private void upd() {
List<Brand_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
if (selectedItems.size() == 1) {
productBrandUpdateController.setEntity(selectedItems.getFirst());
productBrandUpdateController.setProductBrandController(this);
Stage popupStageUpdate = setPopView("/com/example/dkkp/Brand/ProductBrandUpdate.fxml", productBrandUpdateController);
productBrandUpdateController.setPopupStage(popupStageUpdate);
}
;
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
List<Brand_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
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
BrandService brandService = new BrandService(entityManager);
for (Brand_Entity item : selectedItems) {
brandService.deleteBrand(item.getID_BRAND());
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
private ObservableList<Brand_Entity> getProducts() {
List<Brand_Entity> p = brandService.getBrandBy(brandEntity,sortField,sortOrder,setOff,offSet);
return FXCollections.observableArrayList(p);
}
public void setProductController(ProductController productController) {
this.productController = productController;
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