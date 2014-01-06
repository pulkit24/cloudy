package network.packets;

public class FileRetrievalPacket extends Packet{
	private static final long serialVersionUID=3788919898285267112L;
	private String requestedFileName="";
	public FileRetrievalPacket(String requestedFileName){
		this.packetType=PacketTypes.fileRetrievalRequest;
		this.requestedFileName=requestedFileName;
	}
	public String getRequestedFileName(){
		return requestedFileName;
	}
}
