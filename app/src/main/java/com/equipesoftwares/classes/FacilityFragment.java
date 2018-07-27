package com.equipesoftwares.classes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
 * Created by Monali facility list on 7/6/2017.
 */


public class FacilityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerview;
    private Context mContext;
    private TextView tvNoRecordsFound;
    private List<Object> commonarray = new ArrayList<>();
    private List<Object> facilityArray = new ArrayList<>();
    private EndlessScrollListener scrollListner = null;
    private AchievementsAdapter commonStickyAdapter;
    private int pageNo = 1;
    private SchoolDetailPojo resultPojo;
//    private SwipeRefreshLayout swipeRefreshLayout;



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
        return inflater.inflate(R.layout.common_list_with_header, null);


    }

    private void setClasses() {
        mContext = this.getActivity();
    }

    private void setScreenControls(View view) {
        try {

            recyclerview = (RecyclerView) view.findViewById(R.id.rvCommon);
            tvNoRecordsFound = (TextView) view.findViewById(R.id.tvNoRecordsFound);
//            swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
//            swipeRefreshLayout.setProgressViewOffset(false, 0, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle(mContext.getResources().getString(R.string.facilities_infrastructure_title));

    }
    private void setAdapter() {
        try {
            commonStickyAdapter = new AchievementsAdapter(mContext, commonarray);
            recyclerview.setNestedScrollingEnabled(false);
            recyclerview.setAdapter(commonStickyAdapter);
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
            recyclerview.setNestedScrollingEnabled(false);
            recyclerview.addOnScrollListener(scrollListner);
//            swipeRefreshLayout.setOnRefreshListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getSavedSchoolDetails() {
        try {
            Gson gson = new Gson();
            resultPojo = gson.fromJson(Utils.getSavedPojoResponseReturnArray(mContext, KeyConstants.SCHOOL_DETAILS), SchoolDetailPojo.class);
            if (null != resultPojo) {
                setFavList(resultPojo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFavList(SchoolDetailPojo resultPojo) {

        try {
//            swipeRefreshLayout.setRefreshing(false);

            if (pageNo == 1) {
                if (resultPojo.getFacilities().size() == 0) {
                    tvNoRecordsFound.setVisibility(View.VISIBLE);

                }
            }

            if (resultPojo.getFacilities().size() > 0) {
                facilityArray.addAll(resultPojo.getFacilities());

            }

            commonarray.clear();
            commonarray.addAll(facilityArray);
            if(commonarray.size()>1)
            commonStickyAdapter.notifyItemChanged(commonarray.size() -1);


            Utils.printLog("array facility commonarray: ", "" + commonarray.size());
            ((AchievementsAdapter) recyclerview.getAdapter()).notifyDataSetChanged();

            ((HomeActivity)getActivity()).progressDialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void clearArray() {
        try {
            facilityArray.clear();
            commonarray.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        try {
//            swipeRefreshLayout.setEnabled(true);
//            swipeRefreshLayout.setRefreshing(true);
            clearArray();
            getSavedSchoolDetails();
            scrollListner.loading = true;
            scrollListner.previousTotal = 0;
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
                    pageNo = pageNo + 1;

                    loading = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}