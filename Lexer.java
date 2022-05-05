package ass1;

import java.util.ArrayList;
import java.util.HashMap;
import ass1.Token.tokenType;


public class Lexer {
	
	//create an ArrayList
    public static ArrayList<Token> tokens = new ArrayList<Token>();
	
	public static ArrayList<Token> lex(String input) {
		
		//create a HashMap
		HashMap<String, tokenType> HM = new HashMap<String, tokenType>();
		HM.put("PRINT", tokenType.PRINT); //store the print token and its value in the hashMap
		HM.put("READ", tokenType.READ); //store the READ token and its value in the hashMap
		HM.put("DATA", tokenType.DATA); //store the DATA token and its value in the hashMap
		HM.put("INPUT", tokenType.INPUT);
		HM.put("GOSUB", tokenType.GOSUB);
		HM.put("RETURN", tokenType.RETURN);
		HM.put("FOR", tokenType.FOR);
		HM.put("TO", tokenType.TO);
		HM.put("STEP", tokenType.STEP);
		HM.put("NEXT", tokenType.NEXT);
		
		HM.put("IF", tokenType.IF);
		HM.put("THEN", tokenType.THEN);
		
		HM.put("RANDOM", tokenType.FUNCTIONNAME);		
		HM.put("LEFT$", tokenType.FUNCTIONNAME);
		HM.put("RIGHT$", tokenType.FUNCTIONNAME);
		HM.put("MID$", tokenType.FUNCTIONNAME);
		HM.put("NUM$", tokenType.FUNCTIONNAME);
		HM.put("VAL", tokenType.FUNCTIONNAME);
		HM.put("VAL%", tokenType.FUNCTIONNAME);
        
		
		
		//create an ArrayList
        //ArrayList<Token> tokens = new ArrayList<Token>();
	         
        int state = 0;
        String s ="";
        int numOfDecP = 0;
        
        //for loop that iterates for every character of the input string
        loop:
        for(int i = 0; i < input.length(); i++) {
        	
        	//set currentCharacter to the corresponding value
        	char currentCharacter = input.charAt(i);

        	//state machine that creates tokens based on the characters
        	switch(state) {
        	//if the state is 0 
        	case 0:
        		//if the state is 0 this statement sets the state based on the current character
        		if(currentCharacter == ' ') state = 0;
        		else if(Character.isDigit(currentCharacter)) {
        			s += currentCharacter;
        			state = 1;
        			//if the current character is the last one in the line
        			if(i == (input.length()-1) ) {
            				tokens.add(new Token(tokenType.NUMBER, s)); //create a new number token
            				state = 0; //set the state back to 0
            				s = "";
            		}        		 	
        		}
        		else if(currentCharacter == '.') {s += currentCharacter;state = 2;}//save the current character to saving string, and set state to 2     		       		        		
        		else if(currentCharacter == '+') {i--; state = 4;}  //decrement counter, the character will be accounted for in the set state 
        		else if(currentCharacter == '-') {i--; state = 5;}  //same as previous line
        		else if(currentCharacter == '*') {i--; state = 6;}  //same as previous line
        		else if(currentCharacter == '/') {i--; state = 7;}  //same as previous line
        		else if(currentCharacter == '<') {i--; state = 8;}  //same as previous line
        		else if(currentCharacter == '>') {i--; state = 9;}  //same as previous line
        		else if(currentCharacter == '=') {i--; state = 10;} //same as previous line      		
        		else if(currentCharacter == '(') {i--; state = 11;} //same as previous line
        		else if(currentCharacter == ')') {i--; state = 12;} //same as previous line     		
        		else if(currentCharacter == '"') {s += currentCharacter; state = 13;} //        			        		
        		else if(Character.isLetter(currentCharacter)) {/*s += currentCharacter*/ i--; state = 14; } //
        		else if(currentCharacter == ',') {i--; state = 15;}
        		else state = -1;
        		break;
        	
        	//if the state is 1
        	case 1:
        		//if the character is a digit save it to the string and keep the state at 1
        		if(Character.isDigit(currentCharacter)) {
        			s += currentCharacter;
        			state = 1;
        		}
        		/*if the character is a decimal point and decrement counter,
        		  the character will be accounted for in the set state*/
        		else if(currentCharacter == '.') {
        			i--;
        			state = 3;
        		}
        		/*else add a number token with the string s as the value
        		 * set the state back to 0, empty the saving string s*/
        		else {
        			tokens.add(new Token(tokenType.NUMBER, s));
        			i--;
        			state = 0;
        			s = "";
        		}
        		/*if we reached the end, add a number token with the string s as the value
        		  set the state back to 0, empty the saving string s*/
        		if(i == (input.length()-1) ) {
        				//tokens.add(new Token(tokenType.NUMBER, Character.toString(currentCharacter)));ERROR
        				tokens.add(new Token(tokenType.NUMBER, s));
        				state = 0;
            			s = "";
        		}       		
        		break;
    
        	// if the state is 2	
        	case 2:
        		//if the character is a digit add it to the string and keep the state at 2
        		if(Character.isDigit(currentCharacter)) {
        			s += currentCharacter;
        			state = 2;
        		}
        		//else create a number token, decrement counter, set state back to 0
        		else {
        			tokens.add(new Token(tokenType.NUMBER, s));
        			i--;
        			state = 0;
        			s=""; 
        		}
        		//if we reached the end of the input string create a number token
        		if(i == (input.length()-1) ) {
        			tokens.add(new Token(tokenType.NUMBER, s));
        		}
        		break;
 
        	//if the state is 3	
        	case 3:   
        		/*if the current character is a decimal point or a digit, and the number of decimal points 
        		 * is <= 1, add the character to the string and keep track of the number of '.'*/
        		if((currentCharacter == '.' || (Character.isDigit(currentCharacter))) && numOfDecP <= 1 ) {
        			s += currentCharacter;
        			if(currentCharacter == '.'){
        				numOfDecP++;} //increment counter      				       				
        			state = 3; //keep state at 3
        		}
        		//if the number of '.' is more than 1 print an error message and break the loop
        		else if(numOfDecP > 1) {
        			System.out.println("invalid input");
        			break loop;
        		}
        		/*else create a number token, decrement counter, set state to 0,
        		 empty saving string and set the '.' counter to 0 */
        		else {
        			tokens.add(new Token(tokenType.NUMBER, s));
        			i--;
        			state = 0;
        			s = ""; 
        			numOfDecP = 0;
        		}
        		//if we reached the end create a number token and set the '.'counter to 0  
        		if(i == (input.length()-1) ) {
        			tokens.add(new Token(tokenType.NUMBER, s));
        			numOfDecP = 0;
        		}
        		break;
        		
        	//if the state is 4	
        	case 4:	
        		/*if the + sign is followed by a space create a PLUS token else  if its followed 
        		  by a digit assume its just a sign and add it to the saving string*/
        		if(currentCharacter == '+') {
        			if(i <= input.length()-2 ) {
        			    char nextCharacter = input.charAt(i+1);
        			    if(Character.isDigit(nextCharacter) ) {
        				    s += currentCharacter;      				
        			        state = 0;
        			        }else
        			        {
        			          tokens.add(new Token(tokenType.PLUS, Character.toString(currentCharacter)));
        				      state = 0;
        			        }        			
                    }else {
        				tokens.add(new Token(tokenType.PLUS, Character.toString(currentCharacter)));
        				}        			            			
                    } 
        		break;	
        	
        	//if the state is 5	
        	case 5:  
        		/*if the - sign is followed by a space create a PLUS token else  if its followed 
      		     by a digit assume its just a sign and add it to the saving string*/
        		if(currentCharacter == '-') {       			
        			if(i <= input.length()-2 ) {
            			    char nextCharacter = input.charAt(i+1);
            			    if(Character.isDigit(nextCharacter) ) {
            				    s += currentCharacter;            				
            			        state = 0;
            			        }else
            			        {
            			          tokens.add(new Token(tokenType.MINUS, Character.toString(currentCharacter)));
            				      state = 0;            				      
            			        }
            			
                        }else {
            				tokens.add(new Token(tokenType.MINUS, Character.toString(currentCharacter)));
            				state = 0;           				
            			}
                      } 
            		break;	
        			
            //if the state is 6		
        	case 6:	
        		//create a TIMES token
           		if(currentCharacter == '*') {
        			tokens.add(new Token(tokenType.TIMES, Character.toString(currentCharacter)));
        			state = 0; //set state back to 0
        		}
           		break;
           		
           	//if the state is 7	
        	case 7:	
        		//create a DIVIDE token
        		if(currentCharacter == '/') {
        			tokens.add(new Token(tokenType.DIVIDE, Character.toString(currentCharacter)));
        			state = 0;
        		}
        		break;
        		
        	//if the state is 8	
        	case 8:
        		/*if the '<' is followed by a '=' create a LESSTHANOREQUALS token
        		 *if the '<' is followed by a '>' create a NOTEQUALS token
        		 *else create a LESSTHAN token*/       		
         		if(currentCharacter == '<') {
        			
        			String sign = "";
        			sign += currentCharacter;
        			char nextChar = input.charAt(i+1);
        			
        			if(nextChar == '=') {     				
        				sign += nextChar;
        				tokens.add(new Token(tokenType.LESSTHANOREQUALS, sign));
        				state =0; //set state back to 0
        				i++;
        			}else if(nextChar == '>'){
        				sign += nextChar;
        				tokens.add(new Token(tokenType.NOTEQUALS, sign));
        				state =0; //set state back to 0
        				i++;
        			}else
        			{
        				tokens.add(new Token(tokenType.LESSTHAN, sign));
        				state =0; //set state back to 0
        			}
        		}
        		break;
        		
        	//if the case is 9	
        	case 9:	
        		/*if the '>' is followed by a '=' create a GREATERTHANOREQUALS token
        		 *else create a LESSTHAN token*/ 
        		if(currentCharacter == '>') {
        			
        			String sign = "";
        			sign += currentCharacter;
        			char nextChar = input.charAt(i+1);
        			
        			if(nextChar == '=') {     				
        				sign += nextChar;
        				tokens.add(new Token(tokenType.GREATERTHANOREQUALS, sign));
        				state = 0; //set state back to 0
        				i++;
        			}else {
        				tokens.add(new Token(tokenType.GREATERTHAN, sign));
        				state = 0; //set state back to 0
        			}
        		}
        		break;
        		
        	//if the state is 10	
        	case 10:
        		//create an EQUALS token
        		if(currentCharacter == '=') {
        			tokens.add(new Token(tokenType.EQUALS, Character.toString(currentCharacter)));
        			state=0; //set state back to 0
        		}
        		break;
        		
        	//if the state is 11	
        	case 11:
        		//create a LPAREN token
        		if(currentCharacter == '(') {
        			tokens.add(new Token(tokenType.LPAREN, Character.toString(currentCharacter)));
        			state=0;
        		}
        		break;
        		
        	//if the state is 12	
        	case 12:
        		//create a RPAREN token
        		if(currentCharacter == ')') {
        			tokens.add(new Token(tokenType.RPAREN, Character.toString(currentCharacter)));
        			state=0;
        		}
        		break;
        		
        	//if the state is 13	
        	case 13:
        		// add the '"' character and keep adding characters until another '"' is encountered
        		s += currentCharacter;
        		state = 13; //keep state at 13
        		if(currentCharacter == '"') {
        			state = 0; //set state to 0
        			tokens.add(new Token(tokenType.STRING, s));
        			s = ""; //empty string
        		}
        		break;
        		
        	//if the state is 14
        	case 14:
        		/*if the word ends with '$' or '%' create an IDENTIFIER token if the word is not recognized
        		 else if it is create its token
        		 * if the word ends with ':' create a LABEL token*/  
        		
        		//if((s.equals("LEFT")) || (s.equals("RIGHT")) || (s.equals("LEFT")) )
        		
        		
        		if((currentCharacter == '$') && ((s.equals("LEFT")) || (s.equals("RIGHT")) || (s.equals("MID"))  || (s.equals("NUM"))))
        		{
        			s += currentCharacter;	
        			if(HM.containsKey(s)) {      				
       					tokens.add(new Token(HM.get(s), s));
       					s = "";
       					state = 0;
        			}
        		}
        		
        		else if((currentCharacter == '%') && (s.equals("VAL")))
        		{
        			s += currentCharacter;	
        			if(HM.containsKey(s)) {      				
       					tokens.add(new Token(HM.get(s), s));
       					s = "";
       					state = 0;
        			}
        		}
        		
        		else if((s.equals("VAL")  || s.equals("RANDOM")) /*&&*/  ) {
        			if(currentCharacter == '(' || currentCharacter == ' ') {
        				tokens.add(new Token(tokenType.FUNCTIONNAME, s));
        				s = "";
        				state = 0;
        				if(currentCharacter == '(')
        				i--;
        				}
        		}
        		
        		
        		else if((currentCharacter == '$') || (currentCharacter == '%')) {
        			if(HM.containsKey(s)) {
        				//s += currentCharacter;////////////////////////////////////////
       					tokens.add(new Token(HM.get(s), s));
       					s = "";
       					state = 0;
        				}else {
        				s += currentCharacter;
            			state = 0; //set state to 0           			
            			tokens.add(new Token(tokenType.IDENTIFIER, s));
            			s = "";// empty the string
            			}
            		}else if(currentCharacter == ':'){
            			state = 0; //set state to 0
            			tokens.add(new Token(tokenType.LABEL, s));
            			s = "";// empty string
            		}
        		
        		///////////////////////////////////
            		else if(currentCharacter == ' ' || i == (input.length()-1) /*|| currentCharacter == ',' */) {//
            			
            			//System.out.println(currentCharacter + " ooooo");
            			//System.out.println();
            			//System.out.println();
            			
            			/*if(currentCharacter != ',' ) {
            				i--; 
            				state = 0;
            			}*/
            			
            			if(currentCharacter != ' ' ) {
            				s += currentCharacter; 
            			}
            			
            			if(HM.containsKey(s)) {
            				tokens.add(new Token(HM.get(s), s));
           					s = "";
           					state = 0;
            			}else {
            			state = 0; //set state to 0           			
            			tokens.add(new Token(tokenType.IDENTIFIER, s));
            			s = "";// empty the string
            			}
            		}
        		
        		//////////////////////////////////////
            		
            		else {   
            			
            			//System.out.println(currentCharacter + " ppppppp");
            			s += currentCharacter;
            			state = 14; //keep state at 14
            		}
        		break;
        		
        	case 15:	
        		//create a TIMES token
           		if(currentCharacter == ',') {
        			tokens.add(new Token(tokenType.COMMA, Character.toString(currentCharacter)));
        			state = 0; //set state back to 0
        		}
           		break;	
        	}
        }
        
        //add an EndofLine token      
        tokens.add(new Token(tokenType.EndOfLine, null));
        
        //for loop to print the tokens
       /* for (Token eachToken : tokens){       	
     	   System.out.print(eachToken + " ");     	   
        }*/        
        //System.out.println();// return to line
      
        return tokens;
	}
	
	 public static ArrayList<Token> getList() {
	       return tokens;
	   }
}	