/** Send data to a listening server
 * Usage:
 *  1. Initialize using Client(String destinationAddress)
 *  2. Send data using sendData(String data) or sendPacket(Packet packet)
 */
package network.operations.unicast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import network.packets.FileRetrievalPacket;
import cloudy.Configuration;

public class Client{
	private Socket clientSocket=null;
	private BufferedWriter outStream=null;
	public static void main(String args[]) throws IOException{
		/* Tester */
		Client t=new Client("localhost");
		FileRetrievalPacket f=new FileRetrievalPacket("pk.txt");
		t.sendObject(f);
		t.close();
		
//		Client testClient=new Client("localhost");
//		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
//		for(int i=0;i<50;i++){
//			System.out.print(i+": ");
//			testClient.sendData(br.readLine());
//			System.out.println("data sent");
//		}
//		testClient.close();
	}
	public Client(String destinationAddress){
		/* Bind a socket to a destination using its default communications communicationsPort */
		try{
			clientSocket=new Socket(destinationAddress,Configuration.communicationsPort);
			outStream=new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		}catch(UnknownHostException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}catch(IOException e){
			System.err.println("Could not establish socket.");
			System.err.println(e.toString());
		}
	}
	public void sendData(String data){
		/* Send string data */
		try{
			outStream.write(data);
			outStream.newLine(); // Ensures that the receiving server's readLine() doesn't block unnecessarily
			outStream.flush();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
	}
	public void sendObject(Object object){
		/* Send a serializable object */
		try{
			ObjectOutputStream objectOutStream=new ObjectOutputStream(clientSocket.getOutputStream());
			objectOutStream.writeObject(object);
			objectOutStream.flush();
			objectOutStream.close();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
	}
	public void close(){
		try{
			clientSocket.close();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
	}
}
