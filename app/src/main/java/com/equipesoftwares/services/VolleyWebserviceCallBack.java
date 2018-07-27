package com.equipesoftwares.services;

// Interface to get callbacks of volley in calling class
public interface VolleyWebserviceCallBack {
	public void onSuccess(String serverResult, String requestTag, int statusCode);
	public void onError(String serverResult, String requestTag, int statusCode);
}
