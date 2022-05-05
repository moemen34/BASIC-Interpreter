package ass1;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import ass1.BooleanOperationNode.BooleanOperations;
import ass1.MathOpNode.operations;




public class Interpreter {
	
	//creating a list that holds the data declared in data statements
	public static ArrayList<Node> globalDataList = new ArrayList<Node>(); //to hold the data	
	
	//stack used to store statement whwn a gosub is encountered
	static Stack<Node> stack = new Stack<Node>(); // stack for GOSUB
	
	//creating HashMaps
	//hashMap to store integer variables
	static HashMap<String, Integer> StrToInt = new HashMap<String, Integer>();	
	//hashMap to store float variables
	static HashMap<String, Float> StrToFloat = new HashMap<String, Float>();
	//hashMap to store string variables
	static HashMap<String, String> StrToStr = new HashMap<String, String>();
	
	//hashMap to store Statement Nodes
	static HashMap<String, Node> StrToNode = new HashMap<String, Node>();
	
	
	
	
	public static void Interpret() throws Exception {
	   ArrayList<StatementNode> list = new ArrayList<StatementNode>();
	   list = StatementsNode.getList();
		
	   ArrayList<VariableNode> readList = new ArrayList<VariableNode>();
		
	   StatementNode currentStatement = list.get(0);
		
	   while(currentStatement != null) {
		int statementType = 0;
			
		if(currentStatement instanceof ReadNode) statementType = 1;
		else if(currentStatement instanceof AssignmentNode) statementType = 2;
		else if(currentStatement instanceof InputNode) statementType = 3;
		else if(currentStatement instanceof PrintNode) statementType = 4;
		else if(currentStatement instanceof IfNode) statementType = 5;
		else if(currentStatement instanceof GosubNode) statementType = 6;
		else if(currentStatement instanceof ReturnNode) statementType = 7;
		else if(currentStatement instanceof ForNode) statementType = 8;
		else if(currentStatement instanceof NextNode) statementType = 9;
		//else if(currentStatement instanceof ReturnNode) statementType = 10;
						
		switch(statementType) {	
		
		case 1:
                //current statement is a Read statement
				readList = ((ReadNode) currentStatement).getValue();
				
				//loop through every element in the READ list
				for(int i=0; i<readList.size(); i++) {
					
					//check if the data list is empty and throw an exception if so
					if(globalDataList.isEmpty()) {
						throw new Exception ("There is no data for the read node");
					}
					
					VariableNode currentReadNode = readList.get(i);
					Node currentDataNode = globalDataList.get(0);
					
					//if the first element in the list is an Integernode
					if(currentDataNode instanceof IntegerNode) {

						int variableValue = ((IntegerNode) currentDataNode).getValue();
						String variableString = currentReadNode.getValue();
						
						//add the variable to the string to int hashmap if it has the correct ending
						if((variableString.charAt(variableString.length() - 1) != '$')
								&& (variableString.charAt(variableString.length() - 1) != '%')) {
							
							StrToInt.put(variableString, variableValue);
							
						}else
							throw new Exception ("The DATA and READ types don't match");
						
				    //if the first element in the list is an FloatNode
					}else if(currentDataNode instanceof FloatNode) {
						
						float variableValue = ((FloatNode) currentDataNode).getValue();
						String variableString = currentReadNode.getValue();
						
						// if the variable anme has the correct ending add it to the matching hashmap	
						if(variableString.charAt(variableString.length() - 1) == '%'){
							
							StrToFloat.put(variableString, variableValue);
							
						}else {
							System.out.println(variableString + currentDataNode +" pppppp");
							throw new Exception ("The DATA and READ types don't match");
							}
						
				    //if the first element in the list is an StringNode	
					}else if(currentDataNode instanceof StringNode) {
						
						String variableValue = ((StringNode) currentDataNode).getValue();
						String variableString = currentReadNode.getValue();	
							
						// if the variable anme has the correct ending add it to the matching hashmap
						if(variableString.charAt(variableString.length() - 1) == '$'){

							StrToStr.put(variableString, variableValue);
							
						}else
							throw new Exception ("The DATA and READ types don't match");
						
					}
					
					globalDataList.remove(0);
					
				}
				
				currentStatement = currentStatement.getNext();
				
				break;
				
		case 2:
			//The current Satatement is an assignment
			AssignmentNode assignment = (AssignmentNode) currentStatement;
			
			VariableNode assignPart = assignment.getVariableNode();
			Node valuePart = assignment.getValueNode();
			
			//if the right side of the assignment is a math operation
			if(valuePart instanceof MathOpNode) {

				//get the variable name
				String asp = assignPart.getValue();
				
				//if the variable name indicates that it's a float
				if(asp.charAt(asp.length() - 1) == '%' ) {
					String assignString = assignPart.getValue();
					//evaluate the mathOp using the "EvaluateFloatMathOp" method 
					//and add the value to the string to float hashMap
					StrToFloat.put(assignString, EvaluateFloatMathOp(assignment.getValueNode()));
					
				}else {
					//the assignment part is an integer op
					String assignString = assignPart.getValue();
					//evaluate the mathOp using the "EvaluateMathOp" method 
					//and add the value to the string to int hashMap
					StrToInt.put(assignString, EvaluateMathOp(assignment.getValueNode()));			
				}
				
				
			}else if(valuePart instanceof IntegerNode) {
				/*If the  value part is an int add it to the 
				 * string to integer hashMap*/
				
				String assignString = assignPart.getValue();
				int valueInt = ((IntegerNode) valuePart).getValue();
			
				StrToInt.put(assignString, valueInt);
				
				
			}else if(valuePart instanceof FloatNode) {
				/*If the value part is a float add it to the 
				 * string to Float hashMap*/
				
				String assignString = assignPart.getValue();
				Float valueFloat = ((FloatNode) valuePart).getValue();
				
				StrToFloat.put(assignString, valueFloat);
				
			}else if(valuePart instanceof StringNode) {
				/*If the value part is an string add it to the 
				 * string to string hashMap*/
				
				String assignString = assignPart.getValue();
				String valueString = ((StringNode) valuePart).getValue();
				
				StrToStr.put(assignString, valueString);
				
			}else if(valuePart instanceof FunctionNode) {
				
				/*if the value part is a function call the function that
				 * evaluates it and store the value to the corresponding 
				 * HashMap
				 */
				
				FunctionNode func = (FunctionNode) valuePart;
				String assignString = assignPart.getValue();
				
				if(func.getName().equals("LEFT$") || func.getName().equals("RIGHT$")
						|| func.getName().equals("MID$")) {
										
					StrToStr.put(assignString, LEFTorRIGHTorMID(func));
					
					
				}else if(func.getName().equals("RANDOM")) {
								
					StrToInt.put(assignString, RANDOM());
					
				}else if(func.getName().equals("NUM$")) {
					
					if(func.getValue().size() != 1) {
						throw new Exception("Invalid parameters");
					}
					Node par = (Node) func.getValue().get(0);
					
					
					StrToStr.put(assignString, NUM$(par));
					
				}else if(func.getName().equals("VAL")) {
					
					if(func.getValue().size() != 1) {
						throw new Exception("Invalid parameters");
					}
					Node par = (Node) func.getValue().get(0);
					
					//System.out.println(VAL(par));
					StrToInt.put(assignString, VAL(par));
					
				}else if(func.getName().equals("VAL%")) {
					
					//FunctionNode function = func;
					if(func.getValue().size() != 1) {
						throw new Exception("Invalid parameters");
					}
					Node par = (Node) func.getValue().get(0);
					
					//System.out.println(VAL2(par));
					StrToFloat.put(assignString, VAL2(par));
					
				}				
			}		
			
			currentStatement = currentStatement.getNext();
			
			break;
		case 3:
			//the current statement is an input statement
			
			InputNode inputStatement = (InputNode) currentStatement;
			
			ArrayList<Node> inputList = new ArrayList<Node>();
			inputList = inputStatement.getValue();
			
			//print the instructions to the user
			System.out.println(inputList.get(0));
			
			//get input from user
			Scanner keyboard = new Scanner (System.in);
			String input = keyboard.nextLine();
			
			
			//create arraylist to store the input
			ArrayList<String> arr = new ArrayList<String>();
			String s = "";
			for (int i = 0; i < input.length(); i++){
				
			    char c = input.charAt(i); 
			    
			    if (c == ',') {
			    	
			    	arr.add(s);
			    	s = "";
			    //***************	
			    	if(input.charAt(i+1) == ' ') {
			    		i++;
			    	}
			    //***************	
			    }else if (c == '"') {
			    	s += c;
			    	i++;
			    	c = input.charAt(i);
			    	
			    	while(c != '"') {
			    		s += c;
			    		i++;
			    		c = input.charAt(i);
			    	}		    	
			    	s += c;
			    	arr.add(s);
			    	s = "";
			    	if(input.charAt(i+1) != ',') 
			    		throw new Exception("invalid INPUT");
			    	else
			    		i++;   	
			    }else
			    	s += c;
			    
			    if(i == (input.length()-1)) {
			    	arr.add(s);
			    }

			}
			
			
			
			//**************************
			//System.out.println(Arrays.toString(splitInput));
			
			if(arr.size() != (inputList.size() - 1)) {
				throw new Exception ("INVALID INPUT");
			}
			
			for(int i=1; i< inputList.size(); i++) {
				
				VariableNode variable = (VariableNode) inputList.get(i);
				String variableString = variable.getValue();
				
				//(variableString.charAt(variableString.length() - 1) == '%')
				if(variableString.charAt(variableString.length() - 1) == '$' ) {
					
					StrToStr.put(variableString, arr.get(i-1));
					
				}else if(variableString.charAt(variableString.length() - 1) == '%') {
					
					StrToFloat.put(variableString, Float.parseFloat(arr.get(i-1)));
					
				}else {
					StrToInt.put(variableString, Integer.parseInt(arr.get(i-1)));
				}
				
			}
			
			currentStatement = currentStatement.getNext();
			
			break;
			
			
		case 4:
			
			PrintNode printstatement = (PrintNode) currentStatement;
			ArrayList<Node> printList = new ArrayList<Node>();
			
			printList = printstatement.getValue();
			
			for(int i=0; i<printList.size(); i++) {	
				
				Node currentNode = printList.get(i);
				if(currentNode instanceof IntegerNode) {
					System.out.println(currentNode);
					
				}else if(currentNode instanceof FloatNode){
					System.out.println(currentNode);
					
				}else if(currentNode instanceof MathOpNode) {
										
					try {
						
						float x = EvaluateFloatMathOp(currentNode);
						System.out.println(x);
						
					}catch(Exception e) {

						int x = EvaluateMathOp(currentNode);
						System.out.println(x);
					}

				}else if(currentNode instanceof StringNode) {
					
					System.out.println(currentNode);
					
					
				}else if(currentNode instanceof FunctionNode) {
					
					FunctionNode func = (FunctionNode) currentNode;
					
					if(func.getName().equals("LEFT$") || func.getName().equals("RIGHT$")
							|| func.getName().equals("MID$")) {
						
						System.out.println(LEFTorRIGHTorMID(func));//
						
						
					}else if(func.getName().equals("RANDOM")) {
						
						System.out.println(RANDOM());
						
					}else if(func.getName().equals("NUM$")) {
						
						System.out.println(NUM$(currentNode));
						
					}else if(func.getName().equals("VAL")) {
						
						System.out.println(VAL(currentNode));
						
					}else if(func.getName().equals("VAL%")) {
						
						System.out.println(VAL2(currentNode));
					}
						
				}else if(currentNode instanceof VariableNode) {
					
					VariableNode VNode = (VariableNode) currentNode;
					String VNodeStr = VNode.getValue();
					
					if(VNodeStr.charAt(VNodeStr.length() - 1) == '$' ) {
						
						if(StrToStr.containsKey(VNodeStr)) {
							System.out.println(StrToStr.get(VNodeStr));
							
						}else
							throw new Exception ("variable not initialized or declared" + VNodeStr);
						
					}else if(VNodeStr.charAt(VNodeStr.length() - 1) == '%') {
						
						if(StrToFloat.containsKey(VNodeStr)) {
							//LC = StrToInt.get(variable.getValue());
							System.out.println(StrToFloat.get(VNodeStr));
							
						}else
							throw new Exception ("variable not initialized or declared" + VNodeStr);
						
					}else {
						//StrToInt.put(variableString, Integer.parseInt(splitInput[i-1]));
						
						if(StrToInt.containsKey(VNodeStr)) {
							//LC = StrToInt.get(variable.getValue());
							System.out.println(StrToInt.get(VNodeStr));
							
						}else
							throw new Exception ("String not initialized or declared" + VNodeStr);
						
					}					
				}				  
			}
			
			currentStatement = currentStatement.getNext();
			
			break;
		case 5:
			//the current statement is an if statement
			
			IfNode currentIf = (IfNode) currentStatement;		
			
			boolean isInt = false;
			
			BooleanOperationNode boolOp = (BooleanOperationNode) currentIf.getBoolean();
			
			Node rightSide = boolOp.getRight(); 
			Node leftSide  = boolOp.getLeft();
			
			int leftInt = -1;
			float leftFloat = 0;
			
			/*try catch statement that checks if the left side is an integer
			 * operation or an integer node or a variable node that exists
			 * in the string to int hashMap, if an exception is thrown the
			 * the catch statement stops it to the check if the left side is  
			 * a float operation or a float node or a variable node that exists
			 * in the string to float hashMap.
			 * 
			 * it also sets its value
			 */
			try {				
				if(leftSide instanceof MathOpNode) {				
					leftInt = EvaluateMathOp(leftSide);				
				}else if(leftSide instanceof IntegerNode) {
					leftInt = ((IntegerNode) leftSide).getValue();
				}else if(leftSide instanceof VariableNode) {
					if(StrToInt.containsKey(((VariableNode) leftSide).getValue())) {

						leftInt = StrToInt.get(((VariableNode) leftSide).getValue());
					}else {
						leftInt = 0/0; //to cause an error so we can move to the catch statement
					}
				}else leftInt = 0/0; // to cause an error so we can move to the catch statement
						
				isInt = true;				
			}catch(Exception e) {
				if(leftSide instanceof MathOpNode) {				
					leftFloat = EvaluateFloatMathOp(leftSide);				
				}else if(leftSide instanceof FloatNode) {
					leftFloat = ((FloatNode) leftSide).getValue();
				}else if(leftSide instanceof VariableNode) {
					if(StrToFloat.containsKey(((VariableNode) leftSide).getValue())) {

						leftFloat = StrToFloat.get(((VariableNode) leftSide).getValue());
					}
				}
			}

			
			int rightInt = 0;
			float rightFloat = -1;
			
			/*try catch statement that checks if the right side is an integer
			 * operation or an integer node or a variable node that exists
			 * in the string to int hashMap, if an exception is thrown the
			 * the catch statement stops it to the check if the left side is  
			 * a float operation or a float node or a variable node that exists
			 * in the string to float hashMap.
			 * 
			 * it also sets its value
			 */
			try {				
				if(rightSide instanceof MathOpNode) {				
					rightInt = EvaluateMathOp(rightSide);				
				}else if(rightSide instanceof IntegerNode) {
					rightInt = ((IntegerNode) rightSide).getValue();
				}else if(rightSide instanceof VariableNode) {
					
					if(StrToInt.containsKey(((VariableNode) rightSide).getValue())) {					
						
						rightInt = StrToInt.get(((VariableNode) rightSide).getValue());
					}else {
						
						rightInt = 0/0;// to cause an error so we can move to the catch statement
					}
				} else rightInt = 0/0;// to cause an error so we can move to the catch statement
				
			}catch(Exception e) {
				if(rightSide instanceof MathOpNode) {				
					rightFloat = EvaluateFloatMathOp(rightSide);				
				}else if(rightSide instanceof FloatNode) {
					rightFloat = ((FloatNode) rightSide).getValue();
				}else if(rightSide instanceof VariableNode) {
					if(StrToFloat.containsKey(((VariableNode) rightSide).getValue())) {
						
						rightFloat = StrToFloat.get(((VariableNode) rightSide).getValue());
					}
				}	
			}
			
			/*check if the statement is true based
			 * on the operator
			 */		
			BooleanOperations op = boolOp.getOp();
			boolean isTrue = false;
			
			if(op == BooleanOperations.EQUALS) {
				
				if(isInt == true) {
					
					if(leftInt == rightInt) {
						isTrue = true;
					}
				}else {
					if(leftFloat == rightFloat) {
						isTrue = true;
					}
				}
			}else if(op == BooleanOperations.LESSTHAN){
				
				if(isInt == true) {
					
					if(leftInt < rightInt) {
						isTrue = true;
					}
				}else {
					if(leftFloat < rightFloat) {
						isTrue = true;
					}
				}

			}else if(op == BooleanOperations.GREATERTHAN){

				if(isInt == true) {
					
					if(leftInt > rightInt) {
						isTrue = true;
					}
				}else {
					if(leftFloat > rightFloat) {
						isTrue = true;
					}
				}

				
			}else if(op == BooleanOperations.GREATERTHANOREQUALS){

				if(isInt == true) {
					
					if(leftInt >= rightInt) {
						isTrue = true;
					}
				}else {
					if(leftFloat >= rightFloat) {
						isTrue = true;
					}
				}

				
			}else if(op == BooleanOperations.LESSTHANOREQUALS){

				if(isInt == true) {
					
					if(leftInt <= rightInt) {
						isTrue = true;
					}
				}else {
					if(leftFloat <=  rightFloat) {
						isTrue = true;
					}
				}

				
			}else if(op == BooleanOperations.NOTEQUALS){

				if(isInt == true) {
					
					if(leftInt != rightInt) {
						isTrue = true;
					}
				}else {
					if(leftFloat !=  rightFloat) {
						isTrue = true;
					}
				}				
	        }
				
			/*If the boolean value is found to be true 
			 * get the label after the THEN and search in
			 * linked list for the child of the labeled 
			 * statement that is in the hashMap and set 
			 * the next node to be it
			 */
			if(isTrue == true) {
				String label = currentIf.getLabel().getTokenValue();
				
				if(StrToNode.containsKey(label)) {
					
					StatementNode labeledStatement = (StatementNode) StrToNode.get(label);
					StatementNode thisStatement = list.get(0);
					
					while(thisStatement != labeledStatement) {
						thisStatement = thisStatement.getNext();
					}

					currentStatement = thisStatement;					
					break;
				}				
			}
			
			//if the boolean in if is false
			currentStatement = currentStatement.getNext();			
			break;
		
		case 6:
			//the current statement is a gosub
			
			GosubNode currentGosub = (GosubNode) currentStatement;
			
			String gosubLabel = currentGosub.getValue();
			
			/*get the next statement in the linked list 
			and push it to a stack*/
			StatementNode afterGosub = currentStatement.getNext();
			
			stack.push(afterGosub);
			
			
				/*search in the string to node hashMap for the 
				 * statement with the label referred to in Gosub
				 * and search for the child of it in the linked 
				 * list to set the next node to
				 */
				if(StrToNode.containsKey(gosubLabel)) {
					
					StatementNode labeledStatement = (StatementNode) StrToNode.get(gosubLabel);
					StatementNode thisStatement = list.get(0);
					
					while(thisStatement != labeledStatement) {
						thisStatement = thisStatement.getNext();
					}

					currentStatement = thisStatement;					
					break;
				}else
					throw new Exception("No such GOSUB label");

			

			
		case 7:
			//the current statement is a return
			
			/*Check if the stack is empty, if not pop the first
			 * statement node in it and set the next statement to be it  
			 */
			if(!stack.empty()) {
			StatementNode poppedStatement = (StatementNode) stack.pop();
			
			currentStatement = poppedStatement;
			break;
			}else {
				currentStatement = currentStatement.getNext();
				break;
			}
			
			
			
		case 8:
			//the current statement is a for statement
			
			ForNode forStatement = (ForNode) currentStatement;
			
			/*
			 * get the variables declared in the for statement
			 */
			int initial = forStatement.getinitialValue().getValue();
			int increment = forStatement.getincrementValue().getValue();
			int limit = forStatement.getlimitlValue().getValue();
			String variable = forStatement.getVariable().getValue();
			
			/*if the variable has not been declared already add 
			it to the string to int hashMap*/
			if(!StrToInt.containsKey(variable)) {
			StrToInt.put(variable, initial);
			}else {
				//else update the value in the hashMap
				initial = StrToInt.get(variable) + increment;
				StrToInt.replace(variable, initial);
			}
			
			/*check if the increment value is positive or 
			 * negative to compare the the number to the limit
			 * correctly
			 * If the limit has been reached set the next statement
			 * to the statement after the next statement
			 * If the limit has not been reached yet set the next 
			 * statement to the NEXT statement
			 */
			if(increment >= 0) {
				
				if(initial <= limit) {
					currentStatement = currentStatement.getNext();
					break;
				}else {
					currentStatement = forStatement.getAfterNextStatement();
					break;
				}
				
				
			}else if(increment < 0) {
				
				if(initial >= limit) {
					currentStatement = currentStatement.getNext();
					break;
				}else {
					currentStatement = forStatement.getAfterNextStatement();
					break;
				}
				
			}
						
			break;
			
		case 9:
			//the current statement is a Next statement
			
			//set the next statement back to the forLoop
			NextNode nextStatement = (NextNode) currentStatement;
			currentStatement = nextStatement.getNextStatement();
			
			
			
			break;
			
			
			}
	 }//end of while
	}
	

	
/////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * initialize method that calls the four walks and the Interpret method
	 * @throws Exception 
	 */
	public static void Initialize() throws Exception {
		
		
		
		//call the four walk methods
		firstWalk();
		secondWalk();
		thirdWalk();
		fourthWalk();
		
		Interpret();
		
		//////////////////////////////////////////////////////////////////////
		/*System.out.println("////////////***********--------------///////////");
		for(Node k : globalDataList) {
			System.out.println(k);
		}*/
	}
	
	
	
	
	/**
	 * first walk method that Replaces the LabeledStatementNode in the AST with the
	 * child node and Populates the map with the name and the child node
	 */
	public static void firstWalk() {
		//get the list from the parser and set it in the list variable		
		ArrayList<StatementNode> list = StatementsNode.getList();
		
		//loop over the statements to find labeled statements 
		for(int i = 0; i <list.size(); i++ ) {		
			StatementNode statement = list.get(i); 
			//if the current statement is a labeled statement
			if(statement instanceof LabeledStatementNode) {
				//store the current statement in a labeledStatementNode by casting it 
				LabeledStatementNode labeledStatement = (LabeledStatementNode) statement;
				
				//get the label and child of the labeled statement
				String label = labeledStatement.getLabel();			
				StatementNode child = (StatementNode) labeledStatement.getValue();
				
				//replace the labeled statement with its child
				list.set(i,child);
				//add the lable and the statement to the HashMap
				StrToNode.put(label, child);
			}
		}
		
		///System.out.println();
		//System.out.println("*******************PRINTING THE HASHMAP*******************");
		
		//print the contents of the hashMap
		/*StrToNode.entrySet().forEach(entry -> {
		    System.out.println("LABEL: " + entry.getKey() + " | STATEMNET: " + entry.getValue());
		});*/
		
	}
	
	
	

	
	/**
	 * second walk method that Adds a Node reference to NEXT so that we know where to go when 
	 * NEXT executes and adds 2 Node references to the FOR that references the node AFTER next
	 * and the first statement in the loop
	 */
	public static void secondWalk() {
		
		///System.out.println();
		///System.out.println("...................THE FOR AND NEXT NODE REFERENCES...................");
		//variable initialization
		boolean found = false;
		ForNode forStatement = null;
		NextNode nextStatement = null;
		ArrayList<StatementNode> list = StatementsNode.getList();
	
		//for loop that loops over the list
		for(int i = 0; i <list.size(); i++ ) {
			StatementNode statement = list.get(i);
			//if the current statement is a for statement reference the statement
			//right after it as the first in loop reference
			if(statement instanceof ForNode) {			
				found = true;
				forStatement = (ForNode) statement;
				forStatement.setFirstInLoopStatement(list.get(i+1));
			}
			
			//if the current statement is a Next statement reference its next statement
			//back to the for loop and reference the statement after it as the reference
			//statement after the loop
			if((statement instanceof NextNode) && (found == true)) {
				nextStatement = (NextNode) statement;
				
				nextStatement.setNextStatement(forStatement);
				forStatement.setAfterNextStatement(list.get(i+1));
				
				found = false;
				forStatement = null;
				nextStatement = null;
			}			
		}
		
		//for loop that prints the references
		for(int i = 0; i <list.size(); i++ ) {

			StatementNode ThisStatement = list.get(i);
			if(ThisStatement instanceof ForNode) {
				///System.out.println();//PRINT AN EMPTY LINE
				///System.out.println("FOR node reference to the statement after the loop: \n" + ((ForNode) ThisStatement).getAfterNextStatement());
				///System.out.println("FOR node reference to the first statement in the loop: \n"+ ((ForNode) ThisStatement).getFirstInLoopStatement());

			}else if(ThisStatement instanceof NextNode) {
				///System.out.println("NEXT node reference: \n" + ((NextNode) ThisStatement).getNextStatement());
			}
		}
	} 
	
	
	
	
	/**
	 * third walk method that removes the DATA statements and inserts their
	 *  contents into an array that we can use for READ
	 */
	public static void thirdWalk() {
		//variable initialization
		//public static ArrayList<Node> globalDataList = new ArrayList<Node>();
		ArrayList<Node> dataList;
		ArrayList<StatementNode> statementList = StatementsNode.getList();
		
		///System.out.println();
		///System.out.println("///////////////////PRINTING CONTENTS OF THE ARRAY THAT HOLD THE DATA///////////////////");
		
		//for loop that removes the data nodes and stores the data in an array
		for(int i = 0; i < statementList.size(); i++) {
			
			StatementNode statement = statementList.get(i);

			if(statement instanceof DataNode) {
				//print the data statement
				///System.out.println(statement);
				DataNode dataStatement = (DataNode) statement;
				dataList = dataStatement.getValue();
				for(Node n : dataList) {
					globalDataList.add(n);
				}
				//remove the Data statement from the array
				StatementsNode.getList().remove(statement);
			}
		}
		
		//print the data in the list that we can use to read from later
		///for(Node no : globalDataList) {
			///System.out.println(no);
		///}
		
		/*for(StatementNode SN : StatementsNode.getList()) {
			System.out.println(SN);
		}*/
	}
	
	
	
