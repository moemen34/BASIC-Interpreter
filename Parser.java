package ass1;

import java.util.ArrayList;

import ass1.BooleanOperationNode.BooleanOperations;
import ass1.MathOpNode.operations;
import ass1.Token.tokenType;

public class Parser {
	
	private static ArrayList<Token> tokensList = Lexer.getList();
	
	/**
	 * constructor
	 * @param tokens
	 */
	public Parser(ArrayList<Token> tokens){	
		
		tokensList = tokens;				
	}
	
	/**
	 * method that returns the return value from exception
	 * @return return value from exception
	 * @throws Exception
	 */
	public static void parse() throws Exception {		
		
			Statements();
			////////
			Interpreter.Initialize();
	    }
	
	
	
    /**
     * method that represents an expression, it's also one of three methods that create an AST
     * @return new Node or null
     * @throws Exception
     */
	public static Node Expression() throws Exception {
        //variable declarations
		Node rightChild;
		Node leftChild;
		Node Child;		
		operations op = null; 
		
		
		Node function;
		function = Functions();
		
		if(function != null) {
			return function;
		}
		
		
		Child = Term();//call the term method and store the return value to child

		if (Child == null)  return null;		

		boolean isMinusOrPlusToken = false;
		
		/*if the next element on the list is plus or minus operator
		set the boolean value isMinusOrPlusToken to true and create 
		an operations enum corresponding to the token type*/
		if(matchAndRemove(tokenType.PLUS) != null) { 			
			isMinusOrPlusToken = true;
			op = operations.ADD;//set the op to add enum
     	}	
		else if(matchAndRemove(tokenType.MINUS) != null) {
			isMinusOrPlusToken = true;
			op = operations.SUBTRACT;//set the minus to add enum
	    }
				
		
		if(isMinusOrPlusToken == true) {
			 leftChild = Child;//store the value of child in the left node
			 rightChild = Term();//call the term method and store the return value to right child
			if (rightChild == null) {  
				throw new Exception ("Invalid input");//throw exception message
			}else {
				//while loop that iterates if the next token is a plus or minus token
				while((match(tokenType.PLUS)) || (match(tokenType.MINUS))){
					//create a new math node and store it in the leftChild node
					leftChild = new MathOpNode(op, leftChild, rightChild);
					
					if(matchAndRemove(tokenType.PLUS) != null) { 			
						op = operations.ADD;//set the op to add enum
			     	}	
					else if(matchAndRemove(tokenType.MINUS) != null) {
						op = operations.SUBTRACT;//set the op to subtract enum
				    }
					
					rightChild = Term();//call the term method and store the return value to right child
					if (rightChild == null)   
						throw new Exception ("Invalid input");//throw exception message
				}
				//return a new mathOpNode			
				return new MathOpNode(op, leftChild, rightChild);
			}
		}else {	
			return Child ;
		}		
	}
		
	
	
	/**
	 * method that represents a term, it's also one of three methods that create an AST
	 * @return
	 * @throws Exception
	 */
	public static Node Term() throws Exception {
		//variable declaration
		Node rightChild;
		Node leftChild;
		Node Child;
		operations op = null; 
		Child = Factor();//call the Factor method and store the return value to child
		boolean isTimesOrDivideToken = false;
		
		if (Child == null) return null;		
		
		/*if the next element on the list is times or divide operator
		set the boolean value isMinusOrPlusToken to true and create 
		an operations enum corresponding to the token type*/
		if(matchAndRemove(tokenType.TIMES) != null) { 	
			isTimesOrDivideToken = true;
			op = operations.MULTIPLY;
     	}	
		else if(matchAndRemove(tokenType.DIVIDE) != null) {
			isTimesOrDivideToken = true;
			op = operations.DIVIDE;
	    }
		
		
		if(isTimesOrDivideToken == true) {
			 leftChild = Child;//store the value of child in the left node
			 rightChild = Factor();//call the factor method and store the return value to right child
			if (rightChild == null) {
				throw new Exception ("Invalid input"); //throw exception message				
			}
			
			else {
				//while loop that iterates if the next token is a times or divide token			
				while((match(tokenType.TIMES)) || (match(tokenType.DIVIDE))){
					//create a new math node and store it in the leftChild node
					leftChild = new MathOpNode(op, leftChild, rightChild);
					
					if(matchAndRemove(tokenType.TIMES) != null) { 			
						op = operations.MULTIPLY;//set the op to times enum
			     	}	
					else if(matchAndRemove(tokenType.DIVIDE) != null) {
						op = operations.DIVIDE;//set the op to divide enum
				    }
					
					rightChild = Term();//call the factor method and store the return value to right child
					if (rightChild == null)   
						throw new Exception ("Invalid input");//throw exception message
				}															
                //return a new mathOpNode				
				return new MathOpNode(op, leftChild, rightChild);
			}
		}else {
			return Child ;}
	}
	
	
	
