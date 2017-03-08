package com.cooksys.ftd.assignments.concurrency;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import com.cooksys.ftd.assignments.concurrency.model.config.ClientConfig;
import com.cooksys.ftd.assignments.concurrency.model.config.ClientInstanceConfig;
import com.cooksys.ftd.assignments.concurrency.model.config.SpawnStrategy;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Client implements Runnable {

	ArrayList<Thread> clientInstances = new ArrayList<>();

	ClientConfig config = null;

	boolean open = true;

	SpawnStrategy strat = null;

	public Client(ClientConfig config) {
		this.config = config;
		this.strat = config.getSpawnStrategy();
	}

	@Override
	public void run() {

		List<ClientInstanceConfig> clientInstanceConfigs = config.getInstances();
		System.out.println("found clientInstanceConfigs: " + clientInstanceConfigs.size());
		int maxInstances = config.getMaxInstances();
		int instanceCount = 0;

		if (strat == SpawnStrategy.NONE) {
			close();
		}

		else if (strat == SpawnStrategy.SEQUENTIAL) {
			maxInstances = 1;
		}

		while (open == true) {
			try {
				Thread.sleep(100);

				// Parallel, use maxInstances per config

				if (maxInstances != -1) {
					while (instanceCount < (maxInstances + 1)) {
						instanceCount++;
						for (int i = (instanceCount - 1); i < maxInstances && i < clientInstanceConfigs.size(); i++) {

							ClientInstance ci = new ClientInstance(clientInstanceConfigs.get(i),
									new Socket(config.getHost(), config.getPort()));

							clientInstances.add(new Thread(ci));
							clientInstances.get(clientInstances.size() - 1).start();
						}
					}
				} else {
					while (clientInstances.size() < clientInstanceConfigs.size() && instanceCount < (clientInstanceConfigs.size()+1)) {
						System.out.println(clientInstances.size() + " OF " + clientInstanceConfigs.size());
						instanceCount++;
						ClientInstance ci = new ClientInstance(clientInstanceConfigs.get(clientInstances.size()),
								new Socket(config.getHost(), config.getPort()));

						clientInstances.add(new Thread(ci));
						clientInstances.get(clientInstances.size() - 1).start();

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			ListIterator<Thread> iter = clientInstances.listIterator();

			while (iter.hasNext()) {
				Thread ci = iter.next();
				//System.out.println("THREAD " + ci.isAlive());
				if (!ci.isAlive()) {
					//System.out.println("THREAD CLOSING");
					iter.remove();
				}
			}

			if (clientInstances.isEmpty()) {
				close();
			}
			
		}
	}

	void close() {
		System.out.println("All client instances have finished, closing Client");
		open = false;
	}
}
