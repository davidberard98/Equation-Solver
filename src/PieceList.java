/**
 * @author David Berard
 * 
 * List of pieces's so that you can have recursive pieces... = parentheses
 */

package equationsolving;

import java.util.ArrayList;
import java.util.List;


public class PieceList {
	
	public static final int dcount = 1000;
	Piece[] database = new Piece[dcount];
	int counter = 0;
	public List<Variable> constants = new ArrayList<>();
	
	public PieceList () { }
	
	public int add() {
		if(counter + 1 < dcount)
		{
			database[counter] = new Piece(counter);
			return counter++;
		}
		return -1;
	}
	public Piece at(int input) {
		return database[input];
	} 
	public void display() {
		for(int i=0;i<counter;++i)
		{
			System.out.println("Piece " + i + "(thinks " + database[i].id + ")" + ":");
			database[i].display();
			System.out.println();
		}
	}
}