	/**
	 * method that represents an expression, it's also one of three methods that create an AST
	 * @return Node or null
	 * @throws Exception
	 */
	public static Node Factor() throws Exception {
        //variable declarations
		boolean isNumber = false;
		boolean isLParen = false;
		
		Token aTok = matchAndRemove(tokenType.IDENTIFIER);
		
		//if the tokenType is an identifier we return an identifier node
		if(aTok != null) {
			return new VariableNode(aTok.getTokenValue());			
		}
		
		Token aToken = matchAndRemove(tokenType.NUMBER);//call the matchAndRemove mothod and store its value in the aToken value
		
		if(aToken != null) {
			isNumber = true;//set the boolean value to true
			
		}else if(matchAndRemove(tokenType.LPAREN) != null) {
			isLParen = true;//set the boolean value to true			
		}
		
		/*if isNumber is true check if the value can be stored in int and do so
		else store in in a float variable*/
		if(isNumber == true) {		
			String StringValue = aToken.getTokenValue();
			try {
				int intValue = Integer.parseInt(StringValue);
				return new IntegerNode(intValue);
			}catch(NumberFormatException e){				
				float floatValue = Float.parseFloat(StringValue);
				return new FloatNode(floatValue);
			}
						
		}else if(isLParen == true) {
			
			Node exp = Expression();//call the expression method and store the return value to exp node
			if(exp == null) {
				throw new Exception ("invalid input");//throw error message
			}
			
			if (matchAndRemove(tokenType.RPAREN) == null) {
				throw new Exception ("invalid input");//throw error message
			}			
			return exp;
		}else
		return null;
	}
	
	
	
	/**
	 * method that returns a list of statements node or throws an
	 * exception if the list is not found
	 * @return statements node
	 * @throws Exception
	 */
	public static StatementsNode Statements() throws Exception {
		
	    Node SNode;
		ArrayList<Node> list = new ArrayList<Node>();
			
		loop:
			while(!tokensList.isEmpty()) {
				
				while ((matchAndRemove(tokenType.EndOfLine) != null) && (!tokensList.isEmpty())) {
					//System.out.println("Empty line");
					if(tokensList.isEmpty()) {
						//System.out.println(".......................................");
						break loop;
					}
					//print an empty line message while the lines are empty
				}
				
				
				SNode = Statement();
				
				if(SNode != null) { 
				list.add(SNode);
				//System.out.println(SNode);
				
				//System.out.println(Statements());//calling statements()method and printing its value			
				//remove end line token if we reached the end of a line
				if (matchAndRemove(tokenType.EndOfLine) == null) 				
					throw new Exception ("Invalid input");//throw exception message			
				}
			}
			
		return new StatementsNode(list);
	}
	
	
	
