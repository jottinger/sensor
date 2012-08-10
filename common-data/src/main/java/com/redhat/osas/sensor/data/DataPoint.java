package com.redhat.osas.sensor.data;

import java.io.Serializable;

public class DataPoint implements Serializable {
    String deviceId;
    Double longitude;
    Double latitude;
    Long level;
    Long timestamp;

    private static final long serialVersionUID=1928172l;

    public DataPoint() {
    }

    public DataPoint(String deviceId, Double latitude, Double longitude, int level) {
        this.deviceId=deviceId;
        this.latitude=latitude;
        this.longitude=longitude;
        this.level=new Long(level);
        this.timestamp=System.currentTimeMillis();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getTimestamp() {
        return timestamp;
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
        if (latitude != null ? !latitude.equals(dataPoint.latitude) : dataPoint.latitude != null) return false;
        if (level != null ? !level.equals(dataPoint.level) : dataPoint.level != null) return false;
        if (longitude != null ? !longitude.equals(dataPoint.longitude) : dataPoint.longitude != null) return false;
        if (timestamp != null ? !timestamp.equals(dataPoint.timestamp) : dataPoint.timestamp != null) return false;

        return true;
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
        sb.append("{deviceId='").append(deviceId).append('\'');
        sb.append(", longitude='").append(longitude).append('\'');
        sb.append(", latitude='").append(latitude).append('\'');
        sb.append(", level=").append(level);
        sb.append(", timestamp=").append(timestamp);
        sb.append("}:");
        sb.append(super.toString());
        return sb.toString();
    }
}
