package org.example.vkapibot.model.responses.callback;

public enum RequestType {
    CONFIRMATION("confirmation"),
    MESSAGE_NEW("message_new"),
    MESSAGE_REPLY("message_reply");

    public String label;

    private RequestType(String label) {
        this.label = label;
    }
}