	/**
	 * method that returns a statement node or throws an
	 * exception if a statement is not found
	 * @return
	 * @throws Exception
	 */
	public static Node Statement() throws Exception {
		
		boolean isLabeledStatement = false;
		
		Token tok = matchAndRemove(tokenType.LABEL);
		if(tok != null) {
			isLabeledStatement = true;
		}
		
		
	     //if the next statement is a print statement
		 StatementNode PSNode = printStatement();		 
		 if(PSNode != null) {
			 if(isLabeledStatement == false) {
			 return PSNode;	} 
			 else {
				 return new LabeledStatementNode(tok.getTokenValue(), PSNode);
			 }
		 }	 
		//if the next statement is an assignment
		 StatementNode AsNode = assignement();
		 if(AsNode != null) {
			 if(isLabeledStatement == false) {
				 return AsNode;	} 
			 else {
				 return new LabeledStatementNode(tok.getTokenValue(), AsNode);
			 }
		 }	
		 
		//if the next statement is a read statement
		 StatementNode RSNode = readStatement();
         if(RSNode != null) {
			 if(isLabeledStatement == false) {
				 return RSNode;	} 
			 else {
				 return new LabeledStatementNode(tok.getTokenValue(), RSNode);
			 }
		 }
         
       //if the next statement is a DATA statement
         StatementNode DSNode = dataStatement();
         if(DSNode != null) {
			 if(isLabeledStatement == false) {
				 return DSNode;	} 
			 else {
				 return new LabeledStatementNode(tok.getTokenValue(), DSNode);
			 }
		 }
         
       //if the next statement is an INPUT statement
         StatementNode ISNode = inputStatement();
         if(ISNode != null) {
			 if(isLabeledStatement == false) {
				 return ISNode;	} 
			 else {
				 return new LabeledStatementNode(tok.getTokenValue(), ISNode);
			 }
		 }                 
         
         //if the next statement is a GOSUB statement
         StatementNode GSNode = GosubStatement();
         if(GSNode != null) {
			 if(isLabeledStatement == false) {
				 return GSNode;	} 
			 else {
				 return new LabeledStatementNode(tok.getTokenValue(), GSNode);
			 }
		 }   
         
       //if the next statement is a Return statement 
         StatementNode ReturnSNode = ReturnStatement();
         if(ReturnSNode != null) {
        	 if(isLabeledStatement == false) {
        		 return ReturnSNode;	} 
        	 else {
        		 return new LabeledStatementNode(tok.getTokenValue(), ReturnSNode);
        	 }
         }
         
         //if the next statement is a For statement
         StatementNode FSNode = ForStatement();
         if(FSNode != null) {
        	 if(isLabeledStatement == false) {
        		 return FSNode;	} 
        	 else {
        		 return new LabeledStatementNode(tok.getTokenValue(), FSNode);
        	 }
         }   
         
         //if the next statement is a next statement
         StatementNode NSNode = NextStatement();
         if(NSNode != null) {
        	 if(isLabeledStatement == false) {
        		 return NSNode;	} 
        	 else {
        		 return new LabeledStatementNode(tok.getTokenValue(), NSNode);
        	 }
         }  
         
         //if the next statement is an if statement
         StatementNode IfSNode = IfStatement();
         if(IfSNode != null) {
        	 if(isLabeledStatement == false) {
        		 return IfSNode;	} 
        	 else {
        		 return new LabeledStatementNode(tok.getTokenValue(), NSNode);
        	 }
         }  
                
         return null;       
	}
	
	
	/**
	 * method that returns a print node
	 * @return print node that holds a list
	 * @throws Exception
	 */
	public static StatementNode printStatement() throws Exception {
	
		ArrayList<Node> list = new ArrayList<Node>();
		
		if(matchAndRemove(tokenType.PRINT) != null) {
			list = printList();		
		}else {
			return null;
		}
		
		return new PrintNode(list);
	}
	
	
	
	/**
	 * method that returns a list of comma separated expressions or throws
	 * an exception if the list is empty
	 * @return list of comma separated list of expressions
	 * @throws Exception
	 */
	public static ArrayList<Node> printList() throws Exception {
		Node node;
		ArrayList<Node> list = new ArrayList<Node>();

		do {
			node = Expression();
			if(node != null) {
				list.add(node);
			}else {
				node = String();
				
				if(node != null) {
					list.add(node);
				}else {
					throw new Exception ("invalid input");//display error message
				}
			}
		}while(matchAndRemove(tokenType.COMMA) != null);
		
		
		//if list is empty display error message
		if(list.isEmpty()) 
			throw new Exception ("invalid input");
		
		return list;
	}
	
	
	/**
	 * method that returns an assignment node or throws an exception if it wasn't found
	 * @return assignment node
	 * @throws Exception
	 */
	public static StatementNode assignement() throws Exception {
		//declaring variables
		Node node;
		VariableNode VNode;
		String identifierName=null;
		
		Token aTok = matchAndRemove(tokenType.IDENTIFIER);
		if(aTok != null)
		identifierName = aTok.getTokenValue();		
		
		if(aTok != null) {
			if(matchAndRemove(tokenType.EQUALS) != null){
				
				
				///////////////////
				Token strTok = matchAndRemove(tokenType.STRING);
				if(strTok != null){
					StringNode strNode = new StringNode(strTok.getTokenValue());
					VNode = new VariableNode(identifierName);
					return new AssignmentNode(VNode, strNode);
				}
				///////////////////
				
				
				node = Expression();
                if(node != null) {
                	
                	VNode = new VariableNode(identifierName);
                	return new AssignmentNode(VNode, node);
                }
			}else {
				throw new Exception ("invalid input");//display error message
			}
		}
		return null;	
	}
	
	
	/*
	 * method that checks if the next token is a string
	 * token if so it returns a string node else it 
	 * returns null
	 */
	public static Node String() {
		
		Token aTok = matchAndRemove(tokenType.STRING); 
		
		if(aTok == null) {
			return null;
		}else
			return new StringNode(aTok.getTokenValue());
	}
		
	
	/**
	 * method that returns a read node
	 * @return read node that holds a list
	 * @throws Exception
	 */
	public static StatementNode readStatement() throws Exception {
	
		ArrayList<VariableNode> list = new ArrayList<VariableNode>();
		
		if(matchAndRemove(tokenType.READ) != null) {
			list = readList();//get the read list from the readList method		
		}else {
			return null;
		}
		
		return new ReadNode(list);
	}
	
	
	
