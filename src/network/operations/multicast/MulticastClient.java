/**Multicasts some data once to a fixed group
 * Usage:
 *  1. MulticastClient() to initialize
 *  2. sendData(String data) to send string data
 */
package network.operations.multicast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import cloudy.Configuration;

public class MulticastClient{
	private DatagramSocket clientSocket=null;
	public static void main(String args[]) throws IOException{
		// Tester
		MulticastClient testClient=new MulticastClient();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		for(int i=0;i<5;i++){
			System.out.print(i+": ");
			testClient.sendData(br.readLine());
			System.out.println("data sent");
		}
		testClient.close();
	}
	public MulticastClient(){
		try{
			clientSocket=new DatagramSocket();
		}catch(IOException e){
			System.out.println("Could not establish socket.");
			System.out.println(e.toString());
		}
	}
	public void sendData(String data){
		byte[] buffer=data.getBytes();
		try{
			DatagramPacket datagramPacket=new DatagramPacket(
					buffer,buffer.length,InetAddress.getByName(
							Configuration.multicastGroupAddress),Configuration.multicastPort);
			clientSocket.send(datagramPacket);
		}catch(UnknownHostException e){
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
	}
	public void close(){
			clientSocket.close();
	}
}
