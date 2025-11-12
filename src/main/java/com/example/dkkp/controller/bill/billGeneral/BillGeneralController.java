package com.example.dkkp.controller.bill.billGeneral;
import com.example.dkkp.controller.bill.BillController;
import com.example.dkkp.model.Bill_Entity;
import com.example.dkkp.model.EnumType;
import com.example.dkkp.service.BillService;
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
public class BillGeneralController {
  EntityManager entityManager = entityManagerFactory.createEntityManager();
  EntityTransaction transaction = entityManager.getTransaction();
  @FXML
  private MFXTableView < Bill_Entity > billTable;
  @FXML
  private MFXTableColumn < Bill_Entity > ID_BILL;
  @FXML
  private MFXTableColumn < Bill_Entity > DATE_EXP;
  @FXML
  private MFXTableColumn < Bill_Entity > ID_USER;
  @FXML
  private MFXTableColumn < Bill_Entity > BILL_STATUS;
  @FXML
  private MFXTableColumn < Bill_Entity > PHONE_BILL;
  @FXML
  private MFXTableColumn < Bill_Entity > ADD_BILL;
  @FXML
  private MFXTableColumn < Bill_Entity > TOTAL_PRICE;
  @FXML
  private MFXTableColumn < Bill_Entity > DESCRIPTION;
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
  private ObservableList < Bill_Entity > observableList;
  @FXML
  private MFXButton prevBtn, prevPageBtn, nextPageBtn, nextBtn;
  @FXML
  private Label pageLabel1, pageLabel2, pageLabel3;
  public String typeDate;
  public String typePrice;
  public int currentPage = 1;
  private int totalPages = 5;
  String sortField = "ID_BILL";
  String sortOrder = "desc";
  Integer setOff = 2;
  Integer offSet = 0;
  private static final Logger logger = LoggerFactory.getLogger(BillController.class);
  public BillService billService = new BillService(entityManager);
  public Bill_Entity billEntity = new Bill_Entity();
  public BillController billController;
  public BillGeneralCreateController billGeneralCreateController = new BillGeneralCreateController();
  public BillGeneralFilterController billGeneralFilterController = new BillGeneralFilterController();
  public BillGeneralDetailController billGeneralDetailController = new BillGeneralDetailController();
  public BillExportController billExportController = new BillExportController();
  public String billExportName = null;
  @FXML
  public void initialize() throws Exception {
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
    ID_BILL.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Entity::getID_BILL));
    DATE_EXP.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Entity::getDATE_EXP));
    ID_USER.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Entity::getEMAIL_ACC));
    BILL_STATUS.setRowCellFactory(_ -> new MFXTableRowCell < > (bill -> {
      EnumType.Status_Bill status = bill.getBILL_STATUS();
      return status != null ? status.getDescription() : "Unknown";
    }));
    PHONE_BILL.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Entity::getPHONE_BILL));
    ADD_BILL.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Entity::getADD_BILL));
    TOTAL_PRICE.setRowCellFactory(_ -> new MFXTableRowCell < > (product -> {
      Double price = product.getTOTAL_PRICE();
      if (price != null) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(price);
      }
      return "";
    }));
    DESCRIPTION.setRowCellFactory(_ -> new MFXTableRowCell < > (Bill_Entity::getDESCRIPTION));
  }
  public void setWidth() {
    ID_BILL.prefWidthProperty().bind(billTable.widthProperty().multiply(0.2));
    DATE_EXP.prefWidthProperty().bind(billTable.widthProperty().multiply(0.2));
    ID_USER.prefWidthProperty().bind(billTable.widthProperty().multiply(0.2));
    BILL_STATUS.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
    PHONE_BILL.prefWidthProperty().bind(billTable.widthProperty().multiply(0.2));
    ADD_BILL.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
    TOTAL_PRICE.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
    DESCRIPTION.prefWidthProperty().bind(billTable.widthProperty().multiply(0.1));
  }
  private void setSort() {
    ID_BILL.setOnMouseClicked(event -> {
      try {
        handleSort("ID_BILL");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    DATE_EXP.setOnMouseClicked(event -> {
      try {
        handleSort("DATE_EXP");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    ID_USER.setOnMouseClicked(event -> {
      try {
        handleSort("ID_USER");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    BILL_STATUS.setOnMouseClicked(event -> {
      try {
        handleSort("BILL_STATUS");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    PHONE_BILL.setOnMouseClicked(event -> {
      try {
        handleSort("PHONE_BILL");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    ADD_BILL.setOnMouseClicked(event -> {
      try {
        handleSort("ADD_BILL");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    TOTAL_PRICE.setOnMouseClicked(event -> {
      try {
        handleSort("TOTAL_PRICE");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    DESCRIPTION.setOnMouseClicked(event -> {
      try {
        handleSort("DESCRIPTION");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
  private void handleSort(String columnName) throws Exception {
    if (sortField == null || !sortField.equals(columnName)) {
      sortField = columnName;
      sortOrder = "asc";
    } else {
      sortOrder = sortOrder.equals("asc") ? "desc" : "asc";
    }
    refreshProductTable();
  }
  public void refreshProductTable() throws Exception {
    observableList = getBill();
    billTable.setItems(observableList);
  }
  public void updateTotalPage() throws Exception {
    Integer number = billService.getCountBillByCombinedCondition(billEntity, typeDate, typePrice);
    totalPages = (int) Math.ceil((double) number / setOff);
    totalRowLabel.setText("Total row : " + number);
    numberSetOff.setText("Number row per page: " + setOff);
    numberTotalPage.setText("Number pages: " + totalPages);
  }
  private void crt() {
    main.setOnMouseClicked(event -> {
      billTable.getSelectionModel().clearSelection();
      main.requestFocus();
    });
    exportBtn.setOnMouseClicked(event -> {
      try {
        billExportController.setBillGeneralController(this);
        Stage popupStage = setPopView("/com/example/dkkp/ExportName.fxml", billExportController);
        billExportController.setPopupStage(popupStage);;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    setOffField.setOnKeyPressed(event -> {
      try {
        handleKeyPress(event);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    crtBtn.setOnAction(_ -> {
      billGeneralCreateController.setBillGeneralController(this);
      setMainView("/com/example/dkkp/BillGeneral/BillGeneralCreate.fxml", billGeneralCreateController);
    });
    updatePagination();
    refreshBtn.setOnMouseClicked(event -> {
      BillGeneralController billGeneralController = new BillGeneralController();
      billGeneralController.billController = this.billController;
      billController.billGeneralController = billGeneralController;
      billController.setMainView("/com/example/dkkp/BillGeneral/BillGeneralView.fxml", billGeneralController);
    });
    searchFld.setOnMouseClicked(event -> {
      billGeneralFilterController.setBillGeneralController(this);
      Stage popupStage = setPopView("/com/example/dkkp/BillGeneral/BillGeneralFilter.fxml", billGeneralFilterController);
      billGeneralFilterController.setPopupStage(popupStage);
    });
    delBtn.setOnMouseClicked(event -> {
      try {
        del();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    detailBtn.setOnMouseClicked(event -> detail());
    pageLabel1.setOnMouseClicked(event -> {
      try {
        setPage(currentPage);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    pageLabel2.setOnMouseClicked(event -> {
      try {
        setPage(currentPage + 1);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    pageLabel3.setOnMouseClicked(event -> {
      try {
        setPage(currentPage + 2);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    prevBtn.setOnAction(event -> {
      try {
        setPage(1);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    nextBtn.setOnAction(event -> {
      try {
        setPage(totalPages);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    prevPageBtn.setOnAction(event -> {
      try {
        setPage(currentPage - 1);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    nextPageBtn.setOnAction(event -> {
      try {
        setPage(currentPage + 1);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
  public void exportToFile() throws Exception {
    Path currentDir = Path.of(System.getProperty("user.dir"));
    Path destinationDir = currentDir.resolve("src/main/FILE/BILL_FILE/BILL_GENERAL");
    List < Bill_Entity > p = billService.getBillByCombinedCondition(billEntity, typeDate, typePrice, sortField, sortOrder, null, null);
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Bills");
    String[] headers = {
      "ID_BILL",
      "EMAIL",
      "BILL_STATUS",
      "PHONE_BILL",
      "ADD_BILL",
      "TOTAL_PRICE",
      "DATE_EXP",
      "DESCRIPTION"
    };
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
    for (Bill_Entity bill: p) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(bill.getID_BILL() != null ? bill.getID_BILL().toString() : "X");
      row.createCell(1).setCellValue(bill.getEMAIL_ACC() != null ? bill.getEMAIL_ACC() : "N/A");
      row.createCell(2).setCellValue(bill.getBILL_STATUS() != null ? bill.getBILL_STATUS().getDescription() : "Unknown");
      row.createCell(3).setCellValue(bill.getPHONE_BILL() != null ? bill.getPHONE_BILL() : "N/A");
      row.createCell(4).setCellValue(bill.getADD_BILL() != null ? bill.getADD_BILL() : "N/A");
      row.createCell(5).setCellValue(bill.getTOTAL_PRICE() != null ? bill.getTOTAL_PRICE() : 0.0);
      row.createCell(6).setCellValue(bill.getDATE_EXP() != null ? bill.getDATE_EXP().toString() : "");
      row.createCell(7).setCellValue(bill.getDESCRIPTION() != null ? bill.getDESCRIPTION() : "");
    }
    if (Files.notExists(destinationDir)) {
      Files.createDirectories(destinationDir);
    }
    String fileName = "Bill_General-" + billExportName + ".xlsx";
    Path filePath = destinationDir.resolve(fileName);
    try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
      workbook.write(fileOut);
    }
    workbook.close();
    System.out.println("File đã được xuất ra: " + filePath.toAbsolutePath());
  }
  private void detail() {
    try {
      List < Bill_Entity > selectedItems = billTable.getSelectionModel().getSelectedValues();
      if (selectedItems.size() == 1) {
        billGeneralDetailController.setEntity(selectedItems.getFirst());
        billGeneralDetailController.setBillGeneralController(this);
        Stage popupStageUpdate = setPopView("/com/example/dkkp/BillGeneral/BillGeneralDetail.fxml", billGeneralDetailController);
        billGeneralDetailController.setPopupStage(popupStageUpdate);
      }
    } catch (Exception e) {
      System.out.println("co loi " + e.getMessage());
    };
  }
  private void handleKeyPress(KeyEvent event) throws Exception {
    if (event.getCode() == KeyCode.ENTER) {
      setOff = Integer.parseInt(setOffField.getText().trim());
      updateTotalPage();
      if (currentPage > totalPages) {
        setPage(totalPages);
      } else {
        setPage(currentPage);
      }
      billController.setMainView("/com/example/dkkp/BillGeneral/BillGeneralView.fxml", this);
    }
  }
  public void setPage(int page) throws Exception {
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
      pageLabel2.setOnMouseClicked(event -> {
        try {
          setPage(2);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      pageLabel3.setOnMouseClicked(event -> {
        try {
          setPage(3);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    } else if (currentPage == totalPages && totalPages == 2) {
      pageLabel1.setText("1");
      pageLabel2.setText("2");
      pageLabel2.setDisable(true);
      pageLabel1.setDisable(false);
      pageLabel1.setOnMouseClicked(event -> {
        try {
          setPage(1);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    } else if (currentPage == totalPages) {
      pageLabel1.setText(String.valueOf(totalPages - 2));
      pageLabel2.setText(String.valueOf(totalPages - 1));
      pageLabel3.setText(String.valueOf(totalPages));
      pageLabel1.setDisable(false);
      pageLabel2.setDisable(false);
      pageLabel3.setDisable(true);
      pageLabel1.setOnMouseClicked(event -> {
        try {
          setPage(totalPages - 2);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      pageLabel2.setOnMouseClicked(event -> {
        try {
          setPage(totalPages - 1);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      pageLabel3.setOnMouseClicked(event -> {
        try {
          setPage(totalPages);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    } else {
      pageLabel1.setText(String.valueOf(currentPage - 1));
      pageLabel2.setText(String.valueOf(currentPage));
      pageLabel3.setText(String.valueOf(currentPage + 1));
      pageLabel1.setDisable(false);
      pageLabel2.setDisable(true);
      pageLabel3.setDisable(false);
      pageLabel1.setOnMouseClicked(event -> {
        try {
          setPage(currentPage - 1);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      pageLabel2.setOnMouseClicked(event -> {
        try {
          setPage(currentPage);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      pageLabel3.setOnMouseClicked(event -> {
        try {
          setPage(currentPage + 1);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
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
  private void del() throws Exception {
    List < Bill_Entity > selectedItems = billTable.getSelectionModel().getSelectedValues();
    if (!selectedItems.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirm Deletion");
      alert.setHeaderText("Are you sure you want to delete this item?");
      alert.setContentText("This action cannot be undone.");
      ButtonType yesButton = new ButtonType("Yes");
      ButtonType noButton = new ButtonType("No");
      alert.getButtonTypes().setAll(yesButton, noButton);
      Optional < ButtonType > result = alert.showAndWait();
      if (result.isPresent() && result.get() == yesButton) {
        try {
          transaction.begin();
          BillService billService = new BillService(entityManager);
          for (Bill_Entity item: selectedItems) {
            billService.changeBillStatus(item.getID_BILL(), EnumType.Status_Bill.CANC);
          }
          transaction.commit();
        } catch (PersistenceException e) {
          transaction.rollback();
          Alert errorAlert = new Alert(Alert.AlertType.ERROR);
          errorAlert.setTitle("Deletion Error");
          errorAlert.setHeaderText("Unable to delete item(s).");
          TextArea textArea = new TextArea("The selected item(s) cannot be deleted due to foreign key constraints. " +
            "Please ensure the item(s) are not referenced elsewhere before attempting to delete.");
          textArea.setEditable(false);
          textArea.setWrapText(true);
          errorAlert.getDialogPane().setContent(textArea);
          errorAlert.showAndWait();
        } catch (Exception e) {
          transaction.rollback();
          Alert errorAlert = new Alert(Alert.AlertType.ERROR);
          errorAlert.setTitle("Error");
          errorAlert.setHeaderText("An unexpected error occurred." + e.getMessage());
          errorAlert.setContentText("Please try again later.");
          errorAlert.showAndWait();
        }
        updateTotalPage();
        refreshProductTable();
      }
    }
  }
  private ObservableList < Bill_Entity > getBill() throws Exception {
    List < Bill_Entity > p = billService.getBillByCombinedCondition(billEntity, typeDate, typePrice, sortField, sortOrder, setOff, offSet);
    for (Bill_Entity item: p) {
      System.out.println("bill " + item.getID_BILL());
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
      popupStage.setWidth(screenWidth * 0.8);
      popupStage.setHeight(screenHeight * 0.8);
      popupStage.show();
      return popupStage;
    } catch (IOException e) {
      logger.error(e.getMessage());
      return null;
    }
  }
  public void closePopup(Stage popupStage) {
    if (popupStage != null) {
      popupStage.close();
    }
  }
}