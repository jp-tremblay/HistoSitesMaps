package ca.uqac.histositesmaps.marker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import ca.uqac.histositesmaps.R;

/**
 * Created by utilisateur on 26/11/2015.
 */
public class MarkerManagement {

    private static final String FILENAME = "marker_list.database";

    public static void add(Context m, CustomMarker marker){
        setContent(m, getContent(m)+marker.toString()+"\n");
    }

    public static String getContent(Context m){
        //reading text from file
        String s = "";
        try {
            FileInputStream fileIn=m.openFileInput(FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

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
}
