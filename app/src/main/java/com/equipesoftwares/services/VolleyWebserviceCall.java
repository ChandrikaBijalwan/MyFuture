package com.equipesoftwares.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.equipesoftwares.R;
import com.equipesoftwares.common.Dialogs;
import com.equipesoftwares.common.Utils;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VolleyWebserviceCall {

	/*
	 * Post request 
	 * @param Context
	 * @param URL to server
	 * @param HAshmap of keys and values to be passed with service
	 * @param Callback
	 * */

	ProgressDialog pd = null;
	// for gcm id
	int mStatusCode;
	/**
	 * The default socket timeout in milliseconds
	 */
	public static final int MY_SOCKET_TIMEOUT_MS = 10000;

	/**
	 * The default number of retries
	 */
	public static final int DEFAULT_MAX_RETRIES = 1;

	/**
	 * The default backoff multiplier
	 */
	public static final float DEFAULT_BACKOFF_MULT = 1f;


	public void volleyPostCall(final Context context, final String url, final HashMap<String, String> parameter, final VolleyWebserviceCallBack callBack, final Boolean showProgressDialog) {
try{
		((Activity) context).setProgressBarIndeterminateVisibility(true);
		String tag_json_obj = context.getClass().getSimpleName(); // tag will be requied to

		if (showProgressDialog) {
			pd = Dialogs.ShowProgressDialog(context);
			pd.show();
		}

		StringRequest gsonRequest = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String serverJson) {
						try {

							((Activity) context).setProgressBarIndeterminateVisibility(false);
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}

                                callBack.onSuccess(serverJson, url, mStatusCode);

						} catch (Exception e) {
							e.printStackTrace();
							callBack.onSuccess(serverJson, url, mStatusCode);
						}
					}
				}, new Response.ErrorListener() {


			@Override
			public void onErrorResponse(VolleyError error) {
				if (showProgressDialog) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
				}
				((Activity) context)
						.setProgressBarIndeterminateVisibility(false);
				error.printStackTrace();

				// all errors will be recieved here
				if (error instanceof NetworkError) {
					Dialogs.showAlert(context,context.getResources().getString(R.string.networkErrorTitle), context.getResources().getString(R.string.no_connection));

				} else if (error instanceof ServerError) {
					try {
						NetworkResponse response = error.networkResponse;
						if (response != null && response.data != null) {
							switch (response.statusCode) {
								case 400:
									callBack.onError("Server Error! Please Try Later.", url, mStatusCode);
									break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (error instanceof AuthFailureError) {
					callBack.onError(error.getMessage(), url, mStatusCode);
				} else if (error instanceof ParseError) {
					callBack.onError(error.getMessage(), url, mStatusCode);
				} else if (error instanceof NoConnectionError) {
					callBack.onError(error.getMessage(), url, mStatusCode);
				} else if (error instanceof TimeoutError) {
					Dialogs.showAlert(context, "Network Error!", "Please try after some time, due to slow connection");
				}
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params;
				params = parameter;
//                params.put(KeyConstants.KEYCONSTANT_DEVICE_ID, Utils.getSharedPreference(context,KeyConstants.KEYCONSTANT_DEVICE_ID));
//                params.put(KeyConstants.KEYCONSTANT_DEVICE_TYPE,Utils.deviceTypeAndroid+"");
				Utils.printLog("VolleyWebServiceCall Params : ", params + "");
				return params;
			}

//			//add header to api url
//			public Map<String, String> getHeaders() throws AuthFailureError {
//				HashMap params = new HashMap();
//				params.put("X-API-KEY", "123456");
//				return params;
//			}

			// method to check status code from server
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				mStatusCode = response.statusCode;
				Utils.printLog("VolleyWebServiceCall statusCode : ", mStatusCode + "");
				return super.parseNetworkResponse(response);

			}

		};
		gsonRequest.setRetryPolicy(new DefaultRetryPolicy(
				MY_SOCKET_TIMEOUT_MS,
				DEFAULT_MAX_RETRIES,
				DEFAULT_BACKOFF_MULT));
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(gsonRequest, tag_json_obj);

	} catch (Exception e) {
		e.printStackTrace();
	}
}


	/*
	 * GET request 
	 * @param Context
	 * @param URL to server
	 * @param Callback
	 * */
	public void volleyGetCall(final Context context, final String url, final VolleyWebserviceCallBack callBack) {
		try{
		((Activity) context).setProgressBarIndeterminateVisibility(true);
		String tag_json_obj = context.getClass().getSimpleName(); // tag will be requied to
            Utils.printLog("WebService get Tag", "WebService Tag " + tag_json_obj);
            StringRequest gsonRequest = new StringRequest(url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String serverJson) {
						((Activity) context).setProgressBarIndeterminateVisibility(false);
						callBack.onSuccess(serverJson, url, mStatusCode);
					}
				}, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    try{
                        ((Activity) context)
                                .setProgressBarIndeterminateVisibility(false);
                        error.printStackTrace();
                        Utils.cancelWebserviceRequest(context);

                        // all errors will be recieved here
                        if( error instanceof NetworkError) {
                            Dialogs.showAlert(context, context.getResources().getString(R.string.networkErrorTitle), context.getResources().getString(R.string.networkErrorMessage));
                        } else if( error instanceof ServerError) {
                            try {
                                NetworkResponse response = error.networkResponse;
                                if(response != null && response.data != null){
                                    switch(response.statusCode){
                                        case 400:
                                            String json = new String(response.data);
                                            JSONObject obj = new JSONObject(json);
                                            json = obj.getString("message");
                                            int navigateToHome =  obj.getInt("status");

                                            break;
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        } else if( error instanceof AuthFailureError) {
                            callBack.onError(error.getMessage(), url, mStatusCode);
                        } else if( error instanceof ParseError) {
                            callBack.onError(error.getMessage(), url, mStatusCode);
                        } else if( error instanceof NoConnectionError) {
                            callBack.onError(error.getMessage(), url, mStatusCode);
                        } else if( error instanceof TimeoutError) {
                            Dialogs.showAlert(context, context.getResources().getString(R.string.networkErrorTitle), context.getResources().getString(R.string.networkErrorMessage));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    mStatusCode = response.statusCode;
                    return super.parseNetworkResponse(response);
                }
            }
                    ;
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(gsonRequest, tag_json_obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
