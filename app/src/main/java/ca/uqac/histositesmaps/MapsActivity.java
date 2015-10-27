package ca.uqac.histositesmaps;

import android.location.Address;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.location.Geocoder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RestApiInteractor {

    private GoogleMap mMap;
    private Geocoder gc;

    private GPSTracker  gps;
    private RestApi     api;

    private final float DEFAULT_ZOOM = 15.0f;   // Zoom par défaut sur la carte
    private final int   RADIUS = 10000;         // En mètres (maximum 50 km = 50 000 )

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        gps = new GPSTracker(this);
        if (!gps.canGetLocation())
            gps.showSettingsAlert();

        api = new RestApi(this);
        api.setInteractor(this);

        gc = new Geocoder(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ((Button) findViewById(R.id.button_reset)).setOnClickListener(getClickListener());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Modification du code de Jean-pierre pour utiliser la classe GPSTracker
        /*
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = null;

        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException se) {

        }

        // On positionne sur Chicoutimi si impossible d'obtenir l'emplacement actuel
        if (location != null) {
            setLocationWithPos(new LatLng(location.getLatitude(), location.getLongitude()), "Ici");
        } else {
            setLocationWithPos(new LatLng(48.368, -71.07), "Chicoutimi");
        }
        */
        if(gps.canGetLocation()){
            setLocationWithPos(gps.getLatLng(),"Votre position");
        }else{
            setLocationWithPos(new LatLng(48.368, -71.07), "Chicoutimi");
        }
        // Init edittext composant
        setInputText();
    }

    /**
     * Permet de positionner sur la carte à partir d'un nom d'endroit
     *
     * @param newLocation
     */

    private void setNewLocation(String newLocation) {
        if (Geocoder.isPresent()) {
            try {
                String location;
                if (newLocation == null)
                    location = ((EditText) findViewById(R.id.editText)).getText().toString();
                else
                    location = newLocation;

                List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects

                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        LatLng newPos = new LatLng(a.getLatitude(), a.getLongitude());
                        setLocationWithPos(newPos, location);
                    }
                }
            } catch (IOException e) {
                // handle the exception
            }
        }
    }

    /**
     * Ajoute un marker à la position <code>newPos</code>, identifié par la titre (param2)
     * Déplace la "caméra" à cet endroit et efface le composant d'entrée de texte
     *
     * @param newPos
     * @param title
     */

    private void setLocationWithPos(LatLng newPos, String title) {
        reset();

        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(newPos).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos, DEFAULT_ZOOM));
        ((EditText) findViewById(R.id.editText)).setText("");

        try {
            api.search(newPos,RADIUS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void reset(){
        api.reset();
        mMap.clear();
    }
    /**
     * Défini le listener sur le edittext afin que celui-ci modifie l'emplacement sur la carte après
     * entrée d'un nouvel emplacement
     */

    private void setInputText() {
        ((EditText) findViewById(R.id.editText)).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (!event.isShiftPressed()) {
                                // the user is done typing.
                                setNewLocation(null);
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                });
    }

    @Override
    public void getJSONApiResult(JSONApiParser obj) {
        if(obj.isOK())
            for(JSONObject o:obj.getList()){
                LatLng tmpPosition = null;
                String tmpName = null;
                try {
                    tmpName = o.getString("name");
                    JSONObject geometry = o.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double latitude = location.getDouble("lat");
                    double longitude = location.getDouble("lng");
                    tmpPosition = new LatLng(latitude,longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mMap.addMarker(
                        new MarkerOptions()
                        .position(tmpPosition)
                        .title(tmpName)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
            }
        else
            Toast.makeText(MapsActivity.this, "Error while getting new Places : "+obj.getStatus(), Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener getClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationWithPos(gps.getLatLng(), "Votre position");
            }
        };
    }
}

