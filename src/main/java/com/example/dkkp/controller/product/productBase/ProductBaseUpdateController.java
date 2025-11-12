package com.example.dkkp.controller.product.productBase;
import com.example.dkkp.controller.product.TableInterface;
import com.example.dkkp.model.Brand_Entity;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.model.Product_Attribute_Values_Entity;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.service.BrandService;
import com.example.dkkp.service.CategoryService;
import com.example.dkkp.service.ProductBaseService;
import com.example.dkkp.service.ProductFinalService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductBaseUpdateController implements TableInterface {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
private Product_Base_Entity productEntity;
private ProductBaseController productBaseController;
@FXML
private MFXTableView<Product_Attribute_Values_Entity> attributeValueTable;
@FXML
private MFXTableColumn<Product_Attribute_Values_Entity> ID;
@FXML
private MFXTableColumn<Product_Attribute_Values_Entity> ID_ATTRIBUTE;
@FXML
private MFXTableColumn<Product_Attribute_Values_Entity> NAME_ATTRIBUTE;
@FXML
private MFXTableColumn<Product_Attribute_Values_Entity> VALUE;
@FXML
private MFXTableView<Product_Final_Entity> finalProductTable;
@FXML
private MFXTableColumn<Product_Final_Entity> ID_P;
@FXML
private MFXTableColumn<Product_Final_Entity> NAME_P;
@FXML
private MFXTableColumn<Product_Final_Entity> DES;
@FXML
private MFXTextField id;
@FXML
private MFXTextField name;
@FXML
private MFXTextField des;
@FXML
private MFXTextField quantity;
@FXML
private MFXTextField viewCount;
@FXML
private MFXDatePicker date;
@FXML
private MFXFilterComboBox<Brand_Entity> brandField;
@FXML
private MFXFilterComboBox<Category_Entity> cateField;
@FXML
private MFXButton updateBtn;
@FXML
private MFXButton backBtn;
private Stage popupStage;
private ObservableList<Product_Attribute_Values_Entity> list;
private ObservableList<Product_Final_Entity> listP;
@FXML
public void initialize() {
list = getAttributeValue();
listP = getFinalProduct();
attributeValueTable.setItems(list);
finalProductTable.setItems(listP);
setCol();
setWidth();
BrandService brandService = new BrandService(entityManager);
CategoryService categoryService = new CategoryService(entityManager);
Brand_Entity brandEntity = new Brand_Entity();
Category_Entity categoryEntity = new Category_Entity();
// Fetch default entities
Brand_Entity brandEntityDefault = brandService.getFilteredBrand(new Brand_Entity(productEntity.getID_BRAND(), null, null), null, null, null, null).getFirst();
Category_Entity categoryEntityDefault = categoryService.getFilteredCategories(new Category_Entity(productEntity.getID_CATEGORY(), null), null, null, null, null).getFirst();
// Populate and set default values
brandField.getItems().addAll(brandService.getFilteredBrand(brandEntity, null, null, null, null));
cateField.getItems().addAll(categoryService.getFilteredCategories(categoryEntity, null, null, null, null));
// Set default text for comboboxes (assuming toString is overridden to show name/relevant info)
cateField.setText(categoryEntityDefault.toString());
brandField.setText(brandEntityDefault.toString());
pushEntity();
backBtn.setOnMouseClicked(event -> productBaseController.closePopup(popupStage));
updateBtn.setOnMouseClicked(event -> upDateEntity());
}
public ObservableList<Product_Attribute_Values_Entity> getAttributeValue() {
ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Attribute_Values_Entity attributeValueEntity = new Product_Attribute_Values_Entity(null, productEntity.getID_BASE_PRODUCT(), null, null);
List<Product_Attribute_Values_Entity> p = productBaseService.getProductAttributeValuesCombinedCondition(attributeValueEntity, "ID", "asc", null, null);
for (Product_Attribute_Values_Entity item : p) {
System.out.println("ID " + item.getVALUE());
}
return FXCollections.observableArrayList(p);
}
public ObservableList<Product_Final_Entity> getFinalProduct() {
ProductFinalService productFinalService = new ProductFinalService(entityManager);
Product_Final_Entity finalProduct = new Product_Final_Entity(null, productEntity.getID_BASE_PRODUCT(),null,null,null,null, null, null);
List<Product_Final_Entity> p = productFinalService.getProductFinalByCombinedCondition(finalProduct,null,null,null, "ID_SP", "asc", null, null);
for (Product_Final_Entity item : p) {
System.out.println("ID " + item.getNAME_PRODUCT());
}
return FXCollections.observableArrayList(p);
}
public void addButton(){
}
@Override
public void setWidth() {
ID.setMinWidth(30);
ID_ATTRIBUTE.setMinWidth(30);
ID.prefWidthProperty().bind(attributeValueTable.widthProperty().multiply(0.13));
ID_ATTRIBUTE.prefWidthProperty().bind(attributeValueTable.widthProperty().multiply(0.13));
NAME_ATTRIBUTE.prefWidthProperty().bind(attributeValueTable.widthProperty().multiply(0.37));
VALUE.prefWidthProperty().bind(attributeValueTable.widthProperty().multiply(0.37));
}
private void setCol() {
ID.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Attribute_Values_Entity::getID));
ID_ATTRIBUTE.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Attribute_Values_Entity::getID_ATTRIBUTE));
NAME_ATTRIBUTE.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Attribute_Values_Entity::getNAME_ATTRIBUTE));
VALUE.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Attribute_Values_Entity::getVALUE));
ID_P.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getID_SP));
NAME_P.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getNAME_PRODUCT));
DES.setRowCellFactory(_ -> new MFXTableRowCell<>(Product_Final_Entity::getDES_PRODUCT));
}
public void setProductBaseController(ProductBaseController productBaseController) {
this.productBaseController = productBaseController;
}
public void setEntity(Product_Base_Entity product_base_entity) {
this.productEntity = product_base_entity;
}
public void pushEntity() {
if (productEntity != null) {
id.setText(productEntity.getID_BASE_PRODUCT().toString());
name.setText(productEntity.getNAME_PRODUCT());
des.setText(productEntity.getDES_PRODUCT());
quantity.setText(String.valueOf(productEntity.getTOTAL_QUANTITY()));
date.setValue(productEntity.getDATE_RELEASE().toLocalDate());
viewCount.setText(String.valueOf(productEntity.getVIEW_COUNT()));
}
}
public void upDateEntity() {
LocalTime time = LocalTime.MIDNIGHT;
productEntity.setNAME_PRODUCT(name.getText());
productEntity.setDATE_RELEASE(date.getValue().atTime(time));
productEntity.setDES_PRODUCT(des.getText());
// Assuming quantity and viewCount fields are populated and valid integers
try {
productEntity.setTOTAL_QUANTITY(Integer.parseInt(quantity.getText()));
productEntity.setVIEW_COUNT(Integer.parseInt(viewCount.getText()));
} catch (NumberFormatException e) {
Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid number format for Quantity or View Count.", ButtonType.OK);
alert.showAndWait();
return;
}
if (brandField.getSelectionModel().getSelectedItem() != null) {
productEntity.setID_BRAND(brandField.getSelectionModel().getSelectedItem().getID_BRAND());
}
if (cateField.getSelectionModel().getSelectedItem() != null) {
productEntity.setID_CATEGORY(cateField.getSelectionModel().getSelectedItem().getID_CATEGORY());
}
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.setTitle("Confirm Update");
alert.setHeaderText("Are you sure you want to update this item?");
alert.setContentText("This action cannot be undone.");
ButtonType yesButton = new ButtonType("Yes");
ButtonType noButton = new ButtonType("No");
alert.getButtonTypes().setAll(yesButton, noButton);
Optional<ButtonType> result = alert.showAndWait();
if (result.isPresent() && result.get() == yesButton) {
try {
transaction.begin();
ProductBaseService productBaseService = new ProductBaseService(entityManager);
productBaseService.updateProductBase(productEntity);
transaction.commit();
productBaseController.refreshProductTable();
productBaseController.closePopup(popupStage);
} catch (Exception e) {
transaction.rollback();
Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to update product: " + e.getMessage(), ButtonType.OK);
errorAlert.showAndWait();
}
}
}
public void setPopupStage(Stage popupStage) {
this.popupStage = popupStage;
}
}