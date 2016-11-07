package me.belakede.thesis.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SuspectChatClientApplication extends Application {

    public static void main(String[] args) throws Exception {
        Application.launch(SuspectChatClientApplication.class, args);
    }

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("boundary/javafx/chat/chat-box.fxml"));

        Scene scene = new Scene(root, 220, 200);

        stage.setTitle("Messages");
        stage.setMinWidth(220);
        stage.setScene(scene);
        stage.show();
    }
}
