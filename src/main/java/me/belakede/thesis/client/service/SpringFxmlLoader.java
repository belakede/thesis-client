package me.belakede.thesis.client.service;

import com.google.common.base.CaseFormat;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import me.belakede.thesis.client.configuration.SuspectApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SpringFxmlLoader {

    public static final ObjectProperty<Locale> DEFAULT_LOCALE = new SimpleObjectProperty<>(new Locale("en"));
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringFxmlLoader.class);
    private static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(SuspectApplicationConfiguration.class);

    public Object load(URL url) {
        try (InputStream fxmlStream = url.openStream()) {
            LOGGER.trace("Loading: {}", fxmlStream);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/bundles", DEFAULT_LOCALE.get());
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resourceBundle);
            loader.setControllerFactory(APPLICATION_CONTEXT::getBean);
            return loader.load(fxmlStream);
        } catch (IOException ioException) {
            LOGGER.warn("Cant load {}", url, ioException);
            throw new RuntimeException(ioException);
        }
    }

    public <T, C> C load(T instance) {
        return load(instance, DEFAULT_LOCALE.get());
    }

    public <T, C> C load(T instance, Locale locale) {
        boolean pane = Pane.class.isAssignableFrom(instance.getClass());
        if (pane) {
            String filename = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, instance.getClass().getSimpleName()).concat(".fxml");
            String fxmlFile = "/".concat(instance.getClass().getPackage().getName().replace(".", "/")).concat("/" + filename);
            LOGGER.trace("Try to loading {}", fxmlFile);
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles/bundles", locale);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile), resourceBundle);
                loader.setRoot(instance);
                loader.setControllerFactory(APPLICATION_CONTEXT::getBean);
                loader.load();
                return loader.getController();
            } catch (Exception ex) {
                LOGGER.warn("Cant load {}", fxmlFile, ex);
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException("The specified instance is not a Pane");
    }


}