	/**
	 * method that returns a list of comma separated variables or throws
	 * an exception if the list is empty
	 * @return list of comma separated list of expressions
	 * @throws Exception
	 */
	public static ArrayList<VariableNode> readList() throws Exception {
		VariableNode node;
		ArrayList<VariableNode> list = new ArrayList<VariableNode>();

		//do while loop that iterates if the next element in the array is a comma
		do {
			Token aTok = matchAndRemove(tokenType.IDENTIFIER);
			//if the next element is a variable create a variable node and add it to the list
            if(aTok != null) {				
            	node = new VariableNode(aTok.getTokenValue());
            	list.add(node);            	
			}else//throw an exception
				throw new Exception ("invail input");//print error message
		}while(matchAndRemove(tokenType.COMMA) != null);
		
		if(list.isEmpty()) 
			throw new Exception ("invalid input");//display error message if the list is empty
		
		return list;
	}
	
	
	
	/**
	 * method that returns a data node
	 * @return read node that holds a list
	 * @throws Exception
	 */
	public static StatementNode dataStatement() throws Exception {
	
		ArrayList<Node> list = new ArrayList<Node>();
		
		if(matchAndRemove(tokenType.DATA) != null) {
			list = dataList();// get the data list from the dataList method
		}else {
			return null;
		}
	
		if(list == null)
			throw new Exception ("invalid input");//display error message if the list is empty

		return new DataNode(list);
	}
	
	
	
	/**
	 * method that returns a list of comma separated strings, integers, floats or throws
	 * an exception if the list is empty
	 * @return list of comma separated list of strings, integers, floats
	 * @throws Exception
	 */
	public static ArrayList<Node> dataList() throws Exception {
		Node node;
		ArrayList<Node> list = new ArrayList<Node>();
      
	do {
		Token nTok = matchAndRemove(tokenType.NUMBER);
        if(nTok != null) 
        {		
        	//check if the number is an integer or a float and creating a node corresponding to the type
        	//and adding it to the list 
        	try {
                int num = Integer.parseInt(nTok.getTokenValue());
                node = new IntegerNode(num);
            } catch (Exception e) {
            	float num = Float.parseFloat(nTok.getTokenValue());
            	node = new FloatNode(num);
            }        	
        	list.add(node);            	
		}else{		
			//if the next element is a string create a string node and add it to the list
			Token sTok = matchAndRemove(tokenType.STRING);
            if(sTok != null) {	
        	   node = new StringNode(sTok.getTokenValue());
        	   list.add(node);            	
	    	}else
	    		throw new Exception ("invalid input");//display error message
		}
	}while(matchAndRemove(tokenType.COMMA) != null);	

		return list;
	}
	

	
	/**
	 * method that returns a input node
	 * @return read node that holds a list
	 * @throws Exception
	 */
	public static StatementNode inputStatement() throws Exception {
	
		ArrayList<Node> list = new ArrayList<Node>();
		
		if(matchAndRemove(tokenType.INPUT) != null) {
			list = inputList();//get the input list from the input list method		
		}else {
			return null;
		}
	
		return new InputNode(list);
	}
	
	
	
