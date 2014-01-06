/**Listens on a multicast group & port for incoming data
 * Usage:
 *  1. MulticastServer() to initialize
 *  2. listenForData() returns string data received
 */
package network.operations.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import cloudy.Configuration;

public class MulticastServer{
	private MulticastSocket serverSocket=null;
	private InetAddress multicastGroup=null;
	public static void main(String[] args){
		// Tester
		MulticastServer testServer=new MulticastServer();
		String msg=testServer.listenForData();
		while(!msg.equals("bye")){
			System.out.println("Received data: "+msg);
			msg=testServer.listenForData();
		}
		testServer.close();
	}
	public MulticastServer(){
		try{
			serverSocket = new MulticastSocket(Configuration.multicastPort);
			multicastGroup = InetAddress.getByName(Configuration.multicastGroupAddress);
			serverSocket.joinGroup(multicastGroup);
		}catch(SocketTimeoutException e){
			System.out.println("Socket timed out");
		}catch(IOException e){
			System.out.println("Could not establish socket.");
			System.out.println(e.toString());
		}
	}
	public String listenForData(){
		try{
			byte[] buffer=new byte[Configuration.defaultSizeOfMulticastMessage];
			DatagramPacket datagramPacket=new DatagramPacket(buffer, buffer.length);
			serverSocket.receive(datagramPacket);
			String data=new String(datagramPacket.getData(),0,datagramPacket.getLength());
			return data;
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
		return null;
	}
	public void close(){
		try{
			serverSocket.leaveGroup(multicastGroup);
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println("Could not leave group: "+e.toString());
		}
		serverSocket.close();
	}
}