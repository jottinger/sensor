package com.redhat.osas.sensor.web.external;

import com.redhat.osas.sensor.data.DataPoint;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

public class Provider {
    <K, V> Cache<K, V> getCache(String name) {
        try {
            Context ctx = new InitialContext();
            CacheContainer container = (CacheContainer) ctx.lookup(name);
            return container.getCache("sensorData");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public Map<String, DataPoint> getData() {
        Map<String, DataPoint> map = new HashMap<>();
        Cache<String, DataPoint> cache = getCache("sensorData");
        // we do this to mangle the device ids for security.
        int counter = 1;
        for (Map.Entry<String, DataPoint> entry : cache.entrySet()) {
            DataPoint oldValue = entry.getValue();
            String newDeviceId = Integer.toString(oldValue.getDeviceId().hashCode() % 10000);
            DataPoint value = new DataPoint(newDeviceId,
                    oldValue.getLatitude(), oldValue.getLongitude(),
                    oldValue.getLevel(), oldValue.getMaxLevel(), oldValue.getTimestamp());
            map.put(Integer.toString(counter++), value);
        }

        return map;
    }
}
