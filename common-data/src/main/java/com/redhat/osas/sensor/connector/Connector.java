package com.redhat.osas.sensor.connector;

public interface Connector {
    void connect(String uri);
    void disconnect();
    boolean isConnected();
    void publish(String data);
}
