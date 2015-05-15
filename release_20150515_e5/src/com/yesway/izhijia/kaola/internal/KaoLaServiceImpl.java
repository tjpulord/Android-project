package com.yesway.izhijia.kaola.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.telecomsys.autokit.util.JsonUtil;
import com.yesway.izhijia.common.Constant;
import com.yesway.izhijia.common.Utility;
import com.yesway.izhijia.kaola.KaoLaModel.ActivateListener;
import com.yesway.izhijia.kaola.KaoLaModel.KaoLaInformationDownloadListener;
import com.yesway.izhijia.kaola.KaoLaService;
import com.yesway.izhijia.kaola.data.KaoLaContent;
import com.yesway.izhijia.kaola.data.KaoLaRadioItem;
import com.yesway.izhijia.kaola.data.KaoLaResponse;
import com.yesway.izhijia.model.BaseAsyncTask;

public class KaoLaServiceImpl extends KaoLaService {
	private final int MAX_RESPONSE = 3;
	private String schema = "http://open.kaolafm.com/v1/";

	@Override
	public void activateDevice(final String deviceId,
			final ActivateListener listener) {
		downloadTask = new BaseAsyncTask(listener) {
			@Override
			protected Integer doInBackground(String... param) {
				if (listener != null) {
					listener.onRequestProgress();
				}

				String urlAddress = schema + "app/active";
				try {
					HttpPost post = new HttpPost(urlAddress);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("appid", Constant.APPID));
					params.add(new BasicNameValuePair("sign", Utility
							.getSignature(post.getMethod().toLowerCase(),
									urlAddress)));
					params.add(new BasicNameValuePair("deviceid", deviceId));

					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							params, "UTF8");
					post.setEntity(entity);
					HttpClient hc = new DefaultHttpClient();
					HttpResponse ht = hc.execute(post);

					String openid = null;
					if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity he = ht.getEntity();
						InputStream is = he.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						StringBuffer response = new StringBuffer();
						String readLine = null;
						while ((readLine = br.readLine()) != null) {
							response.append(readLine);
						}

						is.close();
						br.close();

						JSONObject jsonObj = new JSONObject(response.toString());
						if (jsonObj.has("result")) {
							openid = jsonObj.getJSONObject("result")
									.getString("openid");
						}
//						if (openid == null) {
//							openid = getOpenIdByDeviceId(deviceId);
//						}
//						Log.d("openid:", openid);
						if (listener != null && openid != null){
							((ActivateListener)listener).onDeviceActiviated(openid);
						}
						return 0;
					} else {
						openid = getOpenIdByDeviceId(deviceId);
						if (listener != null && openid != null){
							((ActivateListener)listener).onDeviceActiviated(openid);
							return 0;
						}else {
							return ht.getStatusLine().getStatusCode();
						}
					}

				} catch (Exception e) {
					return -1;
				}
			}
		};
		downloadTask.execute();
	}

	public String getOpenIdByDeviceId(final String deviceId){
		String urlAddress = schema + "app/init";
		String openid = null;
		try {
			HttpPost post = new HttpPost(urlAddress);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("appid", Constant.APPID));
			params.add(new BasicNameValuePair("sign", Utility
					.getSignature(post.getMethod().toLowerCase(),
							urlAddress)));
			params.add(new BasicNameValuePair("deviceid", deviceId));
			params.add(new BasicNameValuePair("devicetype", "0"));
			params.add(new BasicNameValuePair("osversion", "0"));
			params.add(new BasicNameValuePair("network", "1"));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					params, "UTF8");
			post.setEntity(entity);
			HttpClient hc = new DefaultHttpClient();
			HttpResponse ht = hc.execute(post);

			if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity he = ht.getEntity();
				InputStream is = he.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				StringBuffer response = new StringBuffer();
				String readLine = null;
				while ((readLine = br.readLine()) != null) {
					response.append(readLine);
				}

				is.close();
				br.close();
				
				JSONObject jsonObj = new JSONObject(response.toString());
				if (jsonObj.has("result")){
					openid = jsonObj.getJSONObject("result").getString("openid");
				}
			}
		} catch (Exception e) {
			return openid;
		}
		return openid;
	}
	
	@Override
	public void getAllCategories(final String deviceId, final String openId,
			final KaoLaInformationDownloadListener listener) {
		
		downloadTask = new BaseAsyncTask(listener) {
			@Override
			protected Integer doInBackground(String... params) {
				if (listener != null) {
					listener.onRequestProgress();
				}

				String urlAddress = schema + "category/sublist";
				try {					
					StringBuffer sb = new StringBuffer(urlAddress);
					sb.append("?appid=").append(Constant.APPID).append("&sign=").append(Utility.getSignature("get", urlAddress));
					sb.append("&openid=").append(openId);
					
					HttpGet get = new HttpGet(sb.toString());					
					HttpClient hc = new DefaultHttpClient();
					HttpResponse ht = hc.execute(get);

					Log.d("httpGet", "request all category:"+sb.toString());
					if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity he = ht.getEntity();
						String charset = EntityUtils.getContentCharSet(he);
						InputStream is = he.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						StringBuffer response = new StringBuffer();
						String readLine = null;
						int count = 0;
						while ((readLine = br.readLine()) != null && count++ < MAX_RESPONSE) {
							response.append(readLine);
						}
						is.close();
						br.close();
//						JSONObject jsonObj = new JSONObject(response.toString());
//						if (jsonObj.has("result")) {
//							ArrayList<KaoLaCategory> categories = new ArrayList<KaoLaCategory>();
//							JSONArray results = jsonObj.getJSONArray("result");
//							for (int i = 0; i < results.length(); i++){
//								JSONObject jsonCategory = results.getJSONObject(i);
//								categories.add( new KaoLaCategory().fromJSONObject(jsonCategory,charset));
//							}
//							if (listener != null) {
//								((KaoLaInformationDownloadListener) listener)
//										.onKaoLaCategories(categories);
//							}
//						}
						
						if (listener != null) {
							((KaoLaInformationDownloadListener) listener)
									.onKaoLaCategories(response.toString());
						}
						return 0;
					} else {
						return ht.getStatusLine().getStatusCode();
					}
				} catch (Exception e) {
					return -1;
				}
			}
		};
		downloadTask.execute();
	}

	@Override
	public void getContentListByCategoryId(final String deviceId,final String openId,
			final String categoryId,final KaoLaInformationDownloadListener listener) {
		downloadTask = new BaseAsyncTask(listener) {
			@Override
			protected Integer doInBackground(String... params) {
				if (listener != null) {
					listener.onRequestProgress();
				}
				
				String urlAddress = schema + "content/list";
				try {					
					StringBuffer sb = new StringBuffer(urlAddress);
					sb.append("?appid=").append(Constant.APPID).append("&sign=").append(Utility.getSignature("get", urlAddress));
					sb.append("&openid=").append(openId).append("&cid=").append(categoryId);
					
					HttpGet get = new HttpGet(sb.toString());					
					HttpClient hc = new DefaultHttpClient();
					HttpResponse ht = hc.execute(get);
					Log.d("httpGet", "request content list:"+sb.toString());

					if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity he = ht.getEntity();
						String charset = EntityUtils.getContentCharSet(he);
						InputStream is = he.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						StringBuffer response = new StringBuffer();
						String readLine = null;
						while ((readLine = br.readLine()) != null) {
							response.append(readLine);
						}

						is.close();
						br.close();
						
						JSONObject rootObj = new JSONObject(response.toString());
						if(!rootObj.has("result")){
							return 0;
						}
						
						JSONObject jsonObj = rootObj.getJSONObject("result");					
						KaoLaResponse klResponse = new KaoLaResponse();
						klResponse.setNext(jsonObj.getString("next"));
						klResponse.setPrev(jsonObj.getString("prev"));
						klResponse.setTotal(jsonObj.getString("total"));
						klResponse.setRequestId(rootObj.getString("requestId"));
						List<KaoLaContent> categories = new ArrayList<KaoLaContent>();
						if (jsonObj.has("list")) {
							JSONArray results = jsonObj.getJSONArray("list");
							int maxlenth = results.length();
							if(maxlenth > MAX_RESPONSE){
								maxlenth = MAX_RESPONSE;
							}
							for (int i = 0; i < maxlenth; i++){
								JSONObject jsonCategory = results.getJSONObject(i);
								categories.add( new KaoLaContent().fromJSONObject(jsonCategory,charset));
							}
							
//							if (listener != null) {
//								Log.d("KaoLaInformationDownloadListener", categories.toString());
//								String categoriesString = JsonUtil.ListToJSON(categories);
//								Log.d("KaoLaInformationDownloadListener, after", categoriesString);
//								((KaoLaInformationDownloadListener) listener)
//										.onKaoLaContents(categoriesString);
//							}
						}
						klResponse.setKlContentlist(categories);
						String contentString = klResponse.toString();
						Log.d("KaolaContent", "kaola content:" + contentString);
						Log.d("KaolaContent", "kaola content - size:" + contentString.length());
						if (listener != null) {
							((KaoLaInformationDownloadListener) listener)
									.onKaoLaContents(contentString);
						}
						return 0;
					} else {
						return ht.getStatusLine().getStatusCode();
					}

				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}				
			}
		};
		downloadTask.execute();	
	}

	@Override
	public void getRadioPlayList(final String deviceId,final  String openId,
			final String radioId,final  String clockId,
			final KaoLaInformationDownloadListener listener) {
		downloadTask = new BaseAsyncTask(listener) {
			@Override
			protected Integer doInBackground(String... params) {
				if (listener != null) {
					listener.onRequestProgress();
				}
				String urlAddress = schema + "radio/playlist";
				try {
					StringBuffer sb = new StringBuffer(urlAddress);
					sb.append("?appid=").append(Constant.APPID).append("&sign=").append(Utility.getSignature("get", urlAddress));
					sb.append("&openid=").append(openId).append("&rid=").append(radioId);
					
					if(clockId != null){
						sb.append("&clockid=").append(clockId);
					}
					
					HttpGet get = new HttpGet(sb.toString());					
					HttpClient hc = new DefaultHttpClient();
					HttpResponse ht = hc.execute(get);
					Log.d("httpGet", "request play list:"+sb.toString());

					if (ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						HttpEntity he = ht.getEntity();
						String charset = EntityUtils.getContentCharSet(he);
						InputStream is = he.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						StringBuffer response = new StringBuffer();
						String readLine = null;
						int count = 0;
						while ((readLine = br.readLine()) != null && count < MAX_RESPONSE) {
							response.append(readLine);
							count++;
						}
						is.close();
						br.close();
						JSONObject jsonObj = new JSONObject(response.toString());
						KaoLaResponse klResponse = new KaoLaResponse();
						klResponse.setRequestId(jsonObj.getString("requestId"));
						ArrayList<KaoLaRadioItem> categories = new ArrayList<KaoLaRadioItem>();
						if (jsonObj.has("result")) {
							JSONArray results = jsonObj.getJSONArray("result");
							int maxlength = results.length();
							if(maxlength > MAX_RESPONSE*2){
								maxlength = MAX_RESPONSE*2;
							}
							for (int i = 0; i < maxlength; i++){
								JSONObject jsonCategory = results.getJSONObject(i);
								categories.add( new KaoLaRadioItem().fromJSONObject(jsonCategory, charset));
							}
//							if (listener != null) {
//								((KaoLaInformationDownloadListener) listener)
//										.onKaoLaRadioPlayList(cateString);
//							}
						}
						klResponse.setKlRadiolist(categories);
						String radioString = klResponse.toString();
						Log.d("KaolaContent", "kaola radio list:" + radioString);
						Log.d("KaolaContent", "kaola radio list - size:" + radioString.length());
						if (listener != null) {
							((KaoLaInformationDownloadListener) listener)
									.onKaoLaRadioPlayList(radioString);
						}
						return 0;
					} else {
						return ht.getStatusLine().getStatusCode();
					}

				} catch (Exception e) {
					return -1;
				}
			}
		};
		downloadTask.execute();			
	}
}
