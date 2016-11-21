package me.belakede.thesis.client.service;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.belakede.thesis.game.equipment.PairOfDice;
import me.belakede.thesis.internal.game.equipment.DefaultPairOfDice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RollService {

    private final ListProperty<PairOfDice> rolls = new SimpleListProperty<>();
    private final NotificationService notificationService;

    @Autowired
    public RollService(NotificationService notificationService) {
        this.notificationService = notificationService;
        setRolls(FXCollections.observableArrayList());
        hookupChangeListeners();
    }

    public ObservableList<PairOfDice> getRolls() {
        return rolls.get();
    }

    public void setRolls(ObservableList<PairOfDice> rolls) {
        this.rolls.set(rolls);
    }

    public ListProperty<PairOfDice> rollsProperty() {
        return rolls;
    }

    private void hookupChangeListeners() {
        notificationService.pairOfDiceNotificationProperty().addListener((observable, oldValue, newValue) -> {
            getRolls().add(DefaultPairOfDice.create(newValue.getFirst(), newValue.getSecond()));
        });
    }

}
