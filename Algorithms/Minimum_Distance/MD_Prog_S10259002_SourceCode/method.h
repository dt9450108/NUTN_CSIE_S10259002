/*
 *	Project : algorithm_hw_1
 *	File    : method.h
 *	Time    : 2015_�W��11:34:47
 *	Author  : HUANG SHIH-HAO
 **/

#ifndef METHOD_H_
#define METHOD_H_

#include <stdio.h>
#include <limits.h>
#include <math.h>
#define SWAP(a, b) do { a ^= b; b ^= a; a ^= b; } while(0)

int method1(int *list, int n);
int method2(int *list, int n);
void quick_sort(int *list, int l, int r);

#endif /* METHOD_H_ */
