package com.cooksys.ftd.assignments.concurrency;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientConfig;
import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.config.SpawnStrategy;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Client implements Runnable {

	ArrayList<ClientInstance> clientInstances = new ArrayList<>();

	boolean open = true;

	SpawnStrategy strat = SpawnStrategy.PARALLEL;

	public Client(ClientConfig config) {
		System.out.println("made Client manager");
		try {
			Thread.sleep(1500);

			if (strat == SpawnStrategy.NONE) {
				close();
			}

			else if (strat == SpawnStrategy.PARALLEL) {
				System.out.println("trying parallel");
				for (ClientInstanceConfig c : config.getInstances()) {

					ClientInstance ci = new ClientInstance(c, new Socket(config.getHost(), config.getPort()));
					System.out.println("client socket connected");
					clientInstances.add(ci);

					new Thread(ci).start();
					System.out.println("clientInstance thread starting");
				}
			} else if (strat == SpawnStrategy.SEQUENTIAL) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		do {
			ListIterator<ClientInstance> iter = clientInstances.listIterator();

			while (iter.hasNext()) {
				ClientInstance ci = iter.next();
				//if (ci.requests.isEmpty()) {
					//System.out
						//	.println("trying to close a ClientInstance..." + clientInstances.size() + " will be left");
					//try {
						//ci.getSocket().close();
				//	} catch (IOException e) {
					//	e.printStackTrace();
					//}
					//iter.remove();
				//}
			}

		} while (!clientInstances.isEmpty());
	}

	@Override
	public void run() {
		while (open) {
			if (strat == SpawnStrategy.PARALLEL) {

				// if all threads are done, close()
			} else if (strat == SpawnStrategy.SEQUENTIAL) {
				// iterate clients, when head client is done, go next, when all
				// done close()
			}
		}
	}

	void close() {
		open = false;
	}
}
