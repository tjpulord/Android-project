package com.yesway.izhijia.usermanager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserManager {
	private static final String requestHost = "http://111.207.170.16:9900";
	private static final String sendCodeURI = "/system/other/sendmobilecode";
	private static final String loginURI = "/system/user/mobilelogin";
	private static final String logoutURI = "/system/user/logout";
	private String sessionID;
	private String zjid;
	
	private static UserManager manager = new UserManager();
	
	public static UserManager instance(){
		return manager;
	}
	
	private UserInfo parseLoginMsg(String is){
		UserInfo info = new UserInfo();
		try {
			JSONObject j = new JSONObject(is);
			if(j.getJSONObject("ntspheader").getInt("errcode") != 0)
			{
				return null;
			}
			
			sessionID = j.getString("sessionid");
			JSONObject infoObj = j.getJSONArray("userinfo").getJSONObject(0);
			zjid = infoObj.getString("zjid");
			
			/*List<String> phones = new ArrayList<String>();
			
			JSONArray phonesArray = infoObj.getJSONArray("bindphone");
			for(int i=0; i<phonesArray.length(); ++i){
				
			}*/
			info.setBirthday(infoObj.getString("birthday"));
			info.setCity(infoObj.getString("city"));
			info.setCountry(infoObj.getString("country"));
			info.setCounty(infoObj.getString("county"));
			info.setEmail(infoObj.getString("email"));
			info.setFullname(infoObj.getString("fullname"));
			info.setGender(infoObj.getInt("gender"));
			info.setHeadphoto(infoObj.getString("headphoto"));
			info.setMachinephone(infoObj.getString("machinephone"));
			info.setPersonid(infoObj.getString("personid"));
			UserProduct product = new UserProduct();
			JSONObject productInfo = infoObj.getJSONObject("productinfo");
			if(productInfo !=null)
			{
				product.setActivetime(productInfo.getString("activetime"));
				product.setAddtime(productInfo.getString("addtime"));
				product.setDeadline(productInfo.getString("deadline"));
				product.setEnterpriseid(productInfo.getString("enterpriseid"));
				product.setProductid(productInfo.getInt("productid"));
				product.setProductname(productInfo.getString("productname"));
				product.setStatus(productInfo.getInt("status"));
				info.setProductInfo(product);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return info;
	}
	/*
	private static boolean parseLoginMsg(String is){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //å�–å¾—DocumentBuilderFactoryå®žä¾‹  
        DocumentBuilder builder;
        Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(is.getBytes()));   //è§£æž�è¾“å…¥æµ� å¾—åˆ°Documentå®žä¾‹  
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //ä»ŽfactoryèŽ·å�–DocumentBuilderå®žä¾‹  
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Element rootElement = doc.getDocumentElement();  
        NodeList items = rootElement.getElementsByTagName("ntspheader");  
        if(items.getLength() == 0){
        	return false;
        }
        Node header = items.item(0); 
        NodeList subNodes = header.getChildNodes();
        for(int i=0; i<subNodes.getLength(); ++i){
        	Node subNode = subNodes.item(i);
        	if(subNode.getNodeType() == Node.ELEMENT_NODE &&
        	   ((Element)subNode).getTagName().equals("errcode")){
        		if(Integer.valueOf(((Element)subNode).getNodeValue()) != 0)
        		{
        			return false;
        		}
        	}
        }
        
        items = rootElement.getElementsByTagName("sessionid"); 
        if(items.getLength() == 0){
        	return false;
        }
        
        Node sessionNode = items.item(0); 
        if(sessionNode.getNodeType() != Node.ELEMENT_NODE){
        	return false;
        }
        else{
        	sessionID = sessionNode.getNodeValue();
        }
        
        items = rootElement.getElementsByTagName("userinfo"); 
        if(items.getLength() == 0){
        	return false;
        }
        
        Node infoNode = items.item(0); 
        if(infoNode.getNodeType() != Node.ELEMENT_NODE){
        	return false;
        }
        else{
        	NodeList userInfoList = ((Element)infoNode).getElementsByTagName("a:UserInfo");
        	if(userInfoList.getLength() == 0){
        		return false;
        	}
        	
        	NodeList zjids = ((Element)userInfoList.item(0)).getElementsByTagName("a:zjid");
        	if(zjids.getLength() ==0){
        		return false;
        	}
        	Node zjidNode = zjids.item(0);
        	zjid = ((Element)zjidNode).getNodeValue();
        	return true;
        }
	}*/
	
	private static JSONObject formNtspHeader(String zjid, String sessionid){
		JSONObject j = new JSONObject();

		try {
			j.put("apikey", "8970b2bc-4f69-467c-a7ff-c391d53035f9");
			j.put("version", "1.0");
			j.put("sessionid", sessionid);
			j.put("zjid", zjid);
			j.put("productid", "-1");
			j.put("corpid", "1001");
			j.put("errcode", "0");
			j.put("errmsg", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return j;
	}
	
	public boolean sendMobileCode(String number){
		JSONObject o = formNtspHeader("","");
		JSONObject main = new JSONObject();
		try {
			main.put("ntspheader", o);
			main.put("mobile", number);
			main.put("functiontype", 10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		if(requestHTTP(requestHost+sendCodeURI, main.toString()) == null){
			return false;
		}
		return true;
		/*try {
			JSONObject r = new JSONObject(result);
			r.get("deadline");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}*/
		
	}
	
	public UserInfo login(String number, String checkCode){
		JSONObject o = formNtspHeader("","");
		JSONObject main = new JSONObject();
		try {
			main.put("ntspheader", o);
			main.put("mobile", number);
			main.put("checkcode", checkCode);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String result = requestHTTP(requestHost+loginURI, main.toString());
		return parseLoginMsg(result);
	}
	
	public boolean logout(){
		JSONObject o = formNtspHeader(zjid,sessionID);
		JSONObject main = new JSONObject();
		try {
			main.put("ntspheader", o);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		requestHTTP(requestHost+logoutURI, main.toString());
		return true;
	}
	
    private String requestHTTP(String urlStr, String body) {

		byte[] buffer = new byte[32 * 1024];
		HttpURLConnection conn = null;
		String result = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			//è®¾ç½® URLConnection#setDoOutput() ä¸º true ï¼Œéš�æ€§è®¾ç½®è¯·æ±‚æ–¹æ³•ä¸º POSTã€‚ æ ‡å‡†çš„ HTTP POST æ˜¯ä¸€ç§� application/x-www-form-urlencoded ç±»åž‹çš„ç½‘ç»œè¡¨å�•ï¼Œä¼ é€’çš„å�‚æ•°éƒ½ä¼šè¢«å†™å…¥è¯·æ±‚ä¿¡æ�¯ä¸»ä½“ä¸­ã€‚
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.addRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.addRequestProperty("Content-Length", Integer.toString(body.length()));
			
			OutputStream output = null;
			try {
			     output = conn.getOutputStream();
			     output.write(body.getBytes());
			     output.flush();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally {
			     if (output != null)
			     {
			         try { 
			             output.close(); 
			         } 
			         catch (IOException logOrIgnore) {
			        	 logOrIgnore.printStackTrace();
			         }
			     }
			}

			//conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());   

			ByteArrayOutputStream boutput = new ByteArrayOutputStream();
			
			//System.out.println(conn.getContentLength() + " " + conn.getResponseCode());

			int s = 0;
			int t = 0;
			int count = 0;
			while ((s = bis.read(buffer)) != -1) {
		        //boutput.write(buffer);
				boutput.write(buffer, 0, s);
		        t += s;
		        count++;
			}
			boutput.flush();
			result = boutput.toString();
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}
}
