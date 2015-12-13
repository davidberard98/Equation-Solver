/**
 * @author davidberard
 */

package equationsolving;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.Arrays;

public class EquationSolving {

	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scanner = new Scanner( System.in );
        System.out.print("Your numeric equation: ");
        String input = scanner.nextLine();
        PieceList alleq = new PieceList();
        int id = alleq.add();
        eqel abc = new eqel("a");
        Piece tpiece = alleq.at(id);
        
        tpiece.add(abc);
        System.out.println(tpiece.length());
        tpiece.add(abc);
        System.out.println(tpiece.length());
        tpiece = null;
        
        System.out.println(alleq.at(id).length());
    }

    
}
