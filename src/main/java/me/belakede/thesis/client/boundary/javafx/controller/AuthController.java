package me.belakede.thesis.client.boundary.javafx.controller;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import me.belakede.thesis.client.boundary.javafx.control.RegistrationPane;
import me.belakede.thesis.client.boundary.javafx.converter.LanguageStringConverter;
import me.belakede.thesis.client.boundary.javafx.model.Language;
import me.belakede.thesis.client.boundary.javafx.task.AuthenticationTask;
import me.belakede.thesis.client.domain.Token;
import me.belakede.thesis.client.service.UserService;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static me.belakede.thesis.client.service.SpringFxmlLoader.DEFAULT_LOCALE;

@Controller
public class AuthController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    @FXML
    public TextField port;
    @FXML
    public ChoiceBox<Language> languageBox;
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
    private PopOver popOver;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        initLanguageBox();
        setupDefaultValues();
        setupNotificationPane();
        setupPopOver();
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        languageBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            DEFAULT_LOCALE.setValue(newValue.getLocale());
        });
    }

    private void initLanguageBox() {
        languageBox.getItems().addAll(Language.values());
        languageBox.setConverter(new LanguageStringConverter());
        languageBox.setValue(Language.valueOf(DEFAULT_LOCALE.getValue()));
    }


    private void setupDefaultValues() {
        port.setText("8080");
        serverAddress.setText("http://localhost");
    }

    private void setupPopOver() {
        popOver = new PopOver(new RegistrationPane());
        popOver.setTitle(getResourceBundle().getString("Register"));
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
        String serverAddressText = serverAddress.getText().trim() + ":" + port.getText().trim();

        Task<Optional<Token>> task = new AuthenticationTask(serverAddressText, usernameText, passwordText);
        task.setOnFailed(event -> {
            LOGGER.warn("Authentication failed: Server not found!");
            showNotification(getResourceBundle().getString("Server not found!"));
        });
        task.setOnSucceeded(event -> {
            if (task.getValue().isPresent()) {
                LOGGER.info("Authentication succeed!");
                userService.setToken(task.getValue().get());
                userService.setUsername(usernameText);
                userService.setBaseUrl(serverAddressText);
                hide();
            } else {
                LOGGER.warn("Authentication failed: Username or password is incorrect!");
                showNotification(getResourceBundle().getString("The username or password is incorrect."));
            }
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void showNotification(String message) {
        notificationPane.setText(message);
        notificationPane.setGraphic(new Glyph("FontAweomse", FontAwesome.Glyph.EXCLAMATION_TRIANGLE));
        notificationPane.show();
    }

    public void register(ActionEvent event) {
        popOver.show((Node) event.getSource());
    }

    private void hide() {
        TranslateTransition transition = new TranslateTransition(new Duration(400), parent);
        transition.setToY(-(parent.getHeight()));
        transition.setOnFinished(event -> parent.setVisible(false));
        transition.play();
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
