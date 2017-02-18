/*
* course work "Advanced Programming language" writen by Java.
* name @ Lixue Zhang
* student id @ 1528172
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class pro
{
	/*
	* Arithmetic expression contains the functions as below.
	* e :: = n
	* 		| x
	*		| e1+e2
	*		| e1-e2
	*		| e1*e2
	*		| e1／e2
	* testAexp() : test the interpreter of expressions.
	* a hash table is used to store current variables as <name, value> pair.
	*/
	public static void testAexp()
	{
		// init hash map to save 2 variables: x and y. 
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("x", "7");
		map.put("y", "3");
		System.out.println("x = "+map.get("x")+"; y = "+map.get("y"));
		// case 1: x + y
		int p = new AexpAdd(new AexpVarName("x", map), new AexpVarName("y", map)).eval();
		System.out.println("x+y = "+p);
		// case 2: x * (6-4)
		p = new AexpMul(new AexpVarName("x", map), new AexpDec(new AexpInt(6), new AexpInt(4))).eval();
		System.out.println("x * (6-4) = "+p);
		// case 3: (x*2)/(y+9)
		p = new AexpDiv(new AexpMul(new AexpVarName("x", map), new AexpInt(2)),
						new AexpAdd(new AexpVarName("y", map), new AexpInt(9))).eval();
		System.out.println("(x*2)/(y+9) = "+p);
	}
	/*
	* Boolean expression contains the functions as below.
	* b :: = true
	* 		| false
	*		| e1=e2
	*		| e1<e2
	*		| e1>e2
	*		| !b
	*		| b1 && b2
	*		| b1 || b2
	* testBexp() : test the interpreter of expressions.
	* a hash table is used to store current variables as <name, value> pair.
	*/
	public static void testBexp()
	{
		// init : x = true, y = false.
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("x", "true");
		map.put("y", "false");
		// case 1: output x and y.
		boolean b1 = new BexpVarName("x", map).eval();
		boolean b2 = new BexpVarName("y", map).eval();
		System.out.println("x = "+b1+"; y = "+b2);
		// case 2: x && y,  x || y, not x
		b1 = new BexpAnd(new BexpVarName("x", map), new BexpVarName("y", map)).eval();
		b2 = new BexpOr(new BexpVarName("x", map), new BexpVarName("y", map)).eval();
		boolean b3 = new BexpNot(new BexpVarName("x", map)).eval();
		System.out.println("x and y = "+b1+"; x or y = "+b2+"; not x = "+b3);
		// case 3: 5 > 3, 5 < 3, 5 = 2 + 3
		b1 = new BexpGreater(new AexpInt(5), new AexpInt(3)).eval();
		b2 = new BexpLess(new AexpInt(5), new AexpInt(3)).eval();
		b3 = new BexpEqual(new AexpInt(5), new AexpAdd(new AexpInt(2), new AexpInt(3))).eval();
		System.out.println("5>3="+b1+"; 5<3="+b2+"; 5=2+3 is "+b3);
	}
	/*
	* Commands contain the functions as below.
	* c :: = skip
	* 		| x ::= e
	*		| c1; c2
	*		| if b then c1(or c1;c1';c1''....) else c2(or c2; c2'; c2''.....)
	*		| while b do c(or c1;c2;c3....)
	* testComm() : test the interpreter of commands.
	* a hash table is used to store current variables as <name, value> pair.
	* both boolean and int can be saved in the same hash table.
	*/
	public static void testComm()
	{
		//init : x=12, y=true, z=false
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("x", "12");
		map.put("y", "true");
		map.put("z", "false");
		System.out.println("init state: x = "+map.get("x")+"; y = "+map.get("y")+"; z = "+map.get("z")+";");
		// case 1: x = 9, assign 9 to x.
		new Assign("x", new AexpInt(10), map).interpreter();
		System.out.println("assign 9 to x: x = "+map.get("x"));
		// case 2: y = z assign value using current variable.
		System.out.println("before assign z to y: y = "+map.get("y"));
		new Assign("y", new BexpVarName("z", map), map).interpreter();
		System.out.println("after assign z to y : y = "+map.get("y"));
		// case 3: c1;c2, multiple commands, test Blocks function.
		// Blocks used to interprete all kinds of combinations of commands recursively.
		Comm group[] = new Comm[3];
		group[0] = new Assign("u", new AexpInt(3), map);
		group[1] = new Assign("p", new BexpBool("true"), map);
		group[2] = new Assign("q", new BexpBool("false"), map);
		new Blocks(group[0], new Blocks(group[1], group[2], map), map).interpreter();
		System.out.println("Blocks test (c1;c2): u = "+map.get("u")+"; p = "+map.get("p")+"; q = "+map.get("q"));

		// case 4: if-else( if 5>3 then x *= 2 else x /= 2 )
		System.out.println("if-else test: before if-else: x = "+map.get("x"));
		new IfElse(new BexpGreater(new AexpInt(5), new AexpInt(3)), 
				   new Assign("x", new AexpMul(new AexpVarName("x", map), new AexpInt(2)), map), 
				   new Assign("x", new AexpDiv(new AexpVarName("x", map), new AexpInt(2)), map)).interpreter();
		System.out.println("if-else test: after if-else: x = "+map.get("x"));
		// case 5: while test.
		// code :  x = 10; sum = 0;
		//		   while(x>0) { sum+=x ; x -= 1;}
		// so result should be 55 (sum = 10+9+8+...+1).
		// assign 10 to x;
		new Assign("x", new AexpInt(10), map).interpreter();
		//assign 0 to sum;
		new Assign("sum", new AexpInt(0), map).interpreter();
		// setup command group used in while : sum+= x; x-= 1;
		Comm cc[] = new Comm[2];
		cc[0] = new Assign("sum", new AexpAdd(new AexpVarName("sum", map), new AexpVarName("x", map)), map);
		cc[1] = new Assign("x", new AexpDec(new AexpVarName("x", map), new AexpInt(1)), map);
		Bexp b = new BexpGreater(new AexpVarName("x", map), new AexpInt(0));
		// while b, 2 commands (combined in a Block).
		new While(b, new Blocks(cc[0], cc[1], map)).interpreter();
		System.out.println("sum(10+9+...+1) = "+map.get("sum"));
		// case 6: Skip do nothing here.
		new Skip().interpreter();
	}

	public static void main(String []argv)
	{
		System.out.println("\n----->The test results of Arithmetic expressions : ");
		testAexp();
		System.out.println("\n----->The test results of Boolean expressions : ");
		testBexp();
		System.out.println("\n----->The test results of Commands : ");
		testComm();
		System.out.println("");
	}
}

