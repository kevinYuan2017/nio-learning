package com.yzk.demo.nio.nettyprotocol.codec.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

public class MarshallingDecoder {
    private final Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException {
        this.unmarshaller = MarshallingCodeCFactory.buildUnmarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception {
        int i = in.readerIndex();
        int objectSize = in.readableBytes();
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
        try {
            unmarshaller.start(input);
            Object object = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return object;
        }finally {
            unmarshaller.close();
        }
    }
}
