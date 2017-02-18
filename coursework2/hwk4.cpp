/**
* author : lixue zhang, student id: 1528172
* I used java in hwk1 and hwk2. for this, i turned to c++.
**/


#include <iostream>
#include <string>
#include <map>
using namespace::std;
#include "Cexp.h"

/*
* print int type variables' state saved in hash table.
*/
void printVariables(map<string, int> * vars)
{
	cout<<"[";
	map<string, int>::iterator iter = vars->begin();
	if(iter != vars->end())
	{
		cout<<iter->first<<"="<<iter->second;
		iter++;
	}
	for(iter=iter;iter!=vars->end();iter++)
	{
		cout<<", "<<iter->first<<"="<<iter->second;
	}
	cout<<"]";
}

/*
* print bool type variables' state saved in hash table.
*/
void printVariables(map<string, bool> * vars)
{
	cout<<"[";
	map<string, bool>::iterator iter = vars->begin();
	if(iter != vars->end())
	{
		cout<<iter->first<<"=";
		if(iter->second)
			cout<<"True";
		else
			cout<<"False";
		iter++;
	}
	for(iter=iter;iter!=vars->end();iter++)
	{
		cout<<", "<<iter->first<<"=";
		if(iter->second)
			cout<<"True";
		else
			cout<<"False";
	}
	cout<<"]";
}

/*
* run and print the commands step by step.
* formate is <commands; [int type variables][bool type variables]> ---->
*/
void singleStepEval(Cexp *myCommands, map<string, int> * intVars, map<string, bool> * boolVars)
{
	int i=0;
	while(true) //(myCommands != NULL) && typeid(*myCommands) != typeid(Skip)
	{
		cout<<"<";
		myCommands->printCommand(); 
		printVariables(intVars);
		printVariables(boolVars);
		cout<<"> --->"<<endl;
		if(typeid(*myCommands)==typeid(Skip))
			break;
		myCommands = myCommands->interpreter();
	}

}
/*
* test case 1: test if-else
* x=3; if x < 5 then x=x+1 else x=x/3
*/
void testCase1()
{
	map<string, int> ht;
	map<string, bool> ht_bool;
	Cexp *cmd1 = new Assign(new IntVar("x", &ht), new AexpInt(3), &ht);
	Cexp *cmd2 = new IfElse(new BexpLess(new IntVar("x", &ht), new AexpInt(5)), 
				new Assign(new IntVar("x", &ht), new AexpAdd(new IntVar("x", &ht), new AexpInt(1)), &ht), 
				new Assign(new IntVar("x", &ht), new AexpDiv(new IntVar("x", &ht), new AexpInt(3)), &ht));
	singleStepEval(new Blocks(cmd1, cmd2), &ht, &ht_bool);
}
/*
* test case 2: test while by calculating "5!"
* val=1; num=5; while num>1 do val = val * num; num = num - 1;
*/
void testCase2()
{
	map<string, int> ht;
	map<string, bool> ht_bool;
	// val = 1
	Cexp *val = new Assign(new IntVar("val", &ht), new AexpInt(1), &ht);
	// num = 5
	Cexp *num = new Assign(new IntVar("num", &ht), new AexpInt(5), &ht);
	// val = val * num
	Cexp *val_times_num = new Assign(new IntVar("val", &ht), 
		new AexpMul(new IntVar("val", &ht), new IntVar("num", &ht)), &ht);
	// num = num - 1
	Cexp *num_dec_1 = new Assign(new IntVar("num", &ht), 
		new AexpDec(new IntVar("num", &ht), new AexpInt(1)), &ht);
	// while while num>1 do val = val * num; num = num - 1
	Cexp *whileC = new While(new BexpGreater(new IntVar("num", &ht), new AexpInt(1)), 
				new Blocks(val_times_num, num_dec_1));
	singleStepEval(new Blocks(new Blocks(val, num), whileC), &ht, &ht_bool);
}

/*
* test case 3 : test bool expressions in commands
* var1 = false; var2 = true; if var1 or var2 then var1 = var2; var2 = not var1; else Skip
*/
void testCase3()
{
	map<string, bool> ht;
	map<string, int> ht_int;
	// var1 = false
	Cexp *var1 = new Assign(new BoolVar("var1", &ht), new BexpBool(false), &ht);
	// var2 = true
	Cexp *var2 = new Assign(new BoolVar("var2", &ht), new BexpBool(true), &ht);
	// expOr = var1 or var2
	Bexp *expOr = new BexpOr(new BoolVar("var1", &ht), new BoolVar("var2", &ht));
	// if - else 
	Cexp *if_else = new IfElse(expOr, 
					new Blocks(new Assign(new BoolVar("var1", &ht), new BoolVar("var2", &ht), &ht),
						new Assign(new BoolVar("var2", &ht), new BexpNot(new BoolVar("var1", &ht)), &ht)),
					new Skip());
	singleStepEval(new Blocks(new Blocks(var1, var2), if_else), &ht_int, &ht);
}

int main(int argc, char *argv[])
{
	cout<<"\n**************************** test case 1 ***************************"<<endl;
	testCase1();
	cout<<"\n**************************** test case 2 ***************************"<<endl;
	testCase2();
	cout<<"\n**************************** test case 3 ***************************"<<endl;
	testCase3();
	cout<<"\n\n";
	return 0;
}