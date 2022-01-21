//this class holds the information for the token object
//which will be used to push and pop from parser stack

package edu.ilstu;

public class Token {
	
	char name; //either a nonterminal symbol or +, - , *, /, (, )
	int value = -999;
	int state;
	
	//constructor to create the token object (read in the form T=20:9 (example))
	Token(char name, int value, int state) {
		this.name = name;
		this.value = value;
		this.state = state;
	}
	
	//constructor to create the token object without a value (read in the form *:9 (example))
	Token(char name, int state) {
		this.name = name;
		this.state = state;
	}
	
	//constructor to create the initial token object (read in the form --:9 (example))
	Token(int state) {
		this.state = state;
	}

}
