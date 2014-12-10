package ua.org.gostroy.netty_http_status.service;

import ua.org.gostroy.netty_http_status.core.HttpServerInitializer;
import ua.org.gostroy.netty_http_status.dao.RequestDao;
import ua.org.gostroy.netty_http_status.model.CountStatistic;
import ua.org.gostroy.netty_http_status.model.IpStatistic;
import ua.org.gostroy.netty_http_status.model.RedirectStatistic;
import ua.org.gostroy.netty_http_status.model.RequestStatistic;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class StatusInfoService {

    private RequestDao requestDao;

    public StatusInfoService(RequestDao requestDao) {
        this.requestDao = requestDao;
        requestDao.setEm(HttpServerInitializer.em);
    }

    public CountStatistic findCountStatistic(){
        CountStatistic countStatistic = requestDao.findCountStatistic();
        return countStatistic;
    }

    public List<IpStatistic> findIpStatistic(){
        List<IpStatistic> ipStatistics = requestDao.findIpStatistic();
        return ipStatistics;
    }

    public List<RedirectStatistic> findRedirectStatistic(){
        List<RedirectStatistic> redirectStatistics = requestDao.findRedirectStatistic();
        return redirectStatistics;
    }

    public List<RequestStatistic> findRequestStatistic(Integer count){
        List<RequestStatistic> requestStatistics = requestDao.findRequestStatistic(16);
        return requestStatistics;
    }
}
