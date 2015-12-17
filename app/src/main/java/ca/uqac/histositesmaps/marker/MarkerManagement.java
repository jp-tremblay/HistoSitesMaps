package ca.uqac.histositesmaps.marker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import android.util.Log;

import ca.uqac.histositesmaps.R;

/**
 * Created by utilisateur on 26/11/2015.
 */
public class MarkerManagement extends SQLiteOpenHelper {

    private static final String TABLE_PLACES = "places";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_URL = "url";

    private static final String[] COLUMNS = {KEY_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_URL, KEY_ADDRESS };


    private static final String DATABASE_NAME = "CustomDatabase";
    private static final int DATABASE_VERSION = 3;

    public MarkerManagement(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLACES_TABLE = "CREATE TABLE "+TABLE_PLACES+" ( " +
                KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME+" TEXT, " +
                KEY_LATITUDE+" DOUBLE,"+
                KEY_LONGITUDE+" DOUBLE, "+
                KEY_URL+" TEXT, "+
                KEY_ADDRESS+" TEXT )";

        db.execSQL(CREATE_PLACES_TABLE);
        Log.d("CreateDB", db.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        Log.d("DropDB", db.toString());
        this.onCreate(db);
    }

    public void addPlace(CustomMarker marker){
        Log.d("addPlace", marker.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, marker.getName());
        values.put(KEY_LATITUDE, marker.getCoord().latitude);
        values.put(KEY_LONGITUDE, marker.getCoord().longitude);
        values.put(KEY_URL, marker.getURL());
        values.put(KEY_ADDRESS, marker.getAddress());

        Log.d("addPlace 2", values.toString());

        db.insert(TABLE_PLACES, null, values);

        db.close();
    }

    public CustomMarker getPlace(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_PLACES,
                        COLUMNS,
                        " id = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null,
                        null);

        if (cursor != null)
            cursor.moveToFirst();

        CustomMarker marker = buildPlaceWithCursor(cursor);
        Log.d("getPlace(" + id + ")", marker.toString());

        return marker;
    }

    public List<CustomMarker> getAllPlaces(){
        List<CustomMarker> places = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Log.d("CURSOR", cursor.toString());

        CustomMarker marker = null;
        if (cursor.moveToFirst()) {
            do {
                marker = buildPlaceWithCursor(cursor);
                places.add(marker);
            } while (cursor.moveToNext());
        }
        Log.d("ALL PLACES",places.toString());
        return places;
    }

    private CustomMarker buildPlaceWithCursor(Cursor cursor){
        return new CustomMarker(
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                new LatLng(
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                ),
                cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(KEY_URL))
        );
    }
    /*
    private static final String FILENAME = "marker_list.database";
    private static boolean databaseCreated = false;


    public static void add(Context m, CustomMarker marker){
        setContent(m, getContent(m)+marker.toString()+"\n");
    }

    public static String getContent(Context m){
        //reading text from file
        Log.d("Entered geyContent", "FILE");

        String s = "";
        try {
//            if (!databaseCreated && !m.getFileStreamPath(m.getFilesDir() + "/" +  FILENAME).exists()){
//                File database = new File(m.getFilesDir() + "/" +  FILENAME);
//                Log.d("File created :" + database.getAbsolutePath(), "FILE");
//                database.createNewFile();
//                databaseCreated = true;
//            }
            FileInputStream fileIn = null;
            try {
                fileIn = m.openFileInput(FILENAME);
            } catch (java.io.FileNotFoundException e){
                File file = new File(m.getExternalFilesDir(null), FILENAME);
                OutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                }catch(Exception ee){
                    ee.printStackTrace();
                }finally
                {
                    os.close();
                    fileIn = m.openFileInput(FILENAME);
                }
            }
            //fileIn = m.create new FileInputStream (new File(FILENAME));
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void setContent(Context m, String s){
        try {
            FileOutputStream fileout=m.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(s);
            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
