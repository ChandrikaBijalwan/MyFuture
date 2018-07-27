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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.List;


/**
 * Created by Monali on 7/6/2017.
 */

public class CollegeDetailFragment extends Fragment implements VolleyWebserviceCallBack {

    private Context mContext;
    private TextView tvCollegeAboutDescription,tvCollegeManagement,tvPrincipalName,tvCollegeUniversity,tvCollegeMedium,tvCollegeCourses,tvPlacement,tvVicePrincipal,tvFacultyName;
    private SchoolDetailPojo resultPojo;
    private int isPrincipal;
    private ArrayList<CommonIdValuePojo> resulArrayBoards;
    private Database db;
    private GetBoardListPojo getBoardListPojo;
    private ListView lvBranches;
    private ProgressDialog progressDialog;
    private RelativeLayout rlAboutDetail;


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
        return inflater.inflate(R.layout.college_detail_fragment,null);


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
        tvCollegeAboutDescription=(TextView)view.findViewById(R.id.tvCollegeAboutDescription);
        tvCollegeManagement=(TextView)view.findViewById(R.id.tvCollegeManagement);
        tvPrincipalName=(TextView)view.findViewById(R.id.tvPrincipalName);
        tvCollegeUniversity=(TextView)view.findViewById(R.id.tvCollegeUniversity);
        tvCollegeMedium=(TextView)view.findViewById(R.id.tvCollegeMedium);
        tvCollegeCourses=(TextView)view.findViewById(R.id.tvCollegeCourses);
        tvPlacement=(TextView)view.findViewById(R.id.tvPlacement);
        tvVicePrincipal=(TextView)view.findViewById(R.id.tvVicePrincipalName);
        tvFacultyName=(TextView)view.findViewById(R.id.tvFacultyName);
        lvBranches=(ListView)view.findViewById(R.id.lvBranches);
        rlAboutDetail=(RelativeLayout)view.findViewById(R.id.rlAboutDetail);
        }catch (Exception e){e.printStackTrace();}
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
                /*** set data for courses**/
            if(null !=resultPojo.getCourses() && !resultPojo.getCourses().isEmpty()){
                String course="",full_course="";

                for(int i= 0;i<resultPojo.getCourses().size();i++){
                    course = " <b>" +resultPojo.getCourses().get(i).getCourse_name()+"</b>";
                    int number=i+1;
                    tvCollegeCourses.append("\n");
                    tvCollegeCourses.append(Html.fromHtml("\n"+number+"."+course));
                    if( null!=resultPojo.getCourses().get(i).getBranches()&& !resultPojo.getCourses().get(i).getBranches().isEmpty())
                    if(resultPojo.getCourses().get(i).getBranches().size()>0 )
                        for (int j=0; j<resultPojo.getCourses().get(i).getBranches().size();j++){
                            full_course= resultPojo.getCourses().get(i).getBranches().get(j);
                            tvCollegeCourses.append("\n \t \t* "+full_course);
                        }
                    tvCollegeCourses.append("\n");

                }

            }else {
                tvCollegeCourses.setText(R.string.na_string);
            }
            /*** set data for placements**/
            if(null !=resultPojo.getPlacements() && !resultPojo.getPlacements().isEmpty()) {

                if (resultPojo.getPlacements().size()>0) {
                    tvPlacement.append("Company Name:\n");
                    for (int i = 0; i < resultPojo.getPlacements().size(); i++) {
                        tvPlacement.append(resultPojo.getPlacements().get(i).getCompany_name());
//                        tvPlacement.append("\n");
                    }
                }
            }else
                tvPlacement.setText(R.string.na_string);

                /**College About Details*/

                if(null !=resultPojo.getAbout() && !resultPojo.getAbout().isEmpty()){
                tvCollegeAboutDescription.setText(resultPojo.getAbout());}
                else
                    tvCollegeAboutDescription.setText(R.string.na_string);


            /**College board university details*/

            if(resultPojo.getBoard_id()!=0){
                String boardName=db.getBoardValue(resultPojo.getBoard_id());
//                resulArrayBoards=db.getBoardValue(resultPojo.getBoard_id());
                Utils.printLog("board list",""+boardName);
                tvCollegeUniversity.setText(boardName);
            }
            else
                tvCollegeUniversity.setText(R.string.na_string);



            /**Faculties  Details*/
            if(null !=resultPojo.getFaculties() && !resultPojo.getFaculties().isEmpty()) {

                if (resultPojo.getFaculties().size() > 0) {
                    for (int i = 0; i < resultPojo.getFaculties().size(); i++) {
                        isPrincipal = Integer.parseInt(resultPojo.getFaculties().get(i).getIs_principal());
                        if (1 == isPrincipal) {
                            if (null != resultPojo.getFaculties().get(i).getName() && !resultPojo.getFaculties().get(i).getName().isEmpty()) {
                                tvPrincipalName.setVisibility(View.VISIBLE);
                                tvPrincipalName.setText(getResources().getString(R.string.principal_text) + resultPojo.getFaculties().get(i).getName());
                            } else{
                                tvPrincipalName.setVisibility(View.VISIBLE);
                                tvPrincipalName.setText(R.string.na_string);}

                        }else if (2 == isPrincipal) {
                            if (null != resultPojo.getFaculties().get(i).getName() && !resultPojo.getFaculties().get(i).getName().isEmpty()) {
                                tvVicePrincipal.setVisibility(View.VISIBLE);
                                tvVicePrincipal.setText(getResources().getString(R.string.vice_principle_text) + resultPojo.getFaculties().get(i).getName());
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
                tvPrincipalName.setVisibility(View.VISIBLE);
                tvPrincipalName.setText(R.string.na_string);}


                /**Management  Details*/

                if(null !=resultPojo.getManagement_id() && !resultPojo.getManagement_id().isEmpty()) {
                if(resultPojo.getManagement_id().equals(Utils.mPublic))
                    tvCollegeManagement.setText(R.string.filter_public);
                if(resultPojo.getManagement_id().equals(Utils.mPrivate) )
                    tvCollegeManagement.setText(R.string.filter_private);
                if(resultPojo.getManagement_id().equals(Utils.mGovernment))
                    tvCollegeManagement.setText(R.string.filter_government);
                }else
                    tvCollegeManagement.setText(R.string.na_string);

                /**Medium  Details*/

                if(null !=resultPojo.getMedium() && !resultPojo.getMedium().isEmpty())
                tvCollegeMedium.setText(resultPojo.getMedium());
                else
                    tvCollegeManagement.setText(R.string.na_string);

            progressDialog.dismiss();

        }catch (Exception e){e.printStackTrace();}
    }

    private void getBoardListWS() {
        try{
            progressDialog.show();
            new VolleyWebserviceCall().volleyGetCall(mContext,KeyConstants.GET_BOARD_LISTING,CollegeDetailFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setBoardsDataIntoDB(GetBoardListPojo myPojo) {
        db.clearData();
        db.addAllBoardsToDB(myPojo.getResult());
        //addSpinner();

    }
    private void setAdapter() {

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
