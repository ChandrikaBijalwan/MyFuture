package com.equipesoftwares.classes;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.equipesoftwares.Pojo.SchoolDetailPojo;
import com.equipesoftwares.R;
import com.equipesoftwares.adapters.AchievementsAdapter;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Monali Achievements and activities on 7/6/2017.
 */

    public class AchieveAndActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
        private RecyclerView recyclerview;
        private Context mContext;
        private TextView tvNoRecordsFound,tvActivities,tvAchievements;
        private List<Object> commonarray = new ArrayList<>();
        private List<Object> activitiesArray = new ArrayList<>();
        private  List<Object> achivementsArray = new ArrayList<>();
//        private int pageNo = 1;
        private EndlessScrollListener scrollListner = null;
        private  SchoolDetailPojo  resultPojo;
//        private SwipeRefreshLayout swipeRefreshLayout;
        private boolean isAchievement=true;

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            try {
                setClasses();
                setScreenControls(view);
                setAdapter();
                setListener();
                getSavedSchoolDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            container.removeAllViews();
            return inflater.inflate(R.layout.achivements_activities_layout, null);


        }

        private void setClasses() {
            mContext = this.getActivity();
        }

        private void setScreenControls(View view) {
            try {
                recyclerview = (RecyclerView) view.findViewById(R.id.rvCommon);
                tvNoRecordsFound = (TextView) view.findViewById(R.id.tvNoRecordsFound);
                tvAchievements=(TextView)view.findViewById(R.id.tvAchievements);
                tvActivities=(TextView)view.findViewById(R.id.tvActivities);
//                swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
//                swipeRefreshLayout.setProgressViewOffset(false, 0, 100);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public void onResume(){
        super.onResume();

        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle(mContext.getResources().getString(R.string.achievement_and_activities_title));

    }
    private void setAdapter() {
        try {
            AchievementsAdapter commonAchivementsAdapter = new AchievementsAdapter(mContext, commonarray);
            recyclerview.setAdapter(commonAchivementsAdapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setListener() {
        try {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            recyclerview.setLayoutManager(linearLayoutManager);
            scrollListner = new EndlessScrollListener(linearLayoutManager);
            recyclerview.addOnScrollListener(scrollListner);
//            swipeRefreshLayout.setOnRefreshListener(this);
            recyclerview.setNestedScrollingEnabled(false);
            tvAchievements.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAchievement=true;
                    tvAchievements.setTextColor(getResources().getColor(R.color.black));
                    tvActivities.setTextColor(getResources().getColor(R.color.white));
                    clearArray();
                    ((AchievementsAdapter) recyclerview.getAdapter()).notifyDataSetChanged();
                    setAchievementList(resultPojo);



                }
            });
            tvActivities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAchievement=false;
                    clearArray();
                    tvActivities.setTextColor(getResources().getColor(R.color.black));
                    tvAchievements.setTextColor(getResources().getColor(R.color.white));
                    ((AchievementsAdapter) recyclerview.getAdapter()).notifyDataSetChanged();
                    setActivityList(resultPojo);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void getSavedSchoolDetails() {
            try {
                Gson gson = new Gson();
                resultPojo = gson.fromJson(Utils.getSavedPojoResponseReturnArray(mContext, KeyConstants.SCHOOL_DETAILS), SchoolDetailPojo.class);
                if(isAchievement)
                setAchievementList(resultPojo);
                else
                    setActivityList(resultPojo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setAchievementList(SchoolDetailPojo resultPojo) {

            try {
                    if ((resultPojo.getAchivements().size() == 0) ) {
                        tvNoRecordsFound.setVisibility(View.VISIBLE);
                    }
                if (resultPojo.getAchivements().size() > 0) {
                    achivementsArray.addAll(resultPojo.getAchivements());
                }
                commonarray.clear();
                commonarray.addAll(achivementsArray);
                Utils.printLog("array commonarray: ", "" + commonarray.size());
            ((AchievementsAdapter) recyclerview.getAdapter()).notifyDataSetChanged();
                recyclerview.smoothScrollToPosition(0);
                ((HomeActivity)getActivity()).progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private void setActivityList(SchoolDetailPojo resultPojo) {

        try {
                if ( (resultPojo.getActivities().size() == 0)) {
                    tvNoRecordsFound.setVisibility(View.VISIBLE);
                }
                if (resultPojo.getActivities().size() > 0) {
                    activitiesArray.addAll(resultPojo.getActivities());
                }
            commonarray.clear();
            commonarray.addAll(activitiesArray);
            Utils.printLog("array commonarray: ", "" + commonarray.size());
            ((AchievementsAdapter) recyclerview.getAdapter()).notifyDataSetChanged();
            recyclerview.smoothScrollToPosition(0);
            ((HomeActivity)getActivity()).progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public void clearArray() {
            try {
                achivementsArray.clear();
                activitiesArray.clear();
                commonarray.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRefresh() {
            try {
//                swipeRefreshLayout.setEnabled(true);
//                swipeRefreshLayout.setRefreshing(true);
                Utils.cancelWebserviceRequest(mContext);
                clearArray();
                scrollListner.loading = true;
                scrollListner.previousTotal = 0;
                getSavedSchoolDetails();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        public class EndlessScrollListener extends RecyclerView.OnScrollListener {
            private int previousTotal = 0; // The total number of items in the dataset after the last load
            private boolean loading = true; // True if we are still waiting for the last set of data to load.
            private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
            int firstVisibleItem, visibleItemCount, totalItemCount;
            private LinearLayoutManager mLinearLayoutManager;

            public EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
                this.mLinearLayoutManager = linearLayoutManager;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {


                        loading = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
