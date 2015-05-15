package com.yesway.izhijia.model.car;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

class CarServiceImpl extends CarService {
	private Socket socket;

	@Override
	public void connect(String ip, int port) throws Exception {
		if(socket == null || !socket.isConnected()){
			socket = new Socket();
			SocketAddress address = new InetSocketAddress(ip, port);
			socket.connect(address, 5000);
		}
	}
}
