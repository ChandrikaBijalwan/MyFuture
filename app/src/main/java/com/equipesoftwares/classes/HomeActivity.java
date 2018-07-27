package com.equipesoftwares.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.equipesoftwares.Pojo.Images;
import com.equipesoftwares.Pojo.ResultPojo;
import com.equipesoftwares.R;
import com.equipesoftwares.adapters.SlidingNetworkImageAdapter;
import com.equipesoftwares.adapters.ViewPagerModel;
import com.equipesoftwares.common.CircularNetworkImageView;
import com.equipesoftwares.common.Dialogs;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.equipesoftwares.services.AppController;
import com.equipesoftwares.services.VolleyWebserviceCall;
import com.equipesoftwares.services.VolleyWebserviceCallBack;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
/**
 * Created by Monali on 7/6/2017.
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,VolleyWebserviceCallBack,View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View view;
    private AutoScrollViewPager homeViewpager;
    private ArrayList<ViewPagerModel> arrList = new ArrayList<ViewPagerModel>();
    private TextView tvTitle,navHeader;
    private FragmentTransaction fragmentTransaction;
    private CollegeDetailFragment collegeDetailFragment;
    private SchoolDetailFragment schoolDetailFragment;
    private AchieveAndActivityFragment achieveAndActivityFragment;
    private FacilityFragment facilityFragment;
    private ContactFragment contactFragment;
    private Intent i;
    private String mCurrentFragment = "";
    private String school_id;
    private Bundle bundle;
    private NetworkImageView ivMainLogo;
    private  ResultPojo   serverresult_pojo;
    private   SlidingNetworkImageAdapter slidingNetworkImageAdapter;
    private  Object oImage;
    private ArrayList<Images> mediaArrayList = new ArrayList<>();

    private CircularNetworkImageView ivnav_app_logo;
    private boolean isSchool;
    private   Toolbar toolbar;
    public ProgressDialog progressDialog;
    public Fragment mFragmentToSet = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try{
            setClasses();
            setScreenControls();
            setListeners();
            bundle=getIntent().getExtras();
            school_id= String.valueOf(getIntent().getIntExtra(KeyConstants.SCHOOL_ID, 0));
            if(school_id!=null && !school_id.isEmpty())
            getSchoolDetailsWebserivce();



        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getSchoolDetailsWebserivce() {
        try{

        HashMap<String,String> schoolParamter =  new HashMap<>();
        schoolParamter.put(KeyConstants.WS_SCHOOL_ID,school_id);
        new VolleyWebserviceCall().volleyPostCall(mContext, KeyConstants.GET_SCHOOL_DETAILS, schoolParamter, HomeActivity.this, true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        try{

            slidingNetworkImageAdapter = new SlidingNetworkImageAdapter(mContext,mediaArrayList,Utils.FLAG_HOMEIMAGES_SCROLL);
            homeViewpager.setAdapter(slidingNetworkImageAdapter);
            homeViewpager.startAutoScroll();
            homeViewpager.setInterval(3000);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setScreenControls() {

        try{
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));

            if (toolbar != null) {
                setSupportActionBar(toolbar);}
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            //toolbar.setNavigationIcon(R.drawable.current_location);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            view=navigationView.inflateHeaderView(R.layout.nav_drawer_header);
            navHeader=(TextView)view.findViewById(R.id.tvNavigationHeader);
            ivnav_app_logo=(CircularNetworkImageView)view.findViewById(R.id.ivnav_app_logo);

            homeViewpager = (AutoScrollViewPager) findViewById(R.id.homeViewpager);
            tvTitle = (TextView)findViewById(R.id.tvTitle);
            ivMainLogo=(NetworkImageView)findViewById(R.id.ivMainLogo);


        }catch (Exception e){e.printStackTrace();}


    }

    private void setListeners() {
        try{
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
                navigationView.setNavigationItemSelectedListener(this);

        }catch (Exception e){e.printStackTrace();}
    }

    public void setCurrentFragment(String tagFrg) {
        mCurrentFragment = tagFrg;
    }

    public void showFragment(Fragment nextFragment) {
        try {
            mFragmentToSet=nextFragment;
            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override public void onDrawerSlide(View drawerView, float slideOffset) {}
                @Override public void onDrawerOpened(View drawerView) {}
                @Override public void onDrawerStateChanged(int newState) {

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    //Set your new fragment here
                    if (mFragmentToSet != null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout, mFragmentToSet)
                                .commit();
                        mFragmentToSet = null;
                    }
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setClasses() {
        mContext = this;
        collegeDetailFragment =  new CollegeDetailFragment();
        schoolDetailFragment=new SchoolDetailFragment();
        progressDialog = Dialogs.ShowProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);

    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.navAbout:
                if(isSchool){

                    schoolDetailFragment = new SchoolDetailFragment();
                    showFragment(schoolDetailFragment);

                }else{
                    collegeDetailFragment = new CollegeDetailFragment();
                    showFragment(collegeDetailFragment);

                }

                break;
            case R.id.navAchievementAndActivities:

                achieveAndActivityFragment = new AchieveAndActivityFragment();
                showFragment(achieveAndActivityFragment);
                break;
            case R.id.navFacilitiesAndInfrastructure:
                progressDialog.show();
                facilityFragment = new FacilityFragment();
                showFragment(facilityFragment);

                break;
            case R.id.navContact:
                progressDialog.show();
                contactFragment=new ContactFragment();
                showFragment(contactFragment);
                break;
            case R.id.navBackToSearch:

                Intent i = new Intent(mContext, SearchSchoolActivity.class);
                startActivity(i);
                break;
        }
        // update selected item and title, then close the drawer

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onSuccess(String serverResult, String requestTag, int statusCode) {
        Utils.printLog("serverResult",serverResult);
        try{
            Gson gson = new Gson();
            if (requestTag.contains(KeyConstants.GET_SCHOOL_DETAILS)) {
                serverresult_pojo = gson.fromJson(serverResult,ResultPojo.class);
                if (Utils.SUCCESS == statusCode) {
                    Utils.printLog("serverResult",""+serverresult_pojo.getResult().get(0).getPlacements());
                    String data = gson.toJson(serverresult_pojo.getResult());
                    Utils.setSharedPreference(mContext,KeyConstants.SCHOOL_DETAILS,data);
                    Utils.setSharedPreference(mContext,KeyConstants.SP_SCHOOL_ID,serverresult_pojo.getResult().get(0).getId());
                    setDetails(serverresult_pojo);
                    setAdapter();
//                    slidingNetworkImageAdapter.notifyDataSetChanged();

                }
            }
        }catch (Exception e){e.printStackTrace();}

    }

    private void setDetails(ResultPojo resultPojo) {
        try{
            if(null != resultPojo){

                isSchool=  resultPojo.getResult().get(0).getIs_school();
                if(isSchool){
                    setCurrentFragment(KeyConstants.SCHOOL_FRAGMENT);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction = transaction.replace(R.id.frameLayout, schoolDetailFragment);
                    transaction.commitAllowingStateLoss();
                }else{
                    setCurrentFragment(KeyConstants.COLLEGE_FRAGMENT);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction = transaction.replace(R.id.frameLayout, collegeDetailFragment);
                    transaction.commitAllowingStateLoss();
                }
                oImage =resultPojo.getResult().get(0).getImages();
                mediaArrayList = (ArrayList<Images>) oImage;
                navHeader.setText(resultPojo.getResult().get(0).getName());
                ivnav_app_logo.setDefaultImageResId(R.drawable.my_future_logo);
                if (null != resultPojo.getResult().get(0).getLogo() && !resultPojo.getResult().get(0).getLogo().isEmpty()) {
                    ivnav_app_logo.setImageUrl(KeyConstants.GET_ALL_IMAGES+Utils.getSharedPreference(mContext, KeyConstants.SP_SCHOOL_ID)+"/"+resultPojo.getResult().get(0).getLogo(), AppController.getInstance().getImageLoader());
                }else {
                    ivnav_app_logo.setImageUrl("", AppController.getInstance().getImageLoader());
                }
                ivnav_app_logo.setErrorImageResId(R.drawable.my_future_logo);

                tvTitle.setText(resultPojo.getResult().get(0).getName());
                ivMainLogo.setDefaultImageResId(R.drawable.my_future_logo);
                if (null != resultPojo.getResult().get(0).getLogo() && !resultPojo.getResult().get(0).getLogo().isEmpty()) {
                    ivMainLogo.setImageUrl(KeyConstants.GET_ALL_IMAGES+Utils.getSharedPreference(mContext, KeyConstants.SP_SCHOOL_ID)+"/"+resultPojo.getResult().get(0).getLogo(), AppController.getInstance().getImageLoader());
                }else {
                    ivMainLogo.setImageUrl("", AppController.getInstance().getImageLoader());
                }
                ivMainLogo.setErrorImageResId(R.drawable.my_future_logo);
            }
        }catch (Exception e){e.printStackTrace();}
    }


    @Override
    public void onError(String serverResult, String requestTag, int statusCode) {
        Utils.showToast(serverResult,mContext);
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
            i = new Intent(mContext,AppInformationActivity.class);
            startActivity(i);
            return true;
        }else if(id == R.id.about_us){
            i = new Intent(mContext,AboutUsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);}

    @Override
    public void onClick(View v) {
        try{
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onRefresh() {



    }
}