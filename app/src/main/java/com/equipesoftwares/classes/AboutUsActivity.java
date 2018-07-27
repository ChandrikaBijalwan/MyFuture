package com.equipesoftwares.classes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.equipesoftwares.R;

/**
 * Created by equipe on 7/18/2017.
 */

public class AboutUsActivity extends AppCompatActivity {

    private Context mContext;
    private TextView tvCompanyPhone,tvCompanyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);
        try{
            setClasses();
            setScreenControls();
            setAdapter();
            setListners();
        }catch (Exception e){e.printStackTrace();}
    }

    private void setScreenControls() {
        try{
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCompanyPhone=(TextView)findViewById(R.id.tvCompanyPhone);
        tvCompanyEmail=(TextView)findViewById(R.id.tvCompanyEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        try{
                tvCompanyPhone.setText(getResources().getString(R.string.company_phone_no));
                tvCompanyEmail.setText(getResources().getString(R.string.company_email));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListners() {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setClasses() {
        mContext=this;
    }
}
