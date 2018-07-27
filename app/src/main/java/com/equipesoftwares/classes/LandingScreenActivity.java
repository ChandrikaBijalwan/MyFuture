package com.equipesoftwares.classes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.equipesoftwares.R;
import com.equipesoftwares.adapters.ViewPagerAdapter;
import com.equipesoftwares.adapters.ViewPagerModel;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class LandingScreenActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;
    private Button btnGetStarted;



    private AutoScrollViewPager viewPager;
    ArrayList<ViewPagerModel> arrList = new ArrayList<ViewPagerModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_pager_indicator);
        try {
            setClasses();
            setScreenControls();
            initArrayList();
            setAdapter();
            setListeners();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setScreenControls() {
        try {
            btnGetStarted = (Button) findViewById(R.id.GetStartedbtn);
            viewPager = (AutoScrollViewPager) findViewById(R.id.pager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClasses() {
        mContext = this;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.GetStartedbtn:
                Intent i=new Intent(LandingScreenActivity.this,SearchSchoolActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
    private void setAdapter() {
        try{
            ViewPagerAdapter adapter = new ViewPagerAdapter(mContext, arrList);
            viewPager.setAdapter(adapter);
            viewPager.startAutoScroll();
            viewPager.setInterval(3000);

            CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
            pageIndicator.setViewPager(viewPager);
            pageIndicator.setStrokeColor(getResources().getColor(R.color.default_circle_indicator_fill_color));
            pageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {


                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initArrayList() {
        try{
            arrList.add(new ViewPagerModel("Education is the most powerful weapon which you can use to change the world.",R.drawable.school1));
            arrList.add(new ViewPagerModel("Education is what remains after one has forgotten what one has learned in school.",R.drawable.school2));
            arrList.add(new ViewPagerModel("Education is not preparation for life; education is life itself.",R.drawable.school3));
            arrList.add(new ViewPagerModel("Education is the foundation upon which we build our future.",R.drawable.school4));
            arrList.add(new ViewPagerModel("Education is the movement from darkness to light.",R.drawable.school5));

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setListeners() {
        try {
            btnGetStarted.setOnClickListener(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
