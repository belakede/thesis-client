package me.belakede.thesis.client.service;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RollService {

    private final IntegerProperty first = new SimpleIntegerProperty();
    private final IntegerProperty second = new SimpleIntegerProperty();
    private final NotificationService notificationService;

    @Autowired
    public RollService(NotificationService notificationService) {
        this.notificationService = notificationService;
        setFirst(0);
        setSecond(0);
        hookupChangeListeners();
    }

    private void hookupChangeListeners() {
        notificationService.pairOfDiceNotificationProperty().addListener((observable, oldValue, newValue) -> {
            setFirst(newValue.getFirst());
            setSecond(newValue.getSecond());
        });
    }

    public int getFirst() {
        return first.get();
    }

    public void setFirst(int first) {
        this.first.set(first);
    }

    public IntegerProperty firstProperty() {
        return first;
    }

    public int getSecond() {
        return second.get();
    }

    public void setSecond(int second) {
        this.second.set(second);
    }

    public IntegerProperty secondProperty() {
        return second;
    }

}
