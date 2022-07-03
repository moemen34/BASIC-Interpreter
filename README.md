# BASIC-Interpreter
Interpreter
This is an intrpreter for a modified version of the programing language
BASIC. the Basic.java class has the main method that accepts one text
file as an argument. This text file includes a set of instructions, or 
program written in Basic syntax as follows:

Data Types:
Simple variables do not need to be defined. They are typed by their 
ending character:
$ = string		% = float		any other ending = integer

Example: myString$, percentOfPeople%, count

Structure:
BASIC doesn’t require any setup (like Java with defining a class). It is
literally just a list of commands for the computer to execute. 


GOTO beginning

Labels are more intuitive than line numbers; line numbers are also 
terrible when you want to edit your program and insert new lines in the 
middle. That is why I numbered the lines by 10 above.

In the version of BASIC that we are implementing, there are no user-defined 
functions. There are built-in functions, but you cannot make your own. 
You can have subroutines. A subroutine is like a function, but it doesn’t 
have parameters or return values. It uses global variables for communication.

Example:
FtoC: C = 5*(F-32)/9
RETURN

F=72
GOSUB FtoC
PRINT C

Flow Control
IF expression THEN label
	If expression is true, GOTO label
	Example: IF x<5 THEN xIsSmall

FOR variable = initialValue TO limit STEP increment
NEXT variable Sets variable to initialValue, loops by adding increment to 
variable on each loop until limit is hit/surpassed. Note that the “STEP” and
increment is optional – the step is assumed to be 1 if it is left off. 
NEXT variable marks the end of the “block”.
	Example:
	FOR A = 0 TO 10 STEP 2
	PRINT A
	NEXT A

Dealing with Data
DATA – defines a list of constant data that can be accessed with READ
READ – reads the next item from DATA
	Example:
	DATA 10,”mphipps”
	READ a, a$

INPUT – expects a string, then any number of variables. Prints the string, 
then waits for the user to enter the inputs, comma separated. 
	Example:
	INPUT “What is your name and age?”, name$, age
	PRINT “Hi “, name$, “ you are “, age, “ years old!” 
PRINT – prints any number of values, separated by a comma
	Example:
	PRINT “hello. 5 + 3 = “, 5+3, “ how do you like that “, name$, “?”

Built-in Functions
RANDOM() – returns a random integer
LEFT$(string, int) – returns the leftmost N characters from the string 
RIGHT$(string, int) – returns the rightmost N characters from the string 
MID$(string,int, int) – returns the characters of the string, starting from 
    the 2nd argument and taking the 3rd argument as the count;
    MID$(“Albany”,2,3) = “ban”
NUM$(int or float) – converts a number to a string
VAL(string) – converts a string to an integer
VAL%(string) – converts a string to a float
