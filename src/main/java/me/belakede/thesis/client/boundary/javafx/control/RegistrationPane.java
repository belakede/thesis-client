package me.belakede.thesis.client.boundary.javafx.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.NotificationPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.belakede.thesis.client.boundary.javafx.util.ControlLoader.load;

public class RegistrationPane extends VBox {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationPane.class);

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

    public RegistrationPane() {
        load(this);
        setupNotificationPane();
        setupActionEvents();
    }

    private void setupNotificationPane() {
        notificationPane.setShowFromTop(true);
        notificationPane.prefHeightProperty().bind(heightProperty());
    }

    private void setupActionEvents() {
        submit.setOnAction(event -> {
            if (password.getText().isEmpty() || password.getText().length() < 5 || !password.getText().equals(passwordAgain.getText())) {
                LOGGER.info("Missing or wrong password!");
                notificationPane.setText("Missing or wrong password!");
                notificationPane.show();
            } else {
                LOGGER.info("Register user!");
            }
        });
    }
}
