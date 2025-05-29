package com.chatApp.ChatApp.common.mqtt;



import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service

public class MqttClientService {

    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";


    private static final String CLIENT_ID = MqttClient.generateClientId();
    private MqttClient mqttClient;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public MqttClientService() {
        connectToBroker();
    }


    /**
     * Establishes a connection to the MQTT broker and sets up the client callback.
     * This method will continuously attempt to reconnect until successful.
     */
    private void connectToBroker() {
        while (true) {
            try {
                mqttClient = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setKeepAliveInterval(60);
                options.setCleanSession(false);

                mqttClient.connect(options);
                System.out.println("✅ Connected to MQTT Broker");

                mqttClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        System.err.println("❌ Connection lost: " + cause.getMessage());
                        reconnectClient();
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        System.out.println("📩 Message received on topic " + topic + ": " + new String(message.getPayload()));
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {}
                });

                break; // Exit loop after successful connection
            } catch (MqttException e) {
                System.err.println("⚠️ Failed to connect to MQTT Broker. Retrying...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void reconnectClient() {
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                if (!mqttClient.isConnected()) {
                    System.out.println("🔄 Attempting to reconnect...");
                    mqttClient.reconnect();
                    System.out.println("✅ Reconnected to MQTT Broker");
                }
            } catch (MqttException e) {
                System.err.println("⚠️ Reconnection failed: " + e.getMessage());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void publishMessage(String topic, String messageContent) {
        if (topic == null || messageContent == null) {
            System.err.println("⚠️ Topic or message is null. Skipping publish.");
            return;
        }

        System.out.println("🔄 Preparing to send message to BROKER  ...");
        System.out.println(" [MESSAGE] "+ messageContent);
        System.out.println(" [TOPIC] "+topic );



        try {
            if (!mqttClient.isConnected()) {
                System.out.println("🔄 MQTT Client is not connected. Attempting to reconnect...");
                reconnectClient();
            }

            MqttMessage message = new MqttMessage(messageContent.getBytes());
            message.setQos(1);
            message.setRetained(true);
            mqttClient.publish(topic, message);

            System.out.println("ℹ️ [INFO] Message published to topic: " + topic);
        } catch (MqttException e) {
            System.err.println("❌ Failed to publish message to topic: " + topic);
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void closeConnection() {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
                System.out.println("🔌 Disconnected from MQTT Broker");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        scheduler.shutdown();
    }
}
