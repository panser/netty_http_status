package ua.org.gostroy.netty_http_status.service;

import ua.org.gostroy.netty_http_status.core.HttpServerInitializer;
import ua.org.gostroy.netty_http_status.dao.RequestDao;
import ua.org.gostroy.netty_http_status.model.entity.Request;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class RequestService {

    private RequestDao requestDao;

    public RequestService() {
        requestDao = HttpServerInitializer.requestDao;
        requestDao.setEm(HttpServerInitializer.em);
    }

    public Request save(Request request){
        Request requestNew = requestDao.save(request);
        return requestNew;
    }
}
