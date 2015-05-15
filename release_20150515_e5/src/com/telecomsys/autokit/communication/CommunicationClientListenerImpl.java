package com.telecomsys.autokit.communication;

import android.util.Log;

import com.telecomsys.autokit.communication.CommunicationClient.CommunicationClientListener;

public class CommunicationClientListenerImpl implements CommunicationClientListener {

    private CommunicationManager manager;
    public CommunicationClientListenerImpl(CommunicationManager manager) {
        this.manager = manager;
    }
    
    
    @Override
    public void onListening() {
        manager.showMessage("listening...");
    }


    @Override
    public void onConnected() {
        manager.showMessage("已连接");
        if(manager.getCommunicationStatusListener() != null){
        	manager.getCommunicationStatusListener().onConnected();
        }
    }
    
    @Override
    public void onDisconnected() {
        manager.showMessage("连接已断开");
        if(manager.getCommunicationStatusListener() != null){
        	manager.getCommunicationStatusListener().onDisconnected();
        }
    }
    
    @Override
    public void onPaired() {
        manager.showMessage("连接过期");
    }
    
    @Override
    public void onReceived(AutokitHeader header, byte [] data) {
        String msg = new String(data);
        Log.i("tag", "CMHS received:" + header.getModule() + " " + data.length + " " + msg);
        //manager.showMessage("Connected");
        if (CommunicationManager.MESSAGE_MODULE_CDM.equals(header.getModule())) {
            DynamicCommonDataManager.getInstance().onSyncList(data);
        }
        else if(FileTransmition.MESSAGE_FILE_TRANSMITION.equals(header.getModule())){
        	try {
				FileTransmition.getInstance().fileReceived(header.getPath(), data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if(FileTransmition.MESSAGE_FILE_TRANSMITION_REPLY.equals(header.getModule())){
			FileTransmition.getInstance().fileTransmitionReplyReceived(header.getPath(), header.getParams());
        }
        else if(FileTransmition.MESSAGE_FILE_REQUEST.equals(header.getModule())){
		    FileTransmition.getInstance().fileRequestReceived(header.getPath(), new String(data));
        }
        else if(FileTransmition.MESSAGE_FILE_REQUEST_REPLY.equals(header.getModule())){
        	try {
				FileTransmition.getInstance().fileRequestReplyReceived(header.getPath(), header.getParams());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } 
    }


	@Override
	public void onConnectedFailed() {
		manager.showMessage("连接失败");
		if(manager.getCommunicationStatusListener() != null){
			manager.getCommunicationStatusListener().onConnectedFailed();
		}
	}

}
