/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * EQuation ELement
 */

package equationsolving;

import static equationsolving.eqel.add;
import static equationsolving.eqel.plus;
import java.util.ArrayDeque;
import java.util.Deque;

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
    
    public static final char validOperator = 1;
    public static final char numberException = 2;
    public static final char variableException = 3;
    
    public static final char operatorType = 1;
    public static final char numberType = 2;
    public static final char variableType = 3;
    
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
        if(input.equals("+")) {
            type = operatorType;
            otherValue=1;
        }
        else if(input.equals("-")) {
            type = operatorType;
            otherValue=2;
        }
        else if(input.equals("*")) {
            type = operatorType;
            otherValue=3;
        }
        else if(input.equals("/")) {
            type = operatorType;
            otherValue=4;
        }
        else if(input.equals("(")) {
            type = operatorType;
            otherValue=5;
        }
        else if(input.equals(")")) {
            type = operatorType;
            otherValue=6;
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
    
    public static eqel execute(eqel order, Deque<Double> stack) {
        if(order.type == numberType) {
            stack.push(order.numberValue);
            return new eqel(1.0, numberException);
        }
        else if(order.type == operatorType) {
            if(order.otherValue == plus) return new eqel(add(stack));
            else if(order.otherValue == minus) return new eqel(subtract(stack));
            else if(order.otherValue == multiply) return new eqel(multiply(stack));
            else if(order.otherValue == divide) return new eqel(divide(stack));
        }
        return new eqel(1.0, variableException);
    }
    
    public static double add(Deque<Double> stack) {
        return stack.pop() + stack.pop();
    }
    
    public static double subtract(Deque<Double> stack) {
        return -stack.pop() + stack.pop();
    }
    
    public static double multiply(Deque<Double> stack) {
        return stack.pop() * stack.pop();
    }
    
    public static double divide(Deque<Double> stack) {
        double a = stack.pop();
        double b = stack.pop();
        if(a == 0) {
            return 0;
        }
        return b/a;
    }
    
    public void display() {
        System.out.println(value());
    }
}
