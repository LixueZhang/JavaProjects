#ifndef _AEXP_H_
#define _AEXP_H_

/*
* Arithmetic expression contains the functions as below.
* e :: = n
* 		| x
*		| e1+e2
*		| e1-e2
*		| e1*e2
*		| e1／e2
*/

// base class served as interface. 
class Aexp
{
public: 
	// produce suitable output.
	virtual int eval() { return 0;};
	// print commands. 
	virtual void printAexp() {};
};

// single variable.
class IntVar : public Aexp
{
public:
	IntVar(string name, map<string, int> * m)
	{
		this->name = name;
		this->m = m;
	}

	int eval()
	{
		return (*m)[name];
	}

	void printAexp() { cout<<name;}

	string name;
	map<string, int> *m;
};

// interprete int value.
class AexpInt : public Aexp
{
public:
	AexpInt(int a) { e = a;}
	int eval() {return e;}
	void printAexp() { cout<<e;}
private:
	int e;
};

class AexpAdd : public Aexp
{
public:
	AexpAdd(Aexp * left, Aexp * right)
	{
		this->left = left;
		this->right = right;
	}
	int eval()
	{
		return left->eval() + right->eval();
	}
	void printAexp() { 
		cout<<"(";left->printAexp(); cout<<"+"; right->printAexp(); cout<<")";
	}
private:
	Aexp * left, * right;
};

class AexpDec : public Aexp
{
public:
	AexpDec(Aexp * left, Aexp * right)
	{
		this->left = left;
		this->right = right;
	}
	int eval()
	{
		return left->eval() - right->eval();
	}
	void printAexp() { 
		cout<<"(";left->printAexp(); cout<<"-"; right->printAexp(); cout<<")";
	}
private:
	Aexp * left, * right;
};

class AexpMul : public Aexp
{
public:
	AexpMul(Aexp * left, Aexp * right)
	{
		this->left = left;
		this->right = right;
	}
	int eval()
	{
		return left->eval() * right->eval();
	}
	void printAexp() { 
		left->printAexp(); cout<<"*"; right->printAexp();
	}
private:
	Aexp * left, * right;
};

class AexpDiv : public Aexp
{
public:
	AexpDiv(Aexp * left, Aexp * right)
	{
		this->left = left;
		this->right = right;
	}
	int eval()
	{
		return left->eval() / right->eval();
	}
	void printAexp() { 
		left->printAexp(); cout<<"/"; right->printAexp();
	}
private:
	Aexp * left, * right;
};

#endif