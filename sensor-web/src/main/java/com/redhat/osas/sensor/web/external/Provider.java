package com.redhat.osas.sensor.web.external;

import com.redhat.osas.sensor.data.DataPoint;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/data")
public class Provider {
    @Resource(lookup = "java:comp/env/sensorData")
    private CacheContainer container;

    <K, V> Cache<K, V> getCache(String name) {
        return container.getCache(name);
    }

    @SuppressWarnings("UnusedDeclaration")
    @GET
    public List<DataPoint> getData() {
        List<DataPoint> dataPoints = new ArrayList<>();
        Cache<String, DataPoint> cache = getCache("sensorData");

        // we do this to mangle the device ids for security.
        for (DataPoint oldValue : cache.values()) {
            String newDeviceId = Integer.toString(oldValue.getDeviceId().hashCode() % 10000);
            DataPoint value = new DataPoint(newDeviceId,
                    oldValue.getLatitude(), oldValue.getLongitude(),
                    oldValue.getLevel(), oldValue.getMaxLevel(), oldValue.getTimestamp());
            dataPoints.add(value);
        }

        return dataPoints;
    }
}
