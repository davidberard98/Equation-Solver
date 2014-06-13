/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package equationsolving;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;

/**
 *
 * @author davidberard
 */
public class EquationSolving {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scanner = new Scanner( System.in );
        System.out.print("Your numeric equation: ");
        String input = scanner.nextLine();
        eqel[] RPNone = new eqel[7];
        RPNone[0] = new eqel(2);
        RPNone[1] = new eqel(3);
        RPNone[2] = new eqel(4);
        RPNone[3] = new eqel("+");
        RPNone[4] = new eqel(3);
        RPNone[5] = new eqel("*");
        RPNone[6] = new eqel("/");
        
        RPNSolve(RPNone);
        
        
    }
    
    public static eqel[] RPNCreate (String equation) {
        eqel[] RPN = new eqel[0];
        
        return RPN;
    }
    
    public static int RPNSolve (eqel[] equation) {
        // designed for integers as a test
        //this regex is for easy equations with only numbers, arith func.
        //if(equation.matches("[^0|1|2|3|4|5|6|7|8|9| |\050|\051|+|-|/|*]"))
        //  return -1;
        Deque<Double> stack = new ArrayDeque<>();
        for(int i=0;i<equation.length;++i) {
            equation[i].display();
            eqel result = eqel.execute(equation[i], stack);
            if(result.otherValue == eqel.validOperator) {
                stack.push(result.numberValue);
            }
        }
        System.out.println(stack.pop());
        
        return 1;
    }
    
}
