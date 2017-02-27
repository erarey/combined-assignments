package com.cooksys.ftd.assignments.socket;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.RemoteConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Shared static methods and constants to be used by both the {@link Client} and {@link Server} classes.
 */
public class Utils {
	
	public static final String CONFIG_FILE_PATH = "./config/config.xml";

    /**
     * @return a {@link JAXBContext} initialized with the classes in the
     * com.cooksys.socket.assignment.model package
     */
    public static JAXBContext createJAXBContext() throws JAXBException{
    	
			JAXBContext context = JAXBContext.newInstance(Student.class, RemoteConfig.class, LocalConfig.class, Config.class);
		
    	return context;
    }

    /**
     * Reads a {@link Config} object from the given file path.
     *
     * @param configFilePath the file path to the config.xml file
     * @param jaxb the JAXBContext to use
     * @return a {@link Config} object that was read from the config.xml file
     */
    public static Config loadConfig(String configFilePath, JAXBContext jaxb) throws JAXBException {
    	if (jaxb == null) return null;
    	
    	File f = Paths.get(configFilePath).toFile();
    	
    	if (!f.isFile()) return null;
    	
    	Unmarshaller unmarshaller = jaxb.createUnmarshaller();
    	
    	Config config = (Config)unmarshaller.unmarshal(f);
    	
        return config;
    }
}
