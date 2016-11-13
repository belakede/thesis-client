package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.task.AuthenticationTask;
import org.controlsfx.control.NotificationPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @FXML
    private VBox parent;
    @FXML
    private NotificationPane notificationPane;
    @FXML
    private TextField serverAddress;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public void initialize(URL location, ResourceBundle resources) {
        notificationPane.setShowFromTop(true);
        notificationPane.prefHeightProperty().bind(parent.heightProperty());
    }

    public void submit(ActionEvent actionEvent) {
        String usernameText = username.getText();
        String passwordText = password.getText();
        String serverAddressText = serverAddress.getText();

        Task task = new AuthenticationTask(serverAddressText, usernameText, passwordText);
        task.setOnFailed(event -> {
            LOGGER.warn("Authentication failed!");
            notificationPane.setText("Authentication failed! ");
            notificationPane.show();
        });
        task.setOnSucceeded(event -> {
            LOGGER.info("Authentication succeed!");
            hide();
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void hide() {
        TranslateTransition transition = new TranslateTransition(new Duration(400), parent);
        transition.setToY(-(parent.getHeight()));
        transition.play();
    }

}
