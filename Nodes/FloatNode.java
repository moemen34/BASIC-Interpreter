package ass1;

public class FloatNode extends Node{

	private float value;
	
	//value accessor
	public float getValue() {
		return value;
	}	
	
	/**
	 * constructor
	 * @param val
	 */
	public FloatNode(float val){
		this.value = val;		
	}
	
	@Override
    public String toString() {	
		//return "floatNoad: " + value;
		return  value +"";
	}
}
	
	
	
