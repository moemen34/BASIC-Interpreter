package ass1;

import java.util.ArrayList;

public class FunctionNode extends StatementNode {

	private String functionName;
	private ArrayList<Node> parameterList;
	
	
	//value accessor
	public String getName() {
		return functionName;
	}
	
	public ArrayList getValue() {
		return parameterList;
	}
	
	
	/**
	 * constructor
	 * @param list
	 * @param name
	 */
	public FunctionNode(String name, ArrayList list){
		//super(value);
		this.parameterList = list;	
		this.functionName = name;
	}	
	
	@Override
    public String toString() {	
		return "Function: "+ functionName + "(" + parameterList +")";
		//return list +"";
	}

	@Override
	public void accept(IVisitor v) {
		// TODO Auto-generated method stub
		v.visitFunctionNode(this);
	}
	
}
