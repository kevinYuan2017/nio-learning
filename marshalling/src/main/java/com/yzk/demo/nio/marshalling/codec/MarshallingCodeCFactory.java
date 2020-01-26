package com.yzk.demo.nio.marshalling.codec;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

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
}
