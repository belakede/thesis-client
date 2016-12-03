package me.belakede.thesis.client.boundary.javafx.control.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import me.belakede.thesis.client.boundary.javafx.task.RegistrationTask;
import org.controlsfx.control.NotificationPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class RegistrationPaneController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationPaneController.class);
    private ResourceBundle resourceBundle;

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
    @FXML
    private PasswordField passwordAgain;
    @FXML
    private Button submit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        setupNotificationPane();
        setupActionEvents();
    }

    private void setupNotificationPane() {
        notificationPane.setShowFromTop(true);
        notificationPane.prefHeightProperty().bind(parent.heightProperty());
    }

    private void setupActionEvents() {
        submit.setOnAction(event -> {
            if (password.getText().isEmpty() || password.getText().length() < 5 || !password.getText().equals(passwordAgain.getText())) {
                LOGGER.info("Missing or wrong password!");
                notificationPane.setText(getResourceBundle().getString("Missing or wrong password!"));
                notificationPane.show();
            } else {
                Task task = new RegistrationTask(serverAddress.getText(), username.getText(), password.getText());
                task.setOnFailed(failedEvent -> {
                    LOGGER.info("Registration failed!");
                    notificationPane.setText(getResourceBundle().getString("Registration failed! Please check the server address and the username."));
                    notificationPane.show();
                });
                task.setOnSucceeded(succeededEvent -> {
                    LOGGER.info("Registration succeeded!");
                    submit.setDisable(true);
                    notificationPane.setText(getResourceBundle().getString("Registration succeeded!"));
                    notificationPane.getContent().setDisable(true);
                    notificationPane.show();
                });
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
        });
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
