package me.belakede.thesis.client.service;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import me.belakede.thesis.game.equipment.Figurine;
import me.belakede.thesis.game.field.Field;
import me.belakede.thesis.server.game.response.FigurineNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionService.class);

    private final MapProperty<Figurine, Field> positions = new SimpleMapProperty<>();
    private final MapProperty<Field, Figurine> inversePositions = new SimpleMapProperty<>();
    private final NotificationService notificationService;
    private final BoardService boardService;

    @Autowired
    public PositionService(NotificationService notificationService, BoardService boardService) {
        this.notificationService = notificationService;
        this.boardService = boardService;
        setPositions(FXCollections.observableHashMap());
        setInversePositions(FXCollections.observableHashMap());
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        notificationService.gameStatusNotificationProperty().addListener((observable, oldValue, newValue) -> {
            newValue.getBoardStatus().getPositions().forEach(notification -> {
                LOGGER.debug("Init positions with {}", notification);
                updatePositionByNotification(notification);
            });
        });
        notificationService.figurineNotificationProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Update position based on {}", newValue);
            updatePositionByNotification(newValue);
        });
    }

    private void updatePositionByNotification(FigurineNotification notification) {
        Figurine figurine = notification.getFigurine();
        Field field = boardService.getField(notification.getPosition().getRow(), notification.getPosition().getColumn());
        getPositions().put(figurine, field);
        getInversePositions().put(field, figurine);
    }


    public ObservableMap<Figurine, Field> getPositions() {
        return positions.get();
    }

    public void setPositions(ObservableMap<Figurine, Field> positions) {
        this.positions.set(positions);
    }

    public MapProperty<Figurine, Field> positionsProperty() {
        return positions;
    }

    public ObservableMap<Field, Figurine> getInversePositions() {
        return inversePositions.get();
    }

    public void setInversePositions(ObservableMap<Field, Figurine> inversePositions) {
        this.inversePositions.set(inversePositions);
    }

    public MapProperty<Field, Figurine> inversePositionsProperty() {
        return inversePositions;
    }
}
