/** Reads user-entered commands (replacement of a GUI)
 * Operations:
 * 1. Creating a new file: create filename filecontent
 * 2. Opening a file: open filename
 * 3. Updating a file: update filename filecontent
 * 4. Deleting a file: delete filename
 * Usage:
 * 1. Call getUserCommand() to obtain the command type
 * 2. Call getCommandParameters() to get the array of related string parameters
 */

package file.operations.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CommandReader{
	private String userInput="";
	private int commandType=0;
	private String[] commandParameters;
	public int getUserCommand(){
		getUserInput();
		setCommandDetails();
		return commandType;
	}
	public String[] getCommandParameters(){
		return commandParameters;
	}
	private void getUserInput(){
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		try{
			userInput=br.readLine();
		}catch(IOException e){
			// TODO can't proceed with blank or faulty input
			System.err.println(e.toString());
		}
	}
	private void setCommandDetails(){
		StringTokenizer st=new StringTokenizer(userInput);
		// TODO add check for the no. of tokens in the command (shud be atleast 1)
		String commandTypeText=st.nextToken();
		commandType=Commands.getCommandType(commandTypeText);
		switch(commandType){
			case Commands.create:
				setCommandParameters(st.nextToken(),st.nextToken());
				break;
			case Commands.open:
				setCommandParameters(st.nextToken());
				break;
			case Commands.update:
				setCommandParameters(st.nextToken(),st.nextToken());
				break;
			case Commands.delete:
				setCommandParameters(st.nextToken());
		}
	}
	private void setCommandParameters(String parameter){
		this.commandParameters=new String[1];
		this.commandParameters[0]=new String(parameter);
	}
	private void setCommandParameters(String parameter1, String parameter2){
		this.commandParameters=new String[2];
		this.commandParameters[0]=new String(parameter1);
		this.commandParameters[1]=new String(parameter2);
	}
}
