package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.concurrency.model.message.Request;
import com.cooksys.ftd.assignments.concurrency.model.message.RequestType;
import com.cooksys.ftd.assignments.concurrency.model.message.Response;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ClientHandler implements Runnable {
	private RequestReceiver recieverRunnable;

	private Thread receiver;

	private Object lock = new Object();
	private Socket socket = null;
	private ServerSocket ss = null;
	public boolean open = true;
	private double startTime = 0;

	// TODO: switch to some kind of Syncd LinkedList; determine if
	// unsentResponseSyncd is necessary; getters and setters for lists/queues

	public List<Request> unprocessedRequestsSyncd = Collections.synchronizedList(new ArrayList<Request>());

	public List<Response> unsentResponseSyncd = Collections.synchronizedList(new ArrayList<Response>());

	ClientHandler(Socket socket, ServerSocket ss) {
		startTime = System.currentTimeMillis();
		this.socket = socket;
		this.ss = ss;
		System.out.println("made client handler");
	}

	@Override
	public void run() {
		try {

			recieverRunnable = new RequestReceiver(socket.getInputStream(), unprocessedRequestsSyncd, lock);

			Thread receiver = new Thread(recieverRunnable);

			receiver.start();

			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());

			while (open == true) {
				Thread.sleep(500);

				try {
					List<Request> tempList = new ArrayList<Request>();

					synchronized (lock) {
						tempList.addAll(unprocessedRequestsSyncd);
						unprocessedRequestsSyncd.clear();
					}

					while (!tempList.isEmpty()) {
						Response re = new Response();

						JAXBContext context = JAXBContext.newInstance(Response.class);

						Marshaller marshaller = context.createMarshaller();

						StringWriter sw = new StringWriter();

						Request rt = tempList.remove(tempList.size() - 1);

						switch (rt.getType()) {

						case IDENTITY:
							re.setData("Server's IP is " + socket.getInetAddress() + " at port " + socket.getPort());
							re.setType(RequestType.IDENTITY);
							re.setSuccessful(true);
							break;

						case TIME:
							re.setData("You have been talking to client handler for "
									+ (System.currentTimeMillis() - startTime) + " milliseconds");
							re.setType(RequestType.TIME);
							re.setSuccessful(true);
							break;

						case DONE:
							re.setData("Bye!");
							re.setType(RequestType.DONE);
							re.setSuccessful(true);
							marshaller.marshal(re, sw);

							sw.flush();

							out.write(sw.toString());

							out.flush();
							//close();
							break;
						}
						
						if (re.getType() != RequestType.DONE) {
							marshaller.marshal(re, sw);

							sw.flush();

							out.write(sw.toString());

							out.flush();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					// System.out.println("JAXB failed (inner exception)");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				// socket.getOutputStream().close();
				//socket.close();
				recieverRunnable.open = false;
				if (receiver.isAlive())
					receiver.join();
				// Thread.currentThread().join();
			} catch (InterruptedException e) {
				// System.out.println("Failed to close a ClientHandler's
				// socket");
				e.printStackTrace();
			}
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
