package me.belakede.thesis.client.javafx;


import com.google.common.base.CaseFormat;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ControlLoader {

    private ControlLoader() {
    }

    public static <T> void load(T instance) {
        boolean pane = Pane.class.isAssignableFrom(instance.getClass());
        if (pane) {
            String fxmlFile = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, instance.getClass().getSimpleName()).concat(".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(instance.getClass().getResource(fxmlFile));
            fxmlLoader.setRoot(instance);
            fxmlLoader.setController(instance);
            try {
                fxmlLoader.load();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

}
