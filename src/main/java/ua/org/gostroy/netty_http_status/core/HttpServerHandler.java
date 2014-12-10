package ua.org.gostroy.netty_http_status.core;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.*;
import ua.org.gostroy.netty_http_status.dao.RequestDao;
import ua.org.gostroy.netty_http_status.service.StatusInfoService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Values;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private static Timer timer = new HashedWheelTimer();
    private static final String CONTENT_HELLO = "Hello World";
    private static final String CONTENT_NOT_FOUND = "Not Found";

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }

            handleHttpRequest(ctx, req);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req){
        // Handle a bad request.
        if (!req.getDecoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // Allow only GET methods.
        if (req.getMethod() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

        if ("/hello".equals(req.getUri().toLowerCase())) {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                    Unpooled.wrappedBuffer(Unpooled.copiedBuffer(CONTENT_HELLO, CharsetUtil.UTF_8)));
            timer.newTimeout(new HelloWorldTimerTask(ctx, req, response), 10, TimeUnit.SECONDS);
            return;
        }
        if (req.getUri().toLowerCase().startsWith("/redirect?url=")) {
            QueryStringDecoder qsd = new QueryStringDecoder(req.getUri());
            List<String> redirectUrls = qsd.parameters().get("url");
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
            response.headers().set(LOCATION, redirectUrls);
            sendHttpResponse(ctx, req, response);
            return;
        }
        if ("/status".equals(req.getUri().toLowerCase())) {
            RequestDao requestDao = new RequestDao();
            StatusInfoService statusInfoService = new StatusInfoService(requestDao);

//            CountStatistic countStatistic = statusInfo.findCountStatistic();
//            List<IpStatistic> ipStatistics = statusInfo.findIpStatistic();
//            List<RedirectStatistic> redirectStatistics = statusInfo.findRedirectStatistic();
//            List<RequestStatistic> requestStatistics = statusInfo.findRequestStatistic(16);
//            return;
        }

        // For all other request
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, Unpooled.wrappedBuffer(Unpooled.copiedBuffer(CONTENT_NOT_FOUND, CharsetUtil.UTF_8)));
        sendHttpResponse(ctx, req, response);
        return;
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse response) {
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        boolean keepAlive = HttpHeaders.isKeepAlive(req);
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.write(response);
        }
    }


    private class HelloWorldTimerTask implements TimerTask {
        private ChannelHandlerContext ctx;
        private HttpRequest req;
        private FullHttpResponse response;

        public HelloWorldTimerTask(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse response) {
            this.ctx = ctx;
            this.req = req;
            this.response = response;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            sendHttpResponse(ctx, req, response);
            ctx.flush();
        }

    }
}
