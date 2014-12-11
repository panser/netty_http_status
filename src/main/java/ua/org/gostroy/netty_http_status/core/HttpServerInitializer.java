package ua.org.gostroy.netty_http_status.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import ua.org.gostroy.netty_http_status.dao.RequestDao;
import ua.org.gostroy.netty_http_status.service.RequestService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    public static EntityManager em = Persistence.createEntityManagerFactory("mysql").createEntityManager();
    public static RequestDao requestDao = new RequestDao();
    public static RequestService requestService = new RequestService();
    public static final String WEB_CONTENT_PATH = "/web";
    public static String TEMPLATE_SUFFIX = ".vm";

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
        p.addLast(new HttpServerCodec());
        p.addLast("statisticHandler", new StatisticHandler(AbstractTrafficShapingHandler.DEFAULT_CHECK_INTERVAL));
        p.addLast("mainHandler", new HttpServerHandler());
    }
}
