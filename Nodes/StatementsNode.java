package ass1;

import java.util.ArrayList;

public class StatementsNode extends Node{

	private static ArrayList<StatementNode> list;
	
	
	//value accessor
	public static ArrayList<StatementNode> getList() {
		return list;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */
	public StatementsNode(ArrayList aList){
		this.list = aList;		
	}
	

	
	@Override
    public String toString() {
		
		/*for(StatementNode STNode : list) {
			return STNode +"\n";
		}
		return "";*/
		return list +"";
	}
}
