package ca.uqac.histositesmaps;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by utilisateur on 27/10/2015.
 */

public class RestApi {

    private final String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    private Context             mContext;
    private RestApiInteractor   mInteract;

    private final String    API_KEY;

    private JSONApiParser previous;
    private RequestQueue queue;

    public RestApi(Context m){
        mContext = m;
        API_KEY = m.getString(R.string.google_maps_key);
        previous = null;
        queue = Volley.newRequestQueue(mContext);
    }

    protected void setPrevious(JSONApiParser p){
        previous = p;
        mInteract.getJSONApiResult(previous);
    }

    public void setInteractor(RestApiInteractor m){
        mInteract = m;
    }

    public void search(LatLng location, int radius){
        search(location, radius, null);
    }
    public void search(LatLng location, int radius, String nextToken) {
        String formattedUrl = baseUrl;
        formattedUrl += "key="+API_KEY;
        if (nextToken != null){
            formattedUrl += "&pagetoken="+nextToken;
        }else{
            formattedUrl += "&location="+location.latitude+","+location.longitude;
            formattedUrl += "&radius="+radius;
        }

        Log.d("SEARCHING","URL : "+formattedUrl+" ("+formattedUrl.length()+")");

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
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queue.add(stringRequest);
    }
}

// Classe qui permet de gérer le fait de boucler sur les next_page_token
class RestApiResponseListener implements Response.ErrorListener, Response.Listener<String> {

    private RestApi base;
    private LatLng location;
    private int radius;

    public RestApiResponseListener(RestApi base, LatLng location, int radius){
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
