package ass1;

public class VariableNode extends Node{
	
	private String value;
	
	//value accessor
	public String getValue() {
		return value;
	}
		
	/**
	 * constructor
	 * @param val
	 */
	public VariableNode(String val){
		this.value = val;		
	}
	
	@Override
    public String toString() {	
		return "varriable(" + value +") ";
		//return  value +"";
	}
}

