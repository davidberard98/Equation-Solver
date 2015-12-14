/**
 * @author David Berard
 * 
 * Ordered list of operations, numbers, vars within "eqel"
 */

package equationsolving;

import java.util.List;
import java.util.ArrayList;

public class Piece {
	
	List<eqel> allElements= new ArrayList<>();
	public int id = -1;
	public List<String> notes = new ArrayList<>();
	
	public Piece () { this.id=0; }
	public Piece (int in) { this.id = in; }
	
	public void add (eqel input) {
		allElements.add(input);
	}
	public int length () {
		return allElements.size();
	}
	public eqel at(int i){
		return allElements.get(i);
	}
	public void display () {
		for(int i=0;i<this.length();++i) {
			allElements.get(i).display();
		}
	}
	public void transfer(Piece input)
	{
		this.allElements = input.allElements;
		this.id = input.id;
		this.notes = input.notes;
	}
	
}
