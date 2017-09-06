package com.example.qthjen.appsellcommodity.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnected {

    public static boolean haveNetworkConnected(Context context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        for ( NetworkInfo info : networkInfo) {

            if ( info.getTypeName().equalsIgnoreCase("WIFI"))
                if ( info.isConnected())
                    haveConnectedWifi = true;

            if ( info.getTypeName().equalsIgnoreCase("MOBILE"))
                if ( info.isConnected())
                    haveConnectedMobile = true;
        }

        return haveConnectedWifi||haveConnectedMobile;
    }

}
