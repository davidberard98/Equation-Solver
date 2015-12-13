/**
 * @author davidberard
 * 
 * List of pieces's so that you can have recursive pieces... = parentheses
 */

package equationsolving;

public class PieceList {
	
	public static final int dcount = 100;
	Piece[] database = new Piece[dcount];
	int counter = 0;
	
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
}
