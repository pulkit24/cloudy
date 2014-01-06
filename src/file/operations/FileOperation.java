package file.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import cloudy.Configuration;

public class FileOperation{
	public static File Create(String[] fileParameters){
		/* Steps: 
		 * 1. Some important things must be considered:
		 * 1.1 The file must not already exist, in which case ask for a new name
		 * 1.2 There should be enough space on the disk to temporarily store the file
		 * 2. After checking, save the file
		 */
		String fileName=fileParameters[0];
		String fileContent=fileParameters[1];
		File file=new File(Configuration.defaultFolder+"/"+fileName);
		if(file.exists()){ // Step 1.1
			try{
				System.out.println("A file with this name already exists.");
				System.out.print("Please choose a new name: "); // TODO gracefully mention file exists
				BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
				String newFileName=br.readLine();
				Create(new String[]{newFileName,fileContent}); // Try again
			}catch(IOException e){
				// TODO Auto-generated catch block
				System.err.println(e.toString());
			}
			return null;	// Quit this instance
		}
		if((fileContent.length()+100)>=file.getParentFile().getUsableSpace()){ // Step 1.2 (random 100!)
			System.out.println("Not enough space to save the file"); // TODO
			return null;	// Quit this instance
		}
		// Step 2
		try{
			file.createNewFile();
			FileWriter fw=new FileWriter(file);
			fw.write(fileContent);
			fw.close();
		}catch(IOException e){
			// TODO Auto-generated catch block
			System.err.println(e.toString());
		}
		return file;
	}
	public static void Delete(File file){
		file.delete();
	}
	public static void Open(){
		
	}
}
