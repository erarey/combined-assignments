package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.concurrency.model.config.StayOpenInputStream;
import com.cooksys.ftd.assignments.concurrency.model.message.Request;
import com.cooksys.ftd.assignments.concurrency.model.message.RequestType;
import com.cooksys.ftd.assignments.concurrency.model.message.Response;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ClientHandler implements Runnable {

	Socket socket = null;
	boolean open = true;
	double upTime = 0;

	ClientHandler(Socket socket) {
		upTime = System.currentTimeMillis();
		this.socket = socket;
		System.out.println("made client handler");
	}

	@Override
	public void run() {
		try {
			//InputStream in = new StayOpenInputStream(socket.getInputStream());
			
			InputStreamReader in = new InputStreamReader(new BufferedInputStream(socket.getInputStream()));
			
			OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream()));
			
			//BufferedInputStream buff_in = new BufferedInputStream(in);
			
			//StringWriter out = new StringWriter();
			//StringBuilder out2 = new StringBuilder();
			//BufferedOutputStream buff_out = new BufferedOutputStream(out);
			
			JAXBContext context = JAXBContext.newInstance(Request.class, Response.class);
			
			//will these streams be closed after 1 object?
			
			Marshaller marshaller = context.createMarshaller();
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			while (open) {
				if (socket == null || socket.isClosed() || !socket.isBound()) {
					System.out.println("The socket was closed!, closing down ClientHandler");
					close();
					continue;
				}
				// if DONE, close, else TIME, SEND TIME, else IDENTITY, send ID
				Thread.sleep(2000);
				
				System.out.println("trying to receive...");
				
				Request re = (Request)unmarshaller.unmarshal(in);
				
				System.out.println("received!");
				
				System.out.println(re.getType().toString());
				
				RequestType rt = re.getType();
				
				if (rt == RequestType.IDENTITY)
				{
					Response res = new Response("MY ID IS ...", RequestType.IDENTITY, true);
					marshaller.marshal(res, out);
				}
				else if (rt == RequestType.TIME)
				{
					Response res = new Response("" + (System.currentTimeMillis() - upTime), RequestType.TIME, true);
					marshaller.marshal(res, out);
				}
				else if (rt == RequestType.DONE)
				{
					Response res = new Response("DONE!!", RequestType.DONE, true);
					marshaller.marshal(res, out);
					close();
				}
				/*
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
				*/
				
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
