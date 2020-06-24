<H2>
  Reverse Polish notation (or postfix notation) calculator:
</H2>
<UL>
  <LI>
    <A HREF="#what">What is RPN?</A>
  <LI>
    <A HREF="#learn">Learning RPN</A>
</UL>
<H2>
  <A NAME="what"><!-- --></A>What is RPN?
</H2>
<P>
In the 1920's, Jan Lukasiewicz developed a formal logic system which allowed
mathematical expressions to be specified without parentheses by placing the
operators before (prefix notation) or after (postfix notation) the operands.
For example the (infix notation) expression
<PRE>(4 + 5) &#215; 6
</PRE>
<P>
could be expressed in prefix notation as
<PRE>&#215; 6 + 4 5    or    &#215; + 4 5 6
</PRE>
<P>
and could be expressed in postfix notation as
<PRE>4 5 + 6 &#215;    or    6 4 5 + &#215;
</PRE>
<P>
Prefix notation also came to be known as Polish Notation in honor of Lukasiewicz.
HP adjusted the postfix notation for a calculator keyboard, added a stack
to hold the operands and functions to reorder the stack. HP dubbed the result
Reverse Polish Notation (RPN) also in honor of Lukasiewicz.
<H2>
  <A NAME="learn"><!-- --></A>Learning RPN
</H2>
<P>
Do you remember how you originally learned to do math? Most of us were taught
to write down the numbers we wanted to add and then add them like:
<PRE>    25
    12
   ----
    37
</PRE>
<P>
RPN works the same way. Take your new calculator and key in 25. Press the
ENTER key to tell the calculator that you are finished keying this number.
Now key in 12 and tell the calculator to add it to the previous number by
pressing the + key. The result of 37 will immediately be displayed. Subtraction,
multiplication and division all work the same way but with the -, &#215;,
and &#247; keys substituted for the + key. Try it!
<P>
This also works for more than two numbers. To multiply the numbers 5, 6 and
7 together press 5 ENTER 6 &#215;7 &#215; and read the result. Note that
you didn't press ENTER after the 2nd and 3rd numbers because the operation
key makes it clear that you are finished keying these numbers.
<P>
Many functions require only one number. On an RPN calculator, you still enter
the number and then press the operation key and see the result. (Many calculators
that claim to be algebraic use the same method since it takes less keystrokes
than real algebraic syntax.) For example, to compute the sine of 10 press
1 0 SIN and read the result. To compute e<SUP>5</SUP> press 5 e<SUP>x</SUP>.
<P>
Just remember that RPN calculators perform mathematical operations immediately
when you press the operation keys so the number(s) must be entered first.
There are no "pending operations" or precedence in RPN calculators. When
multiple numbers must be entered in sequence, separate them with the ENTER
key.
<P>
You now know how to use your calculator in the most basic way. The beauty
of RPN is that this model extends to arbitrarily complex expressions without
parentheses and precedence rules. To understand this, you'll want to know
more about the stack.