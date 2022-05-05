package ass1;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import ass1.Token.tokenType;


public class Basic 
{
	
	public static void main(String[] args) throws Exception
	   {
		//number of arguments
		int size = args.length;
		
		/*if statement that makes sure that there is only one 
		*argument and prints an error message otherwise */
		if (size != 1)
		{
			System.out.println("This program allows one and only one argument as a filename");
		}		
		 
		Path path = Paths.get(args[0]);
		
		// trying to read the lines of the file 
			try {
				List<String> lines = Files.readAllLines(path);
				//for loop that iterates for each line in the file
				for(String eachLine :lines)
				{
					//Calling the lex method from the Lexer class 
					Lexer.lex(eachLine);					
				}
				
				//for debugging
				 /*for (Token eachToken : Lexer.tokens){       	
		     	   System.out.print(eachToken + " ");
		     	   if(eachToken.tokenType == tokenType.EndOfLine) {
		     		   System.out.println();
		     	   }
		        }*/
				
				//System.out.println(Parser.parse());
				Parser.parse();				
			} // exception handling
			catch (IOException e) {
				// printing the error trace			
				e.printStackTrace();
				// error message
				System.out.println("An ERROR has accured");
			}	
	   }	
}
