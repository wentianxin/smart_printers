package com.qg.smpt; /**
 * Created by tisong on 7/20/16.
 */

import com.qg.smpt.printer.LifecycleException;
import com.qg.smpt.printer.PrinterConnector;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener()
public final class SmptServletListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    private final static Logger LOGGER = Logger.getLogger(SmptServletListener.class);

    public SmptServletListener() {

    }

    public void contextInitialized(ServletContextEvent sce) {

        // 项目初始化, ServletSocket 线程池

        LOGGER.log(Level.INFO, "启动 printerConnector");
        PrinterConnector printerConnector = new PrinterConnector();

        printerConnector.initialize();

        try {
            printerConnector.start();
        } catch (LifecycleException e) {
            LOGGER.log(Level.ERROR, "printerConnector 启动失败", e);
        }

    }

    public void contextDestroyed(ServletContextEvent sce) {

    }


    public void sessionCreated(HttpSessionEvent se) {

    }

    public void sessionDestroyed(HttpSessionEvent se) {

    }

    public void attributeAdded(HttpSessionBindingEvent sbe) {

    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {

    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {

    }
}
