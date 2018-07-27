package com.equipesoftwares.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.equipesoftwares.Pojo.Achivements;
import com.equipesoftwares.Pojo.Activities;
import com.equipesoftwares.Pojo.Facilities;
import com.equipesoftwares.Pojo.Images;
import com.equipesoftwares.Pojo.SchoolDetailPojo;
import com.equipesoftwares.Pojo.SchoolListPojo;
import com.equipesoftwares.R;
import com.equipesoftwares.common.Dialogs;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.equipesoftwares.services.AppController;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Monali on 8/3/2017.
 */

public class AchievementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Object> favcommonarray;
    private final int LIST = 0, HEADER = 1;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ImageLoader mImageLoader;



    public AchievementsAdapter(Context mContext, List<Object> favcommonarray) {
        try {
            mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.mContext = mContext;
//        this.isPrevious=isPrevious;
            this.favcommonarray = favcommonarray;
            Utils.printLog("monali array size in constructor print",""+this.favcommonarray.size());
            mImageLoader = AppController.getInstance().getImageLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        try {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            switch (viewType) { // Heterogeneous Layouts inside RecyclerView
                case LIST:
                    View v1 = inflater.inflate(R.layout.achivementschildcard, viewGroup, false);
                    viewHolder = new ViewHolder1(v1);
                    break;

                case HEADER:
                    View v2 = inflater.inflate(R.layout.view_list_header, viewGroup, false);
                    viewHolder = new ViewHolder2(v2);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        try {
            switch (viewHolder.getItemViewType()) {
                case LIST:
                    ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                    configureViewHolder1(vh1, position);
                    break;

                case HEADER:
                    ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                    configureViewHolder2(vh2, position);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureViewHolder2(ViewHolder2 vh2, int position) {
        try {
            String header = (String) favcommonarray.get(position);
            vh2.getTextView().setText(header);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        try {
            if (favcommonarray.get(position) instanceof Achivements) {

                return LIST;
            } else if (favcommonarray.get(position) instanceof Activities) {

                return LIST;
            } else if (favcommonarray.get(position) instanceof Facilities) {

                return LIST;
            }
            else if (favcommonarray.get(position) instanceof String) {
                return HEADER;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void configureViewHolder1(final ViewHolder1 vh1, final int position) {
        try {

            if (favcommonarray.get(position) instanceof Achivements){

            final Achivements achivements = (Achivements) favcommonarray.get(position);
                vh1.cv.setTag(achivements);

                vh1.Achievementstv.setText(achivements.getAchivements());
                vh1.AchievementsLogo.setDefaultImageResId(R.drawable.my_future_logo);
                //
                if (null != achivements.getAchievement_images().get(0).getImage() && !achivements.getAchievement_images().get(0).getImage().isEmpty()) {
                    vh1.AchievementsLogo.setImageUrl(KeyConstants.GET_ALL_IMAGES + Utils.getSharedPreference(mContext, KeyConstants.SP_SCHOOL_ID)+"/"+"achievements/"+achivements.getAchievement_images().get(0).getImage(), mImageLoader);
                }else {
                    vh1.AchievementsLogo.setImageUrl("", AppController.getInstance().getImageLoader());
                }
                vh1.AchievementsLogo.setErrorImageResId(R.drawable.my_future_logo);

             }
            if (favcommonarray.get(position) instanceof Activities){
                final Activities activities = (Activities) favcommonarray.get(position);
                vh1.cv.setTag(activities);

                vh1.Achievementstv.setText(activities.getActivities());
                vh1.AchievementsLogo.setDefaultImageResId(R.drawable.my_future_logo);
                if (null != activities.getActivity_images().get(0).getImage() && !activities.getActivity_images().get(0).getImage().isEmpty()) {
                    vh1.AchievementsLogo.setImageUrl(KeyConstants.GET_ALL_IMAGES +Utils.getSharedPreference(mContext, KeyConstants.SP_SCHOOL_ID)+"/"+"activities/"+activities.getActivity_images().get(0).getImage(),mImageLoader);

                }else {
                    vh1.AchievementsLogo.setImageUrl("", AppController.getInstance().getImageLoader());
                }
                vh1.AchievementsLogo.setErrorImageResId(R.drawable.my_future_logo);

            }
            if(favcommonarray.get(position) instanceof Facilities){
                final Facilities facilities = (Facilities) favcommonarray.get(position);
                vh1.cv.setTag(facilities);

                vh1.Achievementstv.setText(facilities.getFacilities());
                vh1.AchievementsLogo.setDefaultImageResId(R.drawable.my_future_logo);
                if (null != facilities.getFacility_images().get(0).getImage() && !facilities.getFacility_images().get(0).getImage().isEmpty()) {
                    vh1.AchievementsLogo.setImageUrl(KeyConstants.GET_ALL_IMAGES +Utils.getSharedPreference(mContext, KeyConstants.SP_SCHOOL_ID)+"/"+"facilities/"+facilities.getFacility_images().get(0).getImage(),mImageLoader);

                }else {
                    vh1.AchievementsLogo.setImageUrl("", AppController.getInstance().getImageLoader());
                }
                vh1.AchievementsLogo.setErrorImageResId(R.drawable.my_future_logo);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return favcommonarray == null ? 0 : favcommonarray.size();

    }


    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View convertView;
        private ArrayList<Images> mediaArrayList = new ArrayList<>();
        private  Object oImage;
        private  CardView cv;
        private TextView Achievementstv;
        private NetworkImageView AchievementsLogo;


        public ViewHolder1(final View itemView) {

            super(itemView);

            convertView = itemView;
            Achievementstv = (TextView) itemView.findViewById(R.id.tvachievements);
            AchievementsLogo = (NetworkImageView) itemView.findViewById(R.id.CardImgAchieve);
            cv = (CardView) itemView.findViewById(R.id.cvAchieve);
            cv.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            Object data = v.getTag();
            if(data instanceof Activities){
                Activities activityPojo=(Activities)v.getTag();
                oImage =activityPojo.getActivity_images();
                mediaArrayList = (ArrayList<Images>) oImage;
                Dialogs.dialogForViewFullImages(mediaArrayList,activityPojo.getType(),mContext);
            }
            if(data instanceof Achivements){
                Achivements achivements=(Achivements) v.getTag();
                oImage =achivements.getAchievement_images();
                mediaArrayList = (ArrayList<Images>) oImage;
                Dialogs.dialogForViewFullImages(mediaArrayList,achivements.getType(),mContext);
            }
            if(data instanceof Facilities){
                Facilities facilities=(Facilities) v.getTag();
                oImage =facilities.getFacility_images();
                mediaArrayList = (ArrayList<Images>) oImage;
                Dialogs.dialogForViewFullImages(mediaArrayList,facilities.getType(),mContext);
            }

        }
    }

        // ViewHolder Class For HEADERS
        public class ViewHolder2 extends RecyclerView.ViewHolder {
            public final View convertView;
            TextView tvHeader;

            public ViewHolder2(View view) {
                super(view);
                convertView = view;
                tvHeader = (TextView) convertView.findViewById(R.id.tvHeader);
            }

            public TextView getTextView() {
                return tvHeader;
            }

            public void setTextView(TextView tvHeader) {
                this.tvHeader = tvHeader;
            }
        }



}