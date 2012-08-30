package com.redhat.osas.sensor.data;

import java.io.Serializable;

public class DataPoint implements Serializable {
    final String deviceId;
    final Double longitude;
    final Double latitude;
    final Long level;
    final Long maxLevel;
    final Long timestamp;

    private static final long serialVersionUID = 1928172l;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataPoint)) return false;

        DataPoint dataPoint = (DataPoint) o;

        if (deviceId != null ? !deviceId.equals(dataPoint.deviceId) : dataPoint.deviceId != null) return false;
        if (latitude != null ? !latitude.equals(dataPoint.latitude) : dataPoint.latitude != null) return false;
        if (level != null ? !level.equals(dataPoint.level) : dataPoint.level != null) return false;
        if (longitude != null ? !longitude.equals(dataPoint.longitude) : dataPoint.longitude != null) return false;
        return !(timestamp != null ? !timestamp.equals(dataPoint.timestamp) : dataPoint.timestamp != null);

    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
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
