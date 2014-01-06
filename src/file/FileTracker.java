package file;

import java.io.File;
import network.Harvester;
import file.operations.FileOperation;
import file.operations.commands.CommandReader;
import file.operations.commands.Commands;

public class FileTracker implements Runnable{
	private Harvester harvester=null;
	public FileTracker(Harvester harvester){
		this.harvester=harvester;
		Thread fileTracker=new Thread(this);
		fileTracker.start();
	}
	public void run(){
		trackFileRequests();
	}
	private void trackFileRequests(){
		CommandReader cr=new CommandReader();
		int command=cr.getUserCommand();
		if(command==Commands.create){
			/* 1. Create a local copy of the file
			 * 2. Notify Harvester to copy the file on the cloud
			 * 3. Delete the local copy
			 * 4. Make an entry into the metadata file
			 */
			File createdFile=FileOperation.Create(cr.getCommandParameters());
			harvester.distributeFile(createdFile);
			FileOperation.Delete(createdFile);
			// TODO create a db entry
		}
	}
}