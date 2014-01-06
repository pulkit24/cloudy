/**Beacons of Gondor! The algorithm tracks the list of nodes available on the cloud network.
 * Operations:
 * 1. Runs a listening server of multicast join requests
 * 2. Receives incoming Beacon packets from CloudTracker, and passes them on in the Beacon list 
 * Beacon packets contain a list of active nodes on the cloud.
 */
package network.operations;

import network.operations.unicast.Client;
import network.packets.BeaconListPacket;
import network.packets.JoinRequestPacket;
import network.packets.Node;

public class Beacon{
	/* Steps (timeline):
	 * 1. If just joined, the beacon list will be just yourself in the list. If you receive any larger beacon
	 * 		list thence, replace your list with that one. (Helps avoid join request timeouts)
	 * 2. New joiners in the network? Simply add 'em at the end. The last guy in the list is responsible for beaconing them
	 * 3. Always compare received list with your own copy (redundant if a single list is being exchanged at a time):
	 * 		3.1 If the new list has added people at the end, replace your copy with this one!
	 * 		3.2 If you had deleted some nodes immediately ahead of you and they show up again, remove them from
	 * 			the new list and repeat 3 (after all, you know better if the next guy is up or down).
	 * 		3.3 Save a copy of the new list.
	 * 3.RESTATED Essentially, simply copy the new list up till your name in it, then skip till you reach your immediate neighbour 
	 * 			according to your own list, then copy the rest of the new list 
	 * 4. If you couldn't send your list forward to the next guy, they're probably down. 
	 * 		Remove them from the list and repeat 4.
	 */
	private BeaconListPacket BeaconList=null;
	public Beacon(){
		/* Just started – simply create a list containing your own name */
		BeaconList=new BeaconListPacket();
	}
	public BeaconListPacket getCurrentBeaconList(){
		return BeaconList;
	}
	public void handleIncomingBeaconList(BeaconListPacket incomingList){
		/* Follow step 3 – automatically handles step 1 */
		System.out.println("Beacon rcvd: <<<"+incomingList.toString());
		if(incomingList.isIdenticalTo(BeaconList)) return; // Do nothing
		else BeaconList.updateWith(incomingList);
		System.out.println("Beacon updt: <<<"+BeaconList.toString());
		transmitBeacon(); // TODO make transmit independent & periodic rather than linear
	}
	public void handleJoinRequest(JoinRequestPacket joinRequest){
		/* Add the new node at the end of the list. 
		 * If this was the last node of the list, it has the responsibility of beaconing the new joiner.
		 * Also, ensure that it doesn't already exist in the list, otherwise delete the original one.
		 * This case can happen when a node dies and comes back up quick, sending a new join request.
		 * In which case, your transmitBeacon() may or may not be already trying to beacon it
		 * If not, only then call transmitBeacon() – this is the case when no beacon list is currently in 
		 * circulation, i.e., the cloud is empty. It is our onus to initiate the beaconing.
		 */
		Node newNode=joinRequest.getRequester();
//		boolean initiateBeaconingAlgorithm=BeaconList.isCloudEmpty();
		if(BeaconList.isNew(newNode))
			BeaconList.removeNode(newNode);
		BeaconList.appendNewJoinee(newNode);
		if(newNode.equals(BeaconList.getMyNextNeighbour()))
			transmitBeacon();
	}
	private void transmitBeacon(){
		/* Step 4 */
		System.out.println("_____in transmit");
		if(BeaconList.isCloudEmpty()) return; // Do not waste time sending beacons to yourself 
		try{
			wait(2000);
		}catch(InterruptedException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
		try{
			Client client=new Client(BeaconList.getMyNextNeighbour().address);
			client.sendObject(BeaconList);
			client.close();
			System.out.println("Beacon sent: >>>"+BeaconList.toString());
		}catch(Exception e){
			// TODO specific exception
			System.err.println("Dead node: "+e.toString());
			/* The neighbour's dead: remove it and try transmitting to the next node */
			BeaconList.removeNode(BeaconList.getMyNextNeighbour());
			transmitBeacon();
		}
		System.out.println("_____out transmit");
	}
}
