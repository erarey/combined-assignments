package com.cooksys.ftd.assignments.concurrency;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

	Socket socket = null;
	ServerSocket ss = null;
	boolean open = true;
	double upTime = 0;

	// TODO: switch to some kind of Syncd LinkedList; determine if
	// unsentResponseSyncd is necessary; getters and setters for lists/queues

	public List<Request> unprocessedRequestsSyncd = Collections.synchronizedList(new ArrayList<Request>());

	public List<Response> unsentResponseSyncd = Collections.synchronizedList(new ArrayList<Response>());

	// ArrayList<String> strCollect = new ArrayList<String>();

	ClientHandler(Socket socket, ServerSocket ss) {
		upTime = System.currentTimeMillis();
		this.socket = socket;
		this.ss = ss;
		System.out.println("made client handler");
	}

	@Override
	public void run() {
		try {

			Thread receiver = new Thread(new RequestReceiver(socket.getInputStream(), unprocessedRequestsSyncd));

			receiver.start();
			// InputStream in = new
			// StayOpenInputStream(socket.getInputStream());

			// InputStreamReader in = new InputStreamReader(new
			// BufferedInputStream(socket.getInputStream()));

			// OutputStreamWriter out = new OutputStreamWriter(new
			// BufferedOutputStream(socket.getOutputStream()));

			// BufferedInputStream buff_in = new BufferedInputStream(in);

			// StringWriter out = new StringWriter();
			// StringBuilder out2 = new StringBuilder();
			// BufferedOutputStream buff_out = new BufferedOutputStream(out);

			JAXBContext context = JAXBContext.newInstance(Response.class);

			// will these streams be closed after 1 object?

			Marshaller marshaller = context.createMarshaller();

			// Unmarshaller unmarshaller = context.createUnmarshaller();

			while (true) {
				// block for testing
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

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
