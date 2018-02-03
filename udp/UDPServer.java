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



	public static void main(String args[]) {

		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);

		}

		UDPServer myServer = new UDPServer(Integer.parseInt(args[0]));

		try{
			myServer.run();
		}

		catch (SocketTimeoutException e) {
			if(totalMessages != -1){
				msg_log();
			}
			e.printStackTrace();
		}
	}


	private void run() throws SocketTimeoutException {

		byte[]		pacData = new byte[128];
		int		pacSize = pacData.length;
		DatagramPacket 	pac;

		try {			
			while(close == false){
				pac = new DatagramPacket(pacData,pacSize);
				recvSoc.receive(pac);
				String message = new String(pac.getData(),0,pac.getLength());
				processMessage(message);
			}
		}

		catch (IOException e) {
			if(totalMessages != -1){
				msg_log();
			}
			e.printStackTrace();
		}

	}					
		

	public void processMessage(String data) {

		MessageInfo message = null;

		try {

			message = new MessageInfo(data);

		}

		catch (Exception e) {

			e.printStackTrace();
		}
			
		

		if (totalMessages == -1){					// TO-DO: Use the data to construct a new MessageInfo object
			totalMessages = message.totalMessages;
			receivedMessages = new int[totalMessages];
		}

		messagesReceived++;
		receivedMessages[message.messageNum] = 1;

		if(messagesReceived == totalMessages){
			msg_log();
			close = true;
		}			
										// TO-DO: On receipt of first message, initialise the receive buffer
			
										// TO-DO: Log receipt of the message



	}
	
     

	public static void msg_log() {
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
		

	public UDPServer(int rp) {
		
		try {
	
			recvSoc = new DatagramSocket(rp);
			recvSoc.setSoTimeout(30000);
			System.out.println("UDPServer ready");
		}		

		catch (Exception msg) {

			msg.printStackTrace();
			
		}
		// Done Initialisation
		
	}

}
