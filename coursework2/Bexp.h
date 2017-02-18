#ifndef _BEXP_H_
#define _BEXP_H_
#include "Aexp.h"

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
*/

// base class served as interface.  
class Bexp
{
public:
	// produce suitable output.
	virtual bool eval() {return false;}
	// print commands.
	virtual void printBexp() {}
};
// single variable like x.
class BoolVar : public Bexp
{
public:
	BoolVar(string name, map<string, bool> *m)
	{
		this->name = name;
		this->m = m;
	}

	bool eval()
	{
		return (*m)[name];
	}
	void printBexp() { cout<<name;}
	map<string, bool> *m;
	string name;	
};
// interprete b::= true/false operation.
class BexpBool : public Bexp
{
private:
	bool  b;
public:
	BexpBool(bool s) {b = s;}
	bool eval() {return b;}
	void printBexp() {
		if(b) cout<<"True";
		else
			cout<<"False";
	}
};

// interprete e1 > e2 operation.
class BexpGreater : public Bexp
{
private:
	Aexp *left, *right;
public:
	BexpGreater(Aexp *left, Aexp *right)
	{
		this->left = left;
		this->right = right;
	}
	bool eval()
	{
		int temp = left->eval() - right->eval();
		if(temp>0)
			return true;
		else
			return false;
	}
	void printBexp() { 
		left->printAexp(); cout<<" > "; right->printAexp();
	}
};
// interprete e1 < e2 operation.
class BexpLess : public Bexp
{
private:
	Aexp *left, *right;
public:
	BexpLess(Aexp *left, Aexp *right)
	{
		this->left = left;
		this->right = right;
	}
	bool eval()
	{
		int temp = left->eval() - right->eval();
		if(temp<0)
			return true;
		else
			return false;
	}
	void printBexp() { 
		left->printAexp(); cout<<" < "; right->printAexp();
	}
};
// interprete e1 = e2 operation.
class BexpEqual : public Bexp
{
private:
	Aexp *leftA, *rightA;
	Bexp *leftB, *rightB;
public:
	BexpEqual(Aexp *left, Aexp *right)
	{
		this->leftA = left;
		this->rightA = right;
		this->leftB = NULL;
		this->rightB = NULL;
	}
	BexpEqual(Bexp *left, Bexp *right)
	{
		this->leftA = NULL;
		this->rightA = NULL;
		this->leftB = left;
		this->rightB = right;
	}
	bool eval()
	{
		if(leftA==NULL || rightA==NULL)
		{
			if(leftB->eval() == rightB->eval())
				return true;
			else
				return false;
		}
		else
		{
			int temp = leftA->eval() - rightA->eval();
			if(temp<0)
				return true;
			else
				return false;
		}
	}
	void printBexp() { 
		if(leftA==NULL || rightA==NULL)
			{leftB->printBexp(); cout<<" = "; rightB->printBexp();}
		else
			{leftA->printAexp(); cout<<" = "; rightA->printAexp();}
	}
};
// interprete "b1 && b2" operation.
class BexpAnd : public Bexp
{
private:
	Bexp * left, * right;
public:
	BexpAnd(Bexp * left, Bexp * right)
	{
		this->left = left;
		this->right = right;
	}
	bool eval()
	{
		if(left->eval() && right->eval())
			return true;
		else
			return false;
	}
	void printBexp()
	{
		left->printBexp();cout<<" && "; right->printBexp();
	}
};
// interprete "b1 || b2" operation.
class BexpOr : public Bexp
{
private:
	Bexp * left, * right;
public:
	BexpOr(Bexp * left, Bexp * right)
	{
		this->left = left;
		this->right = right;
	}
	bool eval()
	{
		if(left->eval() || right->eval())
			return true;
		else
			return false;
	}
	void printBexp()
	{
		left->printBexp(); cout<<" || "; right->printBexp();
	}
};
// interprete "not b" operation.
class BexpNot : public Bexp
{
private:
	Bexp * b;
public:
	BexpNot(Bexp * b)
	{
		this->b = b;
	}
	bool eval()
	{
		if(b->eval())
			return false;
		return true;
	}
	void printBexp()
	{
		cout<<" not "; b->printBexp();
	}
};

#endif