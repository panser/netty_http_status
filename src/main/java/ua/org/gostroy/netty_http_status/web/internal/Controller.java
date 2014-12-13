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

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public abstract class Controller {
    private String uri;
    private ChannelHandlerContext ctx;
    private HttpRequest req;

    public abstract FullHttpResponse getFullHttpResponse();

    public void sendResponse(FullHttpResponse response){
        if(checkHttpRequest(ctx, req)){
            sendHttpResponse(ctx, req, response);
        }
    }

    protected boolean checkHttpRequest(ChannelHandlerContext ctx, HttpRequest req){
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

    public class AnswerWithDelay implements TimerTask {
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


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public HttpRequest getReq() {
        return req;
    }

    public void setReq(HttpRequest req) {
        this.req = req;
    }
}
