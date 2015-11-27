package ca.uqac.histositesmaps.marker;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import ca.uqac.histositesmaps.restapi.RestApi;

/**
 * Created by utilisateur on 26/11/2015.
 */

public class CustomMarker {

    private final String address;
    private final LatLng coord;
    private final String name;


    public CustomMarker(String name,LatLng coord, String address){
        this.address = address;
        this.coord = coord;
        this.name = name;
    }

    public String toString(){
        return name+";"+address+";"+coord.latitude+","+coord.longitude;
    }
}
