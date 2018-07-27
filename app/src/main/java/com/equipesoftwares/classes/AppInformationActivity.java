package com.equipesoftwares.classes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.equipesoftwares.R;

/**
 * Created by equipe on 7/18/2017.
 */

public class AppInformationActivity extends AppCompatActivity {

    private Context mContext;
    private TextView tvVersion,tvNote;
    private TextView tvCompanyPhone,tvCompanyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_information_activity);
        try{
            setClasses();
            setScreenControls();
            setAdapter();
            setListners();
        }catch (Exception e){e.printStackTrace();}
    }

    private void setScreenControls() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvVersion=(TextView)findViewById(R.id.tvVersion);
        tvNote=(TextView)findViewById(R.id.tvNote);
        tvCompanyPhone=(TextView)findViewById(R.id.tvPhone);
        tvCompanyEmail=(TextView)findViewById(R.id.tvEmail);

    }


    private void setAdapter() {

    }

    private void setListners() {
    tvVersion.setText("1.0");
    String phone,email;
            phone=getResources().getString(R.string.company_phone_no);
            email=getResources().getString(R.string.company_email);
        tvNote.setMovementMethod(LinkMovementMethod.getInstance());
    tvNote.setText(getResources().getString(R.string.note_text));
    tvCompanyPhone.setText(phone);
    tvCompanyEmail.setText(email);

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
