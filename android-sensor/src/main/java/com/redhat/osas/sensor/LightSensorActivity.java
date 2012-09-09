package com.redhat.osas.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.redhat.osas.sensor.connector.Connector;
import com.redhat.osas.sensor.connector.HttpConnector;
import com.redhat.osas.sensor.data.DataPoint;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class LightSensorActivity extends Activity {

    private final static String TAG = "osas-light-sensor";

    TextView logBox = null;
    TextView routerUrl = null;
    ProgressBar lightLevel = null;
    ToggleButton sensorActive = null;
    String voiceMailId;
    TelephonyManager telephonyManager;
    LocationManager locationManager;

    Listener listener;
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
    ObjectMapper mapper = new ObjectMapper();

    private Double latitude;
    private Double longitude;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Connector connector;
    private Long maxLevel;
    private TextView deviceId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connector = new HttpConnector();
        log("onCreate");
        setContentView(R.layout.main);
        logBox = (TextView) findViewById(R.id.logBox);
        log("Log initialized");
        routerUrl = (TextView) findViewById(R.id.routerUrl);
        lightLevel = (ProgressBar) findViewById(R.id.lightLevel);
        sensorActive = (ToggleButton) findViewById(R.id.sensorActive);
        deviceId = (TextView) findViewById(R.id.deviceId);

        log("Fields acquired");

        addTextChangeListener(routerUrl);
        addTextChangeListener(deviceId);

        log("Router URL keyboard listener created");
        addCheckedChangeListener();
        log("Sensor Toggle Listener activated");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        log("Telephony Manager acquired");
        voiceMailId = telephonyManager.getVoiceMailNumber();
        log("voice mail id: " + voiceMailId);
        deviceId.setText(voiceMailId);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        log("Location Manager acquired");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        log("Sensor Manager acquired");
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightLevel.setMax((int) lightSensor.getMaximumRange());

        listener = new Listener();
    }

    private void addCheckedChangeListener() {
        sensorActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    log("Sensor now active");
                    try {
                        // used to validate the URL
                        new URL(routerUrl.getText().toString());

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 500, 1, listener);
                        sensorManager.registerListener(listener, lightSensor, 500000);
                    } catch (MalformedURLException e) {
                        log("Invalid URL: " + routerUrl.getText().toString());
                    }
                } else {
                    log("Sensor now disabled");
                    locationManager.removeUpdates(listener);
                    sensorManager.unregisterListener(listener);
                }
            }
        });
    }

    private void addTextChangeListener(TextView textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ("".equals(editable.toString())) {
                    disable();
                    locationManager.removeUpdates(listener);
                    sensorManager.unregisterListener(listener);
                } else {
                    if (!sensorActive.isEnabled()) {
                        sensorActive.setEnabled(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorActive.setEnabled(true);

        log("onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        disable();
        log("onPause()");
    }

    private void disable() {
        // remove sensor listener
        sensorActive.setEnabled(false);
        sensorActive.setChecked(false);
        executor.shutdownNow();
        locationManager.removeUpdates(listener);
        sensorManager.unregisterListener(listener);
        executor = new ScheduledThreadPoolExecutor(5);
    }

    private void log(String message) {
        Log.i(TAG, message);
        if (logBox != null) {
            logBox.append(message);
            logBox.append("\n");
        }
    }

    class Listener implements LocationListener, SensorEventListener {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            //log("Location: " + latitude + "," + longitude);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            lightLevel.setProgress((int) sensorEvent.values[0]);
            if (latitude != null) {
                if (!connector.isConnected()) {
                    connector.connect(routerUrl.getText().toString());
                }
                DataPoint dataPoint = new DataPoint(voiceMailId,
                        latitude, longitude,
                        lightLevel.getProgress(), maxLevel);

                try {
                    String data = mapper.writeValueAsString(dataPoint);
                    log(data);
                    connector.publish(data);
                    log("published: " + data);
                } catch (IOException e) {
                    log(e.getMessage());
                }
            } else {
                log("no latitude yet!");
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            maxLevel = (long) sensor.getMaximumRange();
            lightLevel.setMax(maxLevel.intValue());
        }
    }

}

