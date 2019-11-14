package models;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private String name , address , id ;
    private LatLng latLng;

    public PlaceInfo(String name, String address, String id, String attributions, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.latLng = latLng;
    }

    public PlaceInfo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}
