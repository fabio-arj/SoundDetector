package com.example.sounddetection.publisher

import android.widget.TextView
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import java.util.UUID

class SensorPublisher() {

    private val MQTT_BROKER = "broker.hivemq.com"
    private val MQTT_PORT = 1883
    private val MQTT_TOPIC = "ufg/2025/soundlevel"

    fun publishSensor(value: String) {
        val client = Mqtt5Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(MQTT_BROKER)
            .serverPort(MQTT_PORT)
            .buildBlocking()

        client.connect()

        client.publishWith()
            .topic(MQTT_TOPIC)
            .qos(MqttQos.AT_LEAST_ONCE)
            .payload(value.toByteArray())
            .send()

        client.disconnect()
    }
}
