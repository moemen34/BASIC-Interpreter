package ass1;

public class Token {
	
	// instance of an enum of token types
	public enum tokenType{
		PLUS, MINUS, TIMES, DIVIDE, NUMBER, EndOfLine, EQUALS, LESSTHAN, LESSTHANOREQUALS, GREATERTHAN, GREATERTHANOREQUALS,
		NOTEQUALS, LPAREN, RPAREN, STRING, IDENTIFIER, PRINT, LABEL, COMMA, READ, DATA, INPUT, GOSUB, RETURN, FOR, TO, STEP,
		NEXT,FUNCTIONNAME,IF,THEN;
	}
		//the tokens' type
		tokenType tokenType;
		//the tokens' value
		private String tokenValue;
		
		//token type accessor
		public tokenType getTokenType() {
			return tokenType;
		}
		//token value accessor
		public String getTokenValue() {
			return tokenValue;
		}
        
		/**
		 * constructor
		 * @param tokenType
		 * @param tokenValue
		 */
        public Token (tokenType type, String value) {
        	this.tokenType = type;
        	this.tokenValue = value;
        }
        
        @Override
        public String toString() {        	        	
        	if((tokenType != tokenType.NUMBER) && (tokenType != tokenType.LABEL) && (tokenType != tokenType.IDENTIFIER)
        			&& (tokenType != tokenType.STRING) && (tokenType != tokenType.FUNCTIONNAME) )
        	{
        		return   tokenType +"";
        	}
        	else 
            return   tokenType  + "(" + tokenValue + ")" ;
            
        }
}
