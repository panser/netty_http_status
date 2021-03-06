package ua.org.gostroy.netty_http_status.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import ua.org.gostroy.netty_http_status.dao.RequestDao;
import ua.org.gostroy.netty_http_status.model.entity.Request;
import ua.org.gostroy.netty_http_status.server.handler.HttpServerHandler;
import ua.org.gostroy.netty_http_status.server.handler.statistic.StatisticInHttpHandler;
import ua.org.gostroy.netty_http_status.server.handler.statistic.StatisticInRawHandler;
import ua.org.gostroy.netty_http_status.server.handler.statistic.StatisticOutHttpHandler;
import ua.org.gostroy.netty_http_status.service.RequestService;
import ua.org.gostroy.netty_http_status.web.internal.FrontController;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.concurrent.atomic.AtomicLong;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    public static EntityManager em = Persistence.createEntityManagerFactory("h2").createEntityManager();
    public static RequestDao requestDao = new RequestDao();
    public static RequestService requestService = new RequestService();
    public static FrontController frontController = new FrontController();
    public static final String WEB_CONTENT_PATH = "/web";
    public static String TEMPLATE_SUFFIX = ".vm";
    public static AtomicLong openConnections = new AtomicLong(0);
    public static final AttributeKey<Request> REQUEST_KEY = AttributeKey.valueOf("requestKey");

    private final SslContext sslCtx;

    public HttpServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        EventExecutorGroup dbGroup = new DefaultEventExecutorGroup(50);
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast("statisticInRawHandler", new StatisticInRawHandler());
        p.addLast(new HttpServerCodec());
        p.addLast("http-aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
        p.addLast(new HttpContentCompressor());
        p.addLast("statisticInHttpHandler", new StatisticInHttpHandler());
        p.addLast(dbGroup, "statisticOutHttpHandler", new StatisticOutHttpHandler());
        p.addLast("mainHandler", new HttpServerHandler());

        ch.attr(HttpServerInitializer.REQUEST_KEY).set(new Request());
    }
}
