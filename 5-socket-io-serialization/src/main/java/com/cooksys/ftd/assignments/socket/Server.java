package com.cooksys.ftd.assignments.socket;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.RemoteConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Server extends Utils {


	//public static final String STUDENT_FILE_PATH = "./config/student.xml";


    /**
     * Reads a {@link Student} object from the given file path
     *
     * @param studentFilePath the file path from which to read the student config file
     * @param jaxb the JAXB context to use during unmarshalling
     * @return a {@link Student} object unmarshalled from the given file path
     * @throws JAXBException
     */
    public static Student loadStudent(String studentFilePath, JAXBContext jaxb) throws JAXBException{
		
		
		Unmarshaller unmarshaller = jaxb.createUnmarshaller();
		
		return (Student)unmarshaller.unmarshal(Paths.get(studentFilePath).toFile());
		
    }

    /**
     * The server should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" property of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.LocalConfig} object to create a server socket that
     * listens for connections on the configured port.
     *
     * Upon receiving a connection, the server should unmarshal a {@link Student} object from a file location
     * specified by the config's "studentFilePath" property. It should then re-marshal the object to xml over the
     * socket's output stream, sending the object to the client.
     *
     * Following this transaction, the server may shut down or listen for more connections.
     */
    public static void main(String[] args) {
        //generateConfigStudentTest();
        
        try
        {
        	JAXBContext context = Utils.createJAXBContext();
        	
        	Config config = Utils.loadConfig(Utils.CONFIG_FILE_PATH, context);
        	
        	Integer port = config.getLocal().getPort();
        	
        	ServerSocket ss = new ServerSocket(port);
        	
        	ss.setSoTimeout(100000);
        	
        	Socket client = ss.accept();
        	
        	
        	InputStream in = client.getInputStream();
        	OutputStream out = client.getOutputStream();
        	
        	Unmarshaller unmarshaller = context.createUnmarshaller();
        	
        	Student student = (Student)unmarshaller.unmarshal(Paths.get(config.getStudentFilePath()).toFile());
        	
        	Marshaller marshaller = context.createMarshaller();
        	
        	//marshaller.marshal(student, out);
        	
        	while (client.isBound() && !client.isClosed())
        	{
        		System.out.println("| still connected |");
        		
        		if (client.isConnected())
        		{
        			System.out.println("sending...");
        			marshaller.marshal(student, out);
        			System.out.println("sent.");
        			break;
        		}
        		
        		Thread.sleep(10000);
        		
        	}
        	
        	client.close();
        	
        	//System.out.println(student.getFavoriteIDE());
        	
        	//DataInputStream in = new DataInputStream(client.getInputStream());
        	
        	//InputStream input = new FileInputStream();
        	//System.out.println(in);
        	//BufferedOutputStream out = new BufferedOutputStream();
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

	/**
	 * 
	 */
	private static void generateConfigStudentTest() {
		Config config = new Config();
        config.setStudentFilePath("./config/student.xml");
        LocalConfig localConfig = new LocalConfig();
        localConfig.setPort(12345);
        RemoteConfig remoteConfig = new RemoteConfig();
        remoteConfig.setPort(6789);
        remoteConfig.setHost("someString");
        config.setLocal(localConfig);
        config.setRemote(remoteConfig);
        
        Student student = new Student();
        student.setFavoriteIDE("Eclipse");
        student.setFavoriteLanguage("Java");
        student.setFavoriteParadigm("Any pair of dimes is as valuable as any other");
        student.setFirstName("Eli");
        student.setLastName("Rarey");
        
        try {
			JAXBContext context = Utils.createJAXBContext();
			
			Marshaller marshaller = context.createMarshaller();
			
			marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marshaller.marshal(student, Paths.get("./config/student.xml").toFile());
			marshaller.marshal(student, System.out);

			marshaller.marshal(config, Paths.get("./config/config.xml").toFile());
			marshaller.marshal(config, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
