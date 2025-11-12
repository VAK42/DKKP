package com.example.dkkp.controller;
import com.example.dkkp.model.Product_Final_Entity;
import com.example.dkkp.service.ProductFinalService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import static com.example.dkkp.controller.LoginController.entityManagerFactory;
public class DashboardController {
  EntityManager entityManager = entityManagerFactory.createEntityManager();
  EntityTransaction transaction = entityManager.getTransaction();
  @FXML
  private Label totalRevenueMonth;
  @FXML
  private Label totalRevenueYear;
  @FXML
  private Label bestSellerMonth;
  @FXML
  private Label bestSellerYear;
  @FXML
  private LineChart < Number, Number > revenueChart;
  @FXML
  private LineChart < Number, Number > revenueChartD;
  @FXML
  private PieChart bestSellerChart;
  @FXML
  private PieChart bestSellerChartD;
  @FXML
  private Label totalRevenueMonthD;
  @FXML
  private Label totalRevenueYearD;
  @FXML
  private Label bestSellerMonthD;
  @FXML
  private Label bestSellerYearD;
  @FXML
  private MFXButton APPLY;
  @FXML
  private MFXDatePicker datePickerStart;
  @FXML
  private MFXDatePicker datePickerEnd;
  @FXML
  private MFXDatePicker datePicker;
  @FXML
  private MFXButton APPLYD;
  LocalDateTime c = null;
  LocalDateTime s = null;
  LocalDateTime e = null;
  @FXML
  public void initialize() {
    updateDashBoard();
    btn();
  }
  private void updateDashBoard() {
    updateStatistics();
    updateLineChart();
    updatePieChart();
  }
  private void btn() {
    APPLY.setOnMouseClicked(event -> get2LocalDate());
    APPLYD.setOnMouseClicked(event -> getLocalDate());
  }
  private void getLocalDate() {
    if (datePicker.getValue() != null) {
      LocalDate chosen = datePicker.getValue();
      c = chosen.atStartOfDay();
      System.out.println("day la c " + c);
      updateStatistics();
      updateLineChart();
    }
  }
  private void get2LocalDate() {
    LocalDate start = datePickerStart.getValue();
    LocalDate end = datePickerEnd.getValue();
    LocalDateTime startDateTime = start.atStartOfDay();
    LocalDateTime endDateTime = end.atTime(23, 59, 59);
    s = startDateTime;
    e = endDateTime;
    updatePieChart();
  }
  private void updateStatistics() {
    totalRevenueMonth.setText(String.format("$%.2f", getTotalRevenueForMonth()));
    System.out.println("duam1");
    totalRevenueYear.setText(String.format("$%.2f", getTotalRevenueForYear()));
    System.out.println("duam2");
    bestSellerMonth.setText(getBestSellerForMonth());
    System.out.println("duam3");
    bestSellerYear.setText(getBestSellerForYear());
    System.out.println("duam4");
    totalRevenueMonthD.setText(getTotalRevenueForMonthD().toString());
    totalRevenueYearD.setText(getTotalRevenueForYearD().toString());
    bestSellerMonthD.setText(getBestSellerForMonthD());
    bestSellerYearD.setText(getBestSellerForYearD());
  }
  private void updateLineChart() {
    Integer year = LocalDate.now().getYear();
    if (c != null) year = c.getYear();
    try {
      XYChart.Series < Number, Number > series = new XYChart.Series < > ();
      series.setName("Revenue");
      for (int month = 1; month <= 12; month++) {
        LocalDateTime startDate = getStartOfMonth(year, month);
        LocalDateTime endDate = getEndOfMonth(year, month);
        ProductFinalService productFinalService = new ProductFinalService(entityManager);
        List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
        double totalRevenue = bestSellers.stream()
          .mapToDouble(Product_Final_Entity::getSum_total_price)
          .sum();
        series.getData().add(new XYChart.Data < > (month, totalRevenue));
      }
      revenueChart.getData().clear();
      revenueChart.getData().add(series);
    } catch (Exception e) {
      System.out.println("loi nay " + e.getMessage());
    }
    try {
      XYChart.Series < Number, Number > series = new XYChart.Series < > ();
      series.setName("Revenue");
      for (int month = 1; month <= 12; month++) {
        LocalDateTime startDate = getStartOfMonth(year, month);
        LocalDateTime endDate = getEndOfMonth(year, month);
        ProductFinalService productFinalService = new ProductFinalService(entityManager);
        List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
        double totalRevenue = bestSellers.stream()
          .mapToDouble(Product_Final_Entity::getSum_quantity)
          .sum();
        series.getData().add(new XYChart.Data < > (month, totalRevenue));
      }
      revenueChartD.getData().clear();
      revenueChartD.getData().add(series);
    } catch (Exception e) {
      System.out.println("loi nay " + e.getMessage());
    }
  }
  private void updatePieChart() {
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(s, e);
    bestSellerChart.getData().clear();
    bestSellers.forEach(product -> bestSellerChart.getData().add(new PieChart.Data(product.getNAME_PRODUCT(), product.getSum_total_price())));
    bestSellerChartD.getData().clear();
    bestSellers.forEach(product -> bestSellerChartD.getData().add(new PieChart.Data(product.getNAME_PRODUCT(), product.getSum_quantity())));
  }
  private LocalDateTime getStartOfMonth(int year, int month) {
    return LocalDateTime.of(year, month, 1, 0, 0);
  }
  private LocalDateTime getEndOfMonth(int year, int month) {
    return LocalDateTime.of(year, month, 1, 0, 0)
      .with(TemporalAdjusters.lastDayOfMonth())
      .withHour(23).withMinute(59).withSecond(59);
  }
  private double getTotalRevenueForMonth() {
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfMonth(1).atStartOfDay();
      endDate = c.toLocalDate().withDayOfMonth(c.toLocalDate().lengthOfMonth()).atTime(23, 59, 59);
    }
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    double tong = 0;
    for (Product_Final_Entity product: bestSellers) {
      tong += product.getSum_total_price();
    }
    return tong;
  }
  private Integer getTotalRevenueForMonthD() {
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfMonth(1).atStartOfDay();
      endDate = c.toLocalDate().withDayOfMonth(c.toLocalDate().lengthOfMonth()).atTime(23, 59, 59);
    }
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    Integer tong = 0;
    for (Product_Final_Entity product: bestSellers) {
      tong += product.getSum_quantity();
    }
    return tong;
  }
  private double getTotalRevenueForYear() {
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfYear(1).atStartOfDay();
      endDate = c.toLocalDate().withMonth(12).withDayOfMonth(31).atTime(23, 59, 59, 999999999);
    } else {
      int currentYear = LocalDate.now().getYear();
      startDate = LocalDateTime.of(currentYear, 1, 1, 0, 0, 0, 0);
      endDate = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59, 999999999);
    }
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    double tong = 0;
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    for (Product_Final_Entity product: bestSellers) {
      tong += product.getSum_total_price();
    }
    return tong;
  }
  private Integer getTotalRevenueForYearD() {
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfYear(1).atStartOfDay();
      endDate = c.toLocalDate().withMonth(12).withDayOfMonth(31).atTime(23, 59, 59, 999999999);
    } else {
      int currentYear = LocalDate.now().getYear();
      startDate = LocalDateTime.of(currentYear, 1, 1, 0, 0, 0, 0);
      endDate = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59, 999999999);
    }
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    Integer tong = 0;
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    for (Product_Final_Entity product: bestSellers) {
      tong += product.getSum_quantity();
    }
    return tong;
  }
  private String getBestSellerForMonth() {
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfMonth(1).atStartOfDay();
      endDate = c.toLocalDate().withDayOfMonth(c.toLocalDate().lengthOfMonth()).atTime(23, 59, 59);
    }
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    Product_Final_Entity bestSeller = bestSellers.stream()
      .max(Comparator.comparingDouble(Product_Final_Entity::getSum_total_price))
      .orElseThrow(() -> new IllegalStateException("Can not find best seller product"));
    return bestSeller.getNAME_PRODUCT();
  }
  private String getBestSellerForMonthD() {
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfMonth(1).atStartOfDay();
      endDate = c.toLocalDate().withDayOfMonth(c.toLocalDate().lengthOfMonth()).atTime(23, 59, 59);
    }
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    Product_Final_Entity bestSeller = bestSellers.stream()
      .max(Comparator.comparingDouble(Product_Final_Entity::getSum_quantity))
      .orElseThrow(() -> new IllegalStateException("Can not find best seller product"));
    return bestSeller.getNAME_PRODUCT();
  }
  private String getBestSellerForYear() {
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfYear(1).atStartOfDay();
      endDate = c.toLocalDate().withMonth(12).withDayOfMonth(31).atTime(23, 59, 59, 999999999);
    } else {
      int currentYear = LocalDate.now().getYear();
      startDate = LocalDateTime.of(currentYear, 1, 1, 0, 0, 0, 0);
      endDate = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59, 999999999);
    }
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    Product_Final_Entity bestSeller = bestSellers.stream()
      .max(Comparator.comparingDouble(Product_Final_Entity::getSum_total_price))
      .orElseThrow(() -> new IllegalStateException("Can not find best seller product"));
    return bestSeller.getNAME_PRODUCT();
  }
  private String getBestSellerForYearD() {
    LocalDateTime startDate = null;
    LocalDateTime endDate = null;
    if (c != null) {
      startDate = c.toLocalDate().withDayOfYear(1).atStartOfDay();
      endDate = c.toLocalDate().withMonth(12).withDayOfMonth(31).atTime(23, 59, 59, 999999999);
    } else {
      int currentYear = LocalDate.now().getYear();
      startDate = LocalDateTime.of(currentYear, 1, 1, 0, 0, 0, 0);
      endDate = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59, 999999999);
    }
    ProductFinalService productFinalService = new ProductFinalService(entityManager);
    List < Product_Final_Entity > bestSellers = productFinalService.getProductDashBoard(startDate, endDate);
    Product_Final_Entity bestSeller = bestSellers.stream()
      .max(Comparator.comparingDouble(Product_Final_Entity::getSum_quantity))
      .orElseThrow(() -> new IllegalStateException("Can not find best seller product"));
    return bestSeller.getNAME_PRODUCT();
  }
}