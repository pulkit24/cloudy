package network.packets;

import java.io.Serializable;

public class Node implements Serializable{
	private static final long serialVersionUID=-7431862429607506896L;
	public String name="";
	public String address="";
	public Node(Node node){
		this.name=node.name;
		this.address=node.address;
	}
	public Node(String name,String address){
		this.name=name;
		this.address=address;
	}
	private boolean isNull(){
		return this.name==null || this.address==null;
	}
	public boolean equals(Node node){
		if(!this.isNull() && !node.isNull())
			return this.name.equals(node.name) && this.address.equals(node.address);
		else return false;
	}
	public String toString(){
		return name+" ("+address+")";
	}
}
