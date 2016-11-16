package me.belakede.thesis.client.boundary.javafx.task;

import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.UserMessage;
import me.belakede.thesis.client.service.GameService;
import me.belakede.thesis.client.service.MessageService;
import me.belakede.thesis.client.service.UserService;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.jackson.JacksonContextResolver;
import me.belakede.thesis.server.chat.request.ChatRequest;
import me.belakede.thesis.server.chat.response.Message;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Service
public class MessageReceiverTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiverTask.class);

    private final UserService userService;
    private final GameService gameService;
    private final MessageService messageService;

    @Autowired
    public MessageReceiverTask(UserService userService, GameService gameService, MessageService messageService) {
        this.userService = userService;
        this.gameService = gameService;
        this.messageService = messageService;
    }

    @Override
    protected Void call() throws Exception {
        Client client = ClientBuilder.newBuilder().register(JacksonContextResolver.class, SseFeature.class).build();
        WebTarget webTarget = client.target(userService.getUrl("/chat/join"));
        LOGGER.debug("WebTarget: {}", webTarget);
        EventInput eventInput = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + userService.getAccessToken())
                .post(Entity.json(new ChatRequest(gameService.getRoomId())), EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                LOGGER.warn("Connection lost! InboundEvent is null!");
                break;
            }
            LOGGER.info("UserMessage arrived: {}", inboundEvent.toString());
            Message message = inboundEvent.readData(Message.class, MediaType.APPLICATION_JSON_TYPE);
            messageService.add(new UserMessage(message.getSender(), message.getContent(), message.getTime(), Suspect.PEACOCK));
        }
        LOGGER.info("Channel closed!");
        return null;
    }
}