/*********************** Arithmatic expression interpreter *********************/
// Base class for arithmatic expressions, 
// eval() function is used to interprete arithmatic expressions. 
abstract class Aexp
{
	abstract public int eval();
}
// interprete int value.
class AexpInt extends Aexp
{
	private int e;
	public AexpInt(int a) { e = a;}
	public int eval() {return e;}
}
// interprete a variable from current variable group "map".
// map <variable name, value>
class AexpVarName extends Aexp
{
	private int e;
	private String name;
	private HashMap<String, String> map;
	public AexpVarName(String name, HashMap<String, String> map)
	{
		this.map = map;
		this.name = name;
		if(map.containsKey(name))
			e = Integer.parseInt(map.get(name));
		else
		{
			e = 0;
			map.put(name, 0+"");
		}
	}
	public int eval() 
	{
		if(map.containsKey(name))
			e = Integer.parseInt(map.get(name));
		return e;
	}
}
// interprete arithmetic add expression.
class AexpAdd extends Aexp
{
	private Aexp left, right;
	public AexpAdd(Aexp left, Aexp right)
	{
		this.left = left;
		this.right = right;
	}
	public int eval()
	{
		return left.eval() + right.eval();
	}
}
// interprete arithmetic subtraction expression.
class AexpDec extends Aexp
{
	private Aexp left, right;
	public AexpDec(Aexp left, Aexp right)
	{
		this.left = left;
		this.right = right;
	}
	public int eval()
	{
		return left.eval() - right.eval();
	}
}
// interprete arithmetic multiply expression.
class AexpMul extends Aexp
{
	private Aexp left, right;
	public AexpMul(Aexp left, Aexp right)
	{
		this.left = left;
		this.right = right;
	}
	public int eval()
	{
		return left.eval() * right.eval();
	}
}
// interprete arithmetic divide expression.
class AexpDiv extends Aexp
{
	private Aexp left, right;
	public AexpDiv(Aexp left, Aexp right)
	{
		this.left = left;
		this.right = right;
	}
	public int eval()
	{
		return left.eval() / right.eval();
	}
}

