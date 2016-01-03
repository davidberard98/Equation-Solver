/**
 * @author David Berard
 * 
 * Variable name & value.
 */

package equationsolving;

public class Variable 
{
	public String name;
	public double value;

	public int typeId; // in case unit information is needed
	public int unitId;
	
	public Variable() {}
	public Variable(String nname) {name = nname;}
	public Variable(String nname, double val) {name = nname; value = val;}
	public void display() {
		System.out.println(name + "=" + value);
	}
	
}
