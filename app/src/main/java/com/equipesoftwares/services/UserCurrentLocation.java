package com.equipesoftwares.services;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Manager;
import com.equipesoftwares.common.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;




/**
 * Created by My_Machine on 19/09/2016.
 */
public class UserCurrentLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient mGoogleApiClient;
    protected Manager manager;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.setAppContext(getBaseContext());
        mContext=this;
        if (manager == null) {
            manager = Manager.getInstance();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            if(mGoogleApiClient.isConnected()){
                userLatLong();}
        } else {
            Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();
        }
        super.onStart();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            if(mGoogleApiClient.isConnected()){
                userLatLong();}}
    }

    public void userLatLong() {
        try {

            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                LatLng position = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                manager.setCurrentLatitude(mCurrentLocation.getLatitude());
                manager.setCurrentLongitude(mCurrentLocation.getLongitude());


                Log.e("L/L :", position + "");
                Utils.setSharedPreferenceLat(this, KeyConstants.LAT, mCurrentLocation.getLatitude()+"");
                Utils.setSharedPreferenceLong(this, KeyConstants.LONG, mCurrentLocation.getLongitude()+ "");


                try {
                    String countryCode = Utils.getCountryCodeFromShortName(mContext, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    if(null != countryCode && (!("").equalsIgnoreCase(countryCode) || !countryCode.isEmpty()))
                    {
                        Utils.printLog("Country Code Via Location Services: ", countryCode);
                        Utils.setSharedPreference(mContext, KeyConstants.USER_COUNTRY_CODE, countryCode);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();

                }

            }else {
                Utils.setSharedPreferenceLat(this, KeyConstants.LAT,"0.0");
                Utils.setSharedPreferenceLong(this, KeyConstants.LONG,"0.0");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public void setGPSEnabled() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("GPS is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(callGPSSettingIntent, 1000);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    public boolean isGPSEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {


                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

                if (locationMode == 0) {
                    Toast.makeText(this,"GPS is OFF,Please Enable it!",Toast.LENGTH_SHORT).show();
                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, 1000);
                }else {
                    if (mGoogleApiClient != null) {
                        mGoogleApiClient.connect();
                        if(mGoogleApiClient.isConnected()){
                            userLatLong();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Check Your Network Connection", Toast.LENGTH_SHORT).show();
                    }

                }


            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;


        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }




    }
}


