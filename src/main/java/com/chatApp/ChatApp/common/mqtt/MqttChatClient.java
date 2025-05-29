package com.chatApp.ChatApp.common.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class MqttChatClient {
    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    public static void main(String[] args) throws MqttException {
        MqttClient client = new MqttClient(BROKER, CLIENT_ID);
        client.connect();

        client.subscribe("chat/5.unique-conversation-id", (topic, msg) -> {
            System.out.println("Received: " + new String(msg.getPayload()));
        });

        //client.publish("chat/room1", new MqttMessage("Hello from Java!".getBytes()));
    }
}
