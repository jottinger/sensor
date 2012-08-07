package com.redhat.osas.sensor.connector;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpConnector extends BaseConnector {
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost;

    @Override
    public void connect(String uri) {
        this.uri=uri;
        httppost = new HttpPost(uri);
    }

    @Override
    protected void doPublish(String data) {
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("data", data));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new ConnectorException("Response code for request: "
                        + response.getStatusLine().getStatusCode()
                        + " reason phrase: " + response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
    }
}
