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

    //Since RMIServer implements a remote interface, it's constructor must throw a RemoteException
    public RMIServer() throws RemoteException{}

	public static void main(String[] args){

		RMIServer rmis = null;

        //Initialize new security manager
		if(System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}						

		String serverURL = new String("rmi://localhost/RMIServer");

		try {
		    //Construct the server
			rmis = new RMIServer();
			
			//Bind the rerver to RMI registry
			rebindServer(serverURL, rmis);
			System.out.println("Server ready");
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}


	public void receiveMessage(MessageInfo message) throws RemoteException {
	
	    //On receipt of first message, initialise the receive buffer
		if (totalMessages == -1){
			totalMessages = message.totalMessages;
			receivedMessages = new int[totalMessages];
		}

        //Log the receipt of the message
		messagesReceived++;
		receivedMessages[message.messageNum] = 1;

        //Messages are in order (TCP/IP). If the last message is received, all the messages have been received
		if(message.messageNum == totalMessages - 1){
			msg_log();
		}			
	}

	public void msg_log() {
		int lost = totalMessages - messagesReceived;

        //Print any lost messages
		if(lost > 0){
			System.out.println("The missing message numbers are: ");

			for (int i=0; i < receivedMessages.length; ++i) {	
				if(receivedMessages[i] == 0) {
					System.out.print(i + " ");
				}

			}
			System.out.println();
			System.out.println(messagesReceived + "/" + totalMessages + " messages have been received!");
		}
		else{
			System.out.println();
			System.out.println("All " + totalMessages + "/" + totalMessages + " messages have been received!");
		}
	}
		


	protected static void rebindServer(String serverURL, RMIServer server) {

		try {
		    //Construct a registry on the localhost the listens to the specific port
			LocateRegistry.createRegistry(1099);
			
			//Rebing the serverURL to the remote object
			Naming.rebind(serverURL, server);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
