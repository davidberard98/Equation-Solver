===Compiling From Command Line===
```
javac -sourcepath src -d build/ src/*.java
java -cp build/ equationsolving.EquationSolving
```

==Use==
-Currently, do not type *- eg. "4 * - 2".  Instead, type 4*(-2).

===Ideas===
- Make it so you don't need to separate unknown variables from known ones in List<Variable> when evaluating:just pass in List<Variable> and it'll figure out for you.
- Order of operations.  Once we have no parentheses, then we should try to:
	* Turn logs, exponents, factorials into new Pieces
	* Re-order to have no parentheses with arithmatic.
- Use a more precise type of number instead of double, since it will be doing inefficient calculations (rounding repeatedly)

===Future functions===
- +, -, *, /; logs, exponents, powers.
- constants including pi, e...!!! This is type 8! <span style="font-weight:900;color:green;">DONE</span>

==REDO==
I should redo it so that it goes from List<String> -> List<eqel> before being split into Pieces.

That way, it'll be easier to insert parentheses around ln, etc...

... NEVER MIND! 

Compress "multiplyCheck" "logCheck" etc into a single generic function
