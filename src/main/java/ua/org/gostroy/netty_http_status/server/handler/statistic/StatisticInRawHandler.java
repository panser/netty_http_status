package ua.org.gostroy.netty_http_status.server.handler.statistic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ua.org.gostroy.netty_http_status.model.entity.Request;
import ua.org.gostroy.netty_http_status.server.HttpServerInitializer;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * Created by Panov Sergey on 12/12/2014.
 */
public class StatisticInRawHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Request request = (Request) ctx.channel().attr(HttpServerInitializer.REQUEST_KEY).get();

        request.setIp(((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress());


        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) ctx.channel().attr(HttpServerInitializer.REQUEST_KEY).get();

        request.setTime(new Date());

        if(msg instanceof ByteBuf){
            ByteBuf byteBuf = (ByteBuf) msg;
            int size = byteBuf.readableBytes();
            request.setReceivedBytes((long) size);
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
