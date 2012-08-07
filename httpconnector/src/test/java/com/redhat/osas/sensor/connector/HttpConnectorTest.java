package com.redhat.osas.sensor.connector;

import org.testng.annotations.Test;

public class HttpConnectorTest {

    public void testHttpConnector() {
        System.out.println("in test");
        Connector connector=new HttpConnector();
        System.out.println("we have the connector");
        connector.connect("http://192.168.1.115:8080/sensor-web/sensor");
        connector.publish("{\"latitude\":2,\"longitude\":1,\"level\":78,\"timestamp\":12912912}");
    }

    public static void main(String[] args) {
        new HttpConnectorTest().testHttpConnector();
    }
}
