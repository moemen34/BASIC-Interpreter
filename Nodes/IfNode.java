package ass1;

public class IfNode extends StatementNode {

	//private ArrayList<Node> list;
	
	private BooleanOperationNode BooleanNode; 
	private Token label;

	//variable accessor
	public Node getBoolean() {
		return BooleanNode;
	}
	//initial value accessor
	public Token getLabel() {
		return label;
	}

	
	/**
	 * constructor
	 */
	public IfNode(BooleanOperationNode booleanOp, Token aLabel){
		this.BooleanNode = booleanOp;	
		this.label = aLabel;	

	}	
	
	@Override
    public String toString() {	
		return "if(" + BooleanNode + " THEN " + label.getTokenValue();
	}
	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitIfNode(this);
	}
	

}
