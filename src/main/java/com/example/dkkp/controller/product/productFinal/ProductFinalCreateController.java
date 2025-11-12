package com.example.dkkp.controller.product.productFinal;
import com.example.dkkp.controller.product.TableInterface;
import com.example.dkkp.model.Product_Base_Entity;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.service.ProductBaseService;
import com.example.dkkp.service.ProductFinalService;
import com.example.dkkp.service.Validator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class ProductFinalCreateController {
EntityManager entityManager = entityManagerFactory.createEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
@FXML
private MFXFilterComboBox<Product_Base_Entity> ID_BASE_PRODUCT;
@FXML
private MFXTextField NAME_PRODUCT;
@FXML
private MFXTextField QUANTITY;
@FXML
private MFXTextField DES_PRODUCT;
@FXML
private MFXTextField PRICE;
@FXML
private MFXTextField DISCOUNT;
@FXML
private MFXButton createBtn;
@FXML
private MFXButton back;
@FXML
private MFXButton image;
@FXML
private ImageView imageView;
ProductFinalController productFinalController;
String imagePath = null;
@FXML
public void createProduct() {
String name = (NAME_PRODUCT.getText().isEmpty()) ? null : NAME_PRODUCT.getText();
String des = (DES_PRODUCT.getText().isEmpty()) ? null : DES_PRODUCT.getText();
Double price = (PRICE.getText().isEmpty()) ? null : Double.valueOf(PRICE.getText());
Double discount = (DISCOUNT.getText().isEmpty()) ? null : Double.valueOf(DISCOUNT.getText());
Integer idBaseProduct = (ID_BASE_PRODUCT.getValue() != null) ? ID_BASE_PRODUCT.getValue().getID_BASE_PRODUCT() : null;
String imageName = null;
if (name == null || price ==null || discount ==null || idBaseProduct == null) {
Alert alert = new Alert(Alert.AlertType.WARNING, "Please do not leave the Name, Price, Discount, or Base Product fields empty.", ButtonType.OK);
alert.setTitle("Input Warning");
alert.setHeaderText("Invalid Input");
alert.showAndWait();
return;
}
transaction.begin();
try {
if (imagePath != null) {
Path currentDir = Path.of(System.getProperty("user.dir"));
Path destinationDir = currentDir.resolve("src/main/IMAGE");
File sourceFile = new File(imagePath);
if (!Files.exists(destinationDir)) {
try {
Files.createDirectories(destinationDir);
} catch (IOException e) {
e.printStackTrace();
return;
}
}
String fileExtension = getFileExtension(sourceFile.getName());
String newFileName = System.currentTimeMillis() + fileExtension;
Path destinationPath = destinationDir.resolve(newFileName);
try {
Files.copy(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
imageName = newFileName;
} catch (IOException e) {
e.printStackTrace();
}
}
Product_Final_Entity product = new Product_Final_Entity(null, idBaseProduct, name, 0, des, price, discount, imageName);
ProductFinalService producFinalService = new ProductFinalService(entityManager);
producFinalService.createProductFinal(product);
transaction.commit();
productFinalController.productController.setMainView("/com/example/dkkp/ProductFinal/ProductFinalView.fxml", productFinalController);
} catch (Exception e) {
transaction.rollback();
Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create product: " + e.getMessage(), ButtonType.OK);
alert.setTitle("Database Error");
alert.setHeaderText("Transaction Failed");
alert.showAndWait();
}
}
@FXML
public void initialize() {
createBtn.setOnAction(event -> createProduct());
image.setOnAction(event -> handleOpenFile());
back.setOnMouseClicked(event -> {
productFinalController.productController.setMainView("/com/example/dkkp/ProductFinal/ProductFinalView.fxml", productFinalController);
});
Path currentDir = Path.of(System.getProperty("user.dir"));
Path imageDir = currentDir.resolve("src/main/IMAGE");
File defaultImageFile = imageDir.resolve("default.png").toFile();
imageView.setImage(new Image(defaultImageFile.toURI().toString()));
ProductBaseService productBaseService = new ProductBaseService(entityManager);
Product_Base_Entity productBaseEntity = new Product_Base_Entity();
ID_BASE_PRODUCT.getItems().addAll(productBaseService.getProductBaseByCombinedCondition(productBaseEntity,null,null,null,null,null,null,null));
setTextFormatter();
}
@FXML
private void handleOpenFile() {
FileChooser fileChooser = new FileChooser();
fileChooser.setTitle("Chose a picture file");
fileChooser.getExtensionFilters().addAll(
new FileChooser.ExtensionFilter("IMAGE", "*.png", "*.jpg", "*.jpeg", "*.webp")
);
Stage stage = (Stage) image.getScene().getWindow();
File selectedFile = fileChooser.showOpenDialog(stage);
if (selectedFile != null) {
long fileSizeInBytes = selectedFile.length();
long fileSizeInMB = fileSizeInBytes / (1024 * 1024);
if (fileSizeInMB > 50) {
System.out.println("File's size is too huge! Please choose a file below 50MB.");
return;
}
Image selectedImage = new Image(selectedFile.toURI().toString());
imageView.setImage(selectedImage);
imagePath = selectedFile.getAbsolutePath();
}
}
private String getFileExtension(String fileName) {
int dotIndex = fileName.lastIndexOf('.');
return (dotIndex >= 0) ? fileName.substring(dotIndex) : "";
}
private void setTextFormatter(){
Validator validator3 = new Validator();
Validator validator4 = new Validator();
PRICE.delegateSetTextFormatter(validator3.formatterDouble);
DISCOUNT.delegateSetTextFormatter(validator4.formatterPercentage);
}
public void setProductFinalController(ProductFinalController productFinalController) {
this.productFinalController = productFinalController;
}
}