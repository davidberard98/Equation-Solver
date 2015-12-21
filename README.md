===Compiling From Command Line===
```
javac -sourcepath src -d build/ src/*.java
java -cp build/ Example
```

===Compiling===
Place the java files wherever you want; however, in the java files when you need to use this code:
```java
import equationsolving.*;
// and later...
equationsolving.EquationSolving.parse(String input);
```

If "equationsolving" is too long, it can be changed by changing the
```
package equationsolving;
```
at the top of each file.

==Notes==
- Currently, do not type "* -" eg. "4 * - 2".  Instead, type 4*(-2).

==General Use==
To start off, you need to have a "database" to store the info for an equation
```java
equationsolving.PieceList equationData = new equationsolving.PieceList();
```

Also, it should be noted that this is designed to have the ability to load additional
constants.  Constants are stored in a separate file, and must be loaded into the 
PieceList after creating an instance of a PieceList.  Correct the path or place constants.xml
in the build environment area/by the jar file.

```java
equationsolving.EquationSolving.loadConstants(equationData, "constants.xml");
```

Now that you've set up the database, presumably you'll have an equation that you want to parse, perhaps "4y+z=ln(x)"
Once you have that as a string, (String sequation), then:

```java
int mainid = equationsolving.EquationSolving.parse(sequation, equationData);
```

Note that a "PieceList" (the database) is really just an array of "Pieces" -- which themselves are 
simple pieces of equations.  A piece is composed only of elements where order of operations does not
matter: "a+b+c" might be a piece, but not "a^b+c".  When order of operations is important, it splits
the piece into multiple.  "a^b+c" becomes "\[2\]+c" where \[2\] is a Piece that is "a^b".  Therefore, it is
useful to have a database to hold these Pieces.  And, each Piece has an ID, or the index of the Piece in
the database.  The parse(String, PieceList) function returns the ID of the main Piece representing the
string you entered.

Keep both the **mainid** and the **equationData** as these two contain the important info about the equation.

===Finding Variables===

Once reading the equation, one can find what the variables are (which is often useful)

```java
// Beginning: including things:
import java.util.ArrayList;
import java.util.List;
// Later
List<equationsolving.Variable> variableList = new ArrayList<>();
equationsolving.EquationSolving.findVariables(variableList, mainid, equationData);
```

It will fill the List with the variables found.  One can access the variable names (eg x, y, z, m, etc.):

```java
for(int i=0;i<variableList.size();++i)
{
	System.out.println(variableList.get(i).name);
}
```

This will later become useful when solving for a certain variable, or when defining variables so that an
expression can be evaluated

===Evaluating An Expression===

This is for when there is NOT an equal sign.

First, fill the variables with values

```java
for(int i=0;i<variableList.size();++i)
{
	double thisValue;
	// somehow decide on the values for the variable, perhaps ask user
	variableList.get(i).value = thisValue;
}
```

Then, evaluate:

```java
equationsolving.Piece toBeEvaluated = equationData.at(mainid);
double result = toBeEvaluated.evaluate(variableList, equationData);
```

or, more condensed:

```java
double result = equationData.at(mainid).evaluate(variableList, equationData);
```

===Solving An Equation===

This is when there IS an equal sign, and you want to isolate a certain variable on one side.

```java
String solveForVariable = "x"; // The variable you want alone
int solvedEquationId = equationsolving.EquationSolving.solveFor(equationData, mainid, solveForVariable);
```

===Evaluating for a Variable In An Equation===

This is for when there IS an equal sign, and you want get the value of a certain variable

You need to have the values for all but one variable.  From variableList, fill in values for all but one;
remove that one.  For example:

```java
Scanner scanner = new Scanner( System.in );
int specialIndex = -1;
for(int i=0;i<variableList.size();++i)
{
	System.out.print("Value of " + variableList.get(i).name + " (? to solve for): ");
	String stringValue = scanner.nextLine();
	if(stringValue.equals(new String("?")))
	{
		specialIndex = i;
	}
	else
	{
		variableList.get(i).value = Double.parseDouble(stringValue);
	}
}
String solveForVariable = variableList.get(specialIndex).name;
variableList.remove(specialIndex);
```

```java
double result = equationsolving.EquationSolving.evaluate(equationData, mainid, solveForVariable, variableList);
```

