package me.belakede.thesis.client.configuration;

import java.util.Base64;

public class ClientConfiguration {

    public static final String CLIENT_APP = "c48dfe30-f50e";
    public static final String CLIENT_SECRET = "ef26cafe-557d-41d2-aec6-ff4a44c8871e";

    public static final String getBasicAccessAuthenticationHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((CLIENT_APP + ":" + CLIENT_SECRET).getBytes());
    }

}
