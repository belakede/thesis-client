package me.belakede.thesis.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JavaFXApplicationRunner extends Application implements CommandLineRunner {

    public void run(String... args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("boundary/javafx/main-frame.fxml"));
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Test Application");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }
}
