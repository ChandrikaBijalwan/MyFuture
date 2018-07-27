package com.equipesoftwares.classes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.equipesoftwares.Pojo.CommonIdValuePojo;
import com.equipesoftwares.Pojo.GetBoardListPojo;
import com.equipesoftwares.Pojo.GetManagementListPojo;
import com.equipesoftwares.Pojo.GetSchoolListResultPojo;
import com.equipesoftwares.Pojo.SchoolListPojo;
import com.equipesoftwares.R;
import com.equipesoftwares.adapters.EndlessScrollListener;
import com.equipesoftwares.adapters.PlaceAutoCompleteAdapter;
import com.equipesoftwares.adapters.SchoolListAdapter;
import com.equipesoftwares.common.Database;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.equipesoftwares.common.WrapContentLinearLayoutManager;
import com.equipesoftwares.google.direction.ReverseGeocode;
import com.equipesoftwares.services.AppController;
import com.equipesoftwares.services.GsonRequest;
import com.equipesoftwares.services.UserCurrentLocation;
import com.equipesoftwares.services.VolleyWebserviceCall;
import com.equipesoftwares.services.VolleyWebserviceCallBack;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 Created By Akash Khobragade
 Activity search for college / School with respective of Management */

public class SearchSchoolActivity extends UserCurrentLocation implements View.OnClickListener,VolleyWebserviceCallBack {

    private Context mContext;
    private AutoCompleteTextView LocationAutoCompleteTv;
    private Button btnGo,btnFilter,btnClear;
    private ImageView current_locationImg;
    private ProgressDialog progressDialog;
    private RecyclerView rvSchooLlist;
    private double userLatitude, userLongitude, userCurrentLongitude, userCurrentLatitude;
    private String userAddress;
    private int LOCATION = 1;
    private ArrayList <SchoolListPojo> schoolListPojoArrayList= new ArrayList<SchoolListPojo>();
    private boolean onSearch = false;
    private boolean  isGPSON=false;
    private TextView noResultFoundTv;
    private Handler handler = new Handler();
    private int pageNo = 1;
    private Database db;
    private EndlessScrollListener scrollListener = null;
    private SchoolListAdapter schoolListAdapter;

    //dialogue filter
    private AlertDialog dialog;
    private RadioButton rbSchool,rbkCollege,rbAll;
    private RadioGroup filterRadioGroup;
    private String count = "";

