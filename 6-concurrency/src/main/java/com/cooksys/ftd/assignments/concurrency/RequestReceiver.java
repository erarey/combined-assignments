package com.cooksys.ftd.assignments.concurrency;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.concurrency.model.message.Request;

//TODO: make generic

public class RequestReceiver implements Runnable {

	private InputStream input = null;

	private List<Request> deposit = null;
	
	Object lock = null;
	public RequestReceiver(InputStream in, List<Request> deposit, Object lock) {
		this.input = in;
		this.deposit = deposit;
		this.lock = lock;
	}

	@Override
	public void run() {
		try {
			String classname = "request";

			System.out.println("A receiver thread is active");

			InputStreamReader in = new InputStreamReader(input);
			JAXBContext context = JAXBContext.newInstance(Request.class);

			Unmarshaller unmarshaller = context.createUnmarshaller();

			Request latest = null;

			StringWriter sw = new StringWriter();

			ArrayList<String> xmlObjectsFound = new ArrayList<String>();

			int found = 0;

			while (true) {

				int leftB = 0;
				int rightB = 0;
				String s = "";

				boolean rootNodeComplete = false;

				while (rootNodeComplete == false && in.ready()) {

					char c = (char) in.read();

					s += c;

					// if (c == '<') {
					// leftB++;
					if (c == '>') {
						// System.out.println("looking for Request end node");
						// rightB++;
						// if (leftB == rightB) {
						if (s.indexOf(("</" + classname + ">")) != -1) {
							//System.out.println("found request end node");
							int endOfRootNode = s.indexOf(("</" + classname + ">")) + classname.length() + 3;
							System.out.println("*******" + s);
							xmlObjectsFound.add(s.substring(s.indexOf("<?xml"), endOfRootNode));
							s = "";
							rootNodeComplete = true;
						}
					}
					// }

				}

				// System.out.println(s);
				// Thread.sleep(5000);
				// System.out.println(s);
				// s = sw.toString();
				

				while (!xmlObjectsFound.isEmpty()) {
					 System.out.println("XMLObjectsFound size = " + xmlObjectsFound.size());
					latest = null;
					String xmlS = xmlObjectsFound.remove(xmlObjectsFound.size()-1);
					if (xmlS != "") {
						//System.out.println("trying to convert...");
						//System.out.println(xmlS);
						StringReader tempStringReader = new StringReader(xmlS);
						latest = (Request) unmarshaller.unmarshal(tempStringReader);
						tempStringReader.close();
						System.out.println("RequestReceiver got: ");

						if (latest != null) {
							System.out.println(latest.getType().toString());

							// System.out.println("After receiving,
							// input status is: ");
							synchronized(lock)
							{
								deposit.add(new Request(latest.getType()));
							// s = "";
								latest = null;
							}

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
