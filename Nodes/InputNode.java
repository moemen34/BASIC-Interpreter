package ass1;

import java.util.ArrayList;

public class InputNode extends StatementNode {

	private ArrayList<Node> list;
	
	//value accessor
	public ArrayList getValue() {
		return list;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */
	public InputNode(ArrayList aList){
		//super(value);
		this.list = aList;		
	}	
	
	@Override
    public String toString() {	
		return "Input List: {" + list +"}";
		//return list +"";
	}


	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitInputNode(this);
	}


}
