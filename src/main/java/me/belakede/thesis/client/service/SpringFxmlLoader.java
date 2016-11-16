package me.belakede.thesis.client.service;

import com.google.common.base.CaseFormat;
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

public class SpringFxmlLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringFxmlLoader.class);
    private static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(SuspectApplicationConfiguration.class);

    public Object load(URL url) {
        try (InputStream fxmlStream = url.openStream()) {
            LOGGER.info("Loading: {}", fxmlStream);
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(APPLICATION_CONTEXT::getBean);
            return loader.load(fxmlStream);
        } catch (IOException ioException) {
            LOGGER.warn("Cant load {}", url, ioException);
            throw new RuntimeException(ioException);
        }
    }

    public <T> T load(T instance) {
        boolean pane = Pane.class.isAssignableFrom(instance.getClass());
        if (pane) {
            String filename = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, instance.getClass().getSimpleName()).concat(".fxml");
            String fxmlFile = "/".concat(instance.getClass().getPackage().getName().replace(".", "/")).concat("/" + filename);
            LOGGER.info("Try to loading {}", fxmlFile);
            try (InputStream fxmlStream = getClass().getResourceAsStream(fxmlFile)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setRoot(instance);
                loader.setControllerFactory(APPLICATION_CONTEXT::getBean);
                return loader.load(fxmlStream);
            } catch (Exception ex) {
                LOGGER.warn("Cant load {}", fxmlFile, ex);
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException("The specified instance is not a Pane");
    }


}
