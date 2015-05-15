package com.telecomsys.autokit.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;


class CommunicationClient implements Runnable {

    public interface CommunicationClientListener {

        public void onListening();
        public void onConnected();
        public void onDisconnected();
        public void onConnectedFailed();
        public void onPaired();
        
        public void onReceived(AutokitHeader header, byte [] data);
    }
    

	private Socket m_socket = null;
	protected InputStream m_input = null;
	protected OutputStream m_output = null;
	protected Thread m_thread;
	private boolean m_isNotificationSent = false;
	private byte[] m_readBuffer = null;
	private int m_available = 0;
	//private int m_read = 0;
	private ReadThread m_readThread;
	private static int BUFFER_SIZE = 16384;
	//private Context context;
	private boolean mConnected = false;
	private static int PORT = 12345;
	private ServerSocket m_serverSocket = null;
	
	private CommunicationClientListener listener;
	private static int SYMBOL_SIZE = 4;
	private static int HEADER_FLAG = 4;
	private static int BODY_FLAG = 4;
	private byte [] symbol = {0x44, 0x33, 0x22, 0x11};
	
	public CommunicationClient(CommunicationClientListener listener) {
	    //this.context = context;
	    this.listener = listener;
	}
	
	public boolean isConnected() {
	    return mConnected;
	}
	
	public void listen() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    m_serverSocket = new ServerSocket(PORT); 
                    m_serverSocket.setSoTimeout(3000);
                    listener.onListening();
                    m_socket = m_serverSocket.accept();
                    m_serverSocket.close();
                    m_socket.setKeepAlive(true);
                    m_socket.setTcpNoDelay(true);
                    listener.onConnected();
                    mConnected = true;
                    
                    m_input = m_socket.getInputStream();
                    m_output = m_socket.getOutputStream();
                    
                    CommunicationClient.this.start();
                } catch(Exception e1) { 
                    e1.printStackTrace();
                    m_socket = null;
                } 
            }
        };
        thread.start();
	}

	public void connect(final String address) {

	    Thread thread = new Thread() {
	        public void run() {
	            try {
	                InetAddress serverAddr = InetAddress.getByName(address);//TCPServer.SERVERIP
	                m_socket = new Socket(serverAddr, PORT);
	                //showMessage("Connected.");
	                m_socket.setSoTimeout(3000);
	                mConnected = true;

	                m_input = m_socket.getInputStream();
	                m_output = m_socket.getOutputStream();

	                CommunicationClient.this.start();
	                listener.onConnected();
	            } catch(Exception e1) {
	            	listener.onConnectedFailed();
	                e1.printStackTrace();
	                m_socket = null;
	            } 
	        }
	    };
	    thread.start();

 	}

	private void start() {
		//Start Socket
		m_readBuffer = new byte[BUFFER_SIZE];

		//m_thread = new Thread(this);
		//m_thread.start();

		m_readThread = new ReadThread();
		m_readThread.start();
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ex) {
				//ex.printStackTrace();
			}
			boolean sendNotification = false;
			synchronized(this) {
				if( m_input != null ) {
					if( m_available > 0 && !m_isNotificationSent ) {
						m_isNotificationSent = true;
						sendNotification = true;
					}
				}
			}
			if( sendNotification ) {
				//notifyForData();
			}
		}
	}

//	private int readData(byte[] buffer, int size) {
//		synchronized(this) {
//			m_isNotificationSent = false;
//			if( m_input != null ) {
//
//				if( m_available < 1 ) {
//					return 0;
//				}
//
//				int bytesToRead = m_available - m_read;
//
//				if( bytesToRead > size) {
//					bytesToRead = size;
//				}
//
//				System.arraycopy(m_readBuffer, m_read, buffer, 0, bytesToRead);
//				m_read += bytesToRead;
//
//				if( m_read >= m_available) {
//					m_read = 0;
//					m_available = 0;
//				}
//
//				return bytesToRead;
//			}
//			return 0;
//		}
//	}

	/**
	 * Handles IO exception. Closes connection.
	 * @param ex
	 */
	protected void onIOException(IOException ex) {
		closeConnection();
	}

	/**
	 * Writes data to output IO stream
	 */
