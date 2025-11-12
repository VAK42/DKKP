package com.example.dkkp.controller.product.productOptionValue;
import com.example.dkkp.controller.product.ProductController;
import com.example.dkkp.model.Product_Option_Values_Entity;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.HomeController.numberOr;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductOptionValuesController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXTableView<Product_Option_Values_Entity> productTable;
@FXML
private MFXTableColumn<Product_Option_Values_Entity> ID;
@FXML
private MFXTableColumn<Product_Option_Values_Entity> ID_OPTION;
@FXML
private MFXTableColumn<Product_Option_Values_Entity> NAME_OPTION;
@FXML
private MFXTableColumn<Product_Option_Values_Entity> ID_FINAL_PRODUCT;
@FXML
private MFXTableColumn<Product_Option_Values_Entity> NAME_PRODUCT;
@FXML
private MFXTableColumn<Product_Option_Values_Entity> VALUE;
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
private ObservableList<Product_Option_Values_Entity> observableList;
@FXML
private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
@FXML
private Label pageLabel1, pageLabel2, pageLabel3;
public int currentPage = 1;
private int totalPages = 5;
String sortField = "ID";
String sortOrder = "desc";
Integer setOff = numberOr;
Integer offSet = 0;
private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
public ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Option_Values_Entity productOptionValuesEntity = new Product_Option_Values_Entity();
public ProductController productController;
public ProductOptionValuesCreateController productOptionValuesCreateController = new ProductOptionValuesCreateController();
public ProductOptionValuesFilterController productOptionValuesFilterController = new ProductOptionValuesFilterController();
public ProductOptionValuesUpdateController productOptionValuesUpdateController = new ProductOptionValuesUpdateController();
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
ID.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Option_Values_Entity::getID));
ID_OPTION.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Option_Values_Entity::getID_OPTION));
NAME_OPTION.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Option_Values_Entity::getNAME_OPTION));
ID_FINAL_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Option_Values_Entity::getID_FINAL_PRODUCT));
NAME_PRODUCT.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Option_Values_Entity::getNAME_PRODUCT));
VALUE.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Option_Values_Entity::getVALUE));
}
public void setWidth() {
ID.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
ID_OPTION.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
NAME_OPTION.prefWidthProperty().bind(productTable.widthProperty().multiply(0.2));
ID_FINAL_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.1));
NAME_PRODUCT.prefWidthProperty().bind(productTable.widthProperty().multiply(0.3));
VALUE.prefWidthProperty().bind(productTable.widthProperty().multiply(0.2));
}
private void setSort() {
ID.setOnMouseClicked(event -> handleSort("ID"));
ID_OPTION.setOnMouseClicked(event -> handleSort("ID_OPTION"));
NAME_OPTION.setOnMouseClicked(event -> handleSort("NAME_OPTION"));
ID_FINAL_PRODUCT.setOnMouseClicked(event -> handleSort("ID_FINAL_PRODUCT"));
NAME_PRODUCT.setOnMouseClicked(event -> handleSort("NAME_PRODUCT"));
VALUE.setOnMouseClicked(event -> handleSort("VALUE"));
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
Integer number = productFinalService.getCountProductOptionValuesCombinedCondition(productOptionValuesEntity);
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
setOffField.setOnKeyPressed(this::handleKeyPress);
crtBtn.setOnAction(_ -> {
productOptionValuesCreateController.setProductOptionValuesController(this);
setMainView("/com/example/dkkp/ProductOptionValue/ProductOptionValueCreate.fxml", productOptionValuesCreateController);
});
updatePagination();
refreshBtn.setOnMouseClicked(event -> {
ProductOptionValuesController productOptionValuesController = new ProductOptionValuesController();
productOptionValuesController.productController = this.productController;
productController.productOptionValuesController = productOptionValuesController;
productController.setMainView("/com/example/dkkp/ProductOptionValue/ProductOptionValueView.fxml", productOptionValuesController);
});
searchFld.setOnMouseClicked(event -> {
productOptionValuesFilterController.setProductOptionValuesController(this);
Stage popupStage = setPopView("/com/example/dkkp/ProductOptionValue/ProductOptionValueFilter.fxml", productOptionValuesFilterController);
productOptionValuesFilterController.setPopupStage(popupStage);
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
setPage(Math.min(currentPage, totalPages));
productController.setMainView("/com/example/dkkp/ProductOptionValue/ProductOptionValueView.fxml", this);
}
}
private void upd() {
List<Product_Option_Values_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
if (selectedItems.size() == 1) {
productOptionValuesUpdateController.setEntity(selectedItems.getFirst());
productOptionValuesUpdateController.setProductOptionValuesController(this);
Stage popupStageUpdate = setPopView("/com/example/dkkp/ProductOptionValue/ProductOptionValueUpdate.fxml", productOptionValuesUpdateController);
productOptionValuesUpdateController.setPopupStage(popupStageUpdate);
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
List<Product_Option_Values_Entity> selectedItems = productTable.getSelectionModel().getSelectedValues();
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
for (Product_Option_Values_Entity item : selectedItems) {
productFinalService.deleteProductOptionValues(item.getID(),null,null);
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
private ObservableList<Product_Option_Values_Entity> getProducts() {
List<Product_Option_Values_Entity> p = productFinalService.getProductOptionValuesCombinedCondition(productOptionValuesEntity,sortField,sortOrder,setOff,offSet);
for(Product_Option_Values_Entity item : p) {
System.out.println("San pham " + item.getVALUE());
}
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