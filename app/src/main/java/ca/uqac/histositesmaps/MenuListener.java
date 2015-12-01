package ca.uqac.histositesmaps;

import android.content.Intent;
import android.view.View;

/**
 * Created by utilisateur on 01/12/2015.
 */
public class MenuListener implements View.OnClickListener {

    private MapsActivity activity;
    private Intent       intent;

    public MenuListener(MapsActivity activity, Intent intent){
        this.activity = activity;
        this.intent = intent;
    }
    @Override
    public void onClick(View v) {
        activity.startActivityForResult(intent,1);
    }
}
