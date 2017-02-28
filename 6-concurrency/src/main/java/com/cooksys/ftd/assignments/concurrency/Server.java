package com.cooksys.ftd.assignments.concurrency;

import java.net.ServerSocket;
import java.util.HashSet;

import com.cooksys.ftd.assignments.concurrency.model.config.ServerConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Server implements Runnable {
	
	HashSet<ClientHandler> clientHandlers = new HashSet<>();
	
	ServerConfig config = null;
	
    public Server(ServerConfig config) {
        this.config = config;
    }

    @Override
    public void run() {
    	try {
    		
    		ServerSocket server = new ServerSocket(config.getPort());
    		
    		while (true)
        	{
        		ClientHandler ch = new ClientHandler();
        		ch.setSocket(server.accept());
        		new Thread(ch).start();
        		
        	}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