	/**
	 * fourth walk method that sets the “Next” element for each statement
	 *  to the next item in the StatementsNode arrayList
	 */
	public static void fourthWalk() {
		
		///System.out.println();
		///System.out.println("----------PRINTING THE CONTENTS OF THE MODIFIED ARRAY LIST AND THE NEXT STATEMENT THEY POINT TO------------");
		
		//get the statements list from the parser
		ArrayList<StatementNode> list = new ArrayList<StatementNode>();
		list = StatementsNode.getList();
		
		//for loop that creates a pointer to each statement 
		//to the statement right after it and sets the 
		//pointer/member to null when we reach the last statement
		for(int i = 0; i <list.size(); i++ ) {
			if(i == (list.size() -1)) {
				list.get(i).setNext(null);
				///System.out.println(list.get(i) + " ---> " + "null");
			}else {
				list.get(i).setNext(list.get(i+1));
			
			///System.out.println(list.get(i) + " ---> " + list.get(i).getNext());
			}
		}
	}
	
	
	/**
	 * method that returns a random number
	 * @return
	 */
	public static int RANDOM() {
		
		Random generator = new Random();

        int rand = generator.nextInt(1000);
        
        return rand;
	}
	
	/**
	 * method that converts a number to a string
	 * @param x
	 * @return
	 */
	public static String NUM$(Node x) {
		String str = null;
		
		if(x instanceof VariableNode) {

			int paramInt;
			float paramFloat;
			VariableNode param = (VariableNode) x;
			String paramString = param.getValue();
			
			if(StrToInt.containsKey(paramString)) {

				paramInt = StrToInt.get(paramString);
				
				str = String.valueOf(paramInt);
				return str;
				
			}else if(StrToFloat.containsKey(paramString)) {
				paramFloat = StrToFloat.get(paramString);
				
				str = String.valueOf(paramFloat);
				return str;
			}
			
		}
			
		if(x instanceof IntegerNode) {
			System.out.println("KKk");
			
			IntegerNode intNode = (IntegerNode) x;
			str = String.valueOf(intNode.getValue());
			return str;
		}else if(x instanceof FloatNode) {
			FloatNode floatNode = (FloatNode) x;
			str = String.valueOf(floatNode.getValue());
			return str;
		}		
		return null;
	}
	
	
	
