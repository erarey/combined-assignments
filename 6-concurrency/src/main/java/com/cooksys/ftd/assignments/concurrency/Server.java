package com.cooksys.ftd.assignments.concurrency;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;

import com.cooksys.ftd.assignments.concurrency.model.config.ServerConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Server implements Runnable {
	
	HashSet<ClientHandler> clientHandlers = new HashSet<>();
	
	ServerConfig config = null;
	
    public Server(ServerConfig config) {
    	System.out.println("made Server (clientHandler manager)");
        this.config = config;
    }

    @Override
    public void run() {
    	
    	ServerSocket server = null;
    	
    	try {
    		
    		server = new ServerSocket(config.getPort());
    		server.setSoTimeout(100000);
    		
    		while (true)
        	{
    			System.out.println("searching...");
        		ClientHandler ch = new ClientHandler(server.accept(), server);
        		//ch.setSocket(server.accept());
        		new Thread(ch).start();
        		//Thread.sleep(9000);
        	}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}
