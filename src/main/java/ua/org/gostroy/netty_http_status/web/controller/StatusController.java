package ua.org.gostroy.netty_http_status.web.controller;

import io.netty.handler.codec.http.FullHttpResponse;
import ua.org.gostroy.netty_http_status.service.StatusInfoService;
import ua.org.gostroy.netty_http_status.web.internal.Controller;

/**
 * Created by Panov Sergey on 12/11/2014.
 */
public class StatusController extends Controller {

    @Override
    public FullHttpResponse getFullHttpResponse() {
        StatusInfoService statusInfoService = new StatusInfoService();

//        CountStatistic countStatistic = statusInfo.findCountStatistic();
//        List<IpStatistic> ipStatistics = statusInfo.findIpStatistic();
//        List<RedirectStatistic> redirectStatistics = statusInfo.findRedirectStatistic();
//        List<RequestStatistic> requestStatistics = statusInfo.findRequestStatistic(16);
        return null;
    }
}
