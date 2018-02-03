/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private int messagesReceived = 0;



	public RMIServer() throws RemoteException {
	}

	public static void main(String[] args) {

		RMIServer rmis = null;

		if(System.getSecurityManager() == null) {

			System.setSecurityManager(new SecurityManager());	// TO-DO: Initialise Security Manager
		
		}						

		String serverURL = new String("rmi://localhost/RMIServer");	// TO-DO: Instantiate the server class

		try {
			rmis = new RMIServer();
			rebindServer(serverURL, rmis);
			System.out.println("Server ready");

		}
		catch (RemoteException e) {
			e.printStackTrace();
		}								// TO-DO: Bind to RMI registry

	}


	public void receiveMessage(MessageInfo message) throws RemoteException {
	

		if (totalMessages == -1){					// TO-DO: Use the data to construct a new MessageInfo object
			totalMessages = message.totalMessages;
			receivedMessages = new int[totalMessages];
		}

		messagesReceived++;
		receivedMessages[message.messageNum] = 1;

		if(message.messageNum == totalMessages - 1){
			msg_log();
		}			
										// TO-DO: On receipt of first message, initialise the receive buffer
			
		
	}

	public void msg_log() {
		int lost = totalMessages - messagesReceived;

		if(lost > 0){						// TO-DO: Log results of the messages
			System.out.println("The missing message numbers are: ");

			for (int i=0; i < receivedMessages.length; ++i) {	
				if(receivedMessages[i] == 0) {
					System.out.print(i + " ");
				}

			}
			System.out.println();
			System.out.println(lost + "/" + totalMessages + " messages have been lost!");
		}
		else{
			System.out.println();
			System.out.println("All " + totalMessages + "/" + totalMessages + " messages have been received!");
		}
	}
		


	protected static void rebindServer(String serverURL, RMIServer server) {

		try {
			LocateRegistry.createRegistry(1099);
		} 
		catch (RemoteException e) {
			e.printStackTrace();
		}								// TO-DO:
										// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		try {										//Naming.bind(serverURL, new RMIServer());
			Naming.rebind(serverURL, server);

		} 
		
		catch (RemoteException e) {
			e.printStackTrace();
		} 

		catch (MalformedURLException e) {
			e.printStackTrace();
		}

										// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
										// TO-DO:
									// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
										// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
									// expects different things from the URL field.
	}
}