	/**
	 * method that returns a list of comma separated expressions or throws
	 * an exception if the list is empty
	 * @return list of comma separated list of expressions
	 * @throws Exception
	 */
	public static ArrayList<Node> inputList() throws Exception {
		Node node;
		ArrayList<Node> list = new ArrayList<Node>();
		
		/*check if the first element is a string or a variable and create 
		 * a corresponding node and add it to the list */
		if(/*(match(tokenType.IDENTIFIER) == true) ||*/ (match(tokenType.STRING) == true)){
			
			Token iTok = matchAndRemove(tokenType.IDENTIFIER);
			if(iTok != null) {
				node = new VariableNode(iTok.getTokenValue());
				list.add(node);
				
			}else{
				Token sTok = matchAndRemove(tokenType.STRING);
				if(sTok != null) {
					node = new StringNode(sTok.getTokenValue());
					list.add(node);
				}					
			}
		}
		
		// while loop that gets the list of comma separated variables or throws an exception otherwise
		while(matchAndRemove(tokenType.COMMA) != null) {
			Token vTok = matchAndRemove(tokenType.IDENTIFIER);
			
			if(vTok != null) {
				Node vNode = new VariableNode(vTok.getTokenValue());
				list.add(vNode);
			}else
				throw new Exception ("invalid input");//display error message
			
		}
		
		return list;
	}	
	
	
	
	/**
	 * method that returns a gosubNode
	 * @return GOSUB node or null
	 * @throws Exception
	 */
	public static StatementNode GosubStatement() throws Exception {
		//variables declarations
		String label = null;
		Token lTok;
		
		//check if the next token is a GOSUB token
		if(matchAndRemove(tokenType.GOSUB) != null) {
			//check if the next token is a LABEL token
			if(match(tokenType.LABEL)) {
				lTok = matchAndRemove(tokenType.LABEL);//store the label token in the lTok variable 	
				label = lTok.getTokenValue();//store the label token in the label string
				return new GosubNode(label);//return a GOSUB Node
			}else
				throw new Exception ("invalid input"); //print error message
		}		
		return null;
	}
	
	
	
	/**
	 * method that returns a return Node
	 * @return RETURN Node or null
	 * @throws Exception
	 */
	public static StatementNode ReturnStatement() throws Exception {
        //check if the next token is a RETURN token and return a return node else return null
		if(matchAndRemove(tokenType.RETURN) != null) {
			return new ReturnNode();
		}else
			return null;
	}
	
	
	
	/**
	 * Method that returns a forNode
	 * @return forNode or null
	 * @throws Exception
	 */
	public static StatementNode ForStatement() throws Exception {
		
		//variable initializations
		VariableNode varNode;
		varNode = null;
		Token vtok = null; 
		IntegerNode initial, limit, increment;

		
            //check if the first token is a for token return null if not
			if(matchAndRemove(tokenType.FOR) == null) {
				return null;
			}
			
            //check if the next token in an identifier and throw an error if not
			if(match(tokenType.IDENTIFIER)) {
				vtok = matchAndRemove(tokenType.IDENTIFIER);
				varNode = new VariableNode(vtok.getTokenValue());//store the variable value in a variable node		
			}else
				throw new Exception ("invalid For loop input"); //print error message
			
			//check if the next token in an equals and throw an error if not
			if(matchAndRemove(tokenType.EQUALS) == null) {
				throw new Exception ("invalid For loop input"); //print error message
			}
			
			//call the for helper to get the initial value
			initial = forHelper(); //store the initial value in an Integer node
			if(initial == null) {
				throw new Exception ("invalid For loop input"); //print error message
			}
			
			//check if the next token in an TO and throw an error if not
			if(matchAndRemove(tokenType.TO) == null) {
				throw new Exception ("invalid For loop input"); //print error message
			}
			
			//call the for helper to get the limit value
			limit = forHelper();//store the limit value in an Integer node
			if(limit == null) {
				throw new Exception ("invalid For loop input"); //print error message
			}
            
			//check if the next token in an endOfLine and if so, return a forNode with increment value of 1
			if(match(tokenType.EndOfLine)) {
				return new ForNode(varNode,initial, limit, new IntegerNode(1));
			}
			
			//check if the next token in a STEP and throw an error if not
			if(matchAndRemove(tokenType.STEP) == null) {
				throw new Exception ("invalid For loop input");
			}
			
			//call the for helper to get the limit value
			increment = forHelper();//store the increment value in an Integer node
			if(increment == null) {
				throw new Exception ("invalid For loop input"); //throw error message
			}
			
		return new ForNode(varNode,initial, limit, increment);
	}	
	
	
	
