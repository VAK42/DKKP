package com.example.dkkp.view;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.dkkp.util.ViewUtil;
public class LoginView {
  private static final Logger logger = LoggerFactory.getLogger(LoginView.class);
  private StackPane loginPane;
  public LoginView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dkkp/LoginView.fxml"));
      loginPane = loader.load();
    } catch (IOException e) {
      logger.error("Loading LoginView FXML Failed!", e);
    }
  }
  public void showLoginView(Stage stage) {
    ViewUtil.showView(stage, loginPane);
  }
}