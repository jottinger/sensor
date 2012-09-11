package com.redhat.osas.sensor.data;

import java.io.Serializable;

public class DataPoint implements Serializable {
    String deviceId;
    Double longitude;
    Double latitude;
    Long level;
    Long maxLevel;
    Long timestamp;

    private static final long serialVersionUID = 1928172l;

    public DataPoint() {
    }

    public DataPoint(String deviceId, Double latitude, Double longitude, long level, long maxLevel, long timestamp) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.level = level;
        this.maxLevel = maxLevel;
        this.timestamp = timestamp;
    }

    public DataPoint(String deviceId, Double latitude, Double longitude, long level, long maxLevel) {
        this(deviceId, latitude, longitude, level, maxLevel, System.currentTimeMillis());
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Long getLevel() {
        return level;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getMaxLevel() {
        return maxLevel;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public void setMaxLevel(Long maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataPoint)) return false;

        DataPoint dataPoint = (DataPoint) o;

        if (deviceId != null ? !deviceId.equals(dataPoint.deviceId) : dataPoint.deviceId != null) return false;
        if (timestamp != null ? !timestamp.equals(dataPoint.timestamp) : dataPoint.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DataPoint");
        sb.append("{deviceId='").append(getDeviceId()).append('\'');
        sb.append(", longitude='").append(getLongitude()).append('\'');
        sb.append(", latitude='").append(getLatitude()).append('\'');
        sb.append(", level=").append(getLevel());
        sb.append(", timestamp=").append(getTimestamp());
        sb.append(", maxLevel=").append(getMaxLevel());
        sb.append("}:");
        sb.append(super.toString());
        return sb.toString();
    }
}
