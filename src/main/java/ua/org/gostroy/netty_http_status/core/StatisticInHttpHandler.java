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
public class StatisticInHttpHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) ctx.channel().attr(HttpServerInitializer.REQUEST_KEY).get();

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;

            request.setUri(req.getUri());
            QueryStringDecoder qsd = new QueryStringDecoder(req.getUri());
            List<String> redirectUrls = qsd.parameters().get("url");
            request.setRedirectUrl((redirectUrls != null) ? redirectUrls.get(0) : "");
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