	/**
	 * method that returns a nextNode
	 * @return a nextNode or null
	 * @throws Exception
	 */
	public static StatementNode NextStatement() throws Exception {
		//variable declaration
		VariableNode incrementedVar;
		Token vtok;
		
		//check if the next token is a NEXT token else return null
		Token nextTok = matchAndRemove(tokenType.NEXT);
		if(nextTok == null) 
			return null;
		
		//check if the next token is an IDENTIFIER else throw an error message
		if(match(tokenType.IDENTIFIER)) {
			vtok = matchAndRemove(tokenType.IDENTIFIER); //store the token in the token variable 
			incrementedVar = new VariableNode(vtok.getTokenValue()); //store the variable value in a variable node	
		}else
			throw new Exception ("invalid input"); //print error message
		
		return new NextNode(incrementedVar);
	}
	
	
	
	
	/**
	 * method that returns a ifNode
	 * @return a nextNode or null
	 * @throws Exception
	 */
	public static StatementNode IfStatement() throws Exception {
		BooleanOperationNode booleanOp;
		
		if(matchAndRemove(tokenType.IF) == null) {
			return null;
		}
		
		booleanOp = BooleanExpression();
		
		if(booleanOp == null) {
			throw new Exception ("invalid if statement input"); //print error message
		}
		
		
		if(matchAndRemove(tokenType.THEN) == null) {
			throw new Exception ("invalid if statement input"); //print error message
		}
		
		Token label = matchAndRemove(tokenType.LABEL);
		
		if(label == null) {
			throw new Exception ("invalid if statement input"); //print error message
		}
		
		return new IfNode(booleanOp, label);
		
	}   
	
	
	
	/**
	 * method that returns a booleanOperation node
	 * @return a nextNode or null
	 * @throws Exception
	 */
	public static BooleanOperationNode BooleanExpression() throws Exception {
		
		Node Expression1, Expression2 ;
				
		Expression1 = Expression();
		
		if(Expression1 == null) {
			throw new Exception("invalid if statement input");
		}
		
		BooleanOperations op = Operator();
		if(op == null) {
			throw new Exception("invalid if statement input");
		}
						
		Expression2 = Expression();
		
		if(Expression2 == null) {
			throw new Exception("invalid if statement input");
		}
		
		

		return new BooleanOperationNode(op, Expression1, Expression2);
	}
	
	
	
	
	
