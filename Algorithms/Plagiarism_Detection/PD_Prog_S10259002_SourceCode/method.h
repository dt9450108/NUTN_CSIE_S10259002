/*
 * method.h
 *
 *  Created on: 2015年12月26日
 *      Author: hao
 */

#ifndef METHOD_H_
#define METHOD_H_

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <climits>
#include <cmath>

#include <unistd.h>
#include <sys/resource.h>
#include <sys/times.h>
#include <time.h>
using namespace std;

int editDis(string s1, string s2, int substitCount);
int min(int a, int b, int c);

void trailSpace(string *str);
int mysfind(char ch, int pos, int equ, string str);

double *method1(vector<string> *org, vector<string> *test);
double *method2(vector<string> *org, vector<string> *test);

vector<string> *readFile(string filename);

double getCPUTime();

#endif /* METHOD_H_ */
