package ua.org.gostroy.netty_http_status.model;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class RedirectStatistic {
    private String redirectUrl;
    private Long count;

    public RedirectStatistic(String redirectUrl, Long count) {
        this.redirectUrl = redirectUrl;
        this.count = count;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
