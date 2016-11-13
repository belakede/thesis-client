package me.belakede.thesis.client.boundary.javafx.task;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javafx.concurrent.Task;
import me.belakede.thesis.client.configuration.UserConfiguration;
import me.belakede.thesis.client.domain.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.Base64;

import static me.belakede.thesis.client.configuration.ClientConfiguration.CLIENT_APP;
import static me.belakede.thesis.client.configuration.ClientConfiguration.CLIENT_SECRET;

public class AuthenticationTask extends Task<Token> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTask.class);

    private final String server;
    private final String username;
    private final String password;

    public AuthenticationTask(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Token call() throws Exception {
        Client client = Client.create();
        Token result = null;
        WebResource webResource = client.resource(server + "/oauth/token");
        String request = new StringBuilder("grant_type=password")
                .append("&username=").append(username)
                .append("&password=").append(password)
                .append("&client_id=").append(CLIENT_APP)
                .append("&client_secret=").append(CLIENT_SECRET)
                .toString();

        ClientResponse response = webResource
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", createBasicAccessAuthentication())
                .post(ClientResponse.class, request);

        if (response.getStatus() != 200) {
            LOGGER.warn("HTTP error code : {}", response.getStatus());
            LOGGER.warn("{}", response.toString());
            failed();
        } else {
            result = response.getEntity(Token.class);
            LOGGER.info("Access Token: {}", result.getAccessToken());
            UserConfiguration.getInstance().setToken(result);
            LOGGER.info("Token has been stored!");
        }
        return result;
    }

    @Override
    protected void failed() {
        super.failed();
        updateMessage("Authentication failed!");
    }

    private String createBasicAccessAuthentication() {
        return "Basic " + Base64.getEncoder().encodeToString((CLIENT_APP + ":" + CLIENT_SECRET).getBytes());
    }
}
