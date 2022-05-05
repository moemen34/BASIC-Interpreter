package ass1;

public abstract class StatementNode extends Node {
	
	private StatementNode next;
	
	public StatementNode getNext() {
		return next;
	}
	
	public void setNext(StatementNode next){
	        this.next = next;
	}
	
	/*private Node value;
	
	//value accessor
	public Node getValue() {
		return value;
	}
	
	
	
	/**
	 * constructor
	 * @param val
	 
	public StatementNode(Node val){
		this.value = val;		
	}
		*/
	
	//public abstract Node next();
	
	public abstract void accept(IVisitor v);
	
	@Override
    public String toString() {	
		return "";
	}

}
