package com.telecomsys.autokit.communication;

import com.telecomsys.autokit.communication.FileTransmition.FileTransmitListener;

public abstract class FileRequestInformation implements FileTransmitListener{
	private String des;
	private String src;
	
	FileRequestInformation(final String des, final String src){
		this.des = des;
		this.src = src;
	}

	public String getDestination() {
		return des;
	}

	public String getSource() {
		return src;
	}
	
}