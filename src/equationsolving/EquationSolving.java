/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package equationsolving;
import java.util.Scanner;
import java.util.Deque;

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
        System.out.println(RPNSolve(input));
        
    }
    
    public static eqel[] RPNCreate (String equation) {
        eqel[] RPN = new eqel[0];
        
        
    }
    
    public static int RPNSolve (eqel[] equation) {
        // designed for integers as a test
        //this regex is for easy equations with only numbers, arith func.
        if(equation.matches("[^0|1|2|3|4|5|6|7|8|9| |\050|\051|+|-|/|*]"))
          return -1;
            
    }
    
}
