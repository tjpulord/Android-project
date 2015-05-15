package com.telecomsys.autokit.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5Check {

	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			 digital = bytes[i];
			if(digital < 0) {
				digital += 256;
			}
			if(digital < 16){
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}

	public static String bytesToMD5(byte[] input) {
		String md5str = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buff = md.digest(input);
			md5str = bytesToHex(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}

	public static String strToMD5(String str) {
		byte[] input = str.getBytes();
		return bytesToMD5(input);
	}
	
	public static String fileToMD5(File file) {
		if(file == null) {
			return null;
		}
		if(file.exists() == false) {
			return null;
		}
		if(file.isFile() == false) {
			return null;
		}
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buff = new byte[1024];
			int len = 0;
			while(true) {
				len = fis.read(buff, 0, buff.length);
				if(len == -1){
					break;
				}
				md.update(buff,0,len);
			}
			fis.close();
			return bytesToHex(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}