/**Packet to capture a request for joining a network (convertible to String for use in multicasts
 * Usage for sending a request:
 * 1. Create a blank JoinRequestPacket(), which automatically fills up with default parameters 
 * 2. Use toString() to convert to string, which can then be sent in datagram packets
 * Usage for receiving a request:
 * 1. Create a JoinRequestPacket(String receivedData) passing the data received
 * 2. Use the isRelevantRequest() to check if the requested cloud id is the same as ours
 * 3. Use getRequesterAddress() and getAvailableSpace() to get the respective information
 */
package network.packets;

import java.util.StringTokenizer;
import cloudy.Configuration;

public class JoinRequestPacket extends Packet{
	private static final long serialVersionUID=-1305554455529648404L;
	private int availableSpace=Configuration.availableSpace;
	private String cloudId=Configuration.cloudId;
	public JoinRequestPacket(){
		this.packetType=PacketTypes.joinRequest;
	}
	private JoinRequestPacket(Node sender, int availableSpace, String cloudId){
		/* Useful for over-riding defaults when constructing received packets 
		 * Not available publicly
		 */
		this.packetType=PacketTypes.joinRequest;
		this.sender=sender;
		this.availableSpace=availableSpace;
		this.cloudId=cloudId;
	}
	public JoinRequestPacket(String deconstructedData){
		JoinRequestPacket reconstructedPacket=reconstructFromStringData(deconstructedData);
		this.sender=reconstructedPacket.sender;
		this.availableSpace=reconstructedPacket.availableSpace;
		this.cloudId=reconstructedPacket.cloudId;
	}
	public String toString(){
		return deconstructIntoStringData();
	}
	private String deconstructIntoStringData(){
		/* Put all fields in a string, separated by a standard delimiter */
		return sender.name+Configuration.joinRequestStringDelimiter
			+sender.address+Configuration.joinRequestStringDelimiter
			+availableSpace+Configuration.joinRequestStringDelimiter
			+cloudId;
	}
	private JoinRequestPacket reconstructFromStringData(String deconstructedPacket){
		/* Split the string and construct a packet */
		StringTokenizer st=new StringTokenizer(deconstructedPacket,Configuration.joinRequestStringDelimiter);
		Node requester=new Node(st.nextToken(),st.nextToken());
		int requesterAvailableSpace=Integer.parseInt(st.nextToken());
		// For cloud id, our standard delimiter could be present in the cloud id, which we cannot ignore
		// Plus, StringTokenizer leaves our standard delimiter at the start intact, which we do need to ignore
		String requestedCloudId=(st.nextToken("\n\f")).substring(Configuration.joinRequestStringDelimiter.length()); 
		return new JoinRequestPacket(requester,requesterAvailableSpace,requestedCloudId);
	}
	public boolean isRelevantRequest(){
		/* Test if request belongs to same cloud id and is not made by ourself 
		 * (multicast sends a copy back too!)
		 */
		if(cloudId.equals(Configuration.cloudId) && !sender.equals(Configuration.ownNode))
			return true;
		else return false;
	}
	public Node getRequester(){
		return sender;
	}
	public int getAvailableSpace(){
		return availableSpace;
	}
}
