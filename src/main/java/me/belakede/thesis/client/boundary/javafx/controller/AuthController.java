package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.RegistrationPane;
import me.belakede.thesis.client.boundary.javafx.task.AuthenticationTask;
import me.belakede.thesis.client.configuration.UserConfiguration;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.PopOver;
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
    private PopOver popOver;

    public void initialize(URL location, ResourceBundle resources) {
        setupNotificationPane();
        setupPopOver();
    }

    private void setupPopOver() {
        popOver = new PopOver(new RegistrationPane());
        popOver.setTitle("Register");
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowIndent(15.0);
        popOver.setArrowSize(0.0);
    }

    private void setupNotificationPane() {
        notificationPane.setShowFromTop(true);
        notificationPane.prefHeightProperty().bind(parent.heightProperty());
    }

    public void submit(ActionEvent actionEvent) {
        String usernameText = username.getText().trim();
        String passwordText = password.getText().trim();
        String serverAddressText = serverAddress.getText().trim();

        Task task = new AuthenticationTask(serverAddressText, usernameText, passwordText);
        task.setOnFailed(event -> {
            LOGGER.warn("Authentication failed!");
            notificationPane.setText("Authentication failed! ");
            notificationPane.show();
        });
        task.setOnSucceeded(event -> {
            UserConfiguration.getInstance().setBaseUrl(serverAddressText);
            LOGGER.info("Authentication succeed!");
            hide();
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void register(ActionEvent event) {
        popOver.show((Node) event.getSource());
    }

    private void hide() {
        TranslateTransition transition = new TranslateTransition(new Duration(400), parent);
        transition.setToY(-(parent.getHeight()));
        transition.play();
    }
}
