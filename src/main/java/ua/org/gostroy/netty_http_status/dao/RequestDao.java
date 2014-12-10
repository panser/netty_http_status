package ua.org.gostroy.netty_http_status.dao;

import ua.org.gostroy.netty_http_status.model.CountStatistic;
import ua.org.gostroy.netty_http_status.model.IpStatistic;
import ua.org.gostroy.netty_http_status.model.RedirectStatistic;
import ua.org.gostroy.netty_http_status.model.RequestStatistic;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class RequestDao {

    private EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public CountStatistic findCountStatistic(){
        Query query = em.createNamedQuery("CountStatistic.find");
        CountStatistic countStatistic = (CountStatistic) query.getSingleResult();
        return countStatistic;
    }

    public List<IpStatistic> findIpStatistic(){
        Query query = em.createNamedQuery("IpStatistic.findAll");
        List<IpStatistic> ipStatistics = (List<IpStatistic>) query.getResultList();
        return ipStatistics;
    }

    public List<RedirectStatistic> findRedirectStatistic(){
        Query query = em.createNamedQuery("RedirectStatistic.findAll");
        List<RedirectStatistic> redirectStatistics = (List<RedirectStatistic>) query.getResultList();
        return redirectStatistics;
    }

    public List<RequestStatistic> findRequestStatistic(Integer count){
        Query query = em.createNamedQuery("RequestStatistic.findAll");
        query.setMaxResults(count);
        List<RequestStatistic> requestStatistics = (List<RequestStatistic>) query.getResultList();
        return requestStatistics;
    }

}
