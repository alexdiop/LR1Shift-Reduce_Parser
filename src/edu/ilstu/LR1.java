package edu.ilstu;

//============================================================================
//Name        : LR1 Parser
//Author      : Alexander Diop
//Created     : 2/19/21
//Version     : 0.0.1
//Description : Implement of an LR(1) parser, also known as Shift-Reduce parser
//============================================================================

import java.util.*; 

public class LR1{
	
	static Stack<Token> stack = new Stack<Token>();
	static Queue<Character> queue = new LinkedList<Character>();
	
	public static void main(String[] args){
		
		Token initialStateToken = new Token('☐',0); //starting from state zero
		stack.push(initialStateToken);
		String inputString = "";
		
        if (args.length > 0){ 
        	inputString = args[0];
        } 
        else {
        	System.out.println("Usage: java LR1.java inputString\n");
        	inputString = "35*(19-13-5)-2*3*(9-7)+(2*(17+4)-12)/5";
        }
        
        inputString += "$";
        
        for(int i = 0; i < inputString.length(); i++){
        	queue.add(inputString.charAt(i));
        }
        
        checkContents();
        
        //iterate through the queue
        while(!queue.isEmpty()){
        	
        	char tokenChar = queue.peek();//current token in the string
       
        	switch(tokenChar){
	        	case '+':
	        		additionSubtraction('+', stack.peek().state);
	        		break;
	        	case '-':
	        		additionSubtraction('-', stack.peek().state);
	        		break;
	        	case '*':
	        		multiplicationDivision('*', stack.peek().state);
	        		break;
	        	case '/':
	        		multiplicationDivision('/', stack.peek().state);
	        		break;
	        	case '(':
	        		openParenthesis('(', stack.peek().state);
	        		break;
	        	case ')':
	        		closingParenthesis(')', stack.peek().state);
	        		break;
	        	case '$':
	        		if(!stack.isEmpty()) {
	        			int finalState = stack.peek().state;
		        		endColumn('$', finalState);
	        		}else {
	        			queue.poll();
	        		}
	        		break;
	        	default:
	        		if(Character.isDigit(tokenChar)){
	            		String num = "";
	            		//used for numbers operands longer than one digit
	            		while(queue.peek() != null && Character.isDigit(queue.peek())){
	            			num += queue.poll();
	            		}
	            		int n = Integer.parseInt(num);
	            		
		            	//do something with 'n'
			            //System.out.println("Current State: " + stack.peek().state);
		            	//System.out.println("Pushing " + n);
		            	operand('n', n, stack.peek().state);
	            	}
	        		else{
	            		//this should never run if the input string is formatted correctly
	            	}
	        		break;
	        	}
        }
	}
	
	
	//integer values 'n'
	private static void operand(char inputToken, int value, int state) {
		switch(state){
	    	case 0:
	    	case 4:
	    	case 6:
	    	case 7:{
	    		shift(inputToken, value, 5);//shift 5
	    		break;
	        }
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		checkContents();
	}


	//rules for columns '+' and '-'
	private static void additionSubtraction(char inputToken, int state) {
		
		switch(state){
	    	case 1:
	    	case 8:{
	    		shift(inputToken, 6); //shift 6
	    		break;
	        }
	    	case 2:{
	    		rule3();
	    		break;
	    	}
	    	case 3:{
	    		rule6();
	    		break;
	    	}
	    	case 5:{
	    		rule8();
	    		break;
	    	}
	    	case 9:{
	    		Token operand2 = stack.pop();
	    		Token operator = stack.pop();
	    		Token operand1 = stack.pop();
	    		
	    		if(operator.name == '+'){
	    			rule1(operand1.value,operand2.value);
	    		}else if (operator.name == '-'){
	    			rule2(operand1.value,operand2.value);
	    		}else {
	    			System.out.println("Error In String");
	    		}
	    		break;
	    	}
	    	case 10:{
	    		Token operand2 = stack.pop();
	    		Token operator = stack.pop();
	    		Token operand1 = stack.pop();
	    		
	    		if(operator.name == '*'){
	    			rule4(operand1.value,operand2.value);
	    		}else if (operator.name == '/'){
	    			rule5(operand1.value,operand2.value);
	    		}else {
	    			System.out.println("Error In String");
	    		}
	    		break;
	    	}
	    	case 11:{
	    		rule7();
	    		break;
	    	}
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		checkContents();
	}
	
	//rules for columns '*' and '/'
	private static void multiplicationDivision(char inputToken, int state) {
		
		switch(state){
	    	case 2:
	    	case 9:{
	    		shift(inputToken, 7); //shift 7
	    		break;
	        }
	    	case 3:{
	    		rule6();
	    		break;
	    	}
	    	case 5:{
	    		rule8();
	    		break;
	    	}
	    	case 10:{
	    		Token operand2 = stack.pop();
	    		Token operator = stack.pop();
	    		Token operand1 = stack.pop();
	    		
	    		if(operator.name == '*'){
	    			
	    			rule4(operand1.value,operand2.value);
	    		}else if (operator.name == '/'){
	    			rule5(operand1.value,operand2.value);
	    		}else {
	    			System.out.println("Error In String");
	    		}
	    		break;
	    	}
	    	case 11:{
	    		rule7();
	    		break;
	    	}
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		checkContents();
	}
	
	
	//rules for column '('
	private static void openParenthesis(char inputToken, int state) {
		switch(state){
	    	case 0:
	    	case 4:
	    	case 6:
	    	case 7:{
	    		shift(inputToken, 4); //shift 4
	    		break;
	        }
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		checkContents();
	}
	
	//rules for column ')'
	private static void closingParenthesis(char inputToken, int state) {
		
		switch(state){
			case 2:{
	    		rule3();
	    		break;
	    	}
			case 3:{
	    		rule6();
	    		break;
	    	}
	    	case 5:{
	    		rule8();
	    		break;
	    	}
	    	case 8:{
	    		shift(inputToken, 11); //shift 11
	    		break;
	        }
	    	case 9:{
	    		Token operand2 = stack.pop();
	    		Token operator = stack.pop();
	    		Token operand1 = stack.pop();
	    		
	    		if(operator.name == '+'){
	    			rule1(operand1.value,operand2.value);
	    		}else if (operator.name == '-'){
	    			rule2(operand1.value,operand2.value);
	    		}else {
	    			System.out.println("Error In String");
	    		}
	    		break;
	    	}
	    	case 10:{
	    		Token operand2 = stack.pop();
	    		Token operator = stack.pop();
	    		Token operand1 = stack.pop();
	    		
	    		if(operator.name == '*'){
	    			rule4(operand1.value,operand2.value);
	    		}else if (operator.name == '/'){
	    			rule5(operand1.value,operand2.value);
	    		}else {
	    			System.out.println("Error In String");
	    		}
	    		break;
	    	}
	    	case 11:{
	    		rule7();
	    		break;
	    	}
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		checkContents();
	}
	
	
	//rules for column '$'
	private static void endColumn(char inputToken, int state) {
		
		switch(state){
			case 1:{
				//CORRECT INPUT STRING
				while(!stack.isEmpty()) {
					Token secondToLast = stack.pop();
					
					if(!stack.isEmpty()) {
						Token last = stack.pop();
						if(stack.isEmpty()) {
							System.out.println("\nValid Expression, value = " + secondToLast.value + ".");
						}
					}
				}
				break;
			}
			case 2:{
	    		rule3();
	    		break;
	    	}
			case 3:{
	    		rule6();
	    		break;
	    	}
	    	case 5:{
	    		rule8();
	    		break;
	    	}
	    	case 9:{
	    		Token operand2 = stack.pop();
	    		Token operator = stack.pop();
	    		Token operand1 = stack.pop();
	    		
	    		if(operator.name == '+'){
	    			rule1(operand1.value,operand2.value);
	    		}else if (operator.name == '-'){
	    			rule2(operand1.value,operand2.value);
	    		}else {
	    			System.out.println("Error In String");
	    		}
	    		break;
	    	}
	    	case 10:{
	    		Token operand2 = stack.pop();
	    		Token operator = stack.pop();
	    		Token operand1 = stack.pop();
	    		
	    		if(operator.name == '*'){
	    			rule4(operand1.value,operand2.value);
	    		}else if (operator.name == '/'){
	    			rule5(operand1.value,operand2.value);
	    		}else {
	    			System.out.println("Error In String");
	    		}
	    		break;
	    	}
	    	case 11:{
	    		rule7();
	    		break;
	    	}
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
	}
	
	
	
	//rules for non-terminal symbol 'E'
	private static int columnE(int state){
		switch(state){
	    	case 0:
	    		return 1;
	    	case 4:
	    		return 8;
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		return 0;
	}
	
	//rules for non-terminal symbol 'E'
	private static int columnT(int state){
		
		switch(state){
	    	case 0:
	    		return 2;
	    	case 4:
	    		return 2;
	    	case 6:
	    		return 9;
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		return 0;
	}
	
	//rules for non-terminal symbol 'E'
	private static int columnF(int state){
		
		switch(state){
	    	case 0:
	    		return 3;
	    	case 4:
	    		return 3;
	    	case 6:
	    		return 3;
	    	case 7:
	    		return 10;
	    	default:
	    		//error
	    		System.out.println("INVALID");
	    		System.exit(0);
	    		break;
    	}
		return 0;
	}
	
	// Recursive function to print stack elements 
	// from bottom to top without changing 
	// their order 
	static void PrintStack(Stack<Token> stack) 
	{ 
	    // If stack is empty then return 
	    if (stack.isEmpty()) 
	        return; 
	     
	    Token token = stack.peek(); 
	 
	    // Pop the top element of the stack 
	    stack.pop(); 
	 
	    // Recursively call the function PrintStack 
	    PrintStack(stack); 
	 
	    // Print the stack element starting 
	    // from the bottom 
	    if(token.value != -999)
	    	System.out.print("(" + token.name + "=" + token.value + ":" + token.state + ")");
	    else
	    	System.out.print("(" + token.name + ":" + token.state + ")");
	    	
	 
	    // Push the same element onto the stack 
	    // to preserve the order 
	    stack.push(token); 
	}
	
	static void shift(char inputToken, int value, int state) {
		Token token = new Token (inputToken, value, state);
		stack.push(token);
		
	}
	
	static void shift(char inputToken, int state) {
		queue.poll();
		Token token = new Token (inputToken, state);
		stack.push(token);
	}
	
	//E->E+T
	static void rule1(int value1, int value2) {
		int value = value1 + value2;
		Token token = new Token('E', value, columnE(stack.peek().state));
		stack.push(token);
	}
	
	//E->E-T
	static void rule2(int value1, int value2) {
		int value = value1 - value2;
		Token token = new Token('E', value, columnE(stack.peek().state));
		stack.push(token);
	}
	
	//E->T
	static void rule3() {
		Token temp = stack.pop();
		Token token = new Token('E', temp.value, columnE(stack.peek().state));
		stack.push(token);
	}
	
	
	//T->T*F
	static void rule4(int value1, int value2) {
		int value = value1 * value2;
		Token token = new Token('T', value, columnT(stack.peek().state));
		stack.push(token);
	}
	
	//T->T/F
	static void rule5(int value1, int value2) {
		int value = value1 / value2;
		Token token = new Token('T', value, columnT(stack.peek().state));
		stack.push(token);
	}
	
	//T->F
	static void rule6() {
		Token temp = stack.pop();
		Token token = new Token('T', temp.value, columnT(stack.peek().state));
		stack.push(token);
	}
	
	//F->(E)
	static void rule7() {
		stack.pop();
		Token temp = stack.pop();
		stack.pop();
		Token token = new Token('F', temp.value, columnF(stack.peek().state)); //was: stack.peek().state
		stack.push(token);
	}
	
	//F->n
	static void rule8() {
		Token temp = stack.pop();
		Token token = new Token('F', temp.value, columnF(stack.peek().state));
		stack.push(token);
	}

	static void checkContents() {
		System.out.print("Stack:[");
		PrintStack(stack);
        System.out.print("]	Input Queue:[");
        for (Character item: queue) {
            System.out.print(item);
        }
		System.out.print("]\n");
	}
}
