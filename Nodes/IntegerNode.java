package ass1;


public class IntegerNode extends Node {
	
	private int value;
	
	//value accessor
	public int getValue() {
		return value;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */
	public IntegerNode(int val){
		this.value = val;		
	}
	

	
	@Override
    public String toString() {	
		return value +"";
	}
}
