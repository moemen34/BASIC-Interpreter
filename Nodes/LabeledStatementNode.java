package ass1;


public class LabeledStatementNode extends StatementNode {
	
	
	//private ArrayList<Node> list;
	private String label;
	private StatementNode sNode;
	
	
	
	
	//value accessor
	public Node getValue() {
		return sNode;
	}
	
	public String getLabel() {
		return label;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */
	public LabeledStatementNode(String aLable ,StatementNode aSNode){
		//super(value);
		this.sNode = aSNode;	
		this.label = aLable;
	}	
	
	@Override
    public String toString() {	
		return "Labeled Statement"+"(" +label  +")"+": {" + sNode +"}";
		//return list +"";
	}


	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitLabeledStatementNode(this);

	}
	
}
