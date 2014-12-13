package ua.org.gostroy.netty_http_status.web.internal;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import ua.org.gostroy.netty_http_status.web.controller.HelloController;
import ua.org.gostroy.netty_http_status.web.controller.RedirectController;
import ua.org.gostroy.netty_http_status.web.controller.StatusController;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class FrontController {

    private Timer timer = new HashedWheelTimer();

    public static class HelloControllerHolder {
        public static final HelloController HELLO_CONTROLLER = new HelloController();
    }

    public static class RedirectControllerHolder {
        public static final RedirectController REDIRECT_CONTROLLER = new RedirectController();
    }

    public static class StatusControllerHolder {
        public static final StatusController STATUS_CONTROLLER = new StatusController();
    }

    public static class DefaultControllerHolder {
        public static final DefaultController DEFAULT_CONTROLLER = new DefaultController();
    }

    public Controller getController(HttpRequest req) {
        String uri = req.getUri().toLowerCase();
        Controller controller = DefaultControllerHolder.DEFAULT_CONTROLLER;
        controller.setUri(uri);
        if ("/hello".equals(uri)) {
            controller = HelloControllerHolder.HELLO_CONTROLLER;
            controller.setUri(uri);
        }
        if (uri.startsWith("/redirect?url=")) {
            controller = RedirectControllerHolder.REDIRECT_CONTROLLER;
            controller.setUri(uri);
        }
        if ("/status".equals(uri)) {
            controller = StatusControllerHolder.STATUS_CONTROLLER;
            controller.setUri(uri);
        }

        return controller;
    }


    public void sendResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse response) {
        if (checkHttpRequest(ctx, req)) {
            sendHttpResponse(ctx, req, response);
        }
    }

    public void sendResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse response, long delay) {
        if (checkHttpRequest(ctx, req)) {
            timer.newTimeout(new AnswerWithDelay(ctx, req, response), delay, TimeUnit.SECONDS);
        }
    }


    private boolean checkHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
        // Handle a bad request.
        if (!req.getDecoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return false;
        }

        // Allow only GET methods.
        if (req.getMethod() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return false;
        }

        return true;
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse response) {

        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        boolean keepAlive = HttpHeaders.isKeepAlive(req);
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            ctx.write(response);
        }
    }

    private class AnswerWithDelay implements TimerTask {
        private ChannelHandlerContext ctx;
        private HttpRequest req;
        private FullHttpResponse response;

        public AnswerWithDelay(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse response) {
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