	/**
	 * method that checks if the nexty line is a function
	 * @return functionNode
	 * @throws Exception
	 */
	public static Node Functions() throws Exception {
		
		ArrayList<Node> parameterList = new ArrayList<Node>();		
		Token functionName = matchAndRemove(tokenType.FUNCTIONNAME);
		
		//if the first token is not a function name return null 
		if(functionName == null) {
			return null;
		}
		
		//////////////////////////////////////////////////
		
		
		//if the first token is not a left parenthesis an exception is thrown
		if(matchAndRemove(tokenType.LPAREN) == null) {
			throw new Exception("invalid function"); // throw error message 
		}
		
		//if the first token is a right parenthesis a function with no parameters is returned
		if(matchAndRemove(tokenType.RPAREN) != null) {
			return new FunctionNode(functionName.getTokenValue(), parameterList);
		}
		
		//loop that gets parameters(strings and expressions) and iterates until a right parenthesis is encountered
		while (match(tokenType.RPAREN) != true) {
			Token stringParam = matchAndRemove(tokenType.STRING);
			if(stringParam != null) {
				parameterList.add(new StringNode(stringParam.getTokenValue()));
			}else {
				Node expressionParam = Expression();
				if(expressionParam == null) {
					throw new Exception("invalid function"); // throw error message
				}else {
					parameterList.add(expressionParam);
				}
			}
			//if the next token is not a comma break out of the loop
			if(matchAndRemove(tokenType.COMMA) == null) {
				break; 
			}			
		}
		
		//if the next token is not a right parenthesis throw an error
		if(matchAndRemove(tokenType.RPAREN) == null) {
			throw new Exception("invalid function"); // throw error message 
		}else {
			return new FunctionNode(functionName.getTokenValue(), parameterList);
		}
	}
		///////////////////////////^^^^^^^^^^^^^^^^^^^^^^^^^^^////////////////////////////////////////////
		/*if(matchAndRemove(tokenType.LPAREN) == null) {
			throw new Exception("invalid function"); // throw error message 
		}
		
		if(functionName.getTokenValue().equals("RANDOM")) {
			
			//parameterList = null;
			
			if(matchAndRemove(tokenType.RPAREN) == null) {
				throw new Exception("invalid function"); // throw error message 
			}
			
			return new FunctionNode(functionName.getTokenValue(), parameterList);
			
		}else
		
		
		if(functionName.getTokenValue().equals("LEFT$") || functionName.getTokenValue().equals("RIGHT$")) {
			
			IntegerNode intParam;
			StringNode stringParam;

			
			Token firstParameter = matchAndRemove(tokenType.STRING);
			if(firstParameter == null) {
				throw new Exception("invalid LEFT$/RIGHT function input");
			}
			stringParam = new StringNode(firstParameter.getTokenValue());
			
			if(matchAndRemove(tokenType.COMMA) == null){
				throw new Exception("invalid LEFT$/RIGHT function input");
			}
			
			Token secondParameter = matchAndRemove(tokenType.NUMBER);
			if(secondParameter == null) {
				throw new Exception("invalid LEFT$/RIGHT function input");
			}else {
				try {
					int intValue = Integer.parseInt(secondParameter.getTokenValue());
					intParam = new IntegerNode(intValue);
				}catch(NumberFormatException e){				
					throw new Exception ("invalid LEFT$/RIGHT second parameter input");//throw error message
				}		
			}
			
			if(matchAndRemove(tokenType.RPAREN) == null) {
				throw new Exception("invalid LEFT$/RIGHT function"); // throw error message 
			}
			
			
			parameterList.add(stringParam);
			parameterList.add(intParam);
			
			return new FunctionNode(functionName.getTokenValue(), parameterList);
		}else
		
		if(functionName.getTokenValue().equals("MID$")) {
			
			IntegerNode intParam1,intParam2;
			StringNode stringParam1;
			
			Token firstParameter = matchAndRemove(tokenType.STRING);
			if(firstParameter == null) {
				throw new Exception("invalid LEFT$/RIGHT function input");
			}
			stringParam1 = new StringNode(firstParameter.getTokenValue());
			
			if(matchAndRemove(tokenType.COMMA) == null){
				throw new Exception("invalid LEFT$/RIGHT function input");
			}
			
			Token secondParameter = matchAndRemove(tokenType.NUMBER);
			if(secondParameter == null) {
				throw new Exception("invalid LEFT$/RIGHT function input");
			}else {
				try {
					int intValue = Integer.parseInt(secondParameter.getTokenValue());
					intParam1 = new IntegerNode(intValue);
				}catch(NumberFormatException e){				
					throw new Exception ("invalid LEFT$/RIGHT second parameter input");//throw error message
				}		
			}
			
			if(matchAndRemove(tokenType.COMMA) == null){
				throw new Exception("invalid LEFT$/RIGHT function input");
			}
			
			Token thirdParameter = matchAndRemove(tokenType.NUMBER);
			if(thirdParameter == null) {
				throw new Exception("invalid LEFT$/RIGHT function input");
			}else {
				try {
					int intValue2 = Integer.parseInt(thirdParameter.getTokenValue());
					intParam2 = new IntegerNode(intValue2);
				}catch(NumberFormatException e){				
					throw new Exception ("invalid LEFT$/RIGHT second parameter input");//throw error message
				}		
			}
			
			if(matchAndRemove(tokenType.RPAREN) == null) {
				throw new Exception ("invalid VAL/VAL% function input");
			}
			
			//parameterList = null;
			parameterList.add(stringParam1);
			parameterList.add(intParam1);
			parameterList.add(intParam2);
			
			return new FunctionNode(functionName.getTokenValue(), parameterList);
		}else
		
		
		if(functionName.getTokenValue().equals("NUM$")) {
			
			IntegerNode ifIntParam = null;
			FloatNode ifFloatParam = null;
			Token Parameter = matchAndRemove(tokenType.NUMBER);
			if(Parameter == null) {
				throw new Exception("invalid LEFT$/RIGHT function input");
			}else {
				try {
					int ifIntValue = Integer.parseInt(Parameter.getTokenValue());
					ifIntParam = new IntegerNode(ifIntValue);
				}catch(NumberFormatException e){				
					float ifFloatValue = Float.parseFloat(Parameter.getTokenValue());
					ifFloatParam = new FloatNode(ifFloatValue);
				}		
			}
			///////////////////////////////////////////////this?????//
			if(matchAndRemove(tokenType.RPAREN) == null) {
				throw new Exception ("invalid VAL/VAL% function input");
			}
			
			//parameterList = null;
			if(ifIntParam != null) { 
			parameterList.add(ifIntParam);
			return new FunctionNode(functionName.getTokenValue(), parameterList);
			}else {
				parameterList.add(ifFloatParam);
				return new FunctionNode(functionName.getTokenValue(), parameterList);
			}
		}else
		
		if((functionName.getTokenValue().equals("VAL")) || (functionName.getTokenValue().equals("VAL%"))) {
			
			StringNode stringParam;
			Token stringToken = matchAndRemove(tokenType.STRING);
			if(stringToken == null) {
				throw new Exception ("invalid VAL/VAL% function input");
			}
			
			stringParam = new StringNode(stringToken.getTokenValue());
			parameterList.add(stringParam);
			
			if(matchAndRemove(tokenType.RPAREN) == null) {
				throw new Exception ("invalid VAL/VAL% function input");
			}
			
			return new FunctionNode(functionName.getTokenValue(), parameterList);
		}
		else throw new Exception ("invalid function input");
	
	}
	
	*/

