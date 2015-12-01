package ca.uqac.histositesmaps.marker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import ca.uqac.histositesmaps.R;
import ca.uqac.histositesmaps.marker.CustomMarker;
import ca.uqac.histositesmaps.restapi.RestApiTranslator;

public class FormActivity extends Activity implements View.OnClickListener {

    private EditText address;
    private EditText latitude;
    private EditText longitude;
    private EditText name;

    private RestApiTranslator translator;

    private String address_address = "";
    private String address_name = "";
    private LatLng address_latlng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ((Button) findViewById(R.id.confirm_button)).setOnClickListener(this);

        address     = (EditText) findViewById(R.id.form_adr_v1);
        latitude    = (EditText) findViewById(R.id.form_adr_v2);
        longitude   = (EditText) findViewById(R.id.form_adr_v3);
        name        = (EditText) findViewById(R.id.form_adr_v4);

        /*
        address.setText("25 rue Lorne est G7H2L4 Chicoutimi");
        name.setText("MAISON");
        */

        translator = new RestApiTranslator(this);
    }

    @Override
    public void onClick(View v) {
        address_name = this.name.getText().toString();

        if(latitude.getText().length() != 0 && longitude.getText().length() != 0) {
            address_latlng = new LatLng(Double.parseDouble(latitude.getText().toString()), Double.parseDouble(longitude.getText().toString()));
            translator.getAddress(address_latlng, this);
        }else{
            address_address = this.address.getText().toString();
            translator.getCoordinates(address_address,this);
        }
    }

    public void setAddress(String address){
        this.address.setText(address);
        createObject();
    }
    public void setLatLng(LatLng latlng){
        this.longitude.setText("" + latlng.longitude);
        this.latitude.setText("" + latlng.latitude);
        createObject();
    }

    private void createObject(){
        address_address = this.address.getText().toString();
        address_latlng = new LatLng(
                Double.parseDouble(this.latitude.getText().toString()),
                Double.parseDouble(this.longitude.getText().toString())
        );
        address_name = this.name.getText().toString();

        CustomMarker marker = new CustomMarker(address_name,address_latlng,address_address);

        new MarkerManagement(this).addPlace(marker);

        Intent intent = new Intent();
        intent.putExtra("name",marker.getName());
        intent.putExtra("latitude",marker.getCoord().latitude);
        intent.putExtra("longitude",marker.getCoord().longitude);
        intent.putExtra("address",marker.getAddress());


        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("FROM CHILD", "test");
    }
}
