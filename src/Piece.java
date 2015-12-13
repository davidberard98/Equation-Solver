/**
 * @author davidberard
 * 
 * Ordered list of operations, numbers, vars within "eqel"
 */

package equationsolving;

import java.util.ArrayDeque;
import java.util.Deque;

public class Piece {
	
	Deque<eqel> allElements= new ArrayDeque<>();
	public int id = -1;
	
	public Piece () { this.id=0; }
	public Piece (int in) { this.id = in; }
	
	public void add (eqel input) {
		input.display();
		allElements.push(input);
	}
	public int length () {
		return allElements.size();
	}
	
}
