package com.equipesoftwares.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.equipesoftwares.R;
import com.equipesoftwares.services.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.equipesoftwares.services.AppController.TAG;


/**
 * Created by akash on 15/09/2016.
 */
// Commonly used methods and values throughout the project
public class Utils {

    // Flags
    public static final String FLAG_HOMEIMAGES_SCROLL = "0";
    public static final String FLAG_ACHIEVEMENTS = "1";
    public static final String FLAG_ACTIVITIES = "2";
    public static final String FLAG_FACILITIES = "4";


public static String mSchool = "1",mCollege = "0",mPublic="1",mPrivate="2",mGovernment="3";
    public static boolean mSchoolFlag = false;

    // positive and negative reply for dialogs
    public static int replyPositive = 1, replyNegative = 0;


    // status code from server
    public static int SUCCESS = 200, FAILURE = 400;

    // to match email address pattern
    private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    // method to check whether mobile is connected to internet or not
    public static boolean isConnected(Context context) {
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo netinfo = cm.getActiveNetworkInfo();

            if (netinfo != null && netinfo.isConnectedOrConnecting()) {
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if ((mobile != null && mobile.isConnectedOrConnecting())
                        || (wifi != null && wifi.isConnectedOrConnecting()))
                    return true;
                else
                    return false;
            } else
                return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Display Toast
    public static void showToast(String msg, Context context) {
        // Simple Toast message
        try {
            Toast newToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            newToast.setGravity(Gravity.CENTER, 0, 0);
            newToast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Printing Logs
    public static void printLog(String tag, String msg) {
        try {
            if(msg != null)
                Log.e(tag, msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static String getAddressFromLatLng(Context mContext, String lati, String longi) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        String errorMessage = null;
        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.

            addresses = geocoder.getFromLocation(
                    Double.parseDouble(lati),
                    Double.parseDouble(longi),
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Service not available";
            Utils.printLog("Welcome_Screen", errorMessage);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid Lat Long";
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Address not found";
                Utils.printLog("Welcome_Screen", errorMessage);
            }
            return "";
        } else {
            Address address = addresses.get(0);
            Utils.printLog("Welcome_Screen address utils", ""+addresses.get(0).getAddressLine(0));
            return address.getAddressLine(0);
        }
    }

    // set string value to shared preference Latitude

    public static void setSharedPreferenceLat(Context context, String key, String value) {
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(key, String.valueOf(value));
            prefsEditor.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // get  string value to shared preference Latitude
    public static Double getSharedPreferenceLat(Context context, String key) {
        String prefData = "";
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            prefData = prefs.getString(key, "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return Double.valueOf(prefData);
    }


    public static Double parseDouble(String s1, String s2){
        if(s1 != null && !s1.isEmpty() && s2 != null && !s2.isEmpty())
            return 1.1;
        else
            return 0.0;
    }
    public static String getCountryCodeFromShortName(Context mContext, double currentLatitude, double currentLongitude){
        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
            if (null != addresses && addresses.size() > 0)
            {
                try {
                    String countryName = addresses.get(0).getCountryCode();
                    if (null != countryName && (!("").equalsIgnoreCase(countryName) || !countryName.isEmpty()))
                        return getCountryCode(mContext, countryName);
                    else
                        return "";
                }
                catch (Exception e) {
                    e.printStackTrace();
//                    if(mContext instanceof Home_Activity)
//                        progressDialog.dismiss();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
//            if(mContext instanceof Home_Activity)
//                progressDialog.dismiss();
        }
        return "";
    }


    // set string value to shared preference Longitude
    public static void setSharedPreferenceLong(Context context, String key, String value) {
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(key, String.valueOf(value));
            prefsEditor.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // set string value to shared preference
    public static void setSharedPreference(Context context, String key,
                                           String value) {
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString(key, value);
            Utils.printLog("Shared Prefrence : ", value);
            prefsEditor.commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // get string value from shared preference
    public static String getSharedPreference(Context context, String key) {
        String prefData = "";
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            prefData = prefs.getString(key, "");
        }catch (Exception e){
            e.printStackTrace();
        }
        return prefData;
    }

    // method to hide keyBoard
    public static  void hideSoftInputKeyboard(Context mContext) {
        try {
            // Check if no view has focus:
            View view = ((Activity)mContext).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    // get country code from country name
    public static String getCountryCode(Context context, String countryName) {
        try {
            String arrCountryCode[] = context.getResources().getStringArray(R.array.countryCodes);
            for (int i = 0; i < arrCountryCode.length; i++) {
                if (arrCountryCode[i].contains(countryName.toUpperCase())) {
                    String [] data = arrCountryCode[i].split(",");
                    return "+"+data[0];
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    // to cancel webservice request
    public static void cancelWebserviceRequest(Context mContext){
        try {
            AppController.getInstance().cancelPendingRequests(mContext.getClass().getSimpleName());
            Utils.printLog("Volley:", "Request Canceled " + mContext.getClass().getSimpleName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void clearSharedPreferencesWithKey(Context mContext, String key){
        try{
            Utils.setSharedPreference(mContext, key, "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getSavedPojoResponseReturnArray(Context context, String key){
        JSONArray js = null;
        JSONObject object=null;

        try{

            String s = Utils.getSharedPreference(context,key);
            if(null != s && !("").equalsIgnoreCase(s)) {
                Utils.printLog("Pojo Data of sharedprefrence :", s);
                js = new JSONArray(s);
                object = js.getJSONObject(0);
            }
        }catch (Exception e){e.printStackTrace();}

        return object+"";
    }
    public static String getSharedPreferencePojo(Context context, String key) {
        String prefData = "";
        try {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            prefData = prefs.getString(key, "");
            Utils.printLog("getshared data pojo,",prefData);
        }catch (Exception e){
            e.printStackTrace();
        }
        return prefData;
    }

    public static Integer getDeviceWidth(Context context){
        int finalWidth;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        finalWidth = displaymetrics.widthPixels;
       Utils.printLog("Width:",finalWidth+"");
        return finalWidth;
    }

    public static Integer getDeviceHeight(Context context){
        int finalHeight;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        finalHeight = displaymetrics.heightPixels;
        Utils.printLog("Height:", finalHeight + "");
        return finalHeight;
    }




    public static Date stringTODate3(String date) {
        Date d = null,d1=null;
        String d2="";
        SimpleDateFormat inpFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat outFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            d = inpFormat.parse(date);
            d2 = (outFormat.format(d));
            Utils.printLog("d2",d2);
            d1 = outFormat.parse(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d1;
    }

    public static Date stringTODate4(String date) {
        Date d = null,d1=null;
        SimpleDateFormat inpFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            d = inpFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }






    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        Utils.printLog("Width:",reqWidth+"");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        try{
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeResource(res, resId, options);

    }


}
