package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.message.Request;
import com.cooksys.ftd.assignments.concurrency.model.message.RequestType;
import com.cooksys.ftd.assignments.concurrency.model.message.Response;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ClientInstance implements Runnable {

	//Queue<Request> requests = null;
	//Queue<Response> responses = new LinkedList<Response>();
	
	public List<Request> unsentRequestsSyncd = null;
	
	public List<Response> unprocessedResponsesSyncd = Collections.synchronizedList(new ArrayList<Response>());
	
	//ArrayList<String> strCollect = new ArrayList<String>();
	ClientInstanceConfig config = null;
	Socket socket;

	public Socket getSocket() {
		return socket;
	}

	public ClientInstance(ClientInstanceConfig config, Socket socket) {
		System.out.println("made a client instance");
		this.socket = socket;
		
		unsentRequestsSyncd = Collections.synchronizedList(new ArrayList<Request>(config.getRequests()));
		
		Collections.reverse(unsentRequestsSyncd); //so that it can be treated as a queue via remove of last item.
		
		//requests = new LinkedList<Request>(config.getRequests());
		this.config = config;
	}

	@Override
	public void run() {
		try {
			
			//Output
			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());

			JAXBContext context = JAXBContext.newInstance(Response.class, Request.class);

			Marshaller marshaller = context.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			StringWriter sw = new StringWriter();
			
			while (true)// socket.isConnected() && !socket.isClosed())
			{
				if (!unsentRequestsSyncd.isEmpty()) {

					try {

						System.out.println("There are " + unsentRequestsSyncd.size() + " left to send.");

						Request rt = unsentRequestsSyncd.remove(unsentRequestsSyncd.size()-1);
						
						marshaller.marshal(rt, sw);
						
						sw.flush();
						
						out.write(sw.toString());
			
						out.flush();
						
						//sw.close();
						
						System.out.println("client is waiting for response...");
						//Thread.sleep(4000);
						Thread.sleep(config.getDelay());

					} catch (InterruptedException e) {
						System.out.println("JAXB failed (inner exception)");
					}
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("JAXB failed");
		}
	}

	// void close() {

	// }
}
