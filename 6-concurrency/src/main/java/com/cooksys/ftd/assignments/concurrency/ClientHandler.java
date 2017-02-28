package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ClientHandler implements Runnable {

	Socket socket = null;
	boolean open = true;
	double upTime = 0;

	ClientHandler() {
		upTime = System.currentTimeMillis();
	}

	@Override
	public void run() {
		try {
			InputStream in = socket.getInputStream();
			
			BufferedInputStream buff_in = new BufferedInputStream(in);
			
			OutputStream out = socket.getOutputStream();
			
			BufferedOutputStream buff_out = new BufferedOutputStream(out);
			
			
			while (open) {
				if (socket == null || socket.isClosed() || !socket.isBound()) {
					close();
					continue;
				}
				// if DONE, close, else TIME, SEND TIME, else IDENTITY, send ID
				while (buff_in.read() != -1)
				
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		open = false;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
