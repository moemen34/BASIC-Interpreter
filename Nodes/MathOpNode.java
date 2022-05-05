package ass1;

public class MathOpNode extends Node{
	
	
	public enum operations{ADD, SUBTRACT, MULTIPLY, DIVIDE;}
	
	operations op;
	Node left, right;
	
	//op accessor
	public operations getOp() {
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
	public MathOpNode(operations op, Node leftChild, Node rightChild){
		this.op = op;
		this.left = leftChild;
		this.right = rightChild;
	}
	
	@Override
    public String toString() {	
		
		if(op == operations.ADD) {
			return "MathNode(" + "+, " + left + ", " + right + ")" ;
		}else if(op == operations.SUBTRACT){
			return "MathNode(" + "-, " + left + ", " + right + ")";
		}else if(op == operations.MULTIPLY){
			return "MathNode(" + "*, " + left + ", " + right + ")";
		}else {
			return "MathNode(" + "/, " + left + ", " + right + ")";
		}

	}
}
	