	////////////////////////////helper methods///////////////////////////////////

	
	/**
	 * method that checks if the next token is an operator
	 * if it is it returns it else it returns null
	 */
	public static BooleanOperations/*Token*/ Operator () throws Exception {	
		
		if((match(tokenType.EQUALS) == true)) {
			/*return*/ matchAndRemove(tokenType.EQUALS);
			return BooleanOperations.EQUALS;
			
		}
		else if((match(tokenType.LESSTHAN) == true)) {
			matchAndRemove(tokenType.LESSTHAN);
			return BooleanOperations.LESSTHAN;
			
		}
		else if((match(tokenType.GREATERTHAN) == true)) {
			matchAndRemove(tokenType.GREATERTHAN);
			return BooleanOperations.GREATERTHAN;
			
		}
		else if((match(tokenType.LESSTHANOREQUALS) == true)) {
			matchAndRemove(tokenType.LESSTHANOREQUALS);
			return BooleanOperations.LESSTHANOREQUALS;
			
		}
		else if((match(tokenType.GREATERTHANOREQUALS) == true)) {
			matchAndRemove(tokenType.GREATERTHANOREQUALS);
			return BooleanOperations.GREATERTHANOREQUALS;
		}
		else if((match(tokenType.NOTEQUALS) == true)) {
			matchAndRemove(tokenType.NOTEQUALS);
			return BooleanOperations.NOTEQUALS;
		}else		
			return null;
	}
	
	
	
	
	/**
	 * method that checks if the next token is a number token that holds an integer and returns it
	 * else it returns null
	 * @return integerNode
	 * @throws Exception if the number is not an integer
	 */
	public static IntegerNode forHelper () throws Exception {	
		IntegerNode number;
		Token numTok = matchAndRemove(tokenType.NUMBER);
		if(numTok != null) {				
			try {
				int intValue = Integer.parseInt(numTok.getTokenValue());
				number = new IntegerNode(intValue);
				return number;
			}catch(NumberFormatException e){				
				throw new Exception ("invalid FOR input");//throw error message
			}				
		}else
			return null;
	}
		

	/**
	 * method that checks if the first tokenType of the element in the list matches  
	 * the tokenType passed as a parameter                          
	 * @param x
	 * @return true or false
	 */
	public static boolean match (tokenType x) {		
		Token firstToken = tokensList.get(0);  
		if(firstToken.tokenType == x) 
			return true;
		else return false;
	}
	
	
	/** 
	 * method that checks if the first tokenType of the element in the list matches 
	 * the tokenType passed as a parameter and if so it removes the first element and 
	 * returns it 
	 * @param x
	 * @return the token or null
	 */
	public static Token matchAndRemove(tokenType x) {
		Token firstToken;
		if(!tokensList.isEmpty()) {
		firstToken = tokensList.get(0); 
		}else
			return null;
		
		if(firstToken.tokenType == x) { 
			tokensList.remove(0);
			return firstToken;
		}
		else return null;
	}	
}