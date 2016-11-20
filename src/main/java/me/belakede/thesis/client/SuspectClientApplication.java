package me.belakede.thesis.client;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import me.belakede.thesis.client.service.SpringFxmlLoader;

public class SuspectClientApplication extends Application {

    private static final SpringFxmlLoader SPRING_FXML_LOADER = new SpringFxmlLoader();

    public SuspectClientApplication() {
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Parent root = (Parent) SPRING_FXML_LOADER.load(getClass().getResource("boundary/javafx/main-frame.fxml"));
        Scene scene = new Scene(root, 1280, 800);

        stage.setTitle("Suspect");
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
