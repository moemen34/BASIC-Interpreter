package ass1;

public class StringNode extends Node{
	
    private String value;
	
	    //value accessor
	   public String getValue() {
		   return value;
	    }
	
	
	    /**
	     * constructor
	     * @param val
	     */
	    public StringNode(String val){
    		this.value = val;		
    	}
	
    	@Override
        public String toString() {	
	    	return value +"";
	    }

}
