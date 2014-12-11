package ua.org.gostroy.netty_http_status.web.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.*;
import ua.org.gostroy.netty_http_status.web.internal.Controller;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class HelloController extends Controller {

    private final String CONTENT_HELLO = "Hello World";
    private Timer timer = new HashedWheelTimer();

    @Override
    public FullHttpResponse getFullHttpResponse() {

        ByteBuf CONTENT = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(CONTENT_HELLO, CharsetUtil.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, CONTENT.duplicate());
        return response;
    }

    @Override
    public void sendResponse(FullHttpResponse response) {
        if(checkHttpRequest(getCtx(), getReq())){
            timer.newTimeout(new AnswerWithDelay(getCtx(), getReq(), response), 10, TimeUnit.SECONDS);
        }
    }
}
