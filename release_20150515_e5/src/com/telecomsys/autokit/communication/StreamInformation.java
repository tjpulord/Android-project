package com.telecomsys.autokit.communication;

import com.telecomsys.autokit.communication.FileTransmition.FileTransmitListener;

public abstract class StreamInformation  implements FileTransmitListener {
	private byte[] fileContent;
	private String des;
	
	public StreamInformation(final byte[] fileContent, final String des){
		this.fileContent = fileContent;
		this.des = des;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public String getDestination() {
		return des;
	}
}