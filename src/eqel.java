/**
 * @author David Berard
 * 
 * EQuation ELement: Operator, number, variable...
 */

package equationsolving;

import java.util.ArrayDeque;
import java.util.Deque;


public class eqel { 
    char type;
    double numberValue;
    char otherValue;
    int pieceLocation = -1;
    String name;
    public static final char plus = 1;
    public static final char minus = 2;
    public static final char multiply = 3;
    public static final char divide = 4;
    public static final char lparen = 5;
    public static final char rparen = 6;
    public static final char equal = 7;
    public static final char factorial = 8;
    public static final char power = 9;
    public static final char log = 10;
    public static final char naturallog = 11;
    
    public static final char validOperator = 1;
    public static final char numberException = 2;
    public static final char variableException = 3;
    
    public static final char operatorType = 1;
    public static final char numberType = 2;
    public static final char variableType = 3;
    public static final char parenType = 4;
    public static final char equalType = 5;
    public static final char whitespaceType = 6;
    public static final char pieceType = 7;
    public static final char constantType = 8; // NEED TO IMPLEMENT
    
    public eqel(double num) {
        type = numberType;
        numberValue = num;
        otherValue = validOperator;
    }
    
    public eqel(double num, char exception) {
        type = numberType;
        numberValue = num;
        otherValue = exception;
    }
    
    public eqel(String input) {
		parseThing(input);
    }
    
    public eqel(int input, char thisType) {
		if(thisType == pieceType) {
			pieceLocation = input;
			type = thisType;
		}
	}
    
    public int parseThing(String input)
    {
		if(input.equals("+")) {
            type = operatorType;
            otherValue=plus;
        }
        else if(input.equals("-")) {
            type = operatorType;
            otherValue=minus;
        }
        else if(input.equals("*")) {
            type = operatorType;
            otherValue=multiply;
        }
        else if(input.equals("/")) {
            type = operatorType;
            otherValue=divide;
        }
        else if(input.equals("(") || input.equals("{") || input.equals("[")) {
            type = operatorType;
            otherValue=lparen;
        }
        else if(input.equals(")") || input.equals("}") || input.equals("]")) {
            type = operatorType;
            otherValue=rparen;
        }
        else if(input.equals("=")) {
			type = operatorType;
			otherValue=equal;
		}
		else if(input.equals("!")) {
			type = operatorType;
			otherValue=factorial;
		}
		else if(input.equals("^")) {
			type = operatorType;
			otherValue=power;
		}
		else if(input.equals("log")) {
			type = operatorType;
			otherValue=log;
		}
		else if(input.equals("ln")) {
			type = operatorType;
			otherValue=naturallog;
		}
        else {
            type = variableType;
            name=input;
        }
        return type;
	}
    
    public static char eltype(char in) { // element type
        //char "in" is an actual character
        //char out is just an element type
        if(in == 5) {}
        return 'a';
    }
    
    public String value() {
        if(type == operatorType)
        { /*
            if(otherValue == 1)
                return "+";
            if(otherValue == 2)
                return "-";
            if(otherValue == 3)
                return "*";
            if(otherValue == 4)
                return "/";
            if(otherValue == 5)
                return "(";
            if(otherValue == 6)
                return ")"; */
			String out = "operator " + Integer.toString(otherValue);
			return out;
        }
        if(type == variableType)
        {
            return "variable " + name;
        }
        if(type == numberType)
        {
            return "number " + Double.toString(numberValue);
        }
        if(type == pieceType)
		{
			return "Piece, id " + Integer.toString(pieceLocation);
		}
        return "? " + Integer.toString(type) + " " + Integer.toString(otherValue) + "...";
    }
    
    public void setValue(String tvalue) {
		name = tvalue;
	}
	
	public static int valtype(char input)
	{
		if(input == 32)
			return whitespaceType;
		else if( input == 40 || input == 41 || input == 123 || input == 125 || input == 91 || input == 93 )
			return parenType;
		else if( input == 46 || (48 <= input && input <= 57) )
			return numberType;
		else if( input == 61 )
			return equalType;
		else if( (65 <= input && input <= 90) || (97 <= input && input <= 122))
			return variableType;
		else
			return operatorType;
	}
	
	public static int parenParse(char input)
	{
		/*
		 * 1: (
		 * 2: )
		 * 3: [
		 * 4: ]
		 * 5: {
		 * 6: }
		 */
		int ival = (int) input;
		switch(ival) {
			case 40:	return 1;
			case 41:	return 2;
			case 91:	return 3;
			case 93:	return 4;
			case 123:	return 5;
			case 125:	return 6;
		}
		return -1;
	}
	public static char reverseParenParse(int input)
	{
		switch(input) {
			case 1:	return '(';
			case 2:	return ')';
			case 3:	return '[';
			case 4:	return ']';
			case 5:	return '{';
			case 6:	return '}';
		}
		return ' ';
	}
	
	public double evaluate(char operatorType, double... numbers)
	{
		if(operatorType == plus)
		{
			if(numbers.length > 1)
				return (numbers[0] + numbers[1]);
		}
		if(operatorType == minus)
		{
			if(numbers.length > 1)
				return (numbers[0] - numbers[1]);
			else
				return (-numbers[0]);
		}
		if(operatorType == multiply)
		{
			if(numbers.length > 1)
				return (numbers[0] * numbers[1]);
		}
		if(operatorType == divide)
		{
			if(numbers.length > 1)
				return (numbers[0] / numbers[1]);
		}
		return -1.0;
	}
    
    
    public void display() {
        System.out.println(this.value());
    }
}
