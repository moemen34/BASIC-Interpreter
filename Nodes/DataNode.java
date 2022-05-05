package ass1;

import java.util.ArrayList;

public class DataNode extends StatementNode {

	private ArrayList<Node> list;
	
	//value accessor
	public ArrayList getValue() {
		return list;
	}
	
	
	/**
	 * constructor
	 * @param val
	 */
	public DataNode(ArrayList aList){
		//super(value);
		this.list = aList;		
	}	
	
	@Override
    public String toString() {	
		return "Data List: {" + list +"}";
		//return list +"";
	}


	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitDataNode(this);
	}

}
