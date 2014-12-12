package ua.org.gostroy.netty_http_status.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import ua.org.gostroy.netty_http_status.model.entity.Request;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by Panov Sergey on 12/12/2014.
 */
public class StatisticOutHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;

            HttpContent content = (HttpContent) msg;
            ByteBuf byteBuf = content.content();
            int size = byteBuf.readableBytes();

            Request request = (Request) ctx.channel().attr(HttpServerInitializer.REQUEST_KEY).get();
            request.setSentBytes((long)size);
            Long duration= System.currentTimeMillis() - request.getTime().getTime();
            request.setTimestamp(duration);

            HttpServerInitializer.requestService.save(request);
        }

        super.write(ctx, msg, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
