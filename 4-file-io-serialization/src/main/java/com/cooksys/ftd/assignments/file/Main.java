package com.cooksys.ftd.assignments.file;

import com.cooksys.ftd.assignments.file.model.Contact;
import com.cooksys.ftd.assignments.file.model.Instructor;
import com.cooksys.ftd.assignments.file.model.Session;
import com.cooksys.ftd.assignments.file.model.Student;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    /**
     * Creates a {@link Student} object using the given studentContactFile.
     * The studentContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param studentContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return a {@link Student} object built using the {@link Contact} data in the given file
     */
    public static Student readStudent(File studentContactFile, JAXBContext jaxb) throws JAXBException{
        if (studentContactFile == null) return null;
        if (jaxb == null) return null;
        
        Unmarshaller unmarshaller = jaxb.createUnmarshaller();
        	
        Contact contactdata = (Contact) unmarshaller.unmarshal(studentContactFile);
        
        System.out.println(contactdata.getFirstName() + " " + contactdata.getLastName());
        
        return new Student(contactdata);
        
    }

    /**
     * Creates a list of {@link Student} objects using the given directory of student contact files.
     *
     * @param studentDirectory the directory of student contact files to use
     * @param jaxb the JAXB context to use
     * @return a list of {@link Student} objects built using the contact files in the given directory
     */
    public static List<Student> readStudents(File studentDirectory, JAXBContext jaxb) {
        try
        {
        	if (studentDirectory.toString().isEmpty() || studentDirectory == null) return new ArrayList<>(null);
        	if (jaxb == null) return null;
        	
        	ArrayList<File> file_list = new ArrayList<>(Arrays.asList(studentDirectory.listFiles()));
        	
        
        	Unmarshaller unmarshaller = jaxb.createUnmarshaller();
        	
        	for (File f : file_list)
        	{
        		//if the file doesn't contain @XmlRootElement, skip it
        		FileReader reader = new FileReader(f);
        	    BufferedReader buffReader = new BufferedReader(reader);
        		
        	    boolean confirmed_file_has_rootelement = false;
        	    String currentLine = "not null";
        	    while (confirmed_file_has_rootelement == false && currentLine == null)
        	    {
        	    	currentLine = buffReader.readLine();
        	    	if (currentLine.contains("@XmlRootElement")) 
        	    	{
        	    		
        	    		confirmed_file_has_rootelement = true;
        	    	
        	    		System.out.println("File had a root element!");
        	    	}
        	    }
        	    if (confirmed_file_has_rootelement == false)
        	    {
        	    	System.out.println("File: no root element found.");
        	    	continue;
        	    }
        	    
        	}
        } 
        catch (JAXBException e)
        {
        	
        } 
        catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Creates an {@link Instructor} object using the given instructorContactFile.
     * The instructorContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param instructorContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return an {@link Instructor} object built using the {@link Contact} data in the given file
     */
    public static Instructor readInstructor(File instructorContactFile, JAXBContext jaxb) {
        return null; // TODO
    }

    /**
     * Creates a {@link Session} object using the given rootDirectory. A {@link Session}
     * root directory is named after the location of the {@link Session}, and contains a directory named
     * after the start date of the {@link Session}. The start date directory in turn contains a directory named
     * `students`, which contains contact files for the students in the session. The start date directory
     * also contains an instructor contact file named `instructor.xml`.
     *
     * @param rootDirectory the root directory of the session data, named after the session location
     * @param jaxb the JAXB context to use
     * @return a {@link Session} object built from the data in the given directory
     */
    public static Session readSession(File rootDirectory, JAXBContext jaxb) {
        return null; // TODO
    }

    /**
     * Writes a given session to a given XML file
     *
     * @param session the session to write to the given file
     * @param sessionFile the file to which the session is to be written
     * @param jaxb the JAXB context to use
     */
    public static void writeSession(Session session, File sessionFile, JAXBContext jaxb) {
        // TODO
    }

    /**
     * Main Method Execution Steps:
     * 1. Configure JAXB for the classes in the com.cooksys.serialization.assignment.model package
     * 2. Read a session object from the <project-root>/input/memphis/ directory using the methods defined above
     * 3. Write the session object to the <project-root>/output/session.xml file.
     *
     * JAXB Annotations and Configuration:
     * You will have to add JAXB annotations to the classes in the com.cooksys.serialization.assignment.model package
     *
     * Check the XML files in the <project-root>/input/ directory to determine how to configure the {@link Contact}
     *  JAXB annotations
     *
     * The {@link Session} object should marshal to look like the following:
     *      <session location="..." start-date="...">
     *           <instructor>
     *               <contact>...</contact>
     *           </instructor>
     *           <students>
     *               ...
     *               <student>
     *                   <contact>...</contact>
     *               </student>
     *               ...
     *           </students>
     *      </session>
     */
    public static void main(String[] args) {
        // TODO
    }
}
