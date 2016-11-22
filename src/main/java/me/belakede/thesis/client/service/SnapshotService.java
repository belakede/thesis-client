package me.belakede.thesis.client.service;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SnapshotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotService.class);
    private final LongProperty numberOfSnapshots = new SimpleLongProperty();
    private final StringProperty directory = new SimpleStringProperty();
    private final StringProperty filename = new SimpleStringProperty();
    private final GameService gameService;

    private StringExpression directoryExpression;
    private StringExpression filenameExpression;

    @Autowired
    public SnapshotService(GameService gameService) {
        this.gameService = gameService;
        setNumberOfSnapshots(1);
        hookupChangeListeners();
        setupBindings();
    }

    public void saveAsPng(Pane parent) {
        Platform.runLater(() -> {
            WritableImage image = parent.snapshot(new SnapshotParameters(), null);
            LOGGER.info("Creating {}", getFilename());
            File file = new File(getFilename());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                incrementNumberOfSnapshots();
            } catch (IOException e) {
                LOGGER.warn("Can't create png file");
            }
        });
    }

    private void hookupChangeListeners() {
        directoryProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                removePreviouslyCreatedDirectory(newValue);
                createDirectory(newValue);
            }
        });
    }

    private void setupBindings() {
        directoryExpression = Bindings.format("videos/%s", gameService.roomIdProperty());
        directoryProperty().bind(directoryExpression);
        filenameExpression = Bindings.format("%s/snapshot_%06d.png", directoryProperty(), numberOfSnapshotsProperty());
        filenameProperty().bind(filenameExpression);
    }

    private void removePreviouslyCreatedDirectory(String directory) {
        try {
            Files.newDirectoryStream(Paths.get(directory)).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    LOGGER.warn("Can't remove videos directory");
                }
            });
        } catch (IOException e) {
            LOGGER.warn("Can't remove videos directory");
        }
    }

    private void createDirectory(String directory) {
        try {
            Files.createDirectories(Paths.get(directory));
        } catch (IOException e) {
            LOGGER.warn("Can't create videos directory");
        }
    }

    private long getNumberOfSnapshots() {
        return numberOfSnapshots.get();
    }

    private void setNumberOfSnapshots(long numberOfSnapshots) {
        this.numberOfSnapshots.set(numberOfSnapshots);
    }

    private LongProperty numberOfSnapshotsProperty() {
        return numberOfSnapshots;
    }

    private void incrementNumberOfSnapshots() {
        setNumberOfSnapshots(getNumberOfSnapshots() + 1);
    }

    private StringProperty directoryProperty() {
        return directory;
    }

    private String getFilename() {
        return filename.get();
    }

    private StringProperty filenameProperty() {
        return filename;
    }

}