//	public void writeData(byte[] buffer, int size) {
//		OutputStream out = null;
//		synchronized(this) {
//			out = m_output;
//		}
//		try {
//			if( out != null ) {
//				out.write(buffer, 0, size);
//			}
//		} catch (IOException ex) {
//			onIOException( ex );
//		}
//	}
	
	
	public void writePackage(String module, String type, String path, String md5, String params, byte[] buffer, int size) {

        StringBuffer header = new StringBuffer();
        header.append("{\"module\":\"").append(module).append("\",");
        header.append("\"type\":\"").append(type).append("\",");
        header.append("\"path\":\"").append(path).append("\",");
        header.append("\"md5\":\"").append(md5).append("\",");
        header.append("\"params\":\"").append(params).append("\"}");

        byte [] output = new byte[SYMBOL_SIZE + HEADER_FLAG + BODY_FLAG + header.toString().length() + size];
	    int offset = 0;
	    System.arraycopy(symbol, 0, output, offset, symbol.length);
	    offset += symbol.length;
	    
	    byte [] headerFlag = int2Bytes(header.toString().length());
	    System.arraycopy(headerFlag, 0, output, offset, headerFlag.length);
	    offset += headerFlag.length;
	    
        byte [] bodyFlag = int2Bytes(size);
        System.arraycopy(bodyFlag, 0, output, offset, bodyFlag.length);
        offset += bodyFlag.length;
        
        System.arraycopy(header.toString().getBytes(), 0, output, offset, header.toString().length());
        offset += header.toString().length();
        
        System.arraycopy(buffer, 0, output, offset, size);
        
        OutputStream out = null;
        synchronized(this) {
            out = m_output;
        }
        //TODO:synchronize
        try {
            if( out != null ) {
                out.write(output, 0, output.length);
            }
        } catch (IOException ex) {
            onIOException( ex );
        }
	}
	
	private byte[] int2Bytes(int num) {  
	    byte[] byteNum = new byte[4];  
	    for (int ix = 0; ix < 4; ++ix) {  
	        int offset = 32 - (ix + 1) * 8;  
	        byteNum[4 - 1 - ix] = (byte) ((num >> offset) & 0xff);  
	    }  
	    return byteNum;  
	} 

	public static int bytes2Int(byte[] byteNum) {  
	    int num = 0;  
	    for (int ix = 0; ix < 4; ++ix) {  
	        num <<= 8;  
	        num |= (byteNum[4 - 1 - ix] & 0xff);  
	    }  
	    return num;  
	}  

