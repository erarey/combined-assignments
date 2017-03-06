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

	private Object lock = new Object();
	private Socket socket = null;
	private ServerSocket ss = null;
	public boolean open = true;
	private double startTime = 0;

	// TODO: switch to some kind of Syncd LinkedList; determine if
	// unsentResponseSyncd is necessary; getters and setters for lists/queues

	public List<Request> unprocessedRequestsSyncd = Collections.synchronizedList(new ArrayList<Request>());

	public List<Response> unsentResponseSyncd = Collections.synchronizedList(new ArrayList<Response>());

	// ArrayList<String> strCollect = new ArrayList<String>();

	ClientHandler(Socket socket, ServerSocket ss) {
		startTime = System.currentTimeMillis();
		this.socket = socket;
		this.ss = ss;
		System.out.println("made client handler");
	}

	@Override
	public void run() {
		try {

			Thread receiver = new Thread(new RequestReceiver(socket.getInputStream(), unprocessedRequestsSyncd));

			receiver.start();

			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());

			JAXBContext context = JAXBContext.newInstance(Response.class);

			Marshaller marshaller = context.createMarshaller();

			StringWriter sw = new StringWriter();

			while (open) {
				Thread.sleep(500);
				while (!unprocessedRequestsSyncd.isEmpty()) {

					try {

						// System.out.println("There are " +
						// unprocessedRequestsSyncd.size() + " left to
						// process.");

						// while this is probably better for memory access, it
						// can result in sending responses as if they were in a
						// stack
						Response re = new Response();
						
						synchronized (lock) {
							Request rt = unprocessedRequestsSyncd.remove(unprocessedRequestsSyncd.size() - 1);

							

							switch (rt.getType()) {
							case IDENTITY:
								re.setData("Received request for ID...");
								re.setType(RequestType.IDENTITY);
								re.isSuccessful();
								break;

							case TIME:
								re.setData("" + (System.currentTimeMillis() - startTime));
								re.setType(RequestType.IDENTITY);
								re.isSuccessful();
								break;

							case DONE:
								re.setData("Closing down connection.");
								re.setType(RequestType.DONE);
								re.isSuccessful();
								break;
							}
						}
						// re = new Response(new String("Got your request for
						// identity"), rt.getType(), true);

						System.out.println("trying to write back: " + re.getData());
						marshaller.marshal(re, sw);

						sw.flush();

						out.write(sw.toString());

						out.flush();

						// sw.close();

						// System.out.println("client is waiting for
						// response...");
						// Thread.sleep(4000);
						// Thread.sleep(config.getDelay());

					} catch (Exception e) {
						System.out.println("JAXB failed (inner exception)");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Failed to close a ClientHandler's socket");
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
