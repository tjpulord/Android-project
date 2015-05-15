package com.telecomsys.autokit.communication;

import java.util.ArrayList;
import java.util.List;

public class FileTransmition {
	
	public enum FileTransmitStatus{
		RequestSendFailed,
		RequestSendSucceed,
		MD5VerifyFailed,
		FileStorageFailed,
		FileTransmitted
	}
	
	public interface FileRequestListener{
		public boolean onFileRequested(final String des, final String src);
	}
	
	protected interface FileTransmitListener{
		public void onFileTransmitStatusChanged(FileTransmitStatus s);
	}
	
	private static  FileTransmition ftransmition = new FileTransmition();
	private FileRequestListener fileListener = null;
	public static final String MESSAGE_FILE_TRANSMITION = "mft";
	public static final String MESSAGE_FILE_TRANSMITION_REPLY = "mftr";
	public static final String MESSAGE_FILE_REQUEST = "mfr";
	public static final String MESSAGE_FILE_REQUEST_REPLY = "mfrr";
	public List<FileRequestInformation> fileInfos = new ArrayList<FileRequestInformation>();
	public List<StreamInformation> streamInfos = new ArrayList<StreamInformation>();
	
	public static FileTransmition getInstance(){
		return ftransmition;
		
	}
	
	public void setFileRequestListener(FileRequestListener listener){
		fileListener = listener;
	}
	
	public boolean requestFile(FileRequestInformation info) throws Exception{
//		fileInfos.add(info);
//		CommunicationManager.getInstance().writeData(MESSAGE_FILE_REQUEST, "message", info.getFileWrittenName(), 
//													 null, info.getFileNeededWritten().getBytes(), 
//													 info.getFileNeededWritten().getBytes().length);
//		return true;
		throw new Exception("no implementation!");
	}
	
	public boolean beginStreamTransmition(StreamInformation info){
		streamInfos.add(info);
		CommunicationManager.getInstance().writeData(MESSAGE_FILE_TRANSMITION, "file", info.getDestination(), 
				 null, info.getFileContent(), info.getFileContent().length);
		return true;
		
	}
	
	protected void fileTransmitionReplyReceived(final String des, final String status){
		StreamInformation sinfo = null;
		for(StreamInformation info: streamInfos){
			if(info.getDestination().equals(des)){
				sinfo = info;
				streamInfos.remove(info);
				break;
			}
		}
		if(sinfo != null)
			sinfo.onFileTransmitStatusChanged(FileTransmitStatus.valueOf(status));
	}
	
	protected void fileReceived(final String des, byte[] contents) throws Exception{
		throw new Exception("no implementation!");
	}
	
	protected void fileRequestReceived(final String des, final String src){
		boolean res = (fileListener != null)&&fileListener.onFileRequested(des, src);
			if(!res){
				String temp = "a";
				CommunicationManager.getInstance().writeData(MESSAGE_FILE_REQUEST_REPLY, "message", des, 
						FileTransmitStatus.RequestSendFailed.toString(), temp.getBytes(), temp.getBytes().length);
			}
	}
	
	protected void fileRequestReplyReceived(final String des, final String status) throws Exception{
//		for(FileRequestInformation info: fileInfos){
//			if(info.getFileWrittenName().equals(fileWrittenName)){
//				info.onFileTransmitStatusChanged(FileTransmitStatus.valueOf(status));
//				fileInfos.remove(info);
//				break;
//			}
//		}
		throw new Exception("no implementation!");
	}
}
