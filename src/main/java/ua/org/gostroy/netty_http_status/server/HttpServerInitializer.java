package ua.org.gostroy.netty_http_status.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.util.AttributeKey;
import ua.org.gostroy.netty_http_status.dao.RequestDao;
import ua.org.gostroy.netty_http_status.model.entity.Request;
import ua.org.gostroy.netty_http_status.server.handler.HttpServerHandler;
import ua.org.gostroy.netty_http_status.server.handler.statistic.StatisticInHttpHandler;
import ua.org.gostroy.netty_http_status.server.handler.statistic.StatisticInRawHandler;
import ua.org.gostroy.netty_http_status.server.handler.statistic.StatisticOutRawHandler;
import ua.org.gostroy.netty_http_status.service.RequestService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    public static EntityManager em = Persistence.createEntityManagerFactory("mysql").createEntityManager();
    public static RequestDao requestDao = new RequestDao();
    public static RequestService requestService = new RequestService();
    public static final String WEB_CONTENT_PATH = "/web";
    public static String TEMPLATE_SUFFIX = ".vm";
    public static final AttributeKey<Request> REQUEST_KEY = AttributeKey.valueOf("requestKey");

    private final SslContext sslCtx;

    public HttpServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast("statisticInRawHandler", new StatisticInRawHandler());
        p.addLast("statisticOutRawHandler", new StatisticOutRawHandler());
        p.addLast(new HttpServerCodec());
        p.addLast( "http-aggregator", new HttpObjectAggregator( Integer.MAX_VALUE ) );
        p.addLast("statisticInHttpHandler", new StatisticInHttpHandler());
        p.addLast("mainHandler", new HttpServerHandler());

        Request request = new Request();
        ch.attr(HttpServerInitializer.REQUEST_KEY).set(request);
    }
}
