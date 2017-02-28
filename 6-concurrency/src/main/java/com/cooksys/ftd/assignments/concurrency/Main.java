package com.cooksys.ftd.assignments.concurrency;

import java.nio.file.Paths;
import java.util.ArrayList;
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
       
       HashSet<Thread> serverThreads = new HashSet<>();
       HashSet<Thread> clientThreads = new HashSet<>();
       
       if (!config.getServer().isDisabled())
       {
    	   serverThreads.add(new Thread(new Server(config.getServer())));
    	   
       }
       
       if (!config.getClient().isDisabled())
       {
    	   clientThreads.add(new Thread(new Client(config.getClient())));
    	   
       }
    }
}
