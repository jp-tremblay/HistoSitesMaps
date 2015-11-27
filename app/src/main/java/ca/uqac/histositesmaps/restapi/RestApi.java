package ca.uqac.histositesmaps.restapi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import ca.uqac.histositesmaps.R;
import ca.uqac.histositesmaps.restapi.responselistener.RestApiResponseListener;


/**
 * Created by utilisateur on 27/10/2015.
 */

public abstract class RestApi {

    protected final String baseUrl;

    protected Context             mContext;
    protected RestApiInteractor   mInteract;

    protected final String    API_KEY;

    protected JSONApiParser previous;
    protected RequestQueue queue;

    protected String globalRequest;
    protected HashMap<String,String> params;

    public RestApi(Context m){
        mContext = m;
        baseUrl = getBaseUrl();
        API_KEY = m.getString(R.string.google_maps_key);
        previous = null;
        queue = Volley.newRequestQueue(mContext);
        params = new HashMap<>();
        this.reset();
    }

    public void setParams(String key, String value){
        params.put(key,value);
        Log.d("HASHMAP",params.toString());
    }

    public void setPrevious(JSONApiParser p){
        previous = p;
        mInteract.getJSONApiResult(previous);
    }

    public void setInteractor(RestApiInteractor m){
        mInteract = m;
    }

    public String getCreatedUrl(){
        String url = baseUrl;
        ArrayList<String> alKeys = new ArrayList<>(params.keySet());

        for(String s:alKeys){
            url+=s+"="+params.get(s)+"&";
        }
        url = url.substring(0,url.length()-1);
        return url.replace(" ","%20");
    }

    public void reset(){
        params.clear();
        params.put("key", API_KEY);
        queue.cancelAll(new RequestQueue.RequestFilter() {
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
    abstract String getBaseUrl();
}
