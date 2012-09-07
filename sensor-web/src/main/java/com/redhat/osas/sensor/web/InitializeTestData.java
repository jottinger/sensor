package com.redhat.osas.sensor.web;

import com.redhat.osas.sensor.data.DataPoint;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitializeTestData implements ServletContextListener {
    @Resource(lookup = "java:comp/env/sensorData")
    private CacheContainer container;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Cache<String, DataPoint> cache = container.getCache("sensorData");
        DataPoint dp = new DataPoint("9195551212", 35.773371, -78.67743, 154, 255);
        cache.put(dp.getDeviceId(), dp);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
