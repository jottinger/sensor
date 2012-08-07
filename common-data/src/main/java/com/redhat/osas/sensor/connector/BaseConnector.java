package com.redhat.osas.sensor.connector;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseConnector implements Connector {
    String uri;

    @Override
    public void connect(String uri) {
        try {
            URL url = new URL(uri);
        } catch (MalformedURLException e) {
            throw new ConnectorException(e);
        }
        this.uri = uri;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void publish(String data) {
        if (!isConnected()) {
            if (uri == null) {
                throw new ConnectorException("No uri provided for connector");
            }
            connect(uri);
        }
        doPublish(data);
    }

    protected abstract void doPublish(String data);
}
