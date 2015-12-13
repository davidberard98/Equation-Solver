/**
 * @author davidberard
 * 
 * Ordered list of operations, numbers, vars within "eqel"
 */

package equationsolving;

import java.util.List;
import java.util.ArrayList;

public class Piece {
	
	List<eqel> allElements= new ArrayList<>();
	public int id = -1;
	public String notes = "";
	
	public Piece () { this.id=0; }
	public Piece (int in) { this.id = in; }
	
	public void add (eqel input) {
		input.display();
		allElements.add(input);
	}
	public int length () {
		return allElements.size();
	}
	
}
