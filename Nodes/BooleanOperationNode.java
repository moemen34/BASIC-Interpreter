package ass1;

import ass1.BooleanOperationNode.BooleanOperations;

public class BooleanOperationNode extends Node {

	
	public enum BooleanOperations{EQUALS, LESSTHAN, LESSTHANOREQUALS,
		            GREATERTHAN, GREATERTHANOREQUALS,NOTEQUALS,;}
	
	//BooleanOperations op;
	BooleanOperations op;
	Node left, right;
	
	//op accessor
	public BooleanOperations getOp() {
		return op;
	}	
	//left child acsessor
	public Node getLeft() {
		return left;
	}
	//right child accessor
	public Node getRight() {
		return right;
	}
	
	/**
	 * constructor
	 * @param val
	 */
	public BooleanOperationNode(/*BooleanOperations*/BooleanOperations op, Node leftChild, Node rightChild){
		this.op = op;
		this.left = leftChild;
		this.right = rightChild;
	}

	
	@Override
    public String toString() {	
		
		if(op == BooleanOperations.EQUALS) {
			return "Boolean(" + left + " = " + right + ")" ;
		}else if(op == BooleanOperations.LESSTHAN){
			return "Boolean(" + left + " < " + right + ")" ;
		}else if(op == BooleanOperations.GREATERTHAN){
			return "Boolean(" + left + " > " + right + ")";
		}else if(op == BooleanOperations.GREATERTHANOREQUALS){
			return "Boolean(" + left + " >= " + right + ")";
		}else if(op == BooleanOperations.LESSTHANOREQUALS){
			return "Boolean(" + left + " <= " + right + ")";
		}else{
	    return "Boolean(" + left + " <> " + right + ")";
        }
		//return "Boolean(" + " " + left + " " + op.getTokenValue() +" " + right + ")" ;
	}
}
