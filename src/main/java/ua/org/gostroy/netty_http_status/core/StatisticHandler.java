package ua.org.gostroy.netty_http_status.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.AttributeKey;
import ua.org.gostroy.netty_http_status.model.entity.Request;
import ua.org.gostroy.netty_http_status.service.RequestService;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class StatisticHandler extends ChannelTrafficShapingHandler {

    private Request request = new Request();
    private long startTime;

    public StatisticHandler(long checkInterval) {
        super(checkInterval);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        startTime = System.currentTimeMillis();
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            request.setUri(req.getUri());

            QueryStringDecoder qsd = new QueryStringDecoder(req.getUri());
            List<String> redirectUrls = qsd.parameters().get("url");
            request.setRedirectUrl((redirectUrls != null) ? redirectUrls.get(0) : "");
        }

        request.setIp(((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress());
        request.setTime(LocalDateTime.now());
        request.setTimestamp(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        TrafficCounter tc = trafficCounter();
        request.setReceivedBytes(tc.cumulativeReadBytes());
        request.setSentBytes(tc.cumulativeWrittenBytes());
        request.setSpeed(tc.lastWriteThroughput());

        HttpServerInitializer.requestService.save(request);

        super.channelRead(ctx, msg);
    }

}
