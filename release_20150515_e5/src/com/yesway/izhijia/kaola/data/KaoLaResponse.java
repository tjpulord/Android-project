package com.yesway.izhijia.kaola.data;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.telecomsys.autokit.util.JsonUtil;


public class KaoLaResponse {
	private static final String result = "result";
	private String total;
	private String next;
	private String prev;
	List<KaoLaContent> klContentlist;
	List<KaoLaRadioItem> klRadiolist;
	private String requestId;
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getTotal() {
		return total;
	}
	
	public void setNext(String next) {
		this.next = next;
	}
	
	public String getNext() {
		return next;
	}
	
	public void setPrev(String prev) {
		this.prev = prev;
	}
	
	public String getPrev() {
		return prev;
	}
	
	public void setKlContentlist(List<KaoLaContent> klContentlist) {
		this.klContentlist = klContentlist;
	}
	
	public List<KaoLaContent> getKlContentlist() {
		return klContentlist;
	}
	
	public void setKlRadiolist(List<KaoLaRadioItem> klRadiolist) {
		this.klRadiolist = klRadiolist;
	}
	
	public List<KaoLaRadioItem> getKlRadiolist() {
		return klRadiolist;
	}
	
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public String toString() {
		String resString = "";
		JSONObject jObject = new JSONObject();
		
		try {
			if(total != null){
				jObject.put("total", total);
			}
			if(next!= null){
				jObject.put("next", next);
			}
			if(prev != null){
				jObject.put("prev", prev);
			}
			if(klContentlist!=null){
				JSONArray ja = new JSONArray(JsonUtil.ListToJSON(klContentlist));
				jObject.accumulate("list", ja);
				resString = "{\""+result + "\":";
				resString += JsonUtil.unpackJSONStr(jObject.toString()) + ",\"requestId\":\""+requestId+"\"}";
//				jObject.put("list", JsonUtil.unpackJSONStr());
			}
			if(klRadiolist != null){
				JSONArray ja = new JSONArray(JsonUtil.ListToJSON(klRadiolist));
				jObject.accumulate("result", ja);
//				jObject.put("list", JsonUtil.unpackJSONStr(JsonUtil.ListToJSON(klRadiolist)));
				jObject.put("requstId", requestId);
				resString = JsonUtil.unpackJSONStr(jObject.toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resString;
	}
}
