package ca.uqac.histositesmaps.marker;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import ca.uqac.histositesmaps.restapi.RestApi;

/**
 * Created by utilisateur on 26/11/2015.
 */

public class CustomMarker implements Serializable {

    private final String address;
    private final double latitude;
    private final double longitude;
    private final String name;

    private final boolean FROM_GOOGLE;

    public CustomMarker(String name,LatLng coord, String address){
        this(name,coord,address,false);
    }
    public CustomMarker(String name,LatLng coord, String address,boolean FROM_GOOGLE){
        this.address = address;
        this.latitude = coord.latitude;
        this.longitude = coord.longitude;
        this.name = name;
        this.FROM_GOOGLE = FROM_GOOGLE;
    }

    public boolean isFromGoogle(){  return FROM_GOOGLE; }

    public String getName(){    return name;                             }
    public LatLng getCoord(){   return new LatLng(latitude,longitude);   }
    public String getAddress(){ return address;                          }

    public String toString(){
        return name+";"+address+";"+latitude+","+longitude;
    }
}
