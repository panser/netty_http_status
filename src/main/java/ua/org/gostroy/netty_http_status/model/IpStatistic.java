package ua.org.gostroy.netty_http_status.model;

import java.time.LocalDateTime;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class IpStatistic {
    private String ip;
    private Long count;
    private LocalDateTime time;

    public IpStatistic(String ip, Long count, LocalDateTime time) {
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
