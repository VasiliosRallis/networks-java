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
	
	//Calculate the amount of time spent sending messages
	private static double	execution = 0;
	
	public static void main(String[] args) throws Exception{
		InetAddress serverAddr = null;
		int recvPort;
		int countTo;

		// Get the parameters
		if(args.length < 3){
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try{
			serverAddr = InetAddress.getByName(args[0]);
		}catch(UnknownHostException e){
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);
		
		try{
			UDPClient client = new UDPClient();
			client.testLoop(serverAddr, recvPort, countTo);
			//Print the amount of time spent sending
			System.out.println("Time = " + execution);
		}catch(Exception e){
			e.printStackTrace();
		}								
	}


	public UDPClient(){
		try{
			//Initialize UDP socket for sending data
			sendSoc = new DatagramSocket();

		}catch(SocketException e){
			e.printStackTrace();
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo){
		//Loop for sending messages to server
		for(int tries=0; tries < countTo; tries++){
			String message = new String((Integer.toString(countTo)) + ";" + (Integer.toString(tries)));
			send(message,serverAddr,recvPort);
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort){
		int payloadSize = payload.length();
		byte[] pktData =  new byte[128];
		
		pktData =  payload.getBytes();
		
		try{
			//Start timer
			long startTime = System.nanoTime();
			//Construct the DatagramPacket
			DatagramPacket	pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
			//Send the Datagram Packet to the Server
			sendSoc.send(pkt);
			//Stop timer
			long endTime = System.nanoTime();
			//Convert to milliseconds
			execution += ((endTime - startTime) / 1000000.0);
		}catch (Exception e){
			e.printStackTrace();
		}
		
    }
}
