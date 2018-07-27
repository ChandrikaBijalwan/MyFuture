package com.equipesoftwares.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.equipesoftwares.Pojo.SchoolListPojo;
import com.equipesoftwares.R;
import com.equipesoftwares.classes.HomeActivity;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.equipesoftwares.services.AppController;

import java.util.ArrayList;

/**
 * Created by equipe on 7/6/2017.
 */

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.ViewHolder>{
    Context mContext;
    ArrayList<SchoolListPojo> result;
    int school_id;
    //    private ArrayList<PostImages> postImagesArrayList;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ImageLoader mImageLoader;




    public SchoolListAdapter(Context mContext, ArrayList<SchoolListPojo> arrResult) {
        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.mContext = mContext;
        this.result = arrResult;
        mImageLoader = AppController.getInstance().getImageLoader();
        //this.hotelRoomListFragment = hotelRoomListFragment;
    }
    @Override
    public SchoolListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listschool_layout, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SchoolListAdapter.ViewHolder holder, int position) {
        try{

            SchoolListPojo schoolListPojo = result.get(position);
            holder.cardviewScoolList.setTag(schoolListPojo);
            holder.SchoolNameTv.setText(schoolListPojo.getName());
            holder.Address.setText(schoolListPojo.getAddress()+","+schoolListPojo.getCity());
            holder.SchoolNameTv.setSelected(true);
            holder.SchoolNameTv.requestFocus();
            holder.SchoolNameTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.SchoolNameTv.setHorizontallyScrolling(true);
            holder.school_logo.setDefaultImageResId(R.drawable.my_future_logo);
            if (null != schoolListPojo.getLogo() && !schoolListPojo.getLogo().isEmpty()) {
                holder.school_logo.setImageUrl(KeyConstants.GET_ALL_IMAGES+schoolListPojo.getId()+"/"+schoolListPojo.getLogo(), AppController.getInstance().getImageLoader());
            }else {
                holder.school_logo.setImageUrl("", AppController.getInstance().getImageLoader());
            }
            holder.school_logo.setErrorImageResId(R.drawable.my_future_logo);
        //for caurse



        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView SchoolNameTv,Address;
        private NetworkImageView  school_logo;
        CardView cardviewScoolList;


        public ViewHolder(final View itemView) {
            super(itemView);
            SchoolNameTv=(TextView)itemView.findViewById(R.id.tvCardTitle);
            Address=(TextView)itemView.findViewById(R.id.tvCardSubtitle);
            //img_icon1=(ImageView)itemView.findViewById(R.id.schoolImg);
            school_logo=(NetworkImageView)itemView.findViewById(R.id.searchSchoolCardImg);
            cardviewScoolList=(CardView)itemView.findViewById(R.id.cv);
            cardviewScoolList.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            try {
                Utils.clearSharedPreferencesWithKey(mContext,KeyConstants.SCHOOL_DETAILS);
                SchoolListPojo schoolListPojo = (SchoolListPojo) v.getTag();
                school_id=schoolListPojo.getId();
                if(school_id !=0)
                    Utils.printLog("school id",""+school_id);
                Intent i = new Intent(v.getContext(), HomeActivity.class);
                i.putExtra(KeyConstants.SCHOOL_ID, school_id);
                v.getContext().startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
