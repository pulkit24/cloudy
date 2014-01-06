package network.packets;

import java.io.Serializable;
import cloudy.Configuration;

public class Packet implements Serializable{
	private static final long serialVersionUID=-2719803456291263351L;
	/* Mandatory packet information */
	protected int packetType=PacketTypes.generic;
	protected Node sender=Configuration.ownNode;
	public int getPacketType(){
		return packetType;
	}
}
