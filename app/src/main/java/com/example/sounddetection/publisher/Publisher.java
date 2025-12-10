package com.example.sounddetection.publisher;

import android.widget.TextView;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import java.util.UUID;

public class Publisher {
    // Configurações MQTT
    private final String MQTT_BROKER = "broker.hivemq.com";  // ou seu broker
    private final int MQTT_PORT = 1883;
    private final String MQTT_TOPIC = "ufg/2025/lightlevel";

    private TextView sensorField;
    public Publisher(TextView tv)
    {
        sensorField = tv;
    }

    public void publishSensor(){
        Mqtt5BlockingClient client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(MQTT_BROKER)
                .buildBlocking();
        client.connect();
        client.publishWith()
                .topic(MQTT_TOPIC)
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(sensorField.getText().toString().getBytes())
                .send();
        client.disconnect();
    }
}
