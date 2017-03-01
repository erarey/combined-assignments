package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.message.Request;
import com.cooksys.ftd.assignments.concurrency.model.message.RequestType;
import com.cooksys.ftd.assignments.concurrency.model.message.Response;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ClientInstance implements Runnable {

	Queue<Request> requests = null;
	ClientInstanceConfig config = null;
	Socket socket;

	public ClientInstance(ClientInstanceConfig config, Socket socket) {
		this.socket = socket;
		requests = new LinkedList<Request>(config.getRequests());
		this.config = config;
	}

	@Override
	public void run() {
		try {

			InputStream in = socket.getInputStream();

			BufferedInputStream buff_in = new BufferedInputStream(in);

			OutputStream out = socket.getOutputStream();

			BufferedOutputStream buff_out = new BufferedOutputStream(out);

			while (!requests.isEmpty()) {
				try {
					
					System.out.println("There are " + requests.size() + " left to send.");
					
					Request rt = requests.poll();
					Response response;
					
					if (rt.getType() == RequestType.DONE)
					{
						response = new Response("DONE", rt.getType(), true);
					}
					else if (rt.getType() == RequestType.IDENTITY)
					{
						
					}
					Thread.sleep(config.getDelay());

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void close()
	{
		
	}
}
