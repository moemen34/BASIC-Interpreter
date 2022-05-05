package ass1;

import java.util.ArrayList;

public class ReadNode extends StatementNode {

	private ArrayList<VariableNode> list;
	
	//value accessor
	public ArrayList getValue() {
		return list;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */
	public ReadNode(ArrayList aList){
		//super(aList);
		this.list = aList;		
	}	
	
	@Override
    public String toString() {	
		return "Read List: {" + list +"}";
		//return list +"";
	}


	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitReadNode(this);
	}

}