package ca.uqac.histositesmaps.restapi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import ca.uqac.histositesmaps.marker.FormActivity;
import ca.uqac.histositesmaps.restapi.responselistener.RestApiAddressListener;
import ca.uqac.histositesmaps.restapi.responselistener.RestApiLatLngListener;

/**
 * Created by utilisateur on 26/11/2015.
 */
public class RestApiTranslator extends RestApi {
    public RestApiTranslator(Context m) {
        super(m);
    }

    public void getCoordinates(String address, FormActivity frame){
        this.reset();
        this.setParams("address", address);

        String formattedUrl = getCreatedUrl();
        Log.d("SUPER TOSTRING", "Created url : " + formattedUrl);

        RestApiLatLngListener customListener = new RestApiLatLngListener(frame);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                formattedUrl,
                customListener,
                customListener
        );
        queue.add(stringRequest);
    }

    public void getAddress(LatLng coord, FormActivity frame){
        this.reset();
        this.setParams("latlng", coord.latitude + "," + coord.longitude);

        String formattedUrl = getCreatedUrl();
        Log.d("SUPER TOSTRING", "Created url : " + formattedUrl);

        RestApiAddressListener customListener = new RestApiAddressListener(frame);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                formattedUrl,
                customListener,
                customListener
        );
        queue.add(stringRequest);
    }

    String getBaseUrl() {
        return "https://maps.googleapis.com/maps/api/geocode/json?";
    }
}