	/**
	 * Method that converts a string to an int
	 * @param x
	 * @return
	 * @throws Exception
	 */
	public static int VAL(Node x) throws Exception {
		
		int num;
		
		if (x instanceof VariableNode) {

			VariableNode paramNode = (VariableNode) x;
			String paramString = paramNode.getValue();
			
			if(StrToStr.containsKey(paramString)) {

				String paramVal = StrToStr.get(paramString);
				num = Integer.parseInt(paramVal);
				
				return num;
				
			}
			
		}else if(x instanceof StringNode) {
			
			StringNode strNode = (StringNode) x;
			String param = strNode.getValue();
			
			
			String str = param.substring(1, param.length()-1); 
			
			num = Integer.parseInt(str);
			
			return num;
			
		}else
			throw new Exception("hg");
		
		int v = 69;
		return v;

	}
	
	
	/**
	 * Method that converts a string to a float
	 * @param x
	 * @return float
	 * @throws Exception
	 */
	public static float VAL2(Node x) throws Exception {
		
		float num;
		
		if (x instanceof VariableNode) {
			
			VariableNode paramNode = (VariableNode) x;
			String paramString = paramNode.getValue();
			
			if(StrToStr.containsKey(paramString)) {
				String paramVal = StrToStr.get(paramString);
				num = Float.parseFloat(paramVal);
				
				return num;
				
			}
			
		}else if(x instanceof StringNode) {
			
			StringNode strNode = (StringNode) x;
			String param = strNode.getValue();
			
			String str = param.substring(1, param.length() - 1);
			
			num = Float.parseFloat(str);
			
			return num;
			
		}else
			throw new Exception("Invalid input");
		
		float v = 69;
		return v;
		
	}
	
	
	/**
	 * method that does what the basic functions LEFT$, RIGHT$, MID$ do
	 * @param x string
	 * @return substring of string
	 * @throws Exception
	 */
	public static String LEFTorRIGHTorMID(Node x) throws Exception {
		
		ArrayList<Node> paramList = new ArrayList<Node>();
		FunctionNode function = (FunctionNode) x;
		
		
		if(function.getName().equals("LEFT$") || function.getName().equals("RIGHT$")) {
				
			paramList = function.getValue();
			int param2 = 0;
			String param1 = null;
			String str;
			
			if((paramList.size() != 2)) {
				
				throw new Exception("invalid function invocation!");
			}
			
			if(paramList.get(0) instanceof StringNode) {
				
			StringNode firstParam = (StringNode) paramList.get(0);
			param1 = firstParam.getValue();
			
			}else if(paramList.get(0) instanceof VariableNode) {
				
				VariableNode firstParam = (VariableNode) paramList.get(0);
				String firstParamString = firstParam.getValue();
				
				if(StrToStr.containsKey(firstParamString)) {
					param1 = StrToStr.get(firstParamString);
				}
				
			}else
				throw new Exception ("invalid function parameter");
				
			if(paramList.get(1) instanceof IntegerNode) {
				
			IntegerNode secondParam = (IntegerNode) paramList.get(1);
			param2 = secondParam.getValue();
			
			}else if(paramList.get(1) instanceof VariableNode) {
			
				VariableNode secondParam = (VariableNode) paramList.get(1);
				String secondParamString = secondParam.getValue();
				
				if(StrToInt.containsKey(secondParamString)) {
					param2 = StrToInt.get(secondParamString);
				}				
			}				
			else 
				throw new Exception ("invalid function parameter");		
			
			if(function.getName().equals("LEFT$")) {
				
				str = param1.substring(1, param2+1);
				return str;	
				
			}else if(function.getName().equals("RIGHT$")) {
				
				str = param1.substring((param1.length()-1) - param2, param1.length()-1);
				//System.out.println(str);
				
				return str;
			}						
		}else if(function.getName().equals("MID$")) {
			
			paramList = function.getValue();
			String param1 = null;
			int param2 = 0;
			int param3 = 0;
			String str;
			
			if((paramList.size() != 3)) {
				
				throw new Exception("invalid function invocation!");
			}
			
			if(paramList.get(0) instanceof StringNode) {
				
			StringNode firstParam = (StringNode) paramList.get(0);
			param1 = firstParam.getValue();
			
			}else if(paramList.get(0) instanceof VariableNode) {
				
				//String firstParam = (VariableNode) paramList.get(0).;
				VariableNode firstParam = (VariableNode) paramList.get(0);
				String firstParamString = firstParam.getValue();
				
				if(StrToStr.containsKey(firstParamString)) {
					//LC = StrToInt.get(variable.getValue());
					//System.out.println(StrToStr.get(VNodeStr));
					param1 = StrToStr.get(firstParamString);
				}
				
			}else
				throw new Exception ("invalid function parameter");
							
			if(paramList.get(1) instanceof IntegerNode) {
				
			IntegerNode secondParam = (IntegerNode) paramList.get(1);
			param2 = secondParam.getValue();
			
			}else if(paramList.get(1) instanceof VariableNode) {
			
				//String firstParam = (VariableNode) paramList.get(0).;
				VariableNode secondParam = (VariableNode) paramList.get(1);
				String secondParamString = secondParam.getValue();
				
				if(StrToStr.containsKey(secondParamString)) {
					//LC = StrToInt.get(variable.getValue());
					//System.out.println(StrToStr.get(VNodeStr));
					param2 = StrToInt.get(secondParamString);
				}				
			}				
			else 
				throw new Exception ("invalid function parameter");		
			
			if(paramList.get(2) instanceof IntegerNode) {
				
				IntegerNode thirdParam = (IntegerNode) paramList.get(2);
				param3 = thirdParam.getValue();
				
				}else if(paramList.get(2) instanceof VariableNode) {
				
					//String firstParam = (VariableNode) paramList.get(0).;
					VariableNode thirdParam = (VariableNode) paramList.get(2);
					String thirdParamString = thirdParam.getValue();
					
					if(StrToStr.containsKey(thirdParamString)) {
						//LC = StrToInt.get(variable.getValue());
						//System.out.println(StrToStr.get(VNodeStr));
						param3 = StrToInt.get(thirdParamString);
					}					
				}				
				else 
					throw new Exception ("invalid function parameter");
						
				str = param1.substring(param2+1, param3+1);/////
	
				return str;							
		}		
		return null;
	}
	
	
	
