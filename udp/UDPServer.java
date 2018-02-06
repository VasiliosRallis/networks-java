/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private static int totalMessages = -1;
	private static int[] receivedMessages;
	private static int messagesReceived = 0;
	private boolean close = false;
	
	public static void main(String args[]){

		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);

		}

        //Construct UDPServer
		UDPServer myServer = new UDPServer(Integer.parseInt(args[0]));

		try{
		    //Run Server
			myServer.run();
		}catch(SocketTimeoutException e){
		    //Check that we got at least one message
			if(totalMessages != -1){
				msg_log();
			}
			e.printStackTrace();
		}
	}


	private void run() throws SocketTimeoutException{

		byte[] pacData = new byte[128];
		int	pacSize = pacData.length;
		DatagramPacket pac;

		try{
		    //Loop until to close flag is set to true			
			while(!close){
			    //Construct new DatagramPacket
				pac = new DatagramPacket(pacData,pacSize);
				//Receive packet from client
				recvSoc.receive(pac);
				//Convert the data in the packet to a string
				String message = new String(pac.getData(),0,pac.getLength());
				processMessage(message);
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}					
		

	public void processMessage(String data) {

		MessageInfo message = null;

		try {
		    //Reconstruct the MessageInfo that was sent
			message = new MessageInfo(data);
		}
		catch(Exception e){
			e.printStackTrace();
		}
			
		if (totalMessages == -1){
		    //Initialize the receive buffer
			totalMessages = message.totalMessages;
			receivedMessages = new int[totalMessages];
		}
		
		//Log receipt of the message
		messagesReceived++;
		receivedMessages[message.messageNum] = 1;

        //If we received all the messages then close
        //Because messages might not arrive in order, it is not sufficient if messageNum = totalMessages
		if(messagesReceived == totalMessages){
			msg_log();
			close = true;
		}			
	}
	
	public static void msg_log(){
		int lost = totalMessages - messagesReceived;

		if(lost > 0){
			System.out.println("The missing message numbers are: ");

            //Print which messages have been lost
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
		
	public UDPServer(int rp) {
		
		try {
		    //Construct new socket
			recvSoc = new DatagramSocket(rp);
			//Set socket timeout to 30 seconds
			recvSoc.setSoTimeout(30000);
			System.out.println("UDPServer ready");
		}		
		catch(Exception msg){
			msg.printStackTrace();
		}
		// Done Initialisation
	}
}
