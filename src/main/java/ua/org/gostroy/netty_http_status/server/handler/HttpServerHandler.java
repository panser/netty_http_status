package ua.org.gostroy.netty_http_status.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import ua.org.gostroy.netty_http_status.server.HttpServerInitializer;
import ua.org.gostroy.netty_http_status.web.internal.Controller;
import ua.org.gostroy.netty_http_status.web.internal.FrontController;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

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

            FrontController frontController = HttpServerInitializer.frontController;

            Controller controller = frontController.getController(req);
            FullHttpResponse response = controller.getFullHttpResponse();

            if("/hello".equals(req.getUri().toLowerCase())) {
                frontController.sendResponse(ctx, req, response, 10);
            }else{
                frontController.sendResponse(ctx, req, response);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }



}
