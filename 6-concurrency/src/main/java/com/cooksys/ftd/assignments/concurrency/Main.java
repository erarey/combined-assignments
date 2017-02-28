package com.cooksys.ftd.assignments.concurrency;

import java.nio.file.Paths;
//import java.util.ArrayList;
import java.util.HashSet;

import com.cooksys.ftd.assignments.concurrency.model.config.Config;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Main {

    /**
     * First, load a {@link com.cooksys.ftd.assignments.concurrency.model.config.Config} object from
     * the <project-root>/config/config.xml file.
     *
     * If the embedded {@link com.cooksys.ftd.assignments.concurrency.model.config.ServerConfig} object
     * is not disabled, create a {@link Server} object with the server config and spin off a thread to run it.
     *
     * If the embedded {@link com.cooksys.ftd.assignments.concurrency.model.config.ClientConfig} object
     * is not disabled, create a {@link Client} object with the client config ans spin off a thread to run it.
     */
    public static void main(String[] args) {
       Config config = Config.load(Paths.get(Config.PATH_TO_CONFIG));
       
       Server server = null;
       
       Client client = null;
       
       //LECTURE Q NOTE: 2 ports are provided in case the server is disabled 
       // so that the client can still connect to someone else
       
       if (!config.getServer().isDisabled())
       {
    	   server = new Server(config.getServer());
    	   new Thread(server).start();
    	   
       }
       
       if (!config.getClient().isDisabled())
       {
    	   client = new Client(config.getClient());
    	   new Thread(client).start();
       }
       
    }
}
