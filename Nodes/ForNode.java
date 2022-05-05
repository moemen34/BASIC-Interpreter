package ass1;


public class ForNode extends StatementNode {

	//private ArrayList<Node> list;
	
	private VariableNode variableNode;//private IntegerNode variableValue; should it be 
	private IntegerNode initialValue;
	private IntegerNode limitValue;
	private IntegerNode incrementValue;
	//private StatementsNode statementsNode;
	private StatementNode AfterNextStatement;
	private StatementNode FirstInLoopStatement;
	
	
	public void setAfterNextStatement(StatementNode next){
        this.AfterNextStatement = next;
	}	
	
	public StatementNode getAfterNextStatement() {
		return AfterNextStatement;
	}
	
	public void setFirstInLoopStatement(StatementNode firstInLoop){
		this.FirstInLoopStatement = firstInLoop;
	}	
	
	public StatementNode getFirstInLoopStatement() {
		return FirstInLoopStatement;
	}
	
	
	//variable accessor
	public VariableNode getVariable() {
		return variableNode;
	}
	//initial value accessor
	public IntegerNode getinitialValue() {
		return initialValue;
	}
	//limit value accessor
	public IntegerNode getlimitlValue() {
		return limitValue;
	}
	//increment value accessor
	public IntegerNode getincrementValue() {
		return incrementValue;
	}
	
	/*public StatementsNode getStatementsNode() {
		return statementsNode;
	}*/

	/**
	 * constructor
	 * @param val
	 */
	public ForNode(VariableNode variable, IntegerNode inVal,IntegerNode limVal,IntegerNode incVal/*, StatementsNode STSNode*/){
		this.variableNode = variable;	
		this.initialValue = inVal;	
		this.limitValue = limVal;	
		this.incrementValue = incVal;	
		//.statementsNode = STSNode;
	}	
	
	@Override
    public String toString() {	
		return "For(" + variableNode + "= " + initialValue + ", TO " + limitValue + ", STEP " + incrementValue +")"/* +"\n"/* + /*statementsNode*/;
		//return list +"";
	}
	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitForNode(this);
	}
	
}

