package com.redhat.osas.sensor.web.external;

import com.redhat.osas.sensor.data.DataPoint;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/data")
public class Provider {
    @Resource(lookup = "java:comp/env/sensorData")
    private CacheContainer container;

    @SuppressWarnings("UnusedDeclaration")
    @GET
    public List<DataPoint> getData() {
        List<DataPoint> dataPoints = new ArrayList<>();
        Cache<String, DataPoint> cache = container.getCache("sensorData");

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

    private static final Comparator<DataPoint> comparator = new Comparator<DataPoint>() {
        @Override
        public int compare(DataPoint dataPoint, DataPoint dataPoint1) {
            return (int) (Math.signum(1.0 * dataPoint.getTimestamp() -
                    dataPoint1.getTimestamp()));
        }
    };

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void submitData(List<DataPoint> dp) {
        Collections.sort(dp, comparator);
        Cache<String, DataPoint> cache =
                container.getCache("sensorData");
        for (DataPoint dataPoint : dp) {
            cache.put(dataPoint.getDeviceId(), dataPoint);
        }
    }
}
