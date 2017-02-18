#ifndef _CEXP_H_
#define _CEXP_H_
#include "Bexp.h"

/*
* Commands contain the functions as below.
* c :: = skip
* 		| x ::= e
*		| c1; c2
*		| if b then c1(or c1;c1';c1''....) else c2(or c2; c2'; c2''.....)
*		| while b do c(or c1;c2;c3....)
*/
// base class served as an interface.
class Cexp
{
public:
	// used to interpreter commands.
	virtual Cexp * interpreter() {return NULL;}
	// print commands.
	virtual void printCommand() { cout<<"base class"<<endl;}
};
//skip
class Skip : public Cexp
{
public:
	Cexp * interpreter() 
	{
		return NULL;
	}
	void printCommand() { cout<<"Skip; "; }
};
// c1;c2
class Blocks : public Cexp
{
public:
	Cexp * c1;
	Cexp * c2;
	Blocks(Cexp * c1, Cexp * c2)
	{
		this->c1 = c1;
		this->c2 = c2;
	}
	Cexp * interpreter()
	{
		if(typeid(*c1) == typeid(Skip))
			return c2;
		return (new Blocks(c1->interpreter(), c2));
	}
	void printCommand() {
		c1->printCommand(); c2->printCommand();
	};
};
// x = exp
class Assign : public Cexp
{
private:
	map<string, int> * mInt;
	map<string, bool> * mBool;
	IntVar * int_name;
	BoolVar * bool_name;
	Aexp * aexp;
	Bexp * bexp;
public:
	Assign(IntVar * name, Aexp * exp, map<string, int> * m)
	{
		int_name = name;
		aexp = exp;
		this->mInt = m;
		bool_name = NULL;
		bexp = NULL;
		this->mBool = NULL;
	}
	Assign(BoolVar * name, Bexp * exp, map<string, bool> * m)
	{
		bool_name = name;
		bexp = exp;
		this->mBool = m;
		int_name = NULL;
		aexp = NULL;
		this->mInt = NULL;
	}

	Cexp * interpreter()
	{
		if(mBool == NULL)
		{
			(*mInt)[int_name->name] = aexp->eval();
		}	
		else
		{
			(*mBool)[bool_name->name] = bexp->eval();
		}
		return (new Skip());
	}
	void printCommand()
	{
		if(mBool == NULL)
		{
			cout<<int_name->name<<"=";
			aexp->printAexp();
			cout<<"; ";
		}
		else
		{
			cout<< bool_name->name <<"=";
			bexp->printBexp();
			cout<<"; ";
		}
	}
	
};
// if then else
class IfElse : public Cexp
{
private:
	Bexp * b;
	Cexp * c1;
	Cexp * c2;
public:
	IfElse(Bexp * b, Cexp * c1, Cexp * c2)
	{
		this->b = b;
		this->c1 = c1;
		this->c2 = c2;
	}
	Cexp * interpreter()
	{
		if(b->eval())
		{
			return c1;
		}
		else
		{
			return c2;
		}	
	}
	void printCommand()
	{
		cout<<"if "; b->printBexp(); cout<<" then "; 
		c1->printCommand(); cout<<" else "; c2->printCommand();
	}
};
// while b do c
class While : public Cexp
{
private:
	Bexp * b;
	Cexp * c;
public:
	While(Bexp * b, Cexp * c)
	{
		this->b = b;
		this->c = c;
	}
	Cexp * interpreter()
	{
		if(b->eval())
			return new Blocks(c, new While(b, c));
		return (new Skip());
	}
	void printCommand()
	{
		cout<<"while "; b->printBexp(); cout<<" do "; c->printCommand();
	}
};


#endif