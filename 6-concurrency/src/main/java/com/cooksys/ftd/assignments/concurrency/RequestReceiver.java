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
	
	public RequestReceiver (InputStream in, List<Request> deposit)
	{
		this.input = in;
		this.deposit = deposit;
		
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

			while (true) {

				int leftB = 0;
				int rightB = 0;
				String s = "";

				boolean rootNodeComplete = false;

				while (rootNodeComplete == false && in.ready()) {
					char c = (char) in.read();

					s += c;

					if (c == '<') {
						leftB++;
					} else if (c == '>') {
						// System.out.println("looking for Request end node");
						rightB++;
						if (leftB == rightB) {
							if (s.indexOf(("</" + classname + ">")) != -1) {
								// System.out.println("found request end node");
								int endOfRootNode = s.indexOf(("</" + classname + ">")) + classname.length() + 3;
								xmlObjectsFound.add(s.substring(s.indexOf("<?xml"), endOfRootNode));
								s = s.substring(endOfRootNode);
								rootNodeComplete = true;
							}
						}
					}
				}
				// System.out.println(s);
				// s = sw.toString();
				// System.out.println("XMLObjectsFound size = " +
				// xmlObjectsFound.size());

				for (int i = xmlObjectsFound.size() - 1; i > 0; i--) {
					latest = null;
					String xmlS = xmlObjectsFound.remove(i);
					if (xmlS != "") {
						System.out.println("trying to convert...");
						System.out.println(xmlS);

						latest = (Request) unmarshaller.unmarshal(new StringReader(xmlS));
						System.out.println("Receiver got: ");

						if (latest != null) {
							System.out.println(latest.getType().toString());

							// System.out.println("After receiving,
							// input status is: ");
							
							deposit.add(new Request(latest.getType()));
							// s = "";
							latest = null;

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
