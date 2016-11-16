package me.belakede.thesis.client.service;

import javafx.fxml.FXMLLoader;
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

}
