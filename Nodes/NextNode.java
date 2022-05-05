package ass1;

public class NextNode extends StatementNode {
	
	private VariableNode variableNode;
	private StatementNode nextStatement;
	
	
	public void setNextStatement(StatementNode next){
        this.nextStatement = next;
	}	
	
	public StatementNode getNextStatement() {
		return nextStatement;
	}
	
	
	public VariableNode getVariable() {
		return variableNode;
	}
	
	public NextNode(VariableNode variable){
		this.variableNode = variable;
	}
	
	@Override
    public String toString() {	
		return "NEXT " + variableNode;
		//return list +"";
	}

	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitNextNode(this);
	}	
}