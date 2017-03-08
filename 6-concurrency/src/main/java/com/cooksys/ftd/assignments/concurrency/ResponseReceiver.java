package com.cooksys.ftd.assignments.concurrency;

import java.io.IOException;
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

import com.cooksys.ftd.assignments.concurrency.model.message.Response;

//TODO: make generic

public class ResponseReceiver implements Runnable {

	public boolean open = true;
	
	private InputStream input = null;

	private List<Response> deposit = null;
	Object lock = null;
	
	public ResponseReceiver(InputStream in, List<Response> deposit, Object lock) {
		this.input = in;
		this.deposit = deposit;
		this.lock = lock;
	}

	@Override
	public void run() {
		try {
			String classname = "response";

			System.out.println("A receiver thread is active");

			InputStreamReader in = new InputStreamReader(input);

			

			Response latest = null;

			

			while (open == true) {
				
				String s = "";
				
				ArrayList<String> xmlObjectsFound = new ArrayList<String>();
				
				boolean rootNodeComplete = false;

				while (rootNodeComplete == false && in.ready()) {
					char c = (char) in.read();

					s += c;
					if (c == '>') {
						
						if (s.indexOf(("</" + classname + ">")) != -1) {
							int endOfRootNode = s.indexOf(("</" + classname + ">")) + classname.length() + 3;
							xmlObjectsFound.add(s.substring(s.indexOf("<?xml"), endOfRootNode));
							s = "";
							rootNodeComplete = true;
						}
					}
				}

				while (!xmlObjectsFound.isEmpty()) {
					JAXBContext context = JAXBContext.newInstance(Response.class);

					Unmarshaller unmarshaller = context.createUnmarshaller();
					
					latest = null;
					String xmlS = xmlObjectsFound.remove(xmlObjectsFound.size()-1);
					if (xmlS != "") {
						StringReader tempStringReader = new StringReader(xmlS);
						latest = (Response) unmarshaller.unmarshal(tempStringReader);
						tempStringReader.close();
						System.out.println("ResponseReceiver got: ");

						if (latest != null) {
							System.out.println(latest.getType().toString());

							synchronized(lock){
								deposit.add(new Response(latest.getData(), latest.getType(), true));
								latest = null;
							}

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	void close()
	{
		open = false;
	}
}
