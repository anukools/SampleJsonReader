package com.sample.testjsonwrite;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Anukool Srivastav on 6/15/2017.
 */

public class Utils {

    public static void writeToFile(Context ctx,String fileName, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter(ctx.getDir("my_data_dir", 0) + "/" + fileName);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    public static String readFromFile(File fileName) {
        try {
            //check whether file exists
            FileInputStream is = new FileInputStream(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }
    }
}
