package ca.uqac.histositesmaps;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import  java.io.InputStream;
import android.view.View;
import android.webkit.URLUtil;
import android.support.v4.app.NavUtils;

import ca.uqac.histositesmaps.R;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Image d'archive");
        setContentView(R.layout.activity_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView txtUrl= (TextView) findViewById(R.id.txt_url);
        TextView txtAddress = (TextView) findViewById(R.id.txt_address);



        String url = this.getIntent().getStringExtra("url");
        String address = this.getIntent().getStringExtra("address");

        txtUrl.setText(url);
        txtAddress.setText(address);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Affiche l'image s'il en existe une
        //SInon permet l'ajout
        if (URLUtil.isValidUrl(url)) {
            new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(url);
        } else {
            this.findViewById(R.id.imageView).setAlpha(0.0f);
            this.findViewById(R.id.txt_url).setAlpha(0.0f);
            this.findViewById(R.id.txt_address).setAlpha(0.0f);
            this.findViewById(R.id.url_lbl).setVisibility(View.VISIBLE);
            this.findViewById(R.id.url_txt).setVisibility(View.VISIBLE);
            this.findViewById(R.id.adr_t4).setVisibility(View.VISIBLE);
            this.findViewById(R.id.adr_v4).setVisibility(View.VISIBLE);
            ((EditText)this.findViewById(R.id.adr_v4)).setText(this.getIntent().getStringExtra("markertitle"), TextView.BufferType.EDITABLE);
            ((EditText)this.findViewById(R.id.adr_v4)).setEnabled(false);
            this.findViewById(R.id.confirm_button_place).setVisibility(View.VISIBLE);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
