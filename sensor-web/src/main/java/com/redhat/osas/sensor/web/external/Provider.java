package com.redhat.osas.sensor.web.external;

import com.redhat.osas.sensor.data.DataPoint;
import org.codehaus.jackson.map.ObjectMapper;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Provider {
    ObjectMapper mapper = new ObjectMapper();

    <K, V> Cache<K, V> getCache(String name) {
        try {
            Context ctx = new InitialContext();
            CacheContainer container = (CacheContainer) ctx.lookup(name);
            return container.getCache("dataPoints");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    public Map<String, DataPoint> getData() {
        Map<String, DataPoint> map = new HashMap<>();
        Cache<String, DataPoint> cache = getCache("java:/jboss/infinispan/dataPoints");
        // we do this to mangle the device ids for security.
        for (Map.Entry<String, DataPoint> entry : cache.entrySet()) {
            DataPoint oldValue = entry.getValue();
            String newDeviceId = Integer.toString(oldValue.getDeviceId().hashCode() % 10000);
            DataPoint value = new DataPoint(newDeviceId,
                    oldValue.getLatitude(), oldValue.getLongitude(),
                    oldValue.getLevel(), oldValue.getMaxLevel(), oldValue.getTimestamp());
            map.put(entry.getKey(), value);
        }

        return map;
    }

    public Set<String> getKeys() {
        Cache<String, DataPoint> cache = getCache("java:/jboss/infinispan/dataPoints");
        return cache.keySet();
    }

    public void store(String key, String data) {
        Cache<String, DataPoint> cache = getCache("java:/jboss/infinispan/dataPoints");
        DataPoint dp = new DataPoint(data, 101.0, 12.12, 24, 255);
        cache.put(key, dp);
    }

    public String load(String key) {
        Cache<String, DataPoint> cache = getCache("java:/jboss/infinispan/dataPoints");
        DataPoint dp = cache.get(key);
        if (dp != null) {
            try {
                return mapper.writeValueAsString(dp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
