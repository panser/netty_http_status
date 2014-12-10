package ua.org.gostroy.netty_http_status.model;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class RequestStatistic {
    private String ip;
    private String uri;
    private Long timestamp;
    private Long sentBytes;
    private Long receivedBytes;
    private Long speed;

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Long sentBytes) {
        this.sentBytes = sentBytes;
    }

    public Long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }
}
