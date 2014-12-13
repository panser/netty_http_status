package ua.org.gostroy.netty_http_status.web.internal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public abstract class Controller {
    private String uri;
    private ChannelHandlerContext ctx;
    private HttpRequest req;

    public abstract FullHttpResponse getFullHttpResponse();

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
