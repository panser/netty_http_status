package ua.org.gostroy.netty_http_status.web.internal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/11/2014.
 */
public class DefaultController extends Controller {
    private static final String CONTENT_NOT_FOUND = "Page not found";

    @Override
    public FullHttpResponse getFullHttpResponse() {
        ByteBuf CONTENT = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(CONTENT_NOT_FOUND, CharsetUtil.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, CONTENT.duplicate());
        return response;
    }
}
