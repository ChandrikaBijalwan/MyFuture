package com.equipesoftwares.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.equipesoftwares.Pojo.Images;
import com.equipesoftwares.R;
import com.equipesoftwares.adapters.SlidingNetworkImageAdapter;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


// Commonly used dialogs throughout the project
public class Dialogs {
	static AlertDialog alertDialog;

	public static void showAlert(Context context, String title, String message) {
		try {
			final AlertDialog.Builder builder =
					new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setPositiveButton(context.getResources().getString(R.string.okCaps), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
				}
			});
			builder.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// Display a dialog that user has no Internet connection
	public static void showNoConnectionDialogx(final Context context) {
		try {
			final AlertDialog.Builder builder =
					new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
			builder.setTitle(context.getResources().getString(R.string.no_connection_title));
			builder.setMessage(context.getResources().getString(R.string.no_connection));
			builder.setPositiveButton(context.getResources().getString(R.string.okCaps), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					context.startActivity(new Intent(
							Settings.ACTION_WIRELESS_SETTINGS));
					if (!Utils.isConnected(context)) {
						return;
					}
					dialogInterface.dismiss();
				}
			});

			builder.setNegativeButton(context.getResources().getString(R.string.cancelCaps), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if (context.getClass().getSimpleName().equalsIgnoreCase("SplashActivity")) {
						((Activity) context).finish();
					}
					return;
				}
			});
			builder.show();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ProgressDialog ShowProgressDialog(Context context) {
		ProgressDialog progressdialog = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
		try {
			progressdialog.setMessage(context
					.getString(R.string.ProgressDialog_message));
			progressdialog.setCancelable(false);
			progressdialog.setIndeterminate(true);
		}catch (Exception e){
			e.printStackTrace();
		}
		return progressdialog;
	}

	/**dialog for view full size images with pager*/

	public static void dialogForViewFullImages(final ArrayList<Images> mediaArrayList, String stringFlag, final Context mContext) {
		try {

			if(mediaArrayList !=null && !mediaArrayList.isEmpty()){
				final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.DialogTheme);
				builder.setCancelable(false);
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View dialogView = inflater.inflate(R.layout.custom_dialog_with_image_pager, null);
				builder.setView(dialogView);
				final AlertDialog dialog = builder.create();
				SlidingNetworkImageAdapter slidingNetworkImageAdapter;
				final ImageView ivNext = (ImageView)dialogView.findViewById(R.id.ivNext);
				final ImageView ivPrevious = (ImageView) dialogView.findViewById(R.id.ivPrevious);

				final ViewPager pager=(ViewPager)dialogView.findViewById(R.id.pager);
				final ImageView ivDialog  = (ImageView)dialogView.findViewById(R.id.dialogCancelImg);
                slidingNetworkImageAdapter = new SlidingNetworkImageAdapter(mContext,mediaArrayList,stringFlag);
                pager.setAdapter(slidingNetworkImageAdapter);

				final int arraySize=mediaArrayList.size();
				if(mediaArrayList.size()>1){
					ivPrevious.setVisibility(View.VISIBLE);
					ivNext.setVisibility(View.VISIBLE);
				}else{
					ivPrevious.setVisibility(View.GONE);
					ivNext.setVisibility(View.GONE);
				}

				if(pager.getCurrentItem()==0){
					ivPrevious.setVisibility(View.GONE);
				}else
					ivPrevious.setVisibility(View.VISIBLE);


				ivDialog.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				ivPrevious.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ivNext.setVisibility(View.VISIBLE);
						pager.setCurrentItem(pager.getCurrentItem() - 1, true);
						if(pager.getCurrentItem()==0){
							ivPrevious.setVisibility(View.GONE);
						}else
							ivPrevious.setVisibility(View.VISIBLE);

					}
				});
				ivNext.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ivPrevious.setVisibility(View.VISIBLE);
						pager.setCurrentItem(pager.getCurrentItem() + 1, true);
						if(pager.getCurrentItem()==arraySize-1){
							ivNext.setVisibility(View.GONE);
						}else
							ivNext.setVisibility(View.VISIBLE);
					}
				});
				dialog.show();
				Window window = dialog.getWindow();
				if (window != null) {
					window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
				}

			}else {
				Utils.showToast("No image available",mContext);
			}

			}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
