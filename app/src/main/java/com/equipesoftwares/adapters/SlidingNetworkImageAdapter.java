package com.equipesoftwares.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.equipesoftwares.Pojo.Images;
import com.equipesoftwares.R;
import com.equipesoftwares.common.Dialogs;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.equipesoftwares.services.AppController;

import java.util.ArrayList;

/**
 * Created by Monali on 1/8/2018.
 */

public class SlidingNetworkImageAdapter extends android.support.v4.view.PagerAdapter  {
    private Context mContext;

    private ArrayList<Images> mediaArrayList;
    private LayoutInflater mLayoutInflater;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private NetworkImageView ivScrollingImage;

    private String imagesFor;
    private String baseURLimages;



    public SlidingNetworkImageAdapter(Context context, ArrayList<Images> mediaArrayList, String imagesFor) {
        this.mContext = context;
        this.mediaArrayList = mediaArrayList;
        this.imagesFor = imagesFor;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return mediaArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView;
        baseURLimages = KeyConstants.GET_ALL_IMAGES + Utils.getSharedPreference(mContext, KeyConstants.SP_SCHOOL_ID) + "/";
        itemView = mLayoutInflater.inflate(R.layout.scrolling_images, (ViewGroup) container, false);

        ivScrollingImage = (NetworkImageView) itemView.findViewById(R.id.ivScrollingImages);
        ivScrollingImage.setScaleType(ImageView.ScaleType.FIT_XY);
        ivScrollingImage.setDefaultImageResId(R.drawable.my_future_logo);
        if (imagesFor.equalsIgnoreCase (Utils.FLAG_HOMEIMAGES_SCROLL)) {
            if (!mediaArrayList.get(position).getImage().equalsIgnoreCase("")) {
                ivScrollingImage.setImageUrl(baseURLimages + mediaArrayList.get(position).getImage(), AppController.getInstance().getImageLoader());
            } else {
                ivScrollingImage.setImageUrl("", AppController.getInstance().getImageLoader());
            }

        } else if (imagesFor.equalsIgnoreCase (Utils.FLAG_ACHIEVEMENTS)) {
            if (!mediaArrayList.get(position).getImage().equalsIgnoreCase("")) {
                ivScrollingImage.setImageUrl(baseURLimages + "achievements/" + mediaArrayList.get(position).getImage(), AppController.getInstance().getImageLoader());
            } else {
                ivScrollingImage.setImageUrl("", AppController.getInstance().getImageLoader());
            }

        } else
            if (imagesFor.equalsIgnoreCase(Utils.FLAG_ACTIVITIES)) {
                if (!mediaArrayList.get(position).getImage().equalsIgnoreCase("")) {
                ivScrollingImage.setImageUrl(baseURLimages + "activities/" + mediaArrayList.get(position).getImage(), AppController.getInstance().getImageLoader());
            } else {
                ivScrollingImage.setImageUrl("", AppController.getInstance().getImageLoader());
            }
        }
        else if (imagesFor.equalsIgnoreCase (Utils.FLAG_FACILITIES)) {

                if (!mediaArrayList.get(position).getImage().equalsIgnoreCase("")) {
                ivScrollingImage.setImageUrl(baseURLimages + "facilities/" + mediaArrayList.get(position).getImage(), AppController.getInstance().getImageLoader());
            } else {
                ivScrollingImage.setImageUrl("", AppController.getInstance().getImageLoader());
            }
        }
        ivScrollingImage.setErrorImageResId(R.drawable.my_future_logo);
        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
