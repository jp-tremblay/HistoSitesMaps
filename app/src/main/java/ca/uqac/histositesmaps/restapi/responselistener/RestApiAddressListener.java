package ca.uqac.histositesmaps.restapi.responselistener;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.uqac.histositesmaps.marker.FormActivity;

/**
 * Created by utilisateur on 26/11/2015.
 */
public class RestApiAddressListener implements Response.ErrorListener, Response.Listener<String> {

    private FormActivity frame;

    public RestApiAddressListener(FormActivity frame){
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
            JSONObject result;
            if(results.length()>0){
                result = results.getJSONObject(0);
            }else{
                Log.e("ERROR","NO RESULTS FOUND");
                return;
            }
            String address = result.getString("formatted_address");

            frame.setAddress(address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
