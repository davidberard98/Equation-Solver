/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * EQuation ELement
 */

package equationsolving;

/**
 *
 * @author davidberard
 */
public class eqel {
    char type;
    double numberValue;
    char otherValue;
    String name;
    public static final char plus = 1;
    public static final char minus = 2;
    public static final char multiply = 3;
    public static final char divide = 4;
    public static final char lparen = 5;
    public static final char rparen = 6;
    
    public static final char operatorType = 1;
    public static final char numberType = 2;
    public static final char variableType = 3;
    
    public eqel(double num) {
        type = numberType;
        numberValue = num;
    }
    
    public eqel(String input) {
        if(input.equals("+")) {
            type = operatorType;
            otherValue=1;
        }
        if(input.equals("-")) {
            type = operatorType;
            otherValue=2;
        }
        if(input.equals("*")) {
            type = operatorType;
            otherValue=3;
        }
        if(input.equals("/")) {
            type = operatorType;
            otherValue=4;
        }
        if(input.equals("/")) {
            type = operatorType;
            otherValue=4;
        }
        if(input.equals("/")) {
            type = operatorType;
            otherValue=4;
        }
        else {
            type = variableType;
            name=input;
        }
    }
    
    public static char eltype(char in) { // element type
        //char "in" is an actual character
        //char out is just an element type
        if(in == 5) {}
        return 'a';
    }
    
    public String value() {
        if(type == operatorType)
        {
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
                return ")";
        }
        if(type == variableType)
        {
            return name;
        }
        if(type == numberType)
        {
            return Double.toString(numberValue);
        }
        return "";
    }
    
    public void display() {
        System.out.println(value());
    }
}
