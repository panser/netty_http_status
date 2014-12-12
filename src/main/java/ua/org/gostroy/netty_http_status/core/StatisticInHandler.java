package ua.org.gostroy.netty_http_status.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.AttributeKey;
import ua.org.gostroy.netty_http_status.model.entity.Request;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Panov Sergey on 12/12/2014.
 */
public class StatisticInHandler extends ChannelInboundHandlerAdapter {

    private Request request = new Request();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        request.setIp(((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress());

        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        request.setTime(new Date());

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;

            request.setUri(req.getUri());
            QueryStringDecoder qsd = new QueryStringDecoder(req.getUri());
            List<String> redirectUrls = qsd.parameters().get("url");
            request.setRedirectUrl((redirectUrls != null) ? redirectUrls.get(0) : "");

            HttpContent content = (HttpContent) msg;
            ByteBuf byteBuf = content.content();
            int size = byteBuf.readableBytes();
            request.setReceivedBytes((long) size);

            ctx.channel().attr(HttpServerInitializer.REQUEST_KEY).set(request);
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
