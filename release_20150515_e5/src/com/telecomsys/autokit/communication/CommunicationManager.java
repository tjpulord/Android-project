package com.telecomsys.autokit.communication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.telecomsys.autokit.util.MD5Check;
import com.telecomsys.autokit.util.MainLoopPosting;


public class CommunicationManager {

    public interface CommunicationManagerListener {

        public void onConnected();
        public void onConnectedFailed();
        public void onDisconnected();
    }

    
    public static final String MESSAGE_MODULE_HEARTBEAT = "hb";
    public static final String MESSAGE_MODULE_CDM = "CDM";
    public static final String MESSAGE_MODULE_APPMANAGER = "AppManager";
    
    public static final String MESSAGE_TYPE_MESSAGE = "message";
    public static final String MESSAGE_TYPE_FILE = "file";
    
    private  CommunicationManagerListener statusListener;
    //private Messenger serviceMessenger = null;
    private Context context = null;
    private CommunicationClient cmClient;
    private boolean running = true;
    private static CommunicationManager manager = new CommunicationManager();
    public static CommunicationManager getInstance(){
    	return manager;
    }
    
    public void setContext(Context context){
    	this.context = context;
    }
    
    public CommunicationManager() {
        cmClient = new CommunicationClient(new CommunicationClientListenerImpl(this));
        loop();
    }
    
    public void setCommunicationStatusListener(CommunicationManagerListener listener){
    	statusListener = listener;
    }
    
    public CommunicationManagerListener getCommunicationStatusListener(){
    	return statusListener;
    }
    
    public void connect(String address) {
        cmClient.connect(address);
    }
    
    public void listen() {
        cmClient.listen();
    }
    
    public void disconnect() {
        cmClient.closeConnection();
    }
    
    public void writeData(String module, String type, String path, String params, byte [] data, int size) {
    	String md5 = MD5Check.bytesToMD5(data);
        cmClient.writePackage(module, type, path, md5, params, data, size);
    	String log = "Send data size is" + String.valueOf(size) + " === md5 is" + md5;
    	Log.d("md5", log);
    }

    public void showMessage(final String msg) {
    	
    	if(context == null){
    		return;
    	}
        MainLoopPosting.getInstance().post(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(context,
                        msg, Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }

    private void loop() {
        Thread thread = new Thread() {
            public void run() {
                while (running) {
                    if (cmClient.isConnected()) {
                        String heartbeat = "heartbeat";
                        cmClient.writePackage(MESSAGE_MODULE_HEARTBEAT, MESSAGE_TYPE_MESSAGE, "", "", "",
                                heartbeat.getBytes(), heartbeat.getBytes().length);
                        //for (int i = 0; i < 3; ++i)
                        //    cmClient.writePackage(heartbeat.getBytes(), heartbeat.getBytes().length);
//                        StringBuffer sb = new StringBuffer();
//                        for (int i = 0; i < 16384; ++i) {
//                            int c = 'a' + (i%26);
//                            sb.append((char)c);
//                        }
//                        for (int i = 0; i < 2; ++i)
//                            cmClient.writePackage(MESSAGE_TYPE_HEARTBEAT, 
//                                    sb.toString().getBytes(), sb.toString().getBytes().length);
//                        DynamicCommonDataManager.getInstance().update("testtest", "\"yes\taa\n\rno\"");
                    }
                    try {
                        Thread.sleep(1000);
                    }
                    catch (Exception e) {
                        
                    }
                }
            }
        };
        thread.start();
    }

}
