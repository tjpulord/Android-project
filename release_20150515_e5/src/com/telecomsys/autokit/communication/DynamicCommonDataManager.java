package com.telecomsys.autokit.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.telecomsys.autokit.util.JsonUtil;

import android.util.Log;

public class DynamicCommonDataManager {
    
    public interface DynamicCDMListener {
        public void onUpdate(String key, String value, int status);
    }
    
    private static DynamicCommonDataManager instance;

    private DynamicCommonDataManager() {
        
    }
    
    public static DynamicCommonDataManager getInstance() {
        if (instance == null) {
            instance = new DynamicCommonDataManager();
        }
        return instance;
    }

    public static int STATUS_NORMAL = 0;
    public static int STATUS_ERROR = 1;

    private Map<String, List<DynamicCDMListener>> listeners = new HashMap<String, List<DynamicCDMListener>>();

    //public void initialize(CommunicationManager commManager) {
    //    this.commManager = commManager;
    //}
    
    public void release() {
        
    }

    public void onConnected() {
        
    }
    
    public void onDisconnected() {
        //TODO
    }
    
    public boolean update(String key, String value) {
        String packKey = JsonUtil.packJSONStr(key);
        String packValue = JsonUtil.packJSONStr(value);
        
        StringBuffer sb = new StringBuffer("{\"");
        sb.append(packKey).append("\":\"").append(packValue).append("\"}");
        
        CommunicationManager.getInstance().writeData(CommunicationManager.MESSAGE_MODULE_CDM,
                CommunicationManager.MESSAGE_TYPE_MESSAGE,
                "", "",
                sb.toString().getBytes(), 
                sb.toString().getBytes().length);
        return true;
    }
    
    public boolean subscribe(String key, DynamicCDMListener listener) {
        if (key == null || key.trim().isEmpty() || listener == null)
            return false;
        List<DynamicCDMListener> listenerList = listeners.get(key);
        if (listenerList == null) {
            listenerList = new ArrayList<DynamicCDMListener>();
        }
        //TODO:duplicate checking
        listenerList.add(listener);
        listeners.put(key, listenerList);
        
        return true;
    }
    
    public boolean unsubscribe(String key, DynamicCDMListener listener) {
        if (key == null || key.trim().isEmpty() || listener == null)
            return false;
        List<DynamicCDMListener> listenerList = listeners.get(key);
        if (listenerList == null) {
            return false;
        }
        Iterator<DynamicCDMListener> it = listenerList.iterator();
        while (it.hasNext()) {
            DynamicCDMListener tmpListener = it.next();
            if (tmpListener == listener) {
                it.remove();
                return true;
            }
        }
        
        return false;
    }
    
    protected void onSyncList(byte [] data) {
        String str = new String(data);
        try {
            JSONObject json = new JSONObject(str);
            Iterator<?> it = json.keys();
            while (it.hasNext()) {
                String key = (String)it.next();
                String v = json.getString(key);
                String value = JsonUtil.unpackJSONStr(v);
                Log.i("tag", "CDM received:" + key + " : " + value);
                
                List<DynamicCDMListener> listenerList = listeners.get(key);
                if (listenerList != null) {
                    Iterator<DynamicCDMListener> it1 = listenerList.iterator();
                    while (it1.hasNext()) {
                        DynamicCDMListener listener = it1.next();
                        listener.onUpdate(key, value, STATUS_NORMAL);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
