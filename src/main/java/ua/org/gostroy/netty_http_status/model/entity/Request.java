package ua.org.gostroy.netty_http_status.model.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
@NamedQueries({
        @NamedQuery(name = "CountStatistic.find", query = "SELECT NEW ua.org.gostroy.netty_http_status.model.CountStatistic(COUNT(e.id), COUNT(DISTINCT e.ip)) FROM Request e")
        ,@NamedQuery(name = "IpStatistic.findAll", query = "SELECT NEW ua.org.gostroy.netty_http_status.model.IpStatistic(e.ip, COUNT(e.id), MAX(e.time)) FROM Request e GROUP BY e.ip")
        ,@NamedQuery(name = "RedirectStatistic.findAll", query = "SELECT NEW ua.org.gostroy.netty_http_status.model.RedirectStatistic(e.redirectUrl, COUNT(e.id)) FROM Request e WHERE e.redirectUrl <> '' GROUP BY e.redirectUrl")
        ,@NamedQuery(name = "RequestStatistic.findAll", query = "SELECT NEW ua.org.gostroy.netty_http_status.model.RequestStatistic(e.ip, e.uri, e.timestamp, e.sentBytes, e.receivedBytes, e.speed) FROM Request e ORDER BY e.time DESC")
})
@Entity
@Table(name = "Requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ip;
    private String uri = "";
    private Date time;
    private String redirectUrl = "";
    private Long receivedBytes = 0L;
    private Long sentBytes = 0L;
    private Long timestamp;
    private Long speed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }
}
