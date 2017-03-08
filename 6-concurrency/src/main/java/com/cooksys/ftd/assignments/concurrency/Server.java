package com.cooksys.ftd.assignments.concurrency;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.HashSet;

import com.cooksys.ftd.assignments.concurrency.model.config.ServerConfig;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Server implements Runnable {
	
	boolean open = true;
	
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
    		server.setSoTimeout(80000);
    		
    		while (open == true)
        	{
    			System.out.println("searching...");
        		ClientHandler ch = new ClientHandler(server.accept(), server);
        		//ch.setSocket(server.accept());
        		new Thread(ch).start();
        		//Thread.sleep(9000);
        	}
    	}
    	catch (SocketTimeoutException e)
    	{
    		close();
    		System.out.println("Server received no new contact for a while and is now closing down.");
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
    
    void close()
    {
    	open = false;
    	
    }
}
