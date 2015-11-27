package ca.uqac.histositesmaps.restapi.responselistener;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.uqac.histositesmaps.marker.FormActivity;

/**
 * Created by utilisateur on 26/11/2015.
 */
public class RestApiLatLngListener implements Response.ErrorListener, Response.Listener<String> {
    private FormActivity frame;

    public RestApiLatLngListener(FormActivity frame){
        this.frame = frame;
    }

    public void onErrorResponse(VolleyError error) {
        Log.e("ERROR", error.toString());
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject tmp = new JSONObject(response);
            if(!tmp.getString("status").equals("OK")){
                Log.e("ERROR","ERROR STATUS : "+response);
                return;
            }

            JSONArray results = tmp.getJSONArray("results");
            JSONObject resultObject;
            if(results.length() > 0) resultObject = results.getJSONObject(0);
            else{
                Log.e("ERROR","No results");
                return;
            }

            JSONObject geometry = resultObject.getJSONObject("geometry").getJSONObject("location");
            LatLng location = new LatLng(geometry.getDouble("lat"),geometry.getDouble("lng"));

            frame.setLatLng(location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
