package com.equipesoftwares.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equipesoftwares.Pojo.Achivements;
import com.equipesoftwares.Pojo.CommonIdValuePojo;
import com.equipesoftwares.Pojo.Courses;
import com.equipesoftwares.Pojo.Faculties;
import com.equipesoftwares.Pojo.GetBoardListPojo;
import com.equipesoftwares.Pojo.Placements;
import com.equipesoftwares.Pojo.SchoolDetailPojo;
import com.equipesoftwares.R;
import com.equipesoftwares.common.Database;
import com.equipesoftwares.common.Dialogs;
import com.equipesoftwares.common.KeyConstants;
import com.equipesoftwares.common.Utils;
import com.equipesoftwares.services.VolleyWebserviceCall;
import com.equipesoftwares.services.VolleyWebserviceCallBack;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by equipe on 7/6/2017.
 */

public class SchoolDetailFragment extends Fragment implements VolleyWebserviceCallBack {

    private Context mContext;
    private TextView tvSchoolAboutDescription,tvSchoolManagement,tvPrincipalName,tvSchoolBoard,tvSchoolMedium,tvSchoolClasses,tvVicePrincipal,tvFacultyName;
    private SchoolDetailPojo resultPojo;
    private int isPrincipal;
    private boolean isSchool;

    private Database db;
    private    GetBoardListPojo getBoardListPojo;
    private ArrayList<CommonIdValuePojo> resulArrayBoards;
    private RelativeLayout rlAboutDetail;
    private ProgressDialog progressDialog;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{

            setClasses();
            setScreenControls(view);
            setAdapter();
            setListners();
            getBoardListWS();
            Gson gson = new Gson();
            resultPojo = gson.fromJson(Utils.getSavedPojoResponseReturnArray(mContext, KeyConstants.SCHOOL_DETAILS),SchoolDetailPojo.class);
//            getSavedSchoolDetails();

        }catch (Exception e){e.printStackTrace();}
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();
        return inflater.inflate(R.layout.school_detail_fragment,null);


    }
    private void setClasses() {
        try{
        mContext=this.getActivity();
        db = new Database(mContext);
            progressDialog = Dialogs.ShowProgressDialog(mContext);
            progressDialog.setCanceledOnTouchOutside(false);
        }catch (Exception e){e.printStackTrace();}
    }

    private void setScreenControls(View view) {
        try{
        tvSchoolAboutDescription=(TextView)view.findViewById(R.id.tvSchoolAboutDescription);
        tvSchoolManagement=(TextView)view.findViewById(R.id.tvSchoolManagement);
        tvPrincipalName=(TextView)view.findViewById(R.id.tvPrincipalName);
        tvSchoolBoard=(TextView)view.findViewById(R.id.tvSchoolBoard);
        tvSchoolMedium=(TextView)view.findViewById(R.id.tvSchoolMedium);
        tvSchoolClasses=(TextView)view.findViewById(R.id.tvSchoolClasses);
        tvVicePrincipal=(TextView)view.findViewById(R.id.tvVicePrincipalName);
        tvFacultyName=(TextView)view.findViewById(R.id.tvFacultyName);
        rlAboutDetail=(RelativeLayout)view.findViewById(R.id.rlAboutDetail);
        }catch (Exception e){e.printStackTrace();}

    }

    private void setAdapter() {

    }

    private void setListners() {
    }

    public void onResume(){
        super.onResume();

        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle(mContext.getResources().getString(R.string.about_title));

    }
    public void getSavedSchoolDetails() {
        try{
            /***................................ set data of college...............**/
            rlAboutDetail.setVisibility(View.VISIBLE);
            /***set data for branches **/
            if(null !=resultPojo.getClasses() && !resultPojo.getClasses().isEmpty() ){
                tvSchoolClasses.setText(resultPojo.getClasses());
            }
            else
                tvSchoolClasses.setText(R.string.na_string);


            /**College About Details*/

            if(null !=resultPojo.getAbout() && !resultPojo.getAbout().isEmpty()){
                tvSchoolAboutDescription.setText(resultPojo.getAbout());}
            else
                tvSchoolAboutDescription.setText(R.string.na_string);


            /**College board university details*/

            if(resultPojo.getBoard_id()!=0){
                String boardName=db.getBoardValue(resultPojo.getBoard_id());
                Utils.printLog("board list",""+boardName);
                tvSchoolBoard.setText(boardName);
            }
            else
                tvSchoolBoard.setText(R.string.na_string);


            /**Faculties  Details*/
            if(null !=resultPojo.getFaculties() && !resultPojo.getFaculties().isEmpty()) {

                if (resultPojo.getFaculties().size() > 0) {
                    for (int i = 0; i < resultPojo.getFaculties().size(); i++) {
                        isPrincipal = Integer.parseInt(resultPojo.getFaculties().get(i).getIs_principal());
                        if (1 == isPrincipal) {
                            if (null != resultPojo.getFaculties().get(i).getName() && !resultPojo.getFaculties().get(i).getName().isEmpty()) {
                                tvPrincipalName.setVisibility(View.VISIBLE);
                                tvPrincipalName.setText(getResources().getString(R.string.principal_text) + resultPojo.getFaculties().get(i).getName()+"\t"+ resultPojo.getFaculties().get(i).getQualification());
                            } else{
                                tvPrincipalName.setVisibility(View.VISIBLE);
                                tvPrincipalName.setText(R.string.na_string);}

                        }else if (2 == isPrincipal) {
                            if (null != resultPojo.getFaculties().get(i).getName() && !resultPojo.getFaculties().get(i).getName().isEmpty()) {
                                tvVicePrincipal.setVisibility(View.VISIBLE);
                                tvVicePrincipal.setText(getResources().getString(R.string.vice_principle_text) + resultPojo.getFaculties().get(i).getName()+"\t"+ resultPojo.getFaculties().get(i).getQualification());
                            } else{
                                tvVicePrincipal.setVisibility(View.VISIBLE);
                                tvVicePrincipal.setText(R.string.na_string);}
                        } else if (0 == isPrincipal) {
                            if (null != resultPojo.getFaculties().get(i).getName() && !resultPojo.getFaculties().get(i).getName().isEmpty()) {
                                tvFacultyName.setVisibility(View.VISIBLE);
                                tvFacultyName.append(resultPojo.getFaculties().get(i).getName()+"\t"+ resultPojo.getFaculties().get(i).getQualification());
                            } else{
                                tvFacultyName.setVisibility(View.VISIBLE);
                                tvFacultyName.setText(R.string.na_string);}

                        }

                    }
                }
            }  else{
                tvFacultyName.setVisibility(View.VISIBLE);
                tvFacultyName.setText(R.string.na_string);}


            /**Management  Details*/

            if(null !=resultPojo.getManagement_id() && !resultPojo.getManagement_id().isEmpty()) {
                if(resultPojo.getManagement_id().equals(Utils.mPublic))
                    tvSchoolManagement.setText(R.string.filter_public);
                if(resultPojo.getManagement_id().equals(Utils.mPrivate) )
                    tvSchoolManagement.setText(R.string.filter_private);
                if(resultPojo.getManagement_id().equals(Utils.mGovernment))
                    tvSchoolManagement.setText(R.string.filter_government);
            }else
                tvSchoolManagement.setText(R.string.na_string);

            /**Medium  Details*/

            if(null !=resultPojo.getMedium() && !resultPojo.getMedium().isEmpty())
                tvSchoolMedium.setText(resultPojo.getMedium());
            else
                tvSchoolMedium.setText(R.string.na_string);


            progressDialog.dismiss();
        }catch (Exception e){e.printStackTrace();}
    }
    private void getBoardListWS() {
        try{
            progressDialog.show();
            new VolleyWebserviceCall().volleyGetCall(mContext,KeyConstants.GET_BOARD_LISTING,SchoolDetailFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setBoardsDataIntoDB(GetBoardListPojo myPojo) {
        db.clearData();
        db.addAllBoardsToDB(myPojo.getResult());
        //addSpinner();

    }
    @Override
    public void onSuccess(String serverResult, String requestTag, int statusCode) {
        try{
            Gson gson = new Gson();
            JSONObject obj;
            String json;
            if (requestTag.contains(KeyConstants.GET_BOARD_LISTING)) {
                if (Utils.SUCCESS == statusCode) {
                    getBoardListPojo = gson.fromJson(serverResult, GetBoardListPojo.class);
                    setBoardsDataIntoDB(getBoardListPojo);
                    resulArrayBoards=db.getBoards();
                    getSavedSchoolDetails();
                }else if(Utils.FAILURE == statusCode) {
                    json = serverResult;
                    obj = new JSONObject(json);
                    json = obj.getString("message");
                    if (null != json)
                        Utils.showToast(json, mContext);

                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onError(String serverResult, String requestTag, int statusCode) {

    }

}
