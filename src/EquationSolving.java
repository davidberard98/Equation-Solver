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
        int id = alleq.add();
        eqel abc = new eqel("a");
        Piece tpiece = alleq.at(id);
        
        tpiece.add(abc);
        System.out.println(tpiece.length());
        tpiece.add(abc);
        System.out.println(tpiece.length());
        tpiece = null;
        
        System.out.println(alleq.at(id).length() + "\n\n");
        
        List<String> parts = basicParse(input);
        int mainid = parse(parts, alleq);
        
        orderParse(mainid, alleq);
        
        alleq.display();
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
				if(ctype != eqel.whitespaceType) {
					parts.add(current);
				}
				current = "";
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
    
    public static int parse(List<String> parts, PieceList pls)
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
			int beginid = parse(beginparts, pls);
			int endid = parse(endparts, pls);
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
				int parenSectionId = parse(insideParts, pls);
				eqel thisPiece = new eqel(parenSectionId, eqel.pieceType);
				output.add(thisPiece);
			}
			else
			{
				eqel thisval = new eqel(parts.get(i));
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
			logCheck(involved.get(i), pls);
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
	
	public static List<Integer> logCheck(int mainid, PieceList pls)
	{
		List<Integer> newpieces = new ArrayList<>();
		Piece tpiece = pls.at(mainid);
		int logcount = 0;
		for(int i=0;i<tpiece.length();++i)
		{
			//System.out.println("    " + ((int) tpiece.at(i).otherValue) + " vs "  + ((int) eqel.naturallog) + " & " + ((int) eqel.log));
			if(tpiece.at(i).type == eqel.operatorType 
			&& (tpiece.at(i).otherValue == eqel.naturallog || tpiece.at(i).otherValue == eqel.log)) {
				++logcount;
			}
		}
		if(logcount > 0 && tpiece.length() > 2) 
		{ // if() is to prevent recursion when you have something already separated.
			
			for(int i=0;i<tpiece.length();++i) 
			{
				if(tpiece.at(i).type == eqel.operatorType && tpiece.length() > i+1
				&& (tpiece.at(i).otherValue == eqel.naturallog || tpiece.at(i).otherValue == eqel.log)) 
				{
					eqel function = tpiece.at(i);
					eqel xval = tpiece.at(i+1);
					
					int newid = pls.add();
					Piece replacement = pls.at(newid);
					replacement.add(function);
					replacement.add(xval);
					
					tpiece.allElements.remove(i);
					tpiece.allElements.remove(i);
					
					eqel pieceElement = new eqel(newid, eqel.pieceType);
					tpiece.allElements.add(i, pieceElement);
					
					newpieces.add(newid);
				}
			}
			
		}
		return newpieces;
	}
	
	public static List<Integer> powerCheck(int mainid, PieceList pls)
	{
		List<Integer> newpieces = new ArrayList<>();
		Piece tpiece = pls.at(mainid);
		int powercount = 0;
		for(int i=0;i<tpiece.length();++i)
		{
			if(tpiece.at(i).type == eqel.operatorType && tpiece.at(i).otherValue == eqel.power)
				++ powercount;
		}
		if(powercount > 0 && tpiece.length() > 3)
		{
			// stuff..
		}
		return newpieces;
	}

    
}
