package com.apidemo;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App extends Application {

    private static String TAG = "App";

    static public DatabaseHelper dbHelper;
    static Context context;

    public static String APP_SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
    public static String APP_FOLDERNAME = "API_Demo";
    public static String DB_NAME = "apidemo.db";

    public static String DATABASE_FOLDER_FULLPATH = App.APP_SD_CARD_PATH + "/" + App.APP_FOLDERNAME + "/" + App.DB_NAME;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(context);

        createAppFolder();
    }

    public static void createAppFolder() {
        try {
            File file2 = new File(APP_SD_CARD_PATH, APP_FOLDERNAME);
            if (!file2.exists()) {
                if (!file2.mkdirs()) {
                    showLog(TAG,"--------Create Directory " + APP_FOLDERNAME + "====");
                } else {
                    showLog(TAG, "--------No--1Create Directory " + APP_FOLDERNAME + "====");
                }
            } else {
                showLog(TAG, "--------already created---No--2Create Directory " + APP_FOLDERNAME + "====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLog(String ActivityName, String strMessage) {
        Log.d("From: ", ActivityName + " -- " + strMessage);
    }

    public static void showLogResponce(String strFrom, String strMessage) {
        Log.d("From: " + strFrom, " strResponse: " + strMessage);
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "dd MMM, yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /*------ Check Internet -------*/
    public static boolean isInternetAvail(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

}
