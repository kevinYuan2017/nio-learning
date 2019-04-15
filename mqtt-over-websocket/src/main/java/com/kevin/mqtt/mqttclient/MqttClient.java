package com.kevin.mqtt.mqttclient;

import io.netty.handler.codec.mqtt.*;

public class MqttClient {
    public static void main(String[] args) throws Exception {
        new MqttClient();
    }

    public void connect(String host, int port) throws Exception {
        MqttConnectMessage mqttConnectMessage = new MqttConnectMessage(new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.EXACTLY_ONCE, false, "pass".length()), null, new MqttConnectPayload("client1", "Topic-test", "helo".getBytes(), "user1", "pass".getBytes()));
    }
}
