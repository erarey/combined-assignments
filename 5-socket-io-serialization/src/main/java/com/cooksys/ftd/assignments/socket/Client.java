package com.cooksys.ftd.assignments.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.Student;

public class Client {

    /**
     * The client should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" and "host" properties of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.RemoteConfig} object to create a socket that connects to
     * a {@link Server} listening on the given host and port.
     *
     * The client should expect the server to send a {@link com.cooksys.ftd.assignments.socket.model.Student} object
     * over the socket as xml, and should unmarshal that object before printing its details to the console.
     */
    public static void main(String[] args) {
    	InputStream in = null;
    	OutputStream out = null;
    	Socket server = null;
    	
    	try {
    		
    		JAXBContext jaxb = Utils.createJAXBContext();
        
    		Config config = Utils.loadConfig(Utils.CONFIG_FILE_PATH, jaxb);
    		
    		server = new Socket(config.getRemote().getHost(), config.getRemote().getPort());
    		
    		while (!server.isConnected())
    		{
    			System.out.println("not connected...");
    			Thread.sleep(1000);
    		}
    		
    		in = server.getInputStream();
    		out = server.getOutputStream();
    		
    		Unmarshaller unmarshaller = jaxb.createUnmarshaller();
    		
    		Student student = (Student)unmarshaller.unmarshal(in);
    		
    		System.out.println("According to " + student.getFirstName() + " " + student.getLastName());
    		System.out.println("Paradigm: " + student.getFavoriteParadigm());
    		System.out.println("Language: " + student.getFavoriteLanguage());
    		System.out.println("IDE: " + student.getFavoriteIDE());
    		
    		//server.close();
    		
    		
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		try
    		{
    			in.close();
    			out.close();
    			server.close();
    			
    		}
    		catch(Exception e)
    		{
    			System.out.println("Closing input, output, or socket, failed.");
    		}
    	}
    }
}
