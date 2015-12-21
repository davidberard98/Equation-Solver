/**
 * @author David Berard
 * 
 * List of pieces's so that you can have recursive pieces... = parentheses
 */

package equationsolving;

import java.util.ArrayList;
import java.util.List;


public class PieceList {
	
	//public static final int dcount = 1000;
	//Piece[] database = new Piece[dcount];
	List<Piece> database = new ArrayList<>();
	int counter = 0;
	public List<Variable> constants = new ArrayList<>();
	
	public PieceList () { }
	
	public int add() {
		//if(counter + 1 < dcount)
		//{
			//database[counter] = new Piece(counter);
			database.add(new Piece(counter));
			return counter++;
		//}
		//return -1;
	}
	public Piece at(int input) {
		return database.get(input);
	} 
	public void display() {
		for(int i=0;i<counter;++i)
		{
			System.out.println("Piece " + i + "(thinks " + database.get(i).id + ")" + ":");
			database.get(i).display();
			System.out.println();
		}
	}
}
