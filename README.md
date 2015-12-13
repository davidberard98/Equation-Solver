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
