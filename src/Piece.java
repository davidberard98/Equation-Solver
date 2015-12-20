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
		double output = 0;
		for(int i=0;i<this.length();++i)
		{
			eqel element = this.at(i);
			if(element.type == eqel.numberType || element.type == eqel.constantType)
			{
				output = element.numberValue;
			}
			else if(element.type == eqel.pieceType)
			{
				output = pls.at(element.pieceLocation).evaluate(varvals, pls);
			}
			else if(element.type == eqel.variableType)
			{
				output = this.findPart(varvals,pls, i);
			}
			else if(element.type == eqel.operatorType)
			{
				if((element.otherValue == eqel.plus || element.otherValue == eqel.minus 
				|| element.otherValue == eqel.multiply || element.otherValue == eqel.divide 
				|| element.otherValue == eqel.power) && i < this.length()-1) 
				{
					double b = findPart(varvals, pls, i+1);
					char operation = element.otherValue;
					output = eqel.evaluate(operation, output, b);
					++i;
				}
				else if(element.otherValue == eqel.factorial)
				{
					output = eqel.evaluate(eqel.factorial, output);
				}
				else if((element.otherValue == eqel.logten || element.otherValue == eqel.naturallog) && i<this.length()-1)
				{
					double a = findPart(varvals, pls, i+1);
					output = eqel.evaluate(element.otherValue, a);
					++i;
				}
			}
		}
		return output;
	}
	
	public double findPart(List<Variable> varvals, PieceList pls, int loc)
	{
		double output = 0;
					
		if(this.at(loc).type == eqel.numberType || this.at(loc).type == eqel.constantType)
			output = this.at(loc).numberValue;
		else if(this.at(loc).type == eqel.variableType)
		{
			for(int k=0;k<varvals.size();++k)
			{
				if(varvals.get(k).name.equals(this.at(loc).name))
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
	
	public int eqelContaining(PieceList pls, String varname)
	{
		for(int i=0;i<this.length();++i) // first, non-recursive check
		{
			if(this.at(i).type == eqel.variableType && this.at(i).name.equals(varname))
				return i;
		}
		for(int i=0;i<this.length();++i)
		{
			if(this.at(i).type == eqel.pieceType)
			{
				int result = pls.at(this.at(i).pieceLocation).eqelContaining(pls, varname);
				if(result != -1)
					return i;
			}
		}
		return -1;
	}
	
	public int findEqel(eqel input)
	{
		for(int i=0;i<this.length();++i)
		{
			if(this.at(i).equals(input))
				return i;
		}
		return -1;
	}
	
	public int reverseOperations(List<Piece> orderedOperations, PieceList pls, String chosenVar)
	{ // location being the eqel# where the var resides
		int location = this.eqelContaining(pls, chosenVar);
		if(this.findEqel(new eqel("+")) != -1 || this.findEqel(new eqel("-")) != -1)
		{ // Dealing with + and -.
			return roPlusMinus(orderedOperations, pls, location);
		}
		else
		{
			System.out.println("::Piece.reverseOperations - NO + or -");
		}
		return 1;
	}
	
	public int roPlusMinus(List<Piece> orderedOperations, PieceList pls, int location)
	{
		// Split up into sections.  For example, with special b: a + b - c - d
		// {a}, {+b}, {-c}, {-d}.
		// That way
		List<Piece> splitUp = new ArrayList<>(); 
		Piece current = new Piece();
		int specialpiece = -1;
		for(int i=0;i<this.length();++i)
		{
			if(this.at(i).type == eqel.operatorType)
			{
				splitUp.add(current);
				current = new Piece();
			}
			if(i==location)
				specialpiece = splitUp.size();
			current.add(this.at(i));
		}
		if(current.length() > 0)
			splitUp.add(current);
			
		System.out.println("::Piece.roPlusMinus().  splitUp.size() = " + splitUp.size() + ", specialpiece=" + specialpiece);
		
		// Splitting into sections complete.  Now, convert sections into operations.
		for(int i=0;i<splitUp.size();++i)
		{
			if(i == specialpiece)
				continue;
			if(splitUp.get(i).length() > 2 || splitUp.get(i).length() == 0)
				return -1;
			if(splitUp.get(i).length() == 1) {
				splitUp.get(i).allElements.add(0, new eqel("+"));
			}
			if(splitUp.get(i).at(0).type != eqel.operatorType)
				return -1;
			
			//Actual creation of operations
			Piece nextOperator = new Piece();
			if(splitUp.get(i).at(0).otherValue == eqel.plus)
			{
				nextOperator.add(new eqel("-"));
				nextOperator.add(splitUp.get(i).at(1));
			}
			else // for -(thing)
			{
				nextOperator.add(new eqel("+"));
				nextOperator.add(splitUp.get(i).at(1));
			}
			orderedOperations.add(nextOperator);
		}
		// IF this special part is negative, then a *-1.
		if(splitUp.get(specialpiece).at(0).otherValue == eqel.minus)
		{
			Piece revsign = new Piece();
			double minusoned = -1.0;
			eqel minusonee = new eqel(minusoned);
			revsign.add(new eqel("*"));
			revsign.add(minusonee);
			orderedOperations.add(revsign);
		}
		
		return 1;
	}
	
}
