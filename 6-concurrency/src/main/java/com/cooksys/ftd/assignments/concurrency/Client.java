package com.cooksys.ftd.assignments.concurrency;

import java.util.ArrayList;
import java.util.HashSet;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientConfig;
import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.config.SpawnStrategy;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Client implements Runnable {

	ArrayList<ClientInstance> clientInstances = new ArrayList<>();

	boolean open = true;

	SpawnStrategy strat = SpawnStrategy.NONE;
	
	public Client(ClientConfig config) {
		
		if (strat == SpawnStrategy.NONE) {
			close();
		}
		
		else if (strat == SpawnStrategy.PARALLEL)
		{
			for (ClientInstanceConfig c : config.getInstances()) {
				ClientInstance ci = new ClientInstance(c);

				clientInstances.add(ci);

				new Thread(ci);
			}
		}
		else if (strat == SpawnStrategy.SEQUENTIAL)
		{
			
		}
	}

	@Override
	public void run() {
		while (open) {
			if (strat == SpawnStrategy.PARALLEL)
			{
				
				//if all threads are done, close()
			}
			else if (stat == SpawnStrategy.SEQUENTIAL)
			{
				//iterate clients, when head client is done, go next, when all done close()
			}
		}
	}

	void close() {
		open = false;
	}
}
