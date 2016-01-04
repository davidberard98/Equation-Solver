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
		boolean reverseDone = false;
		if(this.findEqel(new eqel("+")) != -1 || this.findEqel(new eqel("-")) != -1)
		{ // Dealing with + and -.
			if(roPlusMinus(orderedOperations, pls, location) != -1)
				reverseDone = true;
		}
		if(this.findEqel(new eqel("*")) != -1)
		{
			if(roMultiply(orderedOperations, pls, location) != -1)
				reverseDone = true;
		}
		if(this.findEqel(new eqel("/")) != -1)
		{
			if(roDivide(orderedOperations, pls, location) != -1)
				reverseDone = true;
		}
		if(this.findEqel(new eqel("ln")) != -1 || this.findEqel(new eqel("log")) != -1)
		{
			if(roLogs(orderedOperations, pls, location) != -1)
				reverseDone = true;
		}
		if(this.findEqel(new eqel("^")) != -1)
		{
			if(roPower(orderedOperations, pls, location) != -1)
				reverseDone = true;
		}
		if(reverseDone == false && this.length() == 1)
		{
			reverseDone = true;
		}
		
		if(reverseDone)
		{
			if(this.at(location).type == eqel.variableType)
				return 1;
			else if(this.at(location).type == eqel.pieceType)
			{
				// Recursive
				return pls.at(this.at(location).pieceLocation).reverseOperations(orderedOperations, pls, chosenVar);
			}
		}
		else
			return -1;
		return 1;
	}
	
	public int roPower(List<Piece> orderedOperations, PieceList pls, int location)
	{
		if(this.length() != 3)
			return -1;
		if(this.at(1).otherValue != eqel.power)
			return -1;
		if(location == 0) // THIS^b
		{
			// Reverse will be ^(1/b) so repackage (1/b) as [#] = (1/b) (a piece) and put ^[#]
			eqel other = this.at(2);
			Piece rev = new Piece();
			eqel op = new eqel("^");
			int newid = pls.add();
			Piece newpiece = pls.at(newid);
			newpiece.add(new eqel(1.0));
			newpiece.add(new eqel("/"));
			newpiece.add(other);
			eqel newPieceEqel = new eqel(newid, eqel.pieceType);
			
			rev.add(op);
			rev.add(newPieceEqel);
			orderedOperations.add(rev);
		}
		else if(location == 2) // b^THIS.
		{ 
			//Reverse will be log(OTHER)/log(b).  Repackage [#] = log(b) and put {log}, {/[#]}
			eqel other = this.at(0);
			Piece logop = new Piece();
			logop.add(new eqel("log"));
			
			Piece divop = new Piece();
			int newid = pls.add();
			Piece newpiece = pls.at(newid);
			newpiece.add(new eqel("log"));
			newpiece.add(other);
			eqel newPieceEqel = new eqel(newid, eqel.pieceType);
			divop.add(new eqel("/"));
			divop.add(newPieceEqel);
			
			orderedOperations.add(logop);
			if(!(other.type == eqel.numberType && other.numberValue == 10.0))
				orderedOperations.add(divop);
		}
		
		return 1;
	}
	
	public int roLogs(List<Piece> orderedOperations, PieceList pls, int location)
	{
		if(this.length() != 2)
			return -1;
		if(this.at(0).otherValue != eqel.logten && this.at(0).otherValue != eqel.naturallog)
			return -1;
		eqel base = null;
		eqel op = new eqel("^");
		if(this.at(0).otherValue == eqel.logten)
		{
			base = new eqel(10.0);
		}
		else if(this.at(0).otherValue == eqel.naturallog)
		{
			base = new eqel("e", pls.constants);
		}
		Piece rev = new Piece();
		rev.add(base);
		rev.add(op);
		orderedOperations.add(rev);
		
		return 1;
	}
	
	public int roDivide(List<Piece> orderedOperations, PieceList pls, int location)
	{// Assumes a/b is all
		if(this.length() != 3 || (this.length() > 2 && this.at(1).otherValue != eqel.divide) || location%2 != 0) {
			System.out.println("ERROR: Divide operator with incorrect number/order of eqel elements");
			return -1;
		}
		eqel other = null;
		if(location == 0) // THIS/b
		{
			other = this.at(2);
			Piece undo = new Piece();
			undo.add(new eqel("*"));
			undo.add(other);
			orderedOperations.add(undo);
		}
		else if(location == 2) // b/THIS
		{
			other = this.at(0);
			Piece flip = new Piece();
			Piece isolate = new Piece();
			flip.add(new eqel(1.0));
			flip.add(new eqel("/"));
			isolate.add(new eqel("*"));
			isolate.add(other);
			orderedOperations.add(flip);
			orderedOperations.add(isolate);
		}
		
		return 1;
	}
	
	public int roMultiply(List<Piece> orderedOperations, PieceList pls, int location)
	{ // assumes "a*b" is all
		eqel other = null;
		for(int i=0;i<this.length();++i)
		{
			if(this.at(i).type != eqel.operatorType && i != location)
			{
				other = this.at(i);
			}
		}
		if(other != null)
		{
			Piece unmultiply = new Piece();
			unmultiply.add(new eqel("/"));
			unmultiply.add(other);
			orderedOperations.add(unmultiply);
		}
		else
			return -1;
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
			if(this.at(i).type == eqel.operatorType && current.length() > 0)
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
	
	public int reverseImplement(List<Piece> orderedOperations, PieceList pls)
	{
		//Return id of Piece
		int copyid = pls.add();
		pls.at(copyid).transfer(this);
		int currentId = copyid;
		for(int i=0;i<orderedOperations.size();++i)
		{
			Piece toperation = orderedOperations.get(i);
			
			int nextid = pls.add(); // Contain next operation
			eqel pieceEqel = new eqel(currentId, eqel.pieceType); // Eqel holding this piece (paren)
			
			if(toperation.length() == 1)
			{ // logs, lns.
				pls.at(nextid).add(toperation.at(0));
				pls.at(nextid).add(pieceEqel);
			}
			else if(toperation.length() == 2)
			{
				boolean completed = false;
				if(toperation.at(0).type == eqel.operatorType)
				{
					pls.at(nextid).add(pieceEqel);
					pls.at(nextid).add(toperation.at(0));
					pls.at(nextid).add(toperation.at(1));
					completed=true;
				}
				if(toperation.at(1).type == eqel.operatorType)
				{
					pls.at(nextid).add(toperation.at(0));
					pls.at(nextid).add(toperation.at(1));
					pls.at(nextid).add(pieceEqel);
					completed=true;
				}
				if(!completed)
					return -1;
			}
			
			currentId = nextid;
		}
		return currentId;
	}

	public String svalue(PieceList pls)
	{
		String output = new String();
		for(int i=0;i<allElements.size();++i)
		{
			eqel teqel = allElements.get(i);
			if(teqel.type == eqel.operatorType)
			{
				output += eqel.opString(teqel.otherValue);
			}
			else if(teqel.type == eqel.numberType)
			{
				output += Double.toString(teqel.numberValue);
			}
			else if(teqel.type == eqel.variableType || teqel.type == eqel.constantType)
			{
				output += teqel.name;
			}
			else if(teqel.type == eqel.equalType)
			{
				output += "=";
			}
			else if(teqel.type == eqel.pieceType)
			{
				output += "(" + pls.at(teqel.pieceLocation).svalue(pls) + ")";
			}
		}
		return output;
	}
	
}
