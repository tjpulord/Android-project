package com.yesway.izhijia.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.yesway.izhijia.R;

public class Utility {

	public static String getEncryptedPhoneNumber(String phone) {
		char[] tmpPhone = phone.toCharArray();
		for (int i = 0; i < tmpPhone.length; i++) {
			if (i >= 3 && i <= 5) {
				tmpPhone[i] = '*';
			}
		}
		return new String(tmpPhone);
	}

	public static String getSignature(String method, String url)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String appId = Constant.APPID;
		String secretKey = Constant.SECRET_KEY;
		StringBuffer sb = new StringBuffer();
		sb.append(method);

		int index = url.indexOf('?');
		if (index > 0) {
			url = url.substring(0, index);
		}
		sb.append(url).append(appId).append(secretKey);
		
		System.out.println(url + " encoded to:" + sb.toString());
		String encodedStr = URLEncoder.encode(sb.toString(), "UTF-8").toLowerCase();
		return Utility.MD5(encodedStr);
	}

	private final static String MD5(String s) throws NoSuchAlgorithmException {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		byte[] btInput = s.getBytes();
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest mdInst = MessageDigest.getInstance("MD5");
		// 使用指定的字节更新摘要
		mdInst.update(btInput);
		// 获得密文
		byte[] md = mdInst.digest();
		// 把密文转换成十六进制的字符串形式
		int length = md.length;
		char str[] = new char[length * 2];
		int k = 0;
		for (int i = 0; i < length; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	public static ProgressDialog showProgressDlg(Context context) {
		CharSequence title = context.getResources().getString(R.string.please_wait);
		CharSequence msg = context.getResources().getString(R.string.ycarlink_connecting);
		ProgressDialog pd=ProgressDialog.show(context, title, msg,true,true);
		return pd;
	}
	
	public static ProgressDialog showProgressDlg(Context context, int textId) {
		CharSequence title = context.getResources().getString(R.string.please_wait);
		CharSequence msg = context.getResources().getString(textId);
		ProgressDialog pd=ProgressDialog.show(context, title, msg,true,true);
		return pd;
	}

	public static void showErrorDialog(Context ctx, int msgContent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msgContent).setNeutralButton(R.string.confirm, new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}}).setTitle(R.string.request_failed);
		AlertDialog dlg = builder.create();
		dlg.show();
	}
	
	public static byte[] encode(String s){
		byte[] data;
		try {
			data = s.getBytes("utf-8");
			byte[] result = Base64.encode(data, Base64.DEFAULT);
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// check the Network
	public static boolean checkNetworkStatus(Context ctx) {
		ConnectivityManager connectManager = (ConnectivityManager) ctx
				.getSystemService(ctx.CONNECTIVITY_SERVICE);
		if(connectManager != null){
			NetworkInfo [] networkInfos = connectManager.getAllNetworkInfo();
			if(networkInfos != null){
				for(int i=0; i<networkInfos.length; i++){
					if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		}
		return false;
	}
}
