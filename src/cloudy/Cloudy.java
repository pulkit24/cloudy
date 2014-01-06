package cloudy;

import network.CloudTracker;
import network.Harvester;
import file.FileTracker;

public class Cloudy{
	public static void main(String args[]) throws Exception{
		Configuration.load();
		CloudTracker cloudTracker=new CloudTracker();
		Harvester harvester=new Harvester(cloudTracker);
		new FileTracker(harvester);
		System.out.println("ended");
	}
}
