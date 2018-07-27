package com.equipesoftwares.services;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.equipesoftwares.common.Utils;


import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class MultipartRequest extends Request<String> {
	private MultipartEntity entity = new MultipartEntity();
	private HashMap<String,ArrayList<Object>> mFileUploads = null;
	ArrayList<Object> arrayList =  new ArrayList<>();
	private HashMap<String, String> mDtata = null;
	private final Response.Listener<String> mListener;
	private long fileLength = 0L;
	private  MultipartProgressListener multipartProgressListener = null;





	public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, HashMap<String, String> mDtata, HashMap<String,ArrayList<Object>> mFileUploads,
                            MultipartProgressListener progLitener, long fileLenght)
	{
		super(Method.POST, url, errorListener);
		this.mFileUploads = mFileUploads;
		this.mDtata = mDtata;
		mListener = listener;
		this.multipartProgressListener = progLitener;
		this.fileLength = fileLenght;
		buildMultipartEntity();
	}

	public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, HashMap<String, String> mDtata, HashMap<String,ArrayList<Object>> mFileUploads)
	{
		super(Method.POST, url, errorListener);
		this.mFileUploads = mFileUploads;
		this.mDtata = mDtata;
		mListener = listener;
		buildMultipartEntity();
	}

	private void buildMultipartEntity()
	{
		for ( String key : mFileUploads.keySet() ){

			ArrayList<Object> s = mFileUploads.get(key);
			Utils.printLog("s",s+"");

			for (int i = 0; i < s.size(); i++) {
				File f = new File( s.get(i)+"");
				Utils.printLog("f",f+"");
				entity.addPart(key, new FileBody(f, "image/jpeg"));
			}

		}
		try
		{
			for ( String key : mDtata.keySet() ) {
				entity.addPart(key, new StringBody(mDtata.get(key)));
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			VolleyLog.e("UnsupportedEncodingException");
		}
	}

	@Override
	public String getBodyContentType()
	{
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			if(multipartProgressListener != null)
				entity.writeTo((new CountingOutputStream(bos, fileLength,multipartProgressListener)));
			else
				entity.writeTo(bos);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response)
	{
		String jsonString = new String(response.data);
		return Response.success(jsonString.toString(), getCacheEntry());
	}

	@Override
	protected void deliverResponse(String response)
	{
		mListener.onResponse(response);
	}



	public static interface MultipartProgressListener {
		void transferred(long transfered, int progress);
	}

	public static class CountingOutputStream extends FilterOutputStream {
		private final MultipartProgressListener progListener;
		private long transferred;
		private long fileLength;

		public CountingOutputStream(final OutputStream out, long fileLength,
									final MultipartProgressListener listener) {
			super(out);
			this.fileLength = fileLength;
			this.progListener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			try{
				if (progListener != null) {
					this.transferred += len;
					int prog = (int) (transferred * 100 / fileLength);
					this.progListener.transferred(this.transferred, prog);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		public void write(int b) throws IOException {
			out.write(b);
			try {
				if (progListener != null) {
					this.transferred++;
					int prog = (int) (transferred * 100 / fileLength);
					this.progListener.transferred(this.transferred, prog);
				}
			}catch (Exception e){
				e.printStackTrace();
			}

		}

	}
}