package ca.uqac.histositesmaps.restapi.responselistener;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import ca.uqac.histositesmaps.restapi.JSONApiParser;
import ca.uqac.histositesmaps.restapi.RestApi;
import ca.uqac.histositesmaps.restapi.RestApiPlaces;

/**
 * Created by utilisateur on 26/11/2015.
 */
public class RestApiResponseListener implements Response.ErrorListener, Response.Listener<String> {

    private RestApiPlaces base;
    private LatLng location;
    private int radius;

    public RestApiResponseListener(RestApiPlaces base, LatLng location, int radius){
        this.base = base;
        this.location = location;
        this.radius = radius;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("ERROR", error.toString());
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject tmp = new JSONObject(response);
            if(!tmp.getString("status").equals("OK"))
                Log.e("ERROR","ERROR STATUS : "+response);

            JSONApiParser p = new JSONApiParser(tmp);
            base.setPrevious(p);
            String nToken = p.getNextToken();
            Log.d("NEXT TOKEN","VALUE : "+nToken);
            if(nToken != null){
                base.search(location,radius,nToken);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}