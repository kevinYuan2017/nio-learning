package com.yzk.demo.nio.nettyprotocol.codec.codec;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.jboss.marshalling.*;

import java.io.IOException;

public final class MarshallingCodeCFactory {
    private static final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
    private static final MarshallingConfiguration configuration = new MarshallingConfiguration();

    static {
        configuration.setVersion(5);
    }
    /**
     * 创建JBoss Marshalling解码器MarshallingDecoder
     * @return
     * */
    public static MarshallingDecoder buildMarshallingDecoder() {
        return new MarshallingDecoder(new DefaultUnmarshallerProvider(marshallerFactory, configuration));
    }

    /**
     * 创建JBoss Marshalling编码器MarshallingEncoder
     * @return
     * */
    public static MarshallingEncoder buildMarshallingEncoder() {
        return new MarshallingEncoder(new DefaultMarshallerProvider(marshallerFactory, configuration));
    }

    public static Marshaller buildMarshalling() throws IOException {
        return marshallerFactory.createMarshaller(configuration);
    }

    public static Unmarshaller buildUnmarshalling() throws IOException {
        return marshallerFactory.createUnmarshaller(configuration);
    }
}
