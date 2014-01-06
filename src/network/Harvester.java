package network;

import java.io.File;
import network.packets.BeaconListPacket;

public class Harvester{
	private CloudTracker cloudTracker=null;
	public Harvester(CloudTracker cloudTracker){
		this.cloudTracker=cloudTracker;
//		Thread cloudTrackerMainThread=new Thread(cloudTracker);
//		Thread[] cloudTrackerSubThreads=new Thread[cloudTrackerMainThread.getThreadGroup().activeCount()];
//		cloudTrackerMainThread.getThreadGroup().enumerate(cloudTrackerSubThreads);
//		try{
//			wait(4000);
//		}catch(InterruptedException e){
//			// TODO Auto-generated catch block
//			System.err.println(e.toString());
//		}
//		System.out.println("CT"+cloudTrackerSubThreads[0]);
//		System.out.println("CT"+cloudTrackerSubThreads[1]);
//		System.out.println("CT"+cloudTrackerSubThreads[2]);
//		System.out.println("CT"+cloudTrackerSubThreads[3]);
//		System.out.println("CT"+cloudTrackerSubThreads[4]);
	}
	public void distributeFile(File file){
		BeaconListPacket currentBeaconList=cloudTracker.getCurrentBeaconList();
		System.out.println("harvy:"+currentBeaconList.toString());
	}
	public void retrieveFile(File file){
	}
}
