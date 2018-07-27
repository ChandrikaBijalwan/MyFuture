package com.equipesoftwares.classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equipesoftwares.Pojo.SchoolDetailPojo;
import com.equipesoftwares.R;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.equipesoftwares.google.direction.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;


/**
 * Created by equipe on 7/6/2017.
 */

public class ContactFragment extends Fragment implements OnMapReadyCallback {

    private Context mContext;
    private TextView tvAddress,tvEmail,tvWebsite,tvPhone,tvCity;
    private SchoolDetailPojo resultPojo;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private double latitude,longitude;
    private LatLng position;
    private String mapTitle;
    private ProgressDialog progressDialog;
    private View mView;
    private LinearLayout rlContact;
    private  RelativeLayout rlMapFragment;
    private MapView mapFrag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            // Inflate the layout for this fragment
           mView = inflater.inflate(R.layout.contact_fragment, container, false);
            mapFrag = (MapView) mView.findViewById(R.id.map);
            mapFrag.onCreate(savedInstanceState);
            mapFrag.getMapAsync(this);

            Utils.printLog("contact fragment mapasync","contact fragment mapasync");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{

            setClasses();
            setScreenControls(view);
//            initializeMap();
            getSavedSchoolDetails();
        }catch (Exception e){e.printStackTrace();}
    }

/** go to connectGoogleClient asynktask*/

    public void onResume(){
        super.onResume();
        // Set title bar
        if(mapFrag!=null)
            mapFrag.onResume();
        ((HomeActivity) getActivity())
                .setActionBarTitle(mContext.getResources().getString(R.string.contact_title));

    }




    private void getSavedSchoolDetails() {
        try{

            Utils.printLog("contact fragment getSavedSchoolDetails","contact fragment getSavedSchoolDetails");
            Gson gson = new Gson();
            resultPojo = gson.fromJson(Utils.getSavedPojoResponseReturnArray(mContext, KeyConstants.SCHOOL_DETAILS),SchoolDetailPojo.class);
//            new GetLocationTask( mContext).execute();
            if(null != resultPojo) {
                rlContact.setVisibility(View.VISIBLE);
                latitude = Double.parseDouble(resultPojo.getLatitude());
                longitude = Double.parseDouble(resultPojo.getLongitude());
                mapTitle = resultPojo.getName();
                if(null != resultPojo.getAddress() && !("".equalsIgnoreCase(resultPojo.getAddress())))
                    tvAddress.setText(resultPojo.getAddress());
                else
                    tvAddress.setText("-NA-");

                if(null != resultPojo.getEmail() && !("".equalsIgnoreCase(resultPojo.getEmail())))
                    tvEmail.setText(resultPojo.getEmail());
                else
                    tvEmail.setText("-NA-");


                if(null != resultPojo.getWebsite() && !("".equalsIgnoreCase(resultPojo.getWebsite())))
                     tvWebsite.setText(resultPojo.getWebsite());
                else
                     tvWebsite.setText("-NA-");

                if(null != resultPojo.getPhone_no() && !("".equalsIgnoreCase(resultPojo.getPhone_no())))
                    tvPhone.setText(resultPojo.getPhone_no());
                else
                    tvPhone.setText("-NA-");
                if(null != resultPojo.getCity() && !("".equalsIgnoreCase(resultPojo.getCity())))
                    tvCity.setText(resultPojo.getCity());
                else
                    tvCity.setText("-NA-");


            }

        }catch (Exception e){e.printStackTrace();}
    }


    private void setClasses() {
        try{
        mContext=this.getActivity();

        }catch (Exception e){e.printStackTrace();}

    }

    private void setScreenControls(View view) {
        try{

        tvAddress=(TextView)view.findViewById(R.id.tvAddress);
        tvEmail=(TextView)view.findViewById(R.id.tvEmail);
        tvWebsite=(TextView)view.findViewById(R.id.tvWebsite);
        tvPhone=(TextView)view.findViewById(R.id.tvPhone);
        tvCity=(TextView)view.findViewById(R.id.tvCity);
            rlContact=(LinearLayout)view.findViewById(R.id.rlContact);
            rlMapFragment=(RelativeLayout)view.findViewById(R.id.rlMapFragment);

        }catch (Exception e){e.printStackTrace();}

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map = googleMap;


            googleMap.getUiSettings().setMapToolbarEnabled(true);
            Utils.printLog("contact fragment onMapReady","contact fragment onMapReady");
            try {
                if(0.0 != latitude && 0.0 != longitude){
                    position = new LatLng(latitude,longitude);
                }
                MarkerOptions mo = new MarkerOptions();
                mo.position(position);
                mo.title(mapTitle);

                CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(mo.getPosition());

                LatLngBounds bounds = builder.build();
                int padding = 0; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                map.moveCamera(cu);
                if (map != null) {
                    map.addMarker(mo);
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map.animateCamera(zoom);
                }
                ((HomeActivity)getActivity()).progressDialog.dismiss();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }


        }catch (Exception e){e.printStackTrace();}
    }


    @Override
    public void onPause() {
        super.onPause();
        mapFrag.onPause();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFrag.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFrag.onLowMemory();
    }

}
