package com.example.dkkp.controller;
import com.example.dkkp.model.User_Entity;
import com.example.dkkp.service.UserService;
import com.example.dkkp.service.Validator;
import com.example.dkkp.util.ViewUtil;
import com.example.dkkp.view.HomeView;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.util.Objects;
import io.github.palexdev.materialfx.controls.MFXTextField;
public class LoginController {
  static public EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DKKPPersistenceUnit");
  public EntityManager entityManager = entityManagerFactory.createEntityManager();
  public EntityTransaction transaction = entityManager.getTransaction();
  @FXML
  private MFXTextField username;
  @FXML
  private MFXPasswordField password;
  @FXML
  private void handleLogin(ActionEvent event) throws Exception {
    System.out.println("username: " + username.getText());
    System.out.println("paa: " + password.getText());
    if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter both username and password.", ButtonType.OK);
      alert.setTitle("Input Error");
      alert.setHeaderText(null);
      alert.showAndWait();
    } else {
      System.out.println("email " + username.getText());
      if (Validator.isValidEmail(username.getText())) {
        UserService userService = new UserService(entityManager);
        if (userService.login(username.getText(), password.getText())) {
          HomeView homeView = new HomeView();
          Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
          homeView.showHomeView(currentStage);
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong pass or email.", ButtonType.OK);
          alert.setTitle("Input Error");
          alert.setHeaderText(null);
          alert.showAndWait();
        }
      } else {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong email format", ButtonType.OK);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.showAndWait();
      }
    }
  }
}