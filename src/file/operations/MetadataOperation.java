package file.operations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import cloudy.Configuration;

public class MetadataOperation{
	private File metadata=null;
	public MetadataOperation(){
		metadata=new File(Configuration.defaultFolder+"/"+"metadata.txt");
	}
	public void append(String fileName, String[] mirrorList){
		try{
			FileWriter fw=new FileWriter(metadata);
			int fileHash=fileName.hashCode();
			String metadataRecord=createMetadataRecord(fileHash,mirrorList);
			fw.append(metadataRecord);
			fw.close();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
	}
	private static String createMetadataRecord(int fileHash, String[] mirrorList){
		return fileHash+" "+Arrays.toString(mirrorList);
	}
}