/*********************** Boolean expression interpreter *********************/
// base class. 
abstract class Bexp
{
	abstract public boolean eval();
}
// interprete b::= true/false operation.
class BexpBool extends Bexp
{
	public boolean b;
	public BexpBool(String s)
	{
		if(s.equalsIgnoreCase("TRUE"))
			b = true;
		else
			b = false;
	}
	public boolean eval()
	{
		return b;
	}
}
// interprete x : find its value from current variable set.
class BexpVarName extends Bexp
{
	private boolean b;
	private String name;
	private HashMap<String, String> map;
	public BexpVarName(String name, HashMap<String, String> map)
	{
		this.map = map;
		this.name = name;
		if(map.containsKey(name))
			b = Boolean.parseBoolean(map.get(name));
		else
		{
			b = false;
			map.put(name, "false");
		}
	}
	public boolean eval() 
	{
		if(map.containsKey(name))
			b = Boolean.parseBoolean(map.get(name));
		return b;
	}
}
// interprete e1 > e2 operation.
class BexpGreater extends Bexp
{
	public Aexp left, right;
	public BexpGreater(Aexp left, Aexp right)
	{
		this.left = left;
		this.right = right;
	}
	public boolean eval()
	{
		int temp = left.eval() - right.eval();
		if(temp>0)
			return true;
		else
			return false;
	}
}
// interprete e1 < e2 operation.
class BexpLess extends Bexp
{
	public Aexp left, right;
	public BexpLess(Aexp left, Aexp right)
	{
		this.left = left;
		this.right = right;
	}
	public boolean eval()
	{
		int temp = left.eval() - right.eval();
		if(temp<0)
			return true;
		else
			return false;
	}
}
// interprete e1 = e2 operation.
class BexpEqual extends Bexp
{
	public Aexp left, right;
	public BexpEqual(Aexp left, Aexp right)
	{
		this.left = left;
		this.right = right;
	}
	public boolean eval()
	{
		int temp = left.eval() - right.eval();
		if(temp==0)
			return true;
		else
			return false;
	}
}
// interprete "b1 && b2" operation.
class BexpAnd extends Bexp
{
	public Bexp left, right;
	public BexpAnd(Bexp left, Bexp right)
	{
		this.left = left;
		this.right = right;
	}
	public boolean eval()
	{
		if(left.eval() && right.eval())
			return true;
		else
			return false;
	}
}
// interprete "b1 || b2" operation.
class BexpOr extends Bexp
{
	public Bexp left, right;
	public BexpOr(Bexp left, Bexp right)
	{
		this.left = left;
		this.right = right;
	}
	public boolean eval()
	{
		if(left.eval() || right.eval())
			return true;
		else
			return false;
	}
}
// interprete "not b" operation.
class BexpNot extends Bexp
{
	public Bexp b;
	public BexpNot(Bexp b)
	{
		this.b = b;
	}
	public boolean eval()
	{
		if(b.eval())
			return false;
		return true;
	}
}
/*********************** Commands interpreter *********************/
// base class
// objects can call interpreter() to interprete commands.
abstract class Comm
{
	abstract public void interpreter();
}
// do nothing, no variable state would change by this command.
// so i only print a sentence to show it has been called. 
class Skip extends Comm
{
	public void interpreter() 
	{
		System.out.println("skip is called, just for test, no state change!");
	};
}
// interprete "c1;c2" commands by combining to a block. 
class Blocks extends Comm
{
	public Comm c1;
	public Comm c2;
	public HashMap<String, String> map;
	public Blocks(Comm c1, Comm c2, HashMap<String, String> map)
	{
		this.c1 = c1;
		this.c2 = c2;
		this.map = map;
	}
	public void interpreter()
	{
		c1.interpreter();
		c2.interpreter();
	}
}
// interprete "x=3, y=false" opertions.
class Assign extends Comm
{
	HashMap<String, String> map;
	String var_name;
	Aexp aexp;
	Bexp bexp;
	public Assign(String name, Aexp exp, HashMap<String, String> map)
	{
		var_name = name;
		this.aexp = exp;
		this.map = map;
		bexp = null;
	}
	public Assign(String name, Bexp exp, HashMap<String, String> map)
	{
		var_name = name;
		this.bexp = exp;
		this.map = map;
		aexp = null;
	}
	public void interpreter()
	{
		if(aexp==null)
		{
			map.put(var_name, bexp.eval()+"");
		}
		else
			map.put(var_name, aexp.eval()+"");
	}
	
}
// interprete "if-then-else" opertion.
class IfElse extends Comm
{
	public Bexp b;
	public Comm c1;
	public Comm c2;
	public IfElse(Bexp b, Comm c1, Comm c2)
	{
		this.b = b;
		this.c1 = c1;
		this.c2 = c2;
	}
	public void interpreter()
	{
		if(b.eval())
			c1.interpreter();
		else
			c2.interpreter();
	}
}
// interprete "while b do c" opertion.
class While extends Comm
{
	public Bexp b;
	public Comm c;
	public While(Bexp b, Comm c)
	{
		this.b = b;
		this.c = c;
	}
	public void interpreter()
	{
		if(b.eval())
		{
			c.interpreter();
			this.interpreter();
		}
	}
}
