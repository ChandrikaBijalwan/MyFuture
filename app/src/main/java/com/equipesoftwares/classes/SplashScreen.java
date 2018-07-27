package com.equipesoftwares.classes;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.equipesoftwares.R;

public class SplashScreen extends AppCompatActivity {
    private Context mContext;
    private static int SPLASH_TIME_OUT = 3000;
    private Intent intent;
    private WebView webviewActionView;
    private GLSurfaceView glView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext=this;

        webviewActionView = (WebView)findViewById(R.id.webviewActionView);
        webviewActionView.getSettings().setLoadsImagesAutomatically(true);
        webviewActionView.getSettings().setJavaScriptEnabled(true);
        webviewActionView.getSettings().setUseWideViewPort(true);
        webviewActionView.setBackgroundColor(getResources().getColor(R.color.black));
        WebSettings settings = webviewActionView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        // Set scale on devices that supports it
//        webviewActionView.loadUrl("file:///android_asset/splash_boy_with_pencil_gif.gif");
        webviewActionView.loadUrl("http://www.animatedimages.org/data/media/385/animated-teacher-image-0049.gif");

        splash();
    }
    private void splash() {

       // holladailLogo=(ImageView)findViewById(R.id.HolladailLogo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try{
                        intent = new Intent(mContext,LandingScreenActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){e.printStackTrace();}


            }
        }, SPLASH_TIME_OUT);

    }
}
