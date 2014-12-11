package ua.org.gostroy.netty_http_status.web.controller;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import ua.org.gostroy.netty_http_status.core.HttpServerInitializer;
import ua.org.gostroy.netty_http_status.service.StatusInfoService;
import ua.org.gostroy.netty_http_status.web.internal.Controller;

import java.io.*;
import java.util.Properties;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/11/2014.
 */
public class StatusController extends Controller {

    @Override
    public FullHttpResponse getFullHttpResponse() {
        StatusInfoService statusInfoService = new StatusInfoService();

        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine engine = new VelocityEngine(properties);

        VelocityContext context = new VelocityContext();
        context.put("countStatistic", statusInfoService.findCountStatistic());
        context.put("ipStatistics", statusInfoService.findIpStatistic());
        context.put("redirectStatistics", statusInfoService.findRedirectStatistic());
        context.put("requestStatistics", statusInfoService.findRequestStatistic(16));

        Template t = engine.getTemplate(HttpServerInitializer.WEB_CONTENT_PATH + getUri() + HttpServerInitializer.TEMPLATE_SUFFIX);

        StringWriter swOut = new StringWriter();
        t.merge(context, swOut);

        ByteBuf CONTENT = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(swOut.toString(), CharsetUtil.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, CONTENT.duplicate());
        return response;
    }
}
