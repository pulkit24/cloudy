/** Maintains a beacon list
 * Usage:
 * 1. Initialize object and use add(String nodeName, String nodeAddress) to add to the list
 * 2. Remove any time using remove(String nodeName)
 * 3. To display, use print() 
 * 4. To compare two packets, use isIdenticalTo(BeaconListPacket)
 */
package network.packets;

import java.util.Arrays;
import cloudy.Configuration;


public class BeaconListPacket extends Packet{
	private static final long serialVersionUID=-8525715483018679770L;
	private Node[] nodes=null;
	public BeaconListPacket(){
		this.packetType=PacketTypes.beaconList;
		this.nodes=new Node[1];
		nodes[0]=Configuration.ownNode;
	}
	public boolean isCloudEmpty(){
		return nodes.length==1;
	}
	public Node getMyNextNeighbour(){
		if(amILast()) return nodes[0];
		else return nodes[getMyPosition()+1];
	}
	public boolean isNew(Node node){
		if(getPosition(node)==-1) return true;
		else return false;
	}
	public String toString(){
		/* Display the list as neat text */
		return Arrays.toString(nodes);
	}
	public boolean isIdenticalTo(BeaconListPacket anotherList){
		if(Arrays.equals(this.nodes,anotherList.nodes))
			return true;
		return false;
	}
	public void updateWith(BeaconListPacket newList){
		/* Possible situations
		 * 1. Logical: ---Me---MyNextNeighbourAccordingToMyOwnList---
		 * 2. Erroneous but must be handled:
		 * 		2.1 ---MyNextNeighbour--- (no Me) | Impossible, as it wouldn't have been sent to me then
		 * 		2.2 ---Me--- (no MyNextNeighbour) | Impossible, as only I would know when my neighbour is down
		 * 		2.3 --- (None) | Impossible, see 2.1
		 * 		2.4 ---MyNextNeighbour---Me--- (mixed up) | Screw this list
		 */
		int myPositionInNewList=newList.getMyPosition();
		if(myPositionInNewList==-1) return; // Case 2.1: ignore the new list // TODO add error returns here and below
		int myNextNeighbourPositionInNewList=-1;
		if(amILast()){
			/* If I'm the last in my own list, I have no neighbour to track down */
			/* Simply discard all the entries in the new list after me */
			myNextNeighbourPositionInNewList=newList.nodes.length;
		}else{
			Node myNextNeighbourInMyOwnList=getMyNextNeighbour();
			myNextNeighbourPositionInNewList=newList.getPosition(myNextNeighbourInMyOwnList);
			if(myNextNeighbourPositionInNewList==-1) return; // Still -1? Case 2.2: ignore the new list
		}
		/* Simply update the new list, removing contents between Me and MyNextNeighbour */
		if(myPositionInNewList>=myNextNeighbourPositionInNewList) return; // Case 2.4: ignore the new list
		for(int i=myPositionInNewList+1;i<myNextNeighbourPositionInNewList;i++){
			newList.remove(newList.nodes[myPositionInNewList+1]); // static index, 
			// coz each remove automatically decrements the array length by 1
		}
		/* Save this list */
		nodes=newList.nodes;
	}
	public void appendNewJoinee(Node newNode){
		add(newNode);			
	}
	public void removeNode(Node node){
		remove(node);
	}
	private void add(Node newNode){
		/* Adds a new node at the end */
		int length=nodes.length;
		/* Construct new temp arrays and copy original content into them */
		Node[] newNodes=new Node[length+1];
		System.arraycopy(nodes,0,newNodes,0,length);
		/* Append the new node at the end */
		newNodes[length]=newNode;
		nodes=newNodes;
	}
	private void remove(Node node){
		/* Removes a node from anywhere in the list */
		/* Find the position of the node */
		int nodeIndex=0;
		for(nodeIndex=0;nodeIndex<nodes.length;nodeIndex++)
			if(nodes[nodeIndex].equals(node)) break;
		if(nodeIndex==nodes.length) return; // TODO node not found error
		/* Copy contents upto the node, and from after the node, into a new array */
		int length=nodes.length-1;
		Node[] newNodes=new Node[length];
		System.arraycopy(nodes,0,newNodes,0,nodeIndex);
		System.arraycopy(nodes,nodeIndex+1,newNodes,nodeIndex,length-nodeIndex);
		nodes=newNodes;
	}
	private int getPosition(Node node){
		for(int i=0;i<nodes.length;i++)
			if(nodes[i].equals(node))
				return i;
		return -1; // default "node not found"
	}
	private int getMyPosition(){
		return getPosition(Configuration.ownNode);
	}
	private boolean amILast(){
		return getMyPosition()==nodes.length-1;
	}
}
