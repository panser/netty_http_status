package ua.org.gostroy.netty_http_status.web.internal;

import javafx.scene.control.Control;
import ua.org.gostroy.netty_http_status.web.controller.HelloController;
import ua.org.gostroy.netty_http_status.web.controller.RedirectController;
import ua.org.gostroy.netty_http_status.web.controller.StatusController;

/**
 * Created by Panov Sergey on 12/10/2014.
 */
public class ControllerFactory {
    public static class HelloControllerHolder{
        public static final HelloController HELLO_CONTROLLER = new HelloController();
    }
    public static class RedirectControllerHolder{
        public static final RedirectController REDIRECT_CONTROLLER = new RedirectController();
    }
    public static class StatusControllerHolder{
        public static final StatusController STATUS_CONTROLLER = new StatusController();
    }
    public static class DefaultControllerHolder{
        public static final DefaultController DEFAULT_CONTROLLER = new DefaultController();
    }

    public static Controller getController(String uri){
        Controller controller = DefaultControllerHolder.DEFAULT_CONTROLLER;
        if ("/hello".equals(uri)) {
            controller = HelloControllerHolder.HELLO_CONTROLLER;
        }
        if (uri.startsWith("/redirect?url=")) {
            controller = RedirectControllerHolder.REDIRECT_CONTROLLER;
            controller.setUri(uri);
        }
        if ("/status".equals(uri)) {
            controller = StatusControllerHolder.STATUS_CONTROLLER;
        }

        return controller;
    }
}
