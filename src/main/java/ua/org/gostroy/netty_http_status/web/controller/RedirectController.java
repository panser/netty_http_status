package ua.org.gostroy.netty_http_status.web.controller;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import ua.org.gostroy.netty_http_status.web.internal.Controller;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/11/2014.
 */
public class RedirectController extends Controller {

    @Override
    public FullHttpResponse getFullHttpResponse() {
        QueryStringDecoder qsd = new QueryStringDecoder(getUri());
        List<String> redirectUrls = qsd.parameters().get("url");
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, redirectUrls);
        return response;
    }
}
