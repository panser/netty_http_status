package ua.org.gostroy.netty_http_status.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import ua.org.gostroy.netty_http_status.model.entity.Request;

/**
 * Created by Panov Sergey on 12/12/2014.
 */
public class StatisticOutRawHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Request request = (Request) ctx.channel().attr(HttpServerInitializer.REQUEST_KEY).get();

        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            int size = byteBuf.readableBytes();
            request.setSentBytes((long) size);

            request.setSentBytes((long) size);
            Long duration = System.currentTimeMillis() - request.getTime().getTime();
            request.setTimestamp(duration);

            Long speed = 1000 * (request.getReceivedBytes() + request.getSentBytes()) / request.getTimestamp();
            request.setSpeed(speed);

            HttpServerInitializer.requestService.save(request);
        }

        super.write(ctx, msg, promise);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