/*
	private void notifyForData() {

		byte [] totalBuffer = new byte[20 * 1024];
		int totalRead = 0;
		while (true) {
		    byte [] buffer = new byte[4 * 1024];
    		int bytesRead = readData(buffer, buffer.length);
    		//String str = new String(buffer, 0, bytesRead);
    		//listener.onReceived(str);
    		//TODO:unpackage
    		while (bytesRead > 0) {
    		    System.out.println("read " + bytesRead + " bytes");
    		    System.arraycopy(buffer, 0, totalBuffer, totalRead, bytesRead);
    		    totalRead += bytesRead;
    		    
    		    int [] offset = new int[1];
    		    
    		    byte [] result = null;
    		    while (true) {
    		        result = retrieveOnePackage(totalBuffer, totalRead, offset);
    		        if (result != null) {
    		            String str = new String(result);
    		            listener.onReceived(str);
    		            //TODO
    		            int left = totalRead - offset[0] - result.length;
    		        }
    		        else {
    		            break;
    		        }
    		    }
    		}
    		
    		break;
		}
		
		if (m_available < 0) {
			closeConnection();
			//showMessage("Closed.");
		}
	}
*/
	private byte[] retrieveOnePackage(byte [] input, int size, 
	        int [] outputOffset, AutokitHeader header) {
	    int remaining = size;
	    int offset = 0;
	    int outputHead = -1;
	    //check symbol
	    for (int i = 0; i < size - symbol.length; ++i) {
	        boolean foundSymbol = true;
	        for (int j = i; j < i + symbol.length; ++j) {
	            if (input[j] != symbol[j-i]) {
	                foundSymbol = false;
	                break;
	            }
	        }
	        if (foundSymbol) {
	            System.out.println("Found symbol at " + i);
	            remaining = remaining - i - symbol.length;
	            offset = i + symbol.length;
	            outputHead = i;
	            break;
	        }
	    }
	    
	    if (remaining >= HEADER_FLAG + BODY_FLAG && outputHead >= 0) {
	        byte [] headFlag = new byte[4];
	        System.arraycopy(input, offset, headFlag, 0, HEADER_FLAG);
	        int headerSize = bytes2Int(headFlag);
	        offset += HEADER_FLAG;
	        
            byte [] bodyFlag = new byte[4];
            System.arraycopy(input, offset, bodyFlag, 0, BODY_FLAG);
            int bodySize = bytes2Int(bodyFlag);
            offset += BODY_FLAG;
            
            byte [] headerData = new byte[headerSize];
            System.arraycopy(input, offset, headerData, 0, headerSize);
            String headerStr = new String(headerData);
            try {
                JSONTokener jsonParser = new JSONTokener(headerStr);
                JSONObject headerObj = (JSONObject) jsonParser.nextValue();
                header.setModule(headerObj.getString("module"));
                header.setType(headerObj.getString("type"));
                header.setPath(headerObj.getString("path"));
                header.setMd5(headerObj.getString("md5"));
                header.setParams(headerObj.getString("params"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            remaining -= HEADER_FLAG + BODY_FLAG;
            if (remaining >= headerSize + bodySize) {
                byte [] result = new byte[bodySize];
                System.arraycopy(input, offset + headerSize, result, 0, bodySize);
                outputOffset[0] = offset + headerSize;
                return result;
            }
	    }
	    return null;
	}
	
	public void closeConnection() {
	    mConnected = false;
		synchronized(this) {
			if( m_input != null ) {
				try {
					m_input.close();
				} catch (IOException e) {
					// Do nothing
				}
				m_input = null;
			}
			if( m_output != null ) {
				try {
					m_output.close();
				} catch (IOException e) {
					// Do nothing
				}
				m_output = null;
			}
			//onConnectionClosed();
		}
        listener.onDisconnected();
		//notifyForConnectionClosed();
		//clearNotifiables();
	}

	private class ReadThread extends Thread {

		private boolean m_canceled = false;

		public ReadThread() {

		}

		public void run() {

	        byte [] totalBuffer = new byte[64 * 1024];
	        int totalRead = 0;
			while( m_canceled == false ) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				if( m_input != null) {
					try {
						m_available = m_input.read(m_readBuffer, 0, BUFFER_SIZE);
						int bytesRead = m_available;
			            if (bytesRead > 0) {
			                //System.out.println("read " + bytesRead + " bytes");
			                System.arraycopy(m_readBuffer, 0, totalBuffer, totalRead, bytesRead);
			                totalRead += bytesRead;
			                
			                int [] offset = new int[1];
			                
			                byte [] result = null;
			                while (true) {
			                    AutokitHeader header = new AutokitHeader();
			                    result = retrieveOnePackage(totalBuffer, totalRead, offset, header);
			                    if (result != null) {
			                        //String str = new String(result);
			                        listener.onReceived(header, result);
			                        int left = totalRead - offset[0] - result.length;
			                        if (left > 0) {
			                            byte [] temp = new byte[left];
			                            System.arraycopy(totalBuffer, offset[0] + result.length, temp, 0, left);
			                            System.arraycopy(temp, 0, totalBuffer, 0, left);
                                        totalRead = left;
			                        }
			                        else {
			                            totalRead = 0;
			                            break;
			                        }
			                    }
			                    else {
			                        break;
			                    }
			                }
			            }
			            else if (bytesRead < 0) {
			                m_canceled = true;
			                closeConnection();
			            }

					} catch (IOException e) {
						e.printStackTrace();
						//onIOException( e );
						m_canceled = true;
						closeConnection();
					}
				}
			}
		}
	}

	
	
}


