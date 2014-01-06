/**Listens on a communicationsPort for incoming data
 * Usage:
 *  1. Server(data type to be received) to initialize, setting data exchange type as String data or Object
 *  2. listenForData() returns string data received, listenForObject() returns object data 
 */
package network.operations.unicast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import network.packets.FileRetrievalPacket;
import network.packets.Packet;
import cloudy.Configuration;

public class Server{
	private ServerSocket serverSocket=null;
	private Socket boundSocket=null;
	private BufferedReader inStream=null;
	private ObjectInputStream objectInStream=null;
	private int type=0;
	/* Type of data to be received */
	public static int dataType=0;
	public static int objectType=1;
	public static void main(String[] args){
		/* Tester */
		Server s=new Server(Server.objectType);
		System.out.println("recving");
		while(true){
		Packet p=(Packet)s.listenForObject();
		System.out.println("recd: "+p.getPacketType());
		FileRetrievalPacket f=(FileRetrievalPacket)p;
		System.out.println("recd: "+f.getRequestedFileName());
		}
//		s.close();

//		Server testServer=new Server(Server.dataType);
//		String msg=testServer.listenForData();
//		while(msg!=null && !msg.equals("bye")){
//			System.out.println("Received data: "+msg);
//			msg=testServer.listenForData();
//		}
//		testServer.close();
	}
	public Server(int type){
		/* Binds a listening socket to the default communication communicationsPort */
		this.type=type;
		try{
			serverSocket=new ServerSocket(Configuration.communicationsPort);
		}catch(IOException e){
			System.out.println("Could not establish socket.");
			System.out.println(e.toString());
		}
	}
	public String listenForData(){
		/* Listen for string data and return any line received */
		String incomingData="";
		try{
			boundSocket=serverSocket.accept();
			if(type==dataType)
				inStream=new BufferedReader(new InputStreamReader(boundSocket.getInputStream()));
			incomingData=inStream.readLine();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
			return null;
		}
		return incomingData;
	}
	public Object listenForObject(){
		/* Listen for serialized object */
		Object incomingObject=null;
		try{
			boundSocket=serverSocket.accept();
			if(type==objectType)
				objectInStream=new ObjectInputStream(boundSocket.getInputStream());
			incomingObject=objectInStream.readObject();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}catch(ClassNotFoundException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
		return incomingObject;
	}
	public void close(){
		/* Close all sockets associated */
		try{
			inStream.close();
			boundSocket.close();
			serverSocket.close();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}catch(Exception e){
			// Blank catch to neglect closed sockets
		}
	}
}