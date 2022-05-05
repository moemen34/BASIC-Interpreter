package ass1;

public class AssignmentNode extends StatementNode {
	
	private VariableNode variable;
	//private Node variable;
	private Node valueNode;
	
	//value accessor
	public Node getValueNode() {
		return valueNode;
	}
	
	/*public Node getVariableNode() {
		return valueNode;
	}*/
	
	public VariableNode getVariableNode() {
		return variable;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */                       //varN // VN
	public AssignmentNode(VariableNode variableNode, Node valueNode){
		//super(VNode);
		this.variable = variableNode;		
		this.valueNode = valueNode;		
	}
	

	
	@Override
    public String toString() {	
		//return "Assignment: " + variable + " = " + valueNode +"";
		return "Assignment: {" + variable + "= " + valueNode + '}';
	}

	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitAssignmentNode(this);
	}
}