	/**
	 * method that evaluates math nodes
	 * @param x mathop node to evaluate
	 * @return result of the math
	 * @throws Exception
	 */
	public static Integer EvaluateMathOp(Node x) throws Exception {
				
		if(x instanceof IntegerNode) {
			int y = ((IntegerNode) x).getValue();
		}
		
		else if(x instanceof MathOpNode) {
			
			Node rightChild = ((MathOpNode) x).getRight();
			int LC = 0;
			int RC = 0;

			if(rightChild instanceof VariableNode) {
				
				VariableNode var = (VariableNode) rightChild;
				
				if(StrToInt.containsKey(var.getValue())) {
					
					RC = StrToInt.get(var.getValue());	
				}
				
			}else
			
			if(rightChild instanceof IntegerNode) {
				
				RC = ((IntegerNode) rightChild).getValue();
				
			}else if(rightChild instanceof MathOpNode) {
				
				RC = EvaluateMathOp(rightChild);
			}
			
			Node leftChild = ((MathOpNode) x).getLeft();
			
				if(leftChild instanceof VariableNode) {
					
					VariableNode variable = (VariableNode) leftChild;
					
					if(StrToInt.containsKey(variable.getValue())) {
						LC = StrToInt.get(variable.getValue());
					}
					
				}else if(leftChild instanceof IntegerNode) {
					
				LC = ((IntegerNode) leftChild).getValue();

			}else if (leftChild instanceof MathOpNode) {
				
				LC = EvaluateMathOp(leftChild);
			}
			
			operations op = ((MathOpNode) x).getOp();
			
			if(op == operations.ADD) {
				return LC + RC;
			}else if(op == operations.SUBTRACT) {
				return LC - RC;
			}else if(op == operations.MULTIPLY) {
				return LC * RC;
			}else if(op == operations.DIVIDE) {
				return LC / RC;
			}else
				return null;
		}
		return null;
		
	}
	
	
	public static float  EvaluateFloatMathOp(Node x) {
		float y = 90;
		if(x instanceof FloatNode) {
			y = ((FloatNode) x).getValue();
			//return y;
		}
		
		else if(x instanceof MathOpNode) {
			
			Node rightChild = ((MathOpNode) x).getRight();
			float LC = 0,RC = 0;

			if(rightChild instanceof VariableNode) {
				
				VariableNode var = (VariableNode) rightChild;
				
				if(StrToFloat.containsKey(var.getValue())) {
					RC = StrToFloat.get(var.getValue());
				}
				
			}else if(rightChild instanceof FloatNode) {
				RC = ((FloatNode) rightChild).getValue();
			}else {
				RC = EvaluateFloatMathOp(rightChild);
			}
			
			Node leftChild = ((MathOpNode) x).getLeft();
			
				if(leftChild instanceof VariableNode) {
					
					VariableNode variable = (VariableNode) leftChild;
					
					if(StrToFloat.containsKey(variable.getValue())) {
						LC = StrToFloat.get(variable.getValue());					
					}
					
				}else if(leftChild instanceof FloatNode) {
				LC = ((FloatNode) leftChild).getValue();
			}else {
				LC = EvaluateFloatMathOp(leftChild);
			}
		
				
			operations op = ((MathOpNode) x).getOp();
			
			
			if(op == operations.ADD) {
				return LC + RC;
			}else if(op == operations.SUBTRACT) {
				return LC - RC;
			}else if(op == operations.MULTIPLY) {
				return LC * RC;
			}else if(op == operations.DIVIDE) {
				return LC / RC;
			}			
		}		
		return y;			
	}		
}
