package com.yzk.demo.nio.httpxml.codec.abs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.ReferenceCountUtil;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Ref;

public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
    private IBindingFactory factory = null;
    private StringWriter writer = null;
    private final static String CHARSET_NAME = "UTF-8";
    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) throws Exception {
        factory = BindingDirectory.getFactory(body.getClass());
        writer = new StringWriter();
        IMarshallingContext marshallingContext = factory.createMarshallingContext();
        marshallingContext.setIndent(2);
        marshallingContext.marshalDocument(body, CHARSET_NAME, null, writer);
        String xmlString = writer.toString();
        writer.close();
        writer = null;
        return Unpooled.copiedBuffer(xmlString, UTF_8);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}
