package ass1;


public class ReturnNode  extends StatementNode {
	
	@Override
    public String toString() {	
		return "RETURN";
	}

	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitReturnNode(this);
	}	
}