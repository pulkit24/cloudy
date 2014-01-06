/** A collection of command types supported
 */
package file.operations.commands;

public class Commands{
	/* Standard command types */
	public static final int create=1;
	public static final int open=2;
	public static final int update=3;
	public static final int delete=4;
	/* Corresponding command texts */
	private static final String createText="create";
	private static final String openText="open";
	private static final String updateText="update";
	private static final String deleteText="delete";
	/* Friendly function to determine your command type */
	public static int getCommandType(String commandTypeText){
		if(commandTypeText!=null){
			if(commandTypeText.equals(createText)) return create;
			else if(commandTypeText.equals(openText)) return open;
			else if(commandTypeText.equals(updateText)) return update;
			else if(commandTypeText.equals(deleteText)) return delete;
			else return 0;
		} else return 0;
	}
}
