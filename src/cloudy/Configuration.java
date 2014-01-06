package cloudy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import network.packets.Node;

public class Configuration{
	/* File System Parameters */
	public static String defaultFolder="D:/Pulkit/Test Site/Cloud Folder";
	public static int availableSpace=100; // in MB
	/* Fixed Networking Parameters */
	public static int communicationsPort=4000;
	public static int multicastPort=4001;
	public static String multicastGroupAddress="230.0.0.1";
	public static int defaultSizeOfMulticastMessage=1024;
	public static String joinRequestStringDelimiter="##";
	/* Customizable Network Parameters */
	public static Node ownNode=new Node("pulkit","127.0.0.1"); // loaded dynamically
	public static String cloudId="pulkit-test-cloud";
	public static void load(){
		Properties configFile=new Properties();
		try{
			configFile.load(new FileInputStream("config.properties"));

			/* File System Parameters */
			
			defaultFolder=configFile.getProperty("defaultFolder");
			(new File(defaultFolder)).mkdirs(); // Creates the default folder to ensure its availability 
			
			availableSpace=Integer.parseInt(configFile.getProperty("availableSpace"));
			
			/* Fixed Network Parameters */
			
			communicationsPort=Integer.parseInt(configFile.getProperty("communicationsPort"));
			
			multicastPort=Integer.parseInt(configFile.getProperty("multicastPort"));
			multicastGroupAddress=configFile.getProperty("multicastGroupAddress");
			defaultSizeOfMulticastMessage=Integer.parseInt(
					configFile.getProperty("defaultSizeOfMulticastMessage"));
			
			joinRequestStringDelimiter=configFile.getProperty("joinRequestStringDelimiter");
			
			/* Customizable Network Parameters */
			
			ownNode=new Node(InetAddress.getLocalHost().getHostName(),InetAddress.getLocalHost().getHostAddress());
			
			cloudId=configFile.getProperty("cloudId");
		
		}catch(FileNotFoundException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
	}
}
