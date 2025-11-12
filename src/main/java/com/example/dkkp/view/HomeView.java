package com.example.dkkp.view;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.dkkp.util.ViewUtil;
public class HomeView {
  private static final Logger logger = LoggerFactory.getLogger(HomeView.class);
  private BorderPane homePane;
  public HomeView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dkkp/HomeView.fxml"));
      homePane = loader.load();
    } catch (IOException e) {
      logger.error("Loading HomeView FXML Failed!", e);
    }
  }
  public void showHomeView(Stage stage) {
    ViewUtil.showView(stage, homePane);
  }
}