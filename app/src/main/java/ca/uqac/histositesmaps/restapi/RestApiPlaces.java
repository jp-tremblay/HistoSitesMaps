package ca.uqac.histositesmaps.restapi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import ca.uqac.histositesmaps.restapi.responselistener.RestApiResponseListener;

/**
 * Created by utilisateur on 26/11/2015.
 */
public class RestApiPlaces extends RestApi {

    public RestApiPlaces(Context m) {
        super(m);
    }

    @Override
    String getBaseUrl() {
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    }

    public void search(LatLng location, int radius){
        search(location, radius, null);
    }
    public void search(LatLng location, int radius, String nextToken) {
        if (nextToken != null){
            this.setParams("pagetoken",nextToken);
        }else{
            this.setParams("location",location.latitude+","+location.longitude);
            this.setParams("radius",""+radius);
        }

        String formattedUrl = getCreatedUrl();
        Log.d("SUPER TOSTRING","Created url : "+formattedUrl);

        RestApiResponseListener customListener = new RestApiResponseListener(this,location,radius);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                formattedUrl,
                customListener,
                customListener
        );
        // Ajout d'un délai pour éviter les INVALID_REQUEST
        // Demandé par Google

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queue.add(stringRequest);
    }
}
