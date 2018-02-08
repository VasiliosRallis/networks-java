package rmi;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import common.MessageInfo;

public class RMIClient {

    //Calculate the ammount of time spent sending messages
    //Because this is a TCP/IP connection this will also include the time 
    //for the server the receive the message and aknowledge the receipt of the message
	private static double execution = 0;

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);
        
        //Initialize the security manager
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}						

		try {
		    //Get reference ot RMIServer stub from the remote registry
		    //Note the cast to in interface since the client interacts with the interface
		 	iRMIServer = (RMIServerI) Naming.lookup(urlServer);

		 	for(int i = 0; i < numMessages; i++) {
				//String message = new String( (Integer.toString(numMessages)) + ";" + (Integer.toString(i)) );
				MessageInfo msg = new MessageInfo(numMessages,i);
				
		        //Start timer
			    long startTime = System.nanoTime();
			    //Send message
			    iRMIServer.receiveMessage(msg);
                //Stop timer
			    long endTime = System.nanoTime();
			    //Convert to milliseconds 
			    execution += ((endTime - startTime) / 1000000.0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Time = " + execution);
	}	
}
