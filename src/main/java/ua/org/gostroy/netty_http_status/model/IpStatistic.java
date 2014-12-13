package ua.org.gostroy.netty_http_status.model;

import java.util.Date;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class IpStatistic {
    private String ip;
    private Long count;
    private Date time;

    public IpStatistic(String ip, Long count, Date time) {
        this.ip = ip;
        this.count = count;
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
