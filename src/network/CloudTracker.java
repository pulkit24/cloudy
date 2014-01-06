package network;

import network.operations.Beacon;
import network.operations.multicast.MulticastClient;
import network.operations.multicast.MulticastServer;
import network.operations.unicast.Server;
import network.packets.BeaconListPacket;
import network.packets.JoinRequestPacket;
import network.packets.Packet;
import network.packets.PacketTypes;

public class CloudTracker implements Runnable{
	private Beacon beaconAlgorithm=new Beacon();
	/* Daemon/thread types */
	private static String joinRequesterThread="JoinRequester";
	private static String cloudTrackerThread="CloudTracker";
	private static String joinRequestsTrackerThread="JoinRequestsTracker";
	public CloudTracker(){
		Thread cloudTracker=new Thread(this);
		Thread joinRequester=new Thread(this);
		Thread joinRequestsTracker=new Thread(this);
//		cloudTracker.setDaemon(true);
//		joinRequestsTracker.setDaemon(true);
		cloudTracker.setName(CloudTracker.cloudTrackerThread);
		joinRequester.setName(CloudTracker.joinRequesterThread);
		joinRequestsTracker.setName(CloudTracker.joinRequestsTrackerThread);
		cloudTracker.start();
		joinRequestsTracker.start();
		joinRequester.start();
	}
	public BeaconListPacket getCurrentBeaconList(){
		BeaconListPacket currentBeaconList=null;
		synchronized(beaconAlgorithm){
			currentBeaconList=beaconAlgorithm.getCurrentBeaconList();
		}
		return currentBeaconList; 
	}
	public void run(){
		if(Thread.currentThread().getName().equals(CloudTracker.joinRequesterThread))
			sendJoinRequest();
		else if(Thread.currentThread().getName().equals(CloudTracker.cloudTrackerThread))
			track();
		else if(Thread.currentThread().getName().equals(CloudTracker.joinRequestsTrackerThread))
			trackJoinRequests();
	}
	private void sendJoinRequest(){
		/* Always called at every fresh start
		 * Sends JoinRequestPacket packet
		 */
		JoinRequestPacket joinRequest=new JoinRequestPacket();
		MulticastClient multicastClient=new MulticastClient();
		multicastClient.sendData(joinRequest.toString());
		System.out.println("Sent join request");
	}
	private void trackJoinRequests(){
		/* Runs a multicast group server to listen and respond to incoming join requests */
		MulticastServer multicastServer=new MulticastServer();
		String receivedRequest="";
		while(true){
			receivedRequest=new String(multicastServer.listenForData());
			JoinRequestPacket joinRequest=new JoinRequestPacket(receivedRequest);
			if(joinRequest.isRelevantRequest()){
				synchronized(beaconAlgorithm){
					System.out.println("Received a join request from: "+joinRequest.getRequester().address);
					beaconAlgorithm.handleJoinRequest(joinRequest);
				}
			}
		}
	}
	private void track(){
		/* Continuously listens for incoming requests, 
		 * and runs the appropriate algorithms
		 */
		Server server=null;
		Packet receivedRequest=null;
		int receivedRequestType=0;
		server=new Server(Server.objectType);
		while(true){
			receivedRequest=(Packet)server.listenForObject();
			if(receivedRequest==null) continue;
			receivedRequestType=receivedRequest.getPacketType();
			if(receivedRequestType==PacketTypes.beaconList){
				synchronized(beaconAlgorithm){
					beaconAlgorithm.handleIncomingBeaconList((BeaconListPacket)receivedRequest);
				}
			}
		}
	}
}