    private CheckBox cbPublic,cbPrivate,cbGovernment;
    private String countPublic="",countPrivate="",countGovernment="", management="";
    private boolean isDialogVisible = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school);
        try {
            setClasses();
            setScreenControls();
            setAdapters();
            setListners();
            progressDialog = new ProgressDialog(this);
            if(schoolListPojoArrayList.size() > 0){
                schoolListPojoArrayList.clear();}
            getSchoolList(count, "");
            getBoardListWS();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBoardListWS() {
        try{
            new VolleyWebserviceCall().volleyGetCall(mContext,KeyConstants.GET_BOARD_LISTING,SearchSchoolActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClasses() {
        try{
            mContext = this;
            Utils.hideSoftInputKeyboard(mContext);
            db = new Database(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setScreenControls() {
        try{

            current_locationImg = (ImageView) findViewById(R.id.Current_LocationImg);
            rvSchooLlist = (RecyclerView) findViewById(R.id.rvSchooLlist);
            btnFilter=(Button)findViewById(R.id.btnFilter);
            rvSchooLlist.setNestedScrollingEnabled(false);
            noResultFoundTv = (TextView) findViewById(R.id.tvNoRecordsFound);
            btnGo = (Button) findViewById(R.id.btnGo);
            LocationAutoCompleteTv = (AutoCompleteTextView) findViewById(R.id.LocationAutoCompleteTv);
            btnClear=(Button)findViewById(R.id.btnClear);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting, menu);
        MenuItem item1 = menu.findItem(R.id.app_info);

        MenuItem item2 = menu.findItem(R.id.about_us);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_info) {
            Intent i = new Intent(mContext,AppInformationActivity.class);
            startActivity(i);
            return true;
        }else if(id == R.id.about_us){
            Intent i  = new Intent(mContext,AboutUsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);}

    private void setListners() {
        try {
            btnClear.setOnClickListener(this);
            current_locationImg.setOnClickListener(this);
            btnGo.setOnClickListener(this);
            btnFilter.setOnClickListener(this);
            LocationAutoCompleteTv.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        if(schoolListPojoArrayList.size() > 0){
                            schoolListPojoArrayList.clear();}
                        getSchoolList(count, "");
                    }
                    return false;
                }
            });

            LocationAutoCompleteTv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (s.length() == 0) {
                            userLatitude = 0.0;
                            userLongitude = 0.0;
                            //hotelListResultsArrList.clear();
                            LocationAutoCompleteTv.setAdapter(new PlaceAutoCompleteAdapter(mContext, R.layout.simple_expandable_list_item_1));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            LocationAutoCompleteTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    try {
                        LocationAutoCompleteTv.clearListSelection();
                        LocationAutoCompleteTv.dismissDropDown();
                        new Handler().post(new Runnable() {
                            public void run() {
                                LocationAutoCompleteTv.dismissDropDown();
                            }
                        });

                        String string = (String) adapterView.getItemAtPosition(position);
                        generateURL(string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void setAdapters() {
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);

            rvSchooLlist.setLayoutManager(linearLayoutManager);

            scrollListener = new EndlessScrollListener(linearLayoutManager);
            rvSchooLlist.addOnScrollListener(scrollListener);
            linearLayoutManager.scrollToPositionWithOffset(0, 0);
            LocationAutoCompleteTv.setAdapter(new PlaceAutoCompleteAdapter(mContext, R.layout.simple_expandable_list_item_1));

            schoolListAdapter=new SchoolListAdapter(mContext, schoolListPojoArrayList);
            rvSchooLlist.setAdapter(schoolListAdapter);
            rvSchooLlist.smoothScrollToPosition(rvSchooLlist.getAdapter().getItemCount());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GeoCoding
     **/
    public void GeoCoding(String url) {
        try {
            Utils.printLog("GeoCoding URL: ", url);
            AppController.getInstance().getRequestQueue().cancelAll("ReverseGeocoding");
            GsonRequest<ReverseGeocode> gsonReq = new GsonRequest<ReverseGeocode>(Request.Method.GET, url, ReverseGeocode.class, null, new Response.Listener<ReverseGeocode>() {
                @Override
                public void onResponse(ReverseGeocode response) {

                    parseGeocodeResult(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Utils.printLog("onError Response(GeoCoding): ", "" + error);
                }
            });
            AppController.getInstance().addToRequestQueue(gsonReq, "location");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);


    }

    private void generateURL(String string) {
        try {
            Utils.hideSoftInputKeyboard(mContext);
            string = string.replace(" ", "%20");
            String URL = "http://maps.googleapis.com/maps/api/geocode/json?address=" + string + "&sensor=true";
            GeoCoding(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    private void parseGeocodeResult(ReverseGeocode response) {
        try {
            double lat, longi;
            String address = "";
            if (response.getResults().size() > 0) {
                lat = Double.parseDouble(String.valueOf(response.getResults().get(0).getGeometry().getLocation().getLat()));
                longi = Double.parseDouble(String.valueOf(response.getResults().get(0).getGeometry().getLocation().getLng()));
                address = response.getResults().get(0).getFormatted_address();
                userLatitude = lat;
                userLongitude = longi;
                userAddress = address;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.printLog("Request code:",requestCode+"");
        // hotelListResultsArrList.clear();
        if (101 == requestCode) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Fetching your current location");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 5000);

        }else if(1000 == requestCode){
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if(provider != null && !provider.isEmpty() && !("").equalsIgnoreCase(provider)){
                isGPSON=true;
                progressDialog.show();
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        userCurrentLocation();
                        progressDialog.dismiss();
                    }
                },10000);

            }else{
                //Users did not switch on the GPS
                Utils.showToast("GPS is off! Unable to fetch your location.",mContext);
            }
        }
    }

    public void latLng(){
        try{

            userCurrentLatitude=manager.getCurrentLatitude();
            userCurrentLongitude=manager.getCurrentLongitude();
            if(0.0 != userCurrentLatitude && 0.0 != userCurrentLongitude){
                LatLng currentLatlng = new LatLng(userCurrentLatitude,userCurrentLongitude);
                Utils.setSharedPreferenceLat(mContext, KeyConstants.LAT, userCurrentLatitude + "");
                Utils.setSharedPreferenceLong(mContext, KeyConstants.LONG, userCurrentLongitude + "");
            }}catch (Exception e){e.printStackTrace();}
    }

    private void userCurrentLocation(){
        try{

            progressDialog.setMessage("Please wait");
            progressDialog.show();
                    isGPSEnabled(mContext);
                    userLatitude=manager.getCurrentLatitude();
                    userLongitude=manager.getCurrentLongitude();
                    if(0.0 != userLatitude && 0.0 != userLongitude){
                        Utils.setSharedPreferenceLat(mContext,KeyConstants.LAT,userLatitude+"");
                        Utils.setSharedPreferenceLong(mContext, KeyConstants.LONG, userLongitude + "");
                        userAddress = Utils.getAddressFromLatLng(mContext, String.valueOf(userLatitude), String.valueOf(userLongitude));
                        LocationAutoCompleteTv.setText(userAddress);
                        LocationAutoCompleteTv.setAdapter(null);
                        // Utils.setCurorToEnd(placeAutoCompleteTv,userAddress);
                    }
                   progressDialog.dismiss();

        }catch (Exception e){e.printStackTrace();}}



    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Current_LocationImg:
                try{

                    LocationAutoCompleteTv.getText().clear();
                    askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,1);
//                    userCurrentLocation();
                }catch (Exception e){e.printStackTrace();}
                break;

            case R.id.btnGo:
                Utils.hideSoftInputKeyboard(mContext);
                if(schoolListPojoArrayList.size() > 0){
                    schoolListPojoArrayList.clear();}
                getSchoolList(count, "");

                break;
            case R.id.btnFilter:
                Utils.hideSoftInputKeyboard(mContext);
                showFiltersDialog();
                break;

            case R.id.btnClear:
                Utils.hideSoftInputKeyboard(mContext);
                LocationAutoCompleteTv.getText().clear();
                Utils.clearSharedPreferencesWithKey(mContext,KeyConstants.LAT);
                Utils.clearSharedPreferencesWithKey(mContext,KeyConstants.LONG);
                userLatitude=0.0;
                userLongitude=0.0;

                break;
        }

    }

    private void showFiltersDialog() {
        try{
            Utils.printLog("management size",management);
//            String[] strArray = new String[] {strName};
//            System.out.println(strArray[0]);
            if(countPublic.length()>0)
                countPublic="";
            if(countPrivate.length()>0)
                countPrivate="";
            if(countGovernment.length()>0)
                countGovernment="";
            String[] arrayString = management.split("");
            String tempChkValue="";



            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogView = inflater.inflate(R.layout.filter_dilogue_listview, null);

            rbAll =(RadioButton) dialogView.findViewById(R.id.rbAll);
            rbSchool=(RadioButton) dialogView.findViewById(R.id.rbSchool);
            rbkCollege=(RadioButton) dialogView.findViewById(R.id.rbCollege);
            filterRadioGroup=(RadioGroup)dialogView.findViewById(R.id.filterRadioGroup);
            cbPublic = (CheckBox)dialogView.findViewById(R.id.chkPublic);
            cbPrivate = (CheckBox)dialogView.findViewById(R.id.chkPrivate);
            cbGovernment = (CheckBox)dialogView.findViewById(R.id.chkGovernment);
            isDialogVisible = true;
            if(count.equals(Utils.mSchool)) {
                rbSchool.setChecked(true);
            }else if(count.equals(Utils.mCollege)) {
                rbkCollege.setChecked(true);
            }else rbAll.setChecked(true);


            filterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    // find which radio button is selected
                    Log.e("count ",""+count);

                    switch (checkedId) {
                        case R.id.rbSchool:
                            count=Utils.mSchool;
                            break;
                        case R.id.rbCollege:
                            count=Utils.mCollege;
                            break;
                        case R.id.rbAll:
                            count="";
                            break;
                    }
                }
            });


            cbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        countPublic = String.valueOf(Utils.mPublic);
                    }else {
                        countPublic ="";
                    }

                }
            });

            cbPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        countPrivate = String.valueOf(Utils.mPrivate);
                    }else {
                        countPrivate ="";
                    }

                }
            });

            cbGovernment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        countGovernment = String.valueOf(Utils.mGovernment);
                    }else {
                        countGovernment ="";
                    }
                }
            });

            /**String array split managments and checkbox checked or not**/
            for(int i=0; i < arrayString.length; i++){
                Utils.printLog("management arrayString",arrayString[i]);
                tempChkValue=arrayString[i];
                if(tempChkValue.equals(Utils.mPublic))
                    cbPublic.setChecked(true);

                if(tempChkValue.equals(Utils.mPrivate))
                    cbPrivate.setChecked(true);

                if(tempChkValue.equals(Utils.mGovernment))
                    cbGovernment.setChecked(true);


            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(dialogView);
            Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
            Button btnOk = (Button) dialogView.findViewById(R.id.btnApply);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null != countPublic && !countPublic.isEmpty() || null != countPrivate && !countPrivate.isEmpty() || null != countGovernment && !countGovernment.isEmpty())
                        management = countPublic+","+countPrivate+","+countGovernment;
                    else
                        management="";
                    getSchoolList(count,management);
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    isDialogVisible = false;
                }
            });

            dialog = builder.create();
            dialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }}




    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            try{
                if(onSearch==true){
                    if (LocationAutoCompleteTv!=null){
                        pageNo = 1;
                        LocationAutoCompleteTv.setAdapter(new PlaceAutoCompleteAdapter(mContext, R.layout.simple_expandable_list_item_1));
                        onSearch=false;
                    }else{Utils.showToast("Enter Location",mContext);}
                }else
                    userCurrentLocation();
                if(0.0 != userLatitude && 0.0 != userLongitude){
                    userAddress = Utils.getAddressFromLatLng(mContext, String.valueOf(userLatitude), String.valueOf(userLongitude));
                    LocationAutoCompleteTv.setAdapter(null);
                    LocationAutoCompleteTv.setText(userAddress);
                    Utils.printLog("user address from latlong",userAddress);

                }

            }catch (Exception e){e.printStackTrace();}
        }
    }
    private void getSchoolList(String count, String managementType){

        try {
            schoolListPojoArrayList.clear();
            String user_lat = String.valueOf(userLatitude);
            String user_longi = String.valueOf(userLongitude);
             HashMap<String,String> schoolDetails = new HashMap<>();
                schoolDetails.put(KeyConstants.BOARD_ID, "");
                schoolDetails.put(KeyConstants.SCHOOL_NAME, LocationAutoCompleteTv.getText().toString().trim());
                schoolDetails.put(KeyConstants.PAGE_NUMBER, (pageNo + ""));
                schoolDetails.put(KeyConstants.IS_SCHOOL, String.valueOf(count));
                schoolDetails.put(KeyConstants.MANAGEMENT_ID,managementType);
            if(LocationAutoCompleteTv.getText().toString().length()>0){
                if (null != user_lat && !("").equalsIgnoreCase(user_lat) && null != user_longi && !("").equalsIgnoreCase(user_longi) && !("0.0").equalsIgnoreCase(user_lat) && !("0.0").equalsIgnoreCase(user_longi)) {
                    schoolDetails.put(KeyConstants.LATITUDE, user_lat);
                    schoolDetails.put(KeyConstants.LONGITUDE, user_longi);
                }}
            Utils.printLog("Webservice params : ", schoolDetails.toString());
            if(pageNo == 1)
                new VolleyWebserviceCall().volleyPostCall(mContext, KeyConstants.SCHOOL_LISTING_URL, schoolDetails, SearchSchoolActivity.this, true);
            else
                new VolleyWebserviceCall().volleyPostCall(mContext, KeyConstants.SCHOOL_LISTING_URL, schoolDetails, SearchSchoolActivity.this, false);

        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    try{
                        latLng();
                        userCurrentLocation();
                        if(onSearch==true){
                            if (LocationAutoCompleteTv!=null){
                                pageNo = 1;
                                LocationAutoCompleteTv.setAdapter(new PlaceAutoCompleteAdapter(mContext, R.layout.simple_expandable_list_item_1));
                                onSearch=false;
                            }else{Utils.showToast("Enter Location",mContext);}
                        }else

                            userCurrentLatitude=manager.getCurrentLatitude();
                        userCurrentLongitude=manager.getCurrentLongitude();
                        if(0.0 != userCurrentLatitude && 0.0 != userCurrentLongitude){
                            Utils.setSharedPreferenceLat(mContext,KeyConstants.LAT,userCurrentLatitude+"");
                            Utils.setSharedPreferenceLong(mContext, KeyConstants.LONG, userCurrentLongitude + "");
                            userAddress = Utils.getAddressFromLatLng(mContext, String.valueOf(userCurrentLatitude), String.valueOf(userCurrentLongitude));
                            LocationAutoCompleteTv.setText(userAddress);
                            Utils.printLog("user address from lat long",userAddress);
                        }
                    }catch (Exception e){e.printStackTrace();}

                    break;
            }
        }else {
            if(onSearch==true){
                if (LocationAutoCompleteTv!=null){
                    pageNo = 1;

                    onSearch=false;
                }
            }
        }
    }

    @Override
    public void onSuccess(String serverResult, String requestTag, int statusCode) {
        try {

            Gson gson = new Gson();
            JSONObject obj;
            String json;
            if (requestTag.contains(KeyConstants.SCHOOL_LISTING_URL)) {
                if (Utils.SUCCESS == statusCode) {
                    if(isDialogVisible){
                        dialog.dismiss();
                        isDialogVisible=false;
                    }
                    rvSchooLlist.setVisibility(View.VISIBLE);
                    GetSchoolListResultPojo myPojo = gson.fromJson(serverResult, GetSchoolListResultPojo.class);
                    if(null!=myPojo.getResult()){

                        schoolListPojoArrayList.addAll(myPojo.getResult());
                        schoolListAdapter.notifyDataSetChanged();
                        noResultFoundTv.setVisibility(View.GONE);
                        rvSchooLlist.setVisibility(View.VISIBLE);}
                    else{
                        rvSchooLlist.setVisibility(View.GONE);
                        noResultFoundTv.setVisibility(View.VISIBLE);}
                    Utils.printLog("school list Response: ", serverResult);
                }else if (Utils.FAILURE == statusCode){
                    json = serverResult;
                    obj = new JSONObject(json);
                    json = obj.getString("message");
                    if (null != json)
                        Utils.showToast(json, mContext);


                }
            }
            if (requestTag.contains(KeyConstants.GET_BOARD_LISTING)) {
                if (Utils.SUCCESS == statusCode) {
                    GetBoardListPojo getBoardListPojo = gson.fromJson(serverResult, GetBoardListPojo.class);
                    setBoardsDataIntoDB(getBoardListPojo);
                }else if (Utils.FAILURE == statusCode) {
                    json = serverResult;
                    obj = new JSONObject(json);
                    json = obj.getString("message");
                    if (null != json)
                        Utils.showToast(json, mContext);
                }

            }

        }catch (Exception e){e.printStackTrace();}
    }

    private void setBoardsDataIntoDB(GetBoardListPojo myPojo) {
        db.clearData();
        db.addAllBoardsToDB(myPojo.getResult());
        //addSpinner();

    }


    @Override
    public void onError(String serverResult, String requestTag, int statusCode) {
        Utils.showToast("Response error", mContext);
    }
}
