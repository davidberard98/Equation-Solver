/**
 * @author David Berard
 */

package equationsolving;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractList;
import java.util.Arrays;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class EquationSolving {

	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner scanner = new Scanner( System.in );
        System.out.print("Your numeric equation: ");
        String input = scanner.nextLine();
        PieceList alleq = new PieceList();
        
        // READ CONSTANTS WITH XML FILE
        loadConstants(alleq, "constants.xml");
        
        // PARSE THE INPUT
        int mainid = parse(input, alleq);
        
        List<Variable> varlist = new ArrayList<>();
        findVariables(varlist, mainid, alleq);
        int special = -1;
        for(int i=0;i<varlist.size();++i)
        {
			System.out.print("Define " + varlist.get(i).name + " (? = solve for this):");
			String sval = scanner.nextLine();
			if(sval.equals(new String("?")))
				special = i;
			else
				varlist.get(i).value = Double.parseDouble(sval);
		}
		String toSolve = varlist.get(special).name;
		varlist.remove(special);
		
		System.out.println(evaluate(alleq, mainid, toSolve, varlist));
        
        /*
        //EVALUATING WITH VARIABLES
        List<Variable> varlist = new ArrayList<>();
        findVariables(varlist, mainid, alleq);
        for(int i=0;i<varlist.size();++i) // fill values
        {
			System.out.print("Define " + varlist.get(i).name + ":");
			String sval = scanner.nextLine();
			double dval = Double.parseDouble(sval);
			varlist.get(i).value = dval;
		}
		double finalval = alleq.at(mainid).evaluate(varlist, alleq);
		System.out.println("Value is " + finalval);
		
		
		//SOLVING FOR VARIABLE
		List<Variable> varlist = new ArrayList<>();
        findVariables(varlist, mainid, alleq);
        
		System.out.print("Variables include ");
		for(int i=0;i<varlist.size();++i)
		{
			System.out.print(varlist.get(i).name + " ");
		}
		System.out.print("\nSolve for which one? ");
		String chosenVar = scanner.nextLine();

		int nextid = mainid;
		while(nextid > -1)
		{
			int loccontaining = alleq.at(nextid).eqelContaining(alleq, chosenVar);
			System.out.println("From piece " + nextid + ", eqel #" + loccontaining + " has the variable " + chosenVar + ".  It is a " + alleq.at(nextid).at(loccontaining).value());
			if(alleq.at(nextid).at(loccontaining).type == eqel.pieceType)
			{
				nextid = alleq.at(nextid).at(loccontaining).pieceLocation;
			}
			else
				nextid = -1;
		}
		
		// TESTING THE REVERSE OPERATIONS..
		int solvedId = solveFor(alleq, mainid, chosenVar);
		System.out.println("Solved For ID: " + solvedId);
		
		alleq.display();
		*/
		
    }
    
    public static List<String> basicParse(String in)
    {
		List<String> parts = new ArrayList<>();
		int ctype = 0;
		String current = "";
		for(int i=0;i<in.length();++i)
		{
			int ttype = eqel.valtype(in.charAt(i));
			if(ctype != 0 && ctype != ttype) {
				if(i > 0 && ctype == eqel.numberType 
				&& ((in.charAt(i) == '-' && in.charAt(i-1) == 'E') || (in.charAt(i) == 'E')) )
				{
					ttype = eqel.numberType;
				}
				else
				{
					if(ctype != eqel.whitespaceType) {
						parts.add(current);
					}
					current = "";
				}
			}
			current += in.charAt(i);
			ctype = ttype;
		}
		if(ctype != 0 && ctype != eqel.whitespaceType && current != "") {
			// Log the last part.
			parts.add(current);
		}
		return parts;
	}
    
    public static int creatorParse(List<String> parts, PieceList pls)
    {
		// Return id of main eq.
		// Creat the main "piece" associated with this list.
		int mainid = pls.add();
		Piece main = pls.at(mainid);
		
		boolean equalExists = false;
		
		for(int i=0;i<parts.size();++i)
		{
			// check first char of part and see if it is an equal
			if(eqel.valtype(parts.get(i).charAt(0)) == eqel.equalType) {
				equalExists = true;
				//break; // Uncomment once we're done looking at output.
			}
			System.out.println(i + ":" + parts.get(i) + " : " + eqel.valtype(parts.get(i).charAt(0)));
		}
		
		if(equalExists)
		{ /*
			int beginid = pls.add();
			int endid = pls.add();
			Piece begin = pls.at(beginid);
			Piece end = pls.at(endid); */
			int position = 0;
			List<String> beginparts = new ArrayList<>();
			List<String> endparts = new ArrayList<>();
			
			for(int i=0;i<parts.size();++i) {
				if(eqel.valtype(parts.get(i).charAt(0)) == eqel.equalType)
				{
					if(position == 1)
						return -101;
					position = 1;
					continue;
				}
				if(position == 0)
					beginparts.add(parts.get(i));
				else
					endparts.add(parts.get(i));
			}
			int beginid = creatorParse(beginparts, pls);
			int endid = creatorParse(endparts, pls);
			eqel beginElement = new eqel(beginid, eqel.pieceType);
			eqel equalSign = new eqel("=");
			eqel endElement = new eqel(endid, eqel.pieceType);
			main.add(beginElement);
			main.add(equalSign);
			main.add(endElement);
		}
		else // if there's no equal sign, then worry about parentheses & stuff like that
		{
			Piece formatted = mainParse(parts, pls);  // split up
			main.transfer(formatted);
			main.id = mainid;
			formatted = null;
		}
		
		return mainid;
	}
	
	public static Piece mainParse(List<String> parts, PieceList pls)
	{
	// Split up the parse() function: this looks at the nitty gritty +, -, /, *, log.. stuff.
	
	/*
		boolean isAParen  = false;
		for(int i=0;i<parts.size();++i)
		{
			if(eqel.valtype(parts.get(i).charAt(0)) == eqel.parenType)
			{
				isAParen = true;
				break;
			}
		} */
		
		Piece output = new Piece();
		
		for(int i=0;i<parts.size();++i)
		{
			// Types: paren, whitespace (not here), number, equal (not here), variable, constant...
			int ttype = eqel.valtype(parts.get(i).charAt(0));
			if(ttype == eqel.numberType)
			{
				double numbervalue = Double.parseDouble(parts.get(i));
				eqel thisval = new eqel(numbervalue);
				output.add(thisval);
			}
			else if(ttype == eqel.parenType && eqel.parenParse(parts.get(i).charAt(0))%2 == 1)
			{
				int parenCount = 1;
				// Find the char of the closing paren.
				char nextParen = eqel.reverseParenParse(eqel.parenParse(parts.get(i).charAt(0))+1);
				List<String> insideParts = new ArrayList<>();
				int j = 0;
				for(j=1;j+i<parts.size();++j)
				{
					if(eqel.valtype(parts.get(i+j).charAt(0)) == eqel.parenType)
					{
						if(eqel.parenParse(parts.get(i+j).charAt(0)) %2 == 1)
							++parenCount;
						else
							--parenCount;
					}
					if(parenCount == 0)
					{
						if(parts.get(i+j).charAt(0) != nextParen) {
							System.out.println("::ERROR:: Parentheses not matching correctly.");
							return output;
						}
						break;
					}
					insideParts.add(parts.get(i+j));
				}
				i += j;
				int parenSectionId = creatorParse(insideParts, pls);
				eqel thisPiece = new eqel(parenSectionId, eqel.pieceType);
				output.add(thisPiece);
			}
			else
			{
				eqel thisval = new eqel(parts.get(i), pls.constants);
				output.add(thisval);
			}
		}
		return output;
	}
	
	public static void orderParse(int mainid, PieceList pls)
	{
		List<Integer> involved = new ArrayList<>();
		findInvolved(involved, mainid, pls);
		System.out.println("::::length " + involved.size());
		for(int i=0;i<involved.size();++i)
			implyMultiply(involved.get(i), pls);
		
		for(int i=0;i<involved.size();++i)
			factorialCheck(involved, involved.get(i), pls);
		for(int i=0;i<involved.size();++i)
			factorialCheck(involved, involved.get(i), pls);
		for(int i=0;i<involved.size();++i)
			logCheck(involved, involved.get(i), pls);
		for(int i=0;i<involved.size();++i)
			powerCheck(involved, involved.get(i), pls);
		//for(int i=0;i<involved.size();++i) // CURRENTLY DOESN'T DO ANYTHING USEFUL
		//	subtractCheck(involved, involved.get(i), pls);
		for(int i=0;i<involved.size();++i)
			multiplyCheck(involved, involved.get(i), pls);
	}
	
	public static void findInvolved(List<Integer> involved, int mainid, PieceList pls)
	{
		Piece tpiece = pls.at(mainid);
		involved.add(mainid);
		for(int i=0;i<tpiece.length();++i)
		{
			if(tpiece.at(i).type == eqel.pieceType) {
				findInvolved(involved, tpiece.at(i).pieceLocation, pls);
			}
		}
	}
	
	public static void factorialCheck(List<Integer> inuse, int mainid, PieceList pls)
	{
		Piece tpiece = pls.at(mainid);
		int factorialcount = 0;
		for(int i=0;i<tpiece.length();++i)
		{
			//System.out.println("    " + ((int) tpiece.at(i).otherValue) + " vs "  + ((int) eqel.naturallog) + " & " + ((int) eqel.logten));
			if(tpiece.at(i).type == eqel.operatorType && tpiece.at(i).otherValue == eqel.factorial) {
				++factorialcount;
			}
		}
		if(factorialcount > 0 && tpiece.length() > 2)
		{
			for(int i=1;i<tpiece.length();++i)
			{
				if(tpiece.at(i).type == eqel.operatorType && tpiece.at(i).otherValue == eqel.factorial) {
					int newid = repackage(tpiece, pls, i-1, i);
					inuse.add(newid);
					--i;
				}
			}
		}
	}
	
	public static void logCheck(List<Integer> inuse, int mainid, PieceList pls)
	{
		Piece tpiece = pls.at(mainid);
		int logcount = 0;
		for(int i=0;i<tpiece.length();++i)
		{
			//System.out.println("    " + ((int) tpiece.at(i).otherValue) + " vs "  + ((int) eqel.naturallog) + " & " + ((int) eqel.logten));
			if(tpiece.at(i).type == eqel.operatorType 
			&& (tpiece.at(i).otherValue == eqel.naturallog || tpiece.at(i).otherValue == eqel.logten)) {
				++logcount;
			}
		}
		if(logcount > 0 && tpiece.length() > 2) 
		{ // if() is to prevent recursion when you have something already separated.
			
			for(int i=0;i<tpiece.length();++i) 
			{
				if(tpiece.at(i).type == eqel.operatorType && tpiece.length() > i+1
				&& (tpiece.at(i).otherValue == eqel.naturallog || tpiece.at(i).otherValue == eqel.logten)) 
				{
					int newid = repackage(tpiece, pls, i, i+1);
					inuse.add(newid);
				}
			}
			
		}
	}
	
	public static void powerCheck(List<Integer> inuse, int mainid, PieceList pls)
	{
		Piece tpiece = pls.at(mainid);
		int powercount = 0;
		for(int i=0;i<tpiece.length();++i)
		{
			if(tpiece.at(i).type == eqel.operatorType && tpiece.at(i).otherValue == eqel.power)
				++ powercount;
		}
		if(powercount > 0 && tpiece.length() > 3)
		{
			for(int i=0;i<tpiece.length();++i)
			{
				if(tpiece.at(i).type == eqel.operatorType && tpiece.at(i).otherValue == eqel.power 
				&& i > 0 && tpiece.length() > i+1)
				{
					int newid = repackage(tpiece, pls, i-1,i+1);
					inuse.add(newid);
					--i;
				}
			}
		}
	}
	
	public static void implyMultiply(int mainid, PieceList pls)
	{
		Piece tpiece = pls.at(mainid);
		for(int i=0;i<tpiece.length()-1;++i)
		{ // look at each pair...
			if(canImplyMult(tpiece.at(i).type) && canImplyMult(tpiece.at(i+1).type))
			{
				eqel multiplyOperator = new eqel("*");
				tpiece.allElements.add(i+1, multiplyOperator);
			}
		}
	}
	
	public static boolean canImplyMult(char type)
	{
		if(type == eqel.numberType || type == eqel.variableType 
		|| type == eqel.pieceType || type == eqel.constantType)
			return true;
		return false;
	}
	
	public static void multiplyCheck(List<Integer> inuse, int mainid, PieceList pls)
	{ // AND Divide
		Piece tpiece = pls.at(mainid);
		int mdcount = 0;
		for(int i=0;i<tpiece.length();++i)
		{
			// eg a[*]b
			if(tpiece.at(i).type == eqel.operatorType
			&& (tpiece.at(i).otherValue == eqel.multiply || tpiece.at(i).otherValue == eqel.divide))
				++mdcount;
		}
		if(mdcount > 0 && tpiece.length() > 3)
		{
			for(int i=1;i<tpiece.length()-1;++i)
			{
				if(tpiece.at(i).type == eqel.operatorType
				&& (tpiece.at(i).otherValue == eqel.multiply || tpiece.at(i).otherValue == eqel.divide))
				{
					int newid = repackage(tpiece, pls, i-1, i+1);
					--i;
					inuse.add(newid);
				}
			}
		}
	}
	
	public static void subtractCheck(List<Integer> inuse, int mainid, PieceList pls)
	{ //DIFFERENT: checking for operator followed by -, or - at beginning.
		Piece tpiece = pls.at(mainid);
		//FIRST: check for - at beginning.
		if(tpiece.at(0).type == eqel.operatorType && tpiece.at(0).type == eqel.minus)
			tpiece.allElements.add(0, new eqel(0.0));
		//NEXT: check for - after power.
		for(int i=1;i<tpiece.length()-1;++i)
		{
			if(tpiece.at(i).type == eqel.operatorType && tpiece.at(i).type == eqel.minus
			&& tpiece.at(i-1).type == eqel.operatorType)
			{
				int newid = repackage(tpiece, pls, i, i+1);
				inuse.add(newid);
			}
		}
	}
	
	public static int repackage(Piece subject, PieceList pls, int begin, int end)
	{ // INCLUSIVE of both begin & end.
		if(subject.length() > end && subject.length() > begin && end >= begin)
		{
			int newid = pls.add();
			Piece newpiece = pls.at(newid);
			eqel replacement = new eqel(newid, eqel.pieceType);
			for(int i=begin;i<=end;++i) // transfer elements from subject to newpiece
			{
				newpiece.add(subject.at(begin));
				subject.allElements.remove(begin);
			}
			subject.allElements.add(begin, replacement);
			return newid;
		}
		return -1;
	}
	
	public static void findVariables(List<Variable> varlist, int mainid, PieceList pls)
	{
		Piece tpiece = pls.at(mainid);
		for(int i=0;i<tpiece.length();++i)
		{
			if(tpiece.at(i).type == eqel.pieceType)
				findVariables(varlist, tpiece.at(i).pieceLocation, pls);
			else if(tpiece.at(i).type == eqel.variableType)
			{
				boolean fine = true;
				for(int j=0;j<varlist.size();++j)
				{
					if(varlist.get(j).name.equals(tpiece.at(i).name))
					{
						fine = false;
						break;
					}
				}
				if(fine)
					varlist.add(new Variable(tpiece.at(i).name));
			}
		}
	}

	public static List<Variable> readXmlConstants(String filename)
	{
		List<Variable> varlist = new ArrayList<>();
		
		try{
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("constant");
			for(int i=0;i<nList.getLength();++i)
			{
				Variable tvar = new Variable();
				Node nNode = nList.item(i);
				Element eElement = (Element) nNode;
				tvar.name = eElement.getElementsByTagName("name").item(0).getTextContent();
				String sval = eElement.getElementsByTagName("value").item(0).getTextContent();
				tvar.value = Double.parseDouble(sval);
				varlist.add(tvar);
				varlist.get(i).display();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return varlist;
	}
	
	public static int solveFor(PieceList pls, int mainid, String varname)
	{
		int equalLocation = pls.at(mainid).findEqel(new eqel("="));
		if(equalLocation != 1)
			return -1;
		int varLoc = pls.at(mainid).eqelContaining(pls, varname);
		if(varLoc %2 != 0)
			return -1;
		
		Piece varSide = null;
		Piece otherSide = null;
		if(varLoc == 0)
		{
			varSide = pls.at(pls.at(mainid).at(0).pieceLocation);
			otherSide = pls.at(pls.at(mainid).at(2).pieceLocation);
		}
		else if(varLoc == 2)
		{
			varSide = pls.at(pls.at(mainid).at(2).pieceLocation);
			otherSide = pls.at(pls.at(mainid).at(0).pieceLocation);
		}
		
		List<Piece> ops = new ArrayList<>();
		varSide.reverseOperations(ops, pls, varname);
		int newOtherId = otherSide.reverseImplement(ops, pls);
		eqel newOtherContainer = new eqel(newOtherId, eqel.pieceType);
		
		int varOnlyId = pls.add();
		Piece varOnly = pls.at(varOnlyId);
		varOnly.add(new eqel(varname));
		eqel vocontainer = new eqel(varOnlyId, eqel.pieceType);
		
		int finalId = pls.add();
		Piece finalP = pls.at(finalId);
		finalP.add(vocontainer);
		finalP.add(new eqel("="));
		finalP.add(newOtherContainer);
		
		return finalId;
	}
	
	public static double evaluate(PieceList pls, int mainid, String varname, List<Variable> variables)
	{ // Solve for "varname" variable, using values in "variables" WITH AN EQUAL
		int redoneId = solveFor(pls, mainid, varname);
		Piece pieceToEvaluate = pls.at(pls.at(redoneId).at(2).pieceLocation);
		return pieceToEvaluate.evaluate(variables, pls);
	}
	
	public static double evaluate(PieceList pls, int mainid, Variable varname, List<Variable> variables)
	{ // Solve for "varname" variable, using values in "variables" WITH AN EQUAL
		return evaluate(pls, mainid, varname.name, variables);
	}
	
	public static double evaluate(PieceList pls, int mainid, List<Variable> variables)
	{ // Solve an expression NOT CONTAINING AN EQUAL
		return pls.at(mainid).evaluate(variables, pls);
	}
	
	public static void loadConstants(PieceList pls, String filename)
	{
		List<Variable> allConstants = null;
		allConstants = readXmlConstants(filename);
		pls.constants = allConstants;
		//Constants will be stored in the PieceList -- the database containing all equation data.
	}
	
	public static int parse(String input, PieceList alleq)
	{
		List<String> parts = basicParse(input);
        int mainid = creatorParse(parts, alleq);
        orderParse(mainid, alleq);
        return mainid;
	}
	
    
}
