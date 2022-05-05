package ass1;



public class GosubNode  extends StatementNode {

	private String label;
	
	//value accessor
	public String getValue() {
		return label;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */
	public GosubNode(String GosubLabel){

		this.label = GosubLabel;		
	}	
	
	@Override
    public String toString() {	
		return "GOSUB: (" + label +")";
	}


	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitGosubNode(this);
	}
	
}
