/**
 * @author David Berard
 * 
 * List of pieces's so that you can have recursive pieces... = parentheses
 */

package equationsolving;

import java.util.ArrayList;
import java.util.List;

class Equation
{
	PieceList pieces = new PieceList();
	List<Variable> vardef = new ArrayList<>();
	int mainid = 0;
	
	public Equation() {}
	public Equation(String input) { parse(input); }
	
	public boolean parse(String input)
	{
		mainid = EquationSolving.parse(input, pieces);
		return true;
	}
	
	public double evaluate(String varname, List<Variable> variables)
	{
		return EquationSolving.evaluate(pieces, mainid, varname, variables);
	}
	
	public double evaluate(Variable varname, List<Variable> variables)
	{
		return EquationSolving.evaluate(pieces, mainid, varname.name, variables);
	}
	
	public double evaluate(List<Variable> variables)
	{ // Evaluate for expression WITHOUT EQUAL SIGN
		return EquationSolving.evaluate(pieces, mainid, variables);
	}
	
	public void loadConstants(List<Variable> allConstants)
	{
		pieces.constants = allConstants;
		//Constants will be stored in the PieceList -- the database containing all equation data.
	}
	
	public List<Integer> findInvolved()
	{
		return findInvolved(mainid);
	}
	
	public List<Integer> findInvolved(int relevantId)
	{
		List<Integer> output = new ArrayList<>();
		EquationSolving.findInvolved(output, relevantId, pieces);
		for(int i=0;i<output.size();++i)
		{
			for(int j=0;j<i;++j)
			{
				if(output.get(j) == output.get(i))
				{
					output.remove(i);
					break;
				}
			}
		}
		return output;
	}
	
	public List<Variable> findVariables()
	{
		return findVariables(mainid);
	}
	
	public List<Variable> findVariables(int relevantId)
	{
		List<Variable> output = new ArrayList<>();
		EquationSolving.findVariables(output, relevantId, pieces);
		return output;
	}
	
	public Equation solveFor(String varname)
	{
		return solveFor(mainid, varname);
	}
	
	public Equation solveFor(int relevantId, String varname)
	{
		int newid = EquationSolving.solveFor(pieces, relevantId, varname);
		Equation output = isolate(newid);
		// later, maybe remove unneeded? But be careful not to mess up the maineq.
		return output;
	}
	
	public Equation isolate(int subid)
	{
		List<Integer> oldpieces = findInvolved(subid);
		Equation output = new Equation();
		output.pieces.constants = pieces.constants;
		for(int i=0;i<oldpieces.size();++i)
		{
			int nxtid = output.pieces.add();
			output.pieces.at(nxtid).transfer(pieces.at(oldpieces.get(i))); // transfer
			for(int j=0;j<oldpieces.size();++j)
			{ // replace all Piece locations accordingly for output Equation.
			  // Look at every possible piece id
				Piece tpiece = output.pieces.at(i);
				for(int k=0;k<tpiece.length();++k)
				{ // look at each individual piece for this specific piece id
					if(tpiece.at(k).type == eqel.pieceType && tpiece.at(k).pieceLocation == oldpieces.get(j))
					{
						tpiece.at(k).pieceLocation = j;
					}
				}
			}
		}
		return output;
	}
	
	public int remove(int subid)
	{
		List<Integer> oldpieces = findInvolved(subid);
		return remove(oldpieces);
	}
	
	public int remove(List<Integer> unneededpieces)
	{
		for(int i=0;i<unneededpieces.size();++i)
		{
			Piece empty = new Piece();
			pieces.at(i).transfer(empty);
		}
		return mainid;
	}
	
	
}
