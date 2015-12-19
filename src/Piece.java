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
	
	public double evaluate(List<Variable> varvals, PieceList pls)
	{
		for(int i=0;i<this.length();++i)
		{
			eqel element = this.at(i);
			if(element.type == eqel.operator)
			{
				if((element.otherValue == eqel.plus || element.otherValue == eqel.multiply
				|| eqel.otherValue == eqel.divide || element.otherValue == eqel.power) 
				&& i < this.length()-1 && i > 0) // a(operator)b format, no minus
				{
					double a = findPart(varvals, pls, i-1);
					double b = findPart(varvals, pls, i+1);
					char operation = element.otherValue;
					eqel.evaluate(operation, a, b);
				}
			}
		}
	}
	
	public double findPart(List<Variable> varvals, PieceList pls, int loc)
	{
		double output;
					
		if(this.at(loc).type == eqel.numberType)
			output = this.at(loc).numberValue;
		else if(this.at(loc).type == eqel.variableType)
		{
			for(int k=0;k<varvals.size();++k)
			{
				if(varvals.get(k).name == this.at(loc).name)
				{
					output = varvals.get(k).value;
					break;
				}
			}
		}
		else if(this.at(loc).type == eqel.pieceType)
			output = pls.at(this.at(loc).pieceLocation).evaluate(varvals, pls);
			
		return output;
						
	}
	
}
