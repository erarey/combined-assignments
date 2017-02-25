package com.cooksys.ftd.assignments.file;

import com.cooksys.ftd.assignments.file.model.Contact;
import com.cooksys.ftd.assignments.file.model.Instructor;
import com.cooksys.ftd.assignments.file.model.Session;
import com.cooksys.ftd.assignments.file.model.Student;

import ch.qos.logback.classic.net.SyslogAppender;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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

	private static boolean directoryIsOk(File directory) {
		if (directory.toString().isEmpty() || directory == null)
			return false;
		if (!directory.isDirectory())
			return false;

		return true;
	}

	private static boolean stringIsFormattedDate(String s) {
		//
		char[] chars = s.toCharArray();

		if (chars.length == 10 
				&& (chars[0] == '0' || chars[0] == '1') 
				&& (Character.isDigit(chars[1]))
				&& (chars[2] == '-')
				&& (chars[3] > 47 && chars[3] < 52)
				//&& (chars[3] == '0' || chars[3] == '1' || chars[3] == '2' || chars[3] == '3') 
				&& (Character.isDigit(chars[4]))
				&& (chars[5] == '-') 
				&& (Character.isDigit(chars[6])) && (Character.isDigit(chars[7]))
				&& (Character.isDigit(chars[8])) && (Character.isDigit(chars[9]))) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Creates a {@link Student} object using the given studentContactFile. The
	 * studentContactFile should be an XML file containing the marshaled form of
	 * a {@link Contact} object.
	 *
	 * @param studentContactFile
	 *            the XML file to use
	 * @param jaxb
	 *            the JAXB context to use
	 * @return a {@link Student} object built using the {@link Contact} data in
	 *         the given file
	 */
	public static Student readStudent(File studentContactFile, JAXBContext jaxb) {
		if (studentContactFile == null)
			return null;
		if (jaxb == null)
			return null;

		Contact contactdata = new Contact();

		Unmarshaller unmarshaller;

		try {
			unmarshaller = jaxb.createUnmarshaller();
			contactdata = (Contact) unmarshaller.unmarshal(studentContactFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		//System.out.println(contactdata.getFirstName() + " " + contactdata.getLastName());

		return new Student(contactdata);

	}

	/**
	 * Creates a list of {@link Student} objects using the given directory of
	 * student contact files.
	 *
	 * @param studentDirectory
	 *            the directory of student contact files to use
	 * @param jaxb
	 *            the JAXB context to use
	 * @return a list of {@link Student} objects built using the contact files
	 *         in the given directory
	 */
	public static List<Student> readStudents(File studentDirectory, JAXBContext jaxb) {

		ArrayList<Student> students_found = new ArrayList<>();

		if (!directoryIsOk(studentDirectory))
			return null;

		if (jaxb == null)
			return null;

		ArrayList<File> file_list = new ArrayList<>(Arrays.asList(studentDirectory.listFiles()));

		int files_read = 0;

		for (File f : file_list) {
			if (!f.exists() || !f.canRead())
				continue;

			students_found.add(readStudent(f, jaxb));

			files_read++;

			//System.out.println("Files read: " + files_read + " of " + file_list.size());

		}

		return students_found;

	}

	/**
	 * Creates an {@link Instructor} object using the given
	 * instructorContactFile. The instructorContactFile should be an XML file
	 * containing the marshaled form of a {@link Contact} object.
	 *
	 * @param instructorContactFile
	 *            the XML file to use
	 * @param jaxb
	 *            the JAXB context to use
	 * @return an {@link Instructor} object built using the {@link Contact} data
	 *         in the given file
	 * @throws JAXBException
	 */
	public static Instructor readInstructor(File instructorContactFile, JAXBContext jaxb) throws JAXBException {
		if (instructorContactFile == null)
			return null;
		if (jaxb == null)
			return null;

		Unmarshaller unmarshaller = jaxb.createUnmarshaller();

		Contact contactdata = (Contact) unmarshaller.unmarshal(instructorContactFile);

		//System.out.println("Instructor: " + contactdata.getFirstName() + " " + contactdata.getLastName());

		return new Instructor(contactdata);
	}

	/**
	 * Creates a {@link Session} object using the given rootDirectory. A
	 * {@link Session} root directory is named after the location of the
	 * {@link Session}, and contains a directory named after the start date of
	 * the {@link Session}. The start date directory in turn contains a
	 * directory named `students`, which contains contact files for the students
	 * in the session. The start date directory also contains an instructor
	 * contact file named `instructor.xml`.
	 *
	 * @param rootDirectory
	 *            the root directory of the session data, named after the
	 *            session location
	 * @param jaxb
	 *            the JAXB context to use
	 * @return a {@link Session} object built from the data in the given
	 *         directory
	 */

	public static Session readSession(File rootDirectory, JAXBContext jaxb) {

		if (!directoryIsOk(rootDirectory))
			return null;
		if (jaxb == null)
			return null;

		Session session = new Session();

		try {
			// Unmarshaller unmarshaller = jaxb.createUnmarshaller();

			ArrayList<File> file_list = new ArrayList<>(Arrays.asList(rootDirectory.listFiles()));

			int files_read = 0;

			for (File f : file_list) {
				if (!f.exists() || !f.canRead())
					continue;

				if (directoryIsOk(f)) {
					session.setLocation(f.getName());

					ArrayList<File> file_list_1_in = new ArrayList<>(Arrays.asList(f.listFiles()));

					for (File q : file_list_1_in) {
						// if q meets the qualifications to be a date
						if (q.isDirectory() && stringIsFormattedDate(q.getName())) {
							session.setStartDate(q.getName());

							ArrayList<File> file_list_2_in = new ArrayList<>(Arrays.asList(q.listFiles()));

							for (File z : file_list_2_in) {
								if (z.isFile()) {
									session.setInstructor(readInstructor(z, jaxb));
								} else {
									// if (session.getStudents().isEmpty())
									// {
									session.setStudents(readStudents(z, jaxb));
									// }
									// else
									// {
									// List<Student> temp =
									// session.getStudents();
									// temp.addAll(readStudents(z,jaxb));
									// session.setStudents(temp);
									// }
								}
							}
						}
					}
				}

				//System.out.println("Files read: " + files_read + " of " + file_list.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return session;
	}

	/**
	 * Writes a given session to a given XML file
	 *
	 * @param session
	 *            the session to write to the given file
	 * @param sessionFile
	 *            the file to which the session is to be written
	 * @param jaxb
	 *            the JAXB context to use
	 */
	public static void writeSession(Session session, File sessionFile, JAXBContext jaxb) {
		try {
			Marshaller marshaller = jaxb.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//marshaller.marshal(session, System.out);

			marshaller.marshal(session, sessionFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main Method Execution Steps: 1. Configure JAXB for the classes in the
	 * com.cooksys.serialization.assignment.model package 2. Read a session
	 * object from the <project-root>/input/memphis/ directory using the methods
	 * defined above 3. Write the session object to the
	 * <project-root>/output/session.xml file.
	 *
	 * JAXB Annotations and Configuration: You will have to add JAXB annotations
	 * to the classes in the com.cooksys.serialization.assignment.model package
	 *
	 * Check the XML files in the <project-root>/input/ directory to determine
	 * how to configure the {@link Contact} JAXB annotations
	 *
	 * The {@link Session} object should marshal to look like the following: The
	 * {@link Session} object should marshal to look like the following: 90
	 * <session location="..." start-date="..."> 91 <instructor> 92
	 * <contact>...</contact> 93 </instructor> 94 <students> 95 ... 96 <student>
	 * 97 <contact>...</contact> 98 </student> 99 ... 100 </students> 101
	 * </session> 102
	 */
	public static void main(String[] args) {

		try {
			JAXBContext context = JAXBContext.newInstance(Student.class, Contact.class, Instructor.class,
					Session.class);

			Session s = readSession(new File("./input"), context);

			writeSession(s, new File("./output/session.xml"), context);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
