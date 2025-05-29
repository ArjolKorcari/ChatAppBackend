package com.chatApp.ChatApp.common.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class MqttChatClient {
    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    public static void main(String[] args) throws MqttException {
        MqttClientService client = new MqttClientService();


//        client.subscribe("chat/room1", (topic, msg) -> {
//            System.out.println("Received: " + new String(msg.getPayload()));
//        });

        client.publishMessage("chat/room1", "Hello from Java!");
    }
}
