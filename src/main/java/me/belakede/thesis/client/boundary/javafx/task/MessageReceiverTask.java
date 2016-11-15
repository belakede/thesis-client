package me.belakede.thesis.client.boundary.javafx.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import me.belakede.thesis.client.boundary.javafx.model.UserMessage;
import me.belakede.thesis.client.configuration.UserConfiguration;
import me.belakede.thesis.game.equipment.Suspect;
import me.belakede.thesis.jackson.JacksonContextResolver;
import me.belakede.thesis.server.chat.request.ChatRequest;
import me.belakede.thesis.server.chat.response.Message;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class MessageReceiverTask extends Task<List<UserMessage>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiverTask.class);
    private ObservableList<UserMessage> userMessages = FXCollections.observableArrayList();

    public MessageReceiverTask() {
    }

    public ObservableList<UserMessage> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(ObservableList<UserMessage> userMessages) {
        this.userMessages = userMessages;
    }

    @Override
    protected List<UserMessage> call() throws Exception {
        UserConfiguration configuration = UserConfiguration.getInstance();
        Client client = ClientBuilder.newBuilder().register(JacksonContextResolver.class, SseFeature.class).build();
        WebTarget webTarget = client.target(configuration.getBaseUrl() + "/chat/join");
        LOGGER.debug("WebTarget: {}", webTarget);
        EventInput eventInput = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + configuration.getToken().getAccessToken())
                .post(Entity.json(new ChatRequest(configuration.getRoomId())), EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                break;
            }
            LOGGER.info("UserMessage arrived: {}", inboundEvent.toString());
            Message message = inboundEvent.readData(Message.class, MediaType.APPLICATION_JSON_TYPE);
            userMessages.add(new UserMessage(message.getSender(), message.getContent(), message.getTime(), Suspect.PEACOCK));
        }
        LOGGER.info("Channel closed!");
        return userMessages;
    }
}