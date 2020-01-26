package com.yzk.demo.nio.httpfileserver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HttpFileServerHanlder extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = Logger.getLogger(HttpFileServerHanlder.class.getName());
    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
//        LOGGER.info("request-uri: " + msg.uri() + ", url: " + url + ", request method: " + msg.method());
        if (!msg.decoderResult().isSuccess()) {
            respError(ctx, HttpResponseStatus.BAD_REQUEST, "请求参数有误!");
        }else if (!msg.method().equals(HttpMethod.GET)) {
            respError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED, "不支持GET以外的请求方式!");
        }else {
            String path = sanitizeUri(msg.uri());
            if ( path == null) {
                respError(ctx, HttpResponseStatus.FORBIDDEN, "路径禁止访问!");
                return;
            }
//            LOGGER.info("正常访问开始! path: " + path);
            File file = new File(path);
            if (file.isHidden() || !file.exists()) {
                respError(ctx, HttpResponseStatus.NOT_FOUND, "目录或文件不存在!");
                return;
            }

            if (!file.isFile()) {
                respList(ctx, file);
                return;
            }

            RandomAccessFile randomAccessFile = null;

            try {
                randomAccessFile = new RandomAccessFile(file, "r"); // 只读的方式打开文件

            }catch (FileNotFoundException e) {
                respError(ctx, HttpResponseStatus.NOT_FOUND, "路径或文件不存在!");
                return;
            }

            long fileLength = randomAccessFile.length();
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, fileLength);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, new MimetypesFileTypeMap().getContentType(file));
            if (HttpUtil.isKeepAlive(msg)) response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(response);

            ChannelFuture sendFileFuture;
            sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
            sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                    if(total<0){
                        LOGGER.info("Transfer progress: "+progress);
                    }else if (progress % 100 == 0) {
                        LOGGER.info("Transfer progress: "+progress+"/"+total);
                    }
                }

                public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                    LOGGER.info("Transfer complete.");
                }
            });

            //如果使用chunked编码，最后需要发送一个编码结束的空消息体，将LastHttpContent.EMPTY_LAST_CONTENT发送到缓冲区中，
            //来标示所有的消息体已经发送完成，同时调用flush方法将发送缓冲区中的消息刷新到SocketChannel中发送
            ChannelFuture lastContentFuture = ctx.
                    writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            //如果是非keepAlive的，最后一包消息发送完成后，服务端要主动断开连接
            if(!HttpUtil.isKeepAlive(msg)){
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private void respError(ChannelHandlerContext ctx, HttpResponseStatus responseStatus, String errMsg) {
        DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, responseStatus, Unpooled.copiedBuffer(errMsg.getBytes()));
        resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        resp.headers().set(HttpHeaderNames.CONTENT_LENGTH, errMsg.getBytes().length);
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private void respList(ChannelHandlerContext ctx, File dir) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        buf.append("<html><body><ul>");
        buf.append("<li>链接: <a href='" + dir.getParent() + "'>..</a></li>");
        for (File f : dir.listFiles()) {
            if (!ALLOWED_FILE_NAME.matcher(f.getName()).matches()) continue;
//            LOGGER.info("f.getName: " + f.getName());
            buf.append("<li>链接: <a href='" + f.getAbsolutePath() + "'>" + f.getName() + "</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        ByteBuf content = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(content);
        content.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            LOGGER.info(e.getMessage());
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            }catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator + '.')
                || uri.contains('.' + File.separator)
                || uri.startsWith(".")
                || uri.endsWith(".")
                || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }

        return "/".equals(uri) ? System.getProperty("user.dir") : uri.startsWith("/") ? uri : System.getProperty("user.dir") + uri;
    }
}
