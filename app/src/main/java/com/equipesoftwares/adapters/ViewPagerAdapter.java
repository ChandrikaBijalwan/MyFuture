package com.equipesoftwares.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipesoftwares.R;
import com.equipesoftwares.common.Utils;

import java.util.ArrayList;

import static com.equipesoftwares.common.Utils.calculateInSampleSize;


/**
 * Created by My_Machine on 18/09/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {
    Context mContext;
    private TextView tvTittle;
    private TextView tvContent;

    ImageView ivLandingImages;
    ArrayList<ViewPagerModel> arrList;
    LayoutInflater mLayoutInflater;

    // Constructor
    public ViewPagerAdapter(Context mContext, ArrayList<ViewPagerModel> arrList) {
        this.mContext = mContext;
        this.arrList = arrList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==  object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView;
        itemView = mLayoutInflater.inflate(R.layout.child_view_pager,(ViewGroup) container, false);
        try{
        tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        tvContent.setText(arrList.get(position).getDescription());
        ivLandingImages=(ImageView)itemView.findViewById(R.id.ivLandingImages);
        ivLandingImages.setScaleType(ImageView.ScaleType.FIT_XY);
        ivLandingImages.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(),arrList.get(position).getImage(),Utils.getDeviceWidth(mContext),Utils.getDeviceHeight(mContext)));
//        ivLandingImages.setImageResource(arrList.get(position).getImage());
        container.addView(itemView);
        finishUpdate(container);
        }catch (Exception e){e.printStackTrace();}
        return itemView;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        try{

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        }catch (Exception e){e.printStackTrace();}
        return BitmapFactory.decodeResource(res, resId, options);
    }
}


