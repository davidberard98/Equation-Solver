/**
 * @author davidberard
 */

package equationsolving;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
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
        
        System.out.println(alleq.at(id).length() + "\n\n");
        
        parse(input, alleq);
    }
    
    public static int parse(String in, PieceList pls)
    {
		// Return id of main eq.
		int mainid = pls.add();
		Piece main = pls.at(mainid);
		List<String> parts = new ArrayList<>();
		List<Integer> partTypes = new ArrayList<>();
		int ctype = 0;
		String current = "";
		for(int i=0;i<in.length();++i)
		{
			int ttype = eqel.valtype(in.charAt(i));
			if(ctype != 0 && ctype != ttype) {
				if(ctype != eqel.whitespaceType) {
					parts.add(current);
					partTypes.add(ctype);
				}
				current = "";
			}
			current += in.charAt(i);
			ctype = ttype;
		}
		if(ctype != 0 && ctype != eqel.whitespaceType && current != "") {
			// Log the last part.
			parts.add(current);
			partTypes.add(ctype);
		}
		
		for(int i=0;i<parts.size();++i)
		{
			System.out.println(i + ":" + parts.get(i) + " : " + partTypes.get(i));
			//System.out.format("%d:" + parts[i] + " : %d%n", i, partTypes[i]);
		}
		return mainid;
	}

    
}
