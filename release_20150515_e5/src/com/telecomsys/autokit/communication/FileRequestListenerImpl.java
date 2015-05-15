package com.telecomsys.autokit.communication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import android.util.Log;

import com.telecomsys.autokit.communication.FileTransmition.FileRequestListener;
import com.telecomsys.autokit.communication.FileTransmition.FileTransmitStatus;

public class FileRequestListenerImpl implements FileRequestListener {
	
	 public static byte[] getImageFromNetByUrl(String strUrl){  
	        try {  
	            URL url = new URL(strUrl);  
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	            conn.setRequestMethod("GET");  
	            conn.setConnectTimeout(5 * 1000);  
	            InputStream inStream = conn.getInputStream();
	            byte[] btImg = readInputStream(inStream);
	            return btImg;  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  

	    public static byte[] readInputStream(InputStream inStream) throws Exception{  
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while( (len=inStream.read(buffer)) != -1 ){  
	            outStream.write(buffer, 0, len);  
	        }  
	        inStream.close();  
	        return outStream.toByteArray();  
	    } 
	    
	@Override
	public boolean onFileRequested(final String des,
			final String src) {
		
			byte[] buffer = null;
			try {
				if(src.startsWith("/")){
					File file = new File(src);    
			        FileInputStream fis;
					fis = new FileInputStream(file);	  
					int length = fis.available();   
					buffer = new byte[length];   
					fis.read(buffer);       
					fis.close();
				}
				else if(src.startsWith("http://") || src.startsWith("https://")){
					buffer = getImageFromNetByUrl(src);
					
				}
				else{
					return false;
				}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}    
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}  
		FileTransmition.getInstance().beginStreamTransmition(new StreamInformation(buffer, des){
			
			@Override
			public void onFileTransmitStatusChanged(FileTransmitStatus s) {
				switch(s){
				case MD5VerifyFailed:
				{
					FileTransmition.getInstance().beginStreamTransmition(this);
					break;
				}
				case FileStorageFailed:
				case FileTransmitted:
				{
					String temp = "a";
					CommunicationManager.getInstance().writeData(FileTransmition.MESSAGE_FILE_REQUEST_REPLY, "message", des, 
							s.toString(), temp.getBytes(), temp.getBytes().length);
					break;
				}
				default:
					break;
				}
			}
			
		});
		return true;
	}

}
