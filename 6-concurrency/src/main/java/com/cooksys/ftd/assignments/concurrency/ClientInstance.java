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
	Thread receiver = null;
	
	ResponseReceiver receiverRunnable = null;
	
	Object lock = new Object();

	boolean open = true;

	public List<Request> unsentRequests = null;

	public List<Response> unprocessedResponsesSyncd = Collections.synchronizedList(new ArrayList<Response>());

	// ArrayList<String> strCollect = new ArrayList<String>();
	ClientInstanceConfig config = null;
	Socket socket;

	public Socket getSocket() {
		return socket;
	}

	public ClientInstance(ClientInstanceConfig config, Socket socket) {
		System.out.println("made a client instance");
		this.socket = socket;

		unsentRequests = new ArrayList<Request>(config.getRequests());

		Collections.reverse(unsentRequests);

		this.config = config;
	}

	@Override
	public void run() {
		try {
			int expectedResponses = unsentRequests.size();
			
			receiverRunnable = new ResponseReceiver(socket.getInputStream(), unprocessedResponsesSyncd, lock);
			receiver = new Thread(receiverRunnable);
					

			receiver.start();

			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());

			List<Response> tempList = new ArrayList<Response>();

			while (open == true) {
				if (!unsentRequests.isEmpty()) {

					try {
						JAXBContext context = JAXBContext.newInstance(Response.class, Request.class);

						Marshaller marshaller = context.createMarshaller();

						//marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

						StringWriter sw = new StringWriter();

						// System.out.println("There are " +
						// unsentRequests.size() + " left to send.");
						Request rt = unsentRequests.remove(unsentRequests.size() - 1);

						sw.flush();
						out.flush();

						marshaller.marshal(rt, sw);

						sw.flush();

						out.write(sw.toString());

						out.flush();

						sw.close();

						System.out.println("After sending, there are " + unsentRequests.size() + " left");
						// Thread.sleep(4000);

						Thread.sleep(config.getDelay());

					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("JAXB failed (inner exception)");
					}
				}

				synchronized (lock) {
					tempList.addAll(unprocessedResponsesSyncd);
					unprocessedResponsesSyncd.clear();
				}
				while (!tempList.isEmpty()) {
					// System.out.println("There are " +
					// unprocessedResponsesSyncd.size() + " left to process.");

					Response re = tempList.remove(tempList.size() - 1);

					System.out.println("Client received response from Handler: " + re.getType() + " : " + re.getData()
							+ " success: " + re.isSuccessful());
					
					expectedResponses--;

				}
				
				if (expectedResponses == 0) close();

			}

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("JAXB failed");
		} finally {
			close();
			try {
				//socket.getOutputStream().close();
				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void close() {
		open = false;
		try {
			//System.out.println("clientInstance trying to close!!");
			receiverRunnable.open = false;
			receiver.join();
			//Thread.currentThread().stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
