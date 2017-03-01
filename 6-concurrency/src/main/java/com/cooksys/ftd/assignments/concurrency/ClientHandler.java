package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.cooksys.ftd.assignments.concurrency.model.message.RequestType;

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
				String msg_received = "";
				String msg_return = "";
				
				while (buff_in.read() != -1)
				{
					msg_received += buff_in.read();
					System.out.println("received: " + msg_received);
				}
				if (msg_received == RequestType.IDENTITY.toString())
				{
					System.out.println("request for identity");
					msg_return = socket.getInetAddress().getHostName();
					byte[] bytes = msg_return.getBytes();
					out.write(bytes);
					out.flush();
				}
				else if (msg_received == RequestType.TIME.toString())
				{
					System.out.println("request for time");
					Long time = System.currentTimeMillis();
					msg_return = time.toString();
					byte[] bytes = msg_return.getBytes();
					out.write(bytes);
					out.flush();
				}
				else if (msg_received == RequestType.DONE.toString())
				{
					System.out.println("request for done");
					close();
				}
				
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
