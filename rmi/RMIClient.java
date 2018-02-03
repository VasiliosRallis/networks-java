/*
 * Created on 01-Mar-2016
 */
package rmi;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import common.MessageInfo;

public class RMIClient {


	private static double	execution = 0;

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		if(System.getSecurityManager() == null) {

			System.setSecurityManager(new SecurityManager());	// TO-DO: Initialise Security Manager
		
		}						

		try {
		 	iRMIServer = (RMIServerI) Naming.lookup(urlServer);

		 	for(int i = 0; i < numMessages; i++) {
				//String message = new String( (Integer.toString(numMessages)) + ";" + (Integer.toString(i)) );
				MessageInfo msg = new MessageInfo(numMessages,i);
				try{
					long startTime = System.nanoTime();   //Start timer
					iRMIServer.receiveMessage(msg);
					long endTime = System.nanoTime();    //Stop timer
					execution += ((endTime - startTime) / 1000000.0);//Append to a variable
				}
				catch (RemoteException e) {
				     e.printStackTrace();
				}
			}							// TO-DO: Bind to RMIServer


										// TO-DO: Attempt to send messages the specified number of times

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Time = " + execution);

	}

	
}
