package ca.uqac.histositesmaps;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by utilisateur on 27/10/2015.
 */
public class JSONApiParser {
    private JSONObject  origin;
    private String      nextToken;
    private String      status;
    private ArrayList<JSONObject> list;

    public JSONApiParser(JSONObject obj) throws JSONException {
        origin = obj;
        status = obj.getString("status");
        try{
            nextToken = obj.getString("next_page_token");
        }catch (JSONException e){
            nextToken = null;
        }
        list = new ArrayList<JSONObject>();
        JSONArray results = obj.getJSONArray("results");
        for(int i=0; i<results.length();i++){
            JSONObject o = (JSONObject) results.get(i);
            list.add(o);
        }
    }

    public ArrayList<JSONObject> getList(){
        return list;
    }

    public String getNextToken(){
        return nextToken;
    }

    public boolean isOK(){
        return status.equals("OK");
    }
    public String getStatus(){
        return status;
    }

    public String toString(){
        String r = "";
        if(list.size() == 0) return "";
        for(JSONObject o: list){
            try {
                r += o.get("name")+",";
            } catch (JSONException e) {
            }
        }
        return r.substring(0,r.length()-1);
    }

}
