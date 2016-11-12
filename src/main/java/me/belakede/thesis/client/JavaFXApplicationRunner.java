package me.belakede.thesis.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JavaFXApplicationRunner extends Application implements CommandLineRunner {

    public static void main(String[] args) {
        launch(args);
    }

    public void run(String... args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("boundary/javafx/main-frame.fxml"));
        Scene scene = new Scene(root, 1280, 800);

        stage.setTitle("Test Application");
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        stage.setResizable(false);
        stage.setFullScreen(hasLowResolution());
        stage.setScene(scene);
        stage.show();
    }

    private boolean hasLowResolution() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        return visualBounds.getWidth() <= 1280 || visualBounds.getHeight() <= 800;
    }
}
