package ua.org.gostroy.netty_http_status.web.internal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import ua.org.gostroy.netty_http_status.core.HttpServerInitializer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Panov Sergey on 12/11/2014.
 */
public class DefaultController extends Controller {
    private static final String CONTENT_NOT_FOUND = "Page not found";

    @Override
    public FullHttpResponse getFullHttpResponse() {
        String page_content;

        try{
            VelocityContext context = new VelocityContext();
            ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();
            InputStream inputStream = classpathResourceLoader.getResourceStream(HttpServerInitializer.WEB_CONTENT_PATH + getUri());
            Reader templateReader = new InputStreamReader(inputStream);

            StringWriter swOut = new StringWriter();
            Velocity.evaluate(context, swOut, "LOG", templateReader);
            page_content = swOut.toString();
        }catch (Exception e){
            page_content = CONTENT_NOT_FOUND;
        }


        ByteBuf CONTENT = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(page_content, CharsetUtil.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, CONTENT.duplicate());
        return response;
    }
}
