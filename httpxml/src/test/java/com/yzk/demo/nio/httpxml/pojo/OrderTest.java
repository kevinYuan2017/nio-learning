package com.yzk.demo.nio.httpxml.pojo;

import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class OrderTest {
    private IBindingFactory factory = null;
    private StringWriter writer = null;
    private StringReader reader = null;
    private final static String CHARSET_NAME = "UTF-8";
    private String encode2Xml(Order order) throws JiBXException, IOException {
        factory = BindingDirectory.getFactory(Order.class);
        writer = new StringWriter();
        IMarshallingContext marshallingContext = factory.createMarshallingContext();
        marshallingContext.setIndent(2);
        marshallingContext.marshalDocument(order, CHARSET_NAME,null, writer);
        String xmlString = writer.toString();
        writer.close();
        writer = null;
        System.out.println(xmlString);
        return xmlString;
    }

    private Order decoder2Order(String xmlString) throws JiBXException {
        reader = new StringReader(xmlString);
        IUnmarshallingContext unmarshallingContext = factory.createUnmarshallingContext();
        return (Order) unmarshallingContext.unmarshalDocument(reader);
    }

    public static void main(String[] args) throws JiBXException, IOException {
        OrderTest orderTest = new OrderTest();
        Order order1 = Order.create(123);
        String xmlString = orderTest.encode2Xml(order1);
        Order order2 = orderTest.decoder2Order(xmlString);
        System.out.println(order2);
    }
}
