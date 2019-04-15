package com.kevin.mqtt.mqttserver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;

public class MqttHandler extends SimpleChannelInboundHandler<MqttMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {
        System.out.println(mqttMessage.payload());

//        MqttMessage pubRecMessage = MqttMessageFactory.newMessage(
//                new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 2),
//                MqttMessageIdVariableHeader.from(variableHeader.messageId()),
//                null);
    }
}
