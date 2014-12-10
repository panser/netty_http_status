package ua.org.gostroy.netty_http_status.model;


/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class CountStatistic {
    private Long totalRequest;
    private Long uniqRequest;

    public CountStatistic() {
    }

    public CountStatistic(Long totalRequest, Long uniqRequest) {
        this.totalRequest = totalRequest;
        this.uniqRequest = uniqRequest;
    }

    public Long getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(Long totalRequest) {
        this.totalRequest = totalRequest;
    }

    public Long getUniqRequest() {
        return uniqRequest;
    }

    public void setUniqRequest(Long uniqRequest) {
        this.uniqRequest = uniqRequest;
    }
}
