package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.config.StayOpenInputStream;
import com.cooksys.ftd.assignments.concurrency.model.message.Request;
import com.cooksys.ftd.assignments.concurrency.model.message.RequestType;
import com.cooksys.ftd.assignments.concurrency.model.message.Response;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ClientInstance implements Runnable {

	Queue<Request> requests = null;
	Queue<Response> responses = new LinkedList<Response>();

	ClientInstanceConfig config = null;
	Socket socket;
	
	public Socket getSocket()
	{
		return socket;
	}
	
	public ClientInstance(ClientInstanceConfig config, Socket socket) {
		System.out.println("made a client instance");
		this.socket = socket;
		requests = new LinkedList<Request>(config.getRequests());
		this.config = config;
	}

	@Override
	public void run() {
		try {

			//InputStream in = new StayOpenInputStream(socket.getInputStream());

			// BufferedInputStream buff_in = new BufferedInputStream(in);

			//OutputStream out = socket.getOutputStream();
			
			InputStreamReader in = new InputStreamReader(new BufferedInputStream(socket.getInputStream()));
			
			//OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()));
			
			// BufferedOutputStream buff_out = new BufferedOutputStream(out);

			JAXBContext context = JAXBContext.newInstance(Response.class, Request.class);

			Marshaller marshaller = context.createMarshaller();
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			while (socket.isConnected() && !socket.isClosed())
			{
				if (!requests.isEmpty()) {
					try {

						System.out.println("There are " + requests.size() + " left to send.");

						Request rt = requests.poll();
						
						//marshaller.marshal(rt, System.out);
						
						Socket temp_socket = new Socket(socket.getInetAddress(), socket.getPort());
						
						OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(temp_socket.getOutputStream()));
						
						marshaller.marshal(rt, out);
						
						//out.close();
						if (temp_socket.isClosed()){//report whether it was closed, if it was, reopen it!
							temp_socket = new Socket(socket.getInetAddress(), socket.getPort());
						}
						
						Response re = new Response();
						System.out.println("client is waiting for response...");
						
						re = (Response)unmarshaller.unmarshal(in);
						
						responses.add(re);
						
						while (!responses.isEmpty())
						{
							System.out.println("RECEIVED RESPONSE: " + responses.poll().getType().toString());
						}
						
						//in.close();
						
						Thread.sleep(config.getDelay());

					} catch (InterruptedException e) {
						System.out.println("JAXB failed (inner exception)");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("JAXB failed");
		}
	}

	//void close() {

	//}
}
