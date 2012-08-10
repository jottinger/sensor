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
        /*for(String key:cache.keySet()) {
            map.put(key, cache.get(key));
        }
        */
        map.putAll(cache);
        return map;
    }

    public Set<String> getKeys() {
        Cache<String, DataPoint> cache = getCache("java:/jboss/infinispan/dataPoints");
        return cache.keySet();
    }

    public void store(String key, String data) {
        Cache<String, DataPoint> cache = getCache("java:/jboss/infinispan/dataPoints");
        DataPoint dp = new DataPoint();
        dp.setDeviceId(data);
        dp.setLevel(10l);
        dp.setLatitude(12.12);
        dp.setLongitude(24.24);
        dp.setTimestamp(System.currentTimeMillis());
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
