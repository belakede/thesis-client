package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.service.NotificationService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.jackson.JacksonContextResolver;
import me.belakede.thesis.server.game.response.HeartBeatNotification;
import me.belakede.thesis.server.game.response.Notification;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Service
public class JoinToGameTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinToGameTask.class);

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public JoinToGameTask(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newClient();
        client.register(JacksonContextResolver.class);
        client.register(SseFeature.class);
        WebTarget webTarget = client.target(userService.getUrl("/game/join"));
        LOGGER.debug("WebTarget: {}", webTarget);
        EventInput eventInput = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                LOGGER.warn("Connection lost! InboundEvent is null!");
                break;
            }
            Notification notification = inboundEvent.readData(Notification.class, MediaType.APPLICATION_JSON_TYPE);
            if (notification instanceof HeartBeatNotification) {
                LOGGER.trace("Heartbeat");
            } else {
                LOGGER.info("Notification arrived: {}", notification);
                notificationService.add(notification);
            }
        }
        LOGGER.info("Channel closed!");
        return null;
    }
}
