package com.example.dkkp.controller.bill;
import com.example.dkkp.controller.bill.billDetail.BillDetailController;
import com.example.dkkp.controller.bill.billGeneral.BillGeneralController;
import com.example.dkkp.controller.product.ProductController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
public class BillController {
  @FXML
  public StackPane main;
  @FXML
  private MFXButton billGeneral;
  @FXML
  private MFXButton billDetail;
  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
  public BillGeneralController billGeneralController = new BillGeneralController();
  public BillDetailController billDetailController = new BillDetailController();
  @FXML
  public void initialize() {
    billGeneralController.setBillController(this);
    getButton();
    setActiveTab(billGeneral);
    setMainView("/com/example/dkkp/BillGeneral/BillGeneralView.fxml", billGeneralController);
  }
  private void getButton() {
    billGeneral.setOnAction(event -> {
      billGeneralController.setBillController(this);
      setActiveTab(billGeneral);
      setMainView("/com/example/dkkp/BillGeneral/BillGeneralView.fxml", billGeneralController);
    });
    billDetail.setOnMouseClicked(event -> {
      billDetailController.setBillController(this);
      setActiveTab(billDetail);
      setMainView("/com/example/dkkp/BillDetail/BillDetailView.fxml", billDetailController);
    });
  }
  public void setMainView(String fxmlPath, Object controller) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      loader.setController(controller);
      main.getChildren().clear();
      main.getChildren().add(loader.load());
    } catch (IOException e) {
      System.out.printf("Loi " + e.getMessage());
    }
  }
  private void setActiveTab(Button activeTab) {
    billDetail.getStyleClass().remove("activeProductBtn");
    billGeneral.getStyleClass().remove("activeProductBtn");
    activeTab.getStyleClass().add("activeProductBtn");
  }
}