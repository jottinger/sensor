package com.redhat.osas.sensor.connector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseConnector implements Connector {
    public static final String ERROR_MESSAGE =
            "No uri provided for connector";
    String uri;

    @Override
    public void connect(String uri) {
        try {
            new URL(uri);
        } catch (MalformedURLException e) {
            throw new ConnectorException(e);
        }
        this.uri = uri;
        doConnect(this.uri);
    }

    @SuppressWarnings("UnusedParameters")
    protected void doConnect(String uri) {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void publish(final String data) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                if (!isConnected()) {
                    if (uri == null) {
                        throw new ConnectorException(ERROR_MESSAGE);
                    }
                    connect(uri);
                }
                doPublish(data);
            }
        });
        service.shutdown();
    }

    protected void doPublish(String data) {
    }
}
