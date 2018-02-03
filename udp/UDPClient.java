/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;
	private static double	execution = 0;
	public static void main(String[] args) throws Exception{
		InetAddress	serverAddr = null;
		int		recvPort;
		int 		countTo;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		try {								// TO-DO: Construct UDP client class and try to send messages

			UDPClient client = new UDPClient();
			client.testLoop(serverAddr, recvPort, countTo);
			System.out.println("Time = " + execution);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}								
	}


	public UDPClient() {

		try{
			sendSoc = new DatagramSocket();				// TO-DO: Initialise the UDP socket for sending data

		}

		catch (SocketException e){

			e.printStackTrace();

		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {

		for(int tries=0; tries < countTo; tries++){
			String message = new String( (Integer.toString(countTo)) + ";" + (Integer.toString(tries)) );
			send(message,serverAddr,recvPort);								// TO-DO: Send the messages to the server
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {						// TO-DO: build the datagram packet and send it to the server
		int	payloadSize = payload.length();
		byte[]	pktData =  new byte[128];
			pktData =  payload.getBytes();
		
		try{
			long startTime = System.nanoTime();   //Start timer
			DatagramPacket	pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
			sendSoc.send(pkt);
			long endTime = System.nanoTime();    //Stop timer
			execution += ((endTime - startTime) / 1000000.0);//Append to a variable
		}

		catch (Exception e) {

			e.printStackTrace();
		}
		

		
	}
}
