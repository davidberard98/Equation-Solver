===Compiling From Command Line===
```
javac -sourcepath src -d build/ src/*.java
java -cp build/ equationsolving.EquationSolving
```

===Ideas===
- Order of operations.  Once we have no parentheses, then we should try to:
	* Turn logs, exponents, factorials into new Pieces
	* Re-order to have no parentheses with arithmatic.

===Future functions===
- +, -, *, /; logs, exponents, powers.
- constants including pi, e...!!! This is type 8!

==REDO==
I should redo it so that it goes from List<String> -> List<eqel> before being split into Pieces.

That way, it'll be easier to insert parentheses around ln, etc...
