package com.cooksys.ftd.assignments.concurrency;

import java.util.ArrayList;
import java.util.List;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.message.Request;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ClientInstance implements Runnable {

	List<Request> requests = null;
	ClientInstanceConfig config = null;
	
    public ClientInstance(ClientInstanceConfig config) {
        requests = config.getRequests();
        this.config = config;
    }

    @Override
    public void run() {
        while (requests.get(0) != null)
        {
        	try {
        		System.out.println("There are " + requests.size() + " left to send.");
        		
				Thread.sleep(config.getDelay());
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
}
