/*
 * method.h
 *
 *  Created on: 2015年10月24日
 *      Author: hao
 */

#ifndef METHOD_H_
#define METHOD_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "myqueue.h"
#define MAX(X, Y) ((X) > (Y) ? (X) : (Y))
#define MIN(X, Y) ((X) < (Y) ? (X) : (Y))

int maxfriend(int **adjm, int vnum);
int *fdiameter(int **d, int vnum);
int **bfs(int start, int **adjm, int vnum);
int findmax(int **dtable, int n);
void printDmre(int *dr);
void freeDisTable(int **d, int n);

void FindkClique(int **adjm, int vnum, int l, int printbool);
int checkClique(int *v, int n, int **adjm);

void FindMaxClique(int **adjm, int vnum, int tvertex, int printbool);
int *checkMaxClique(int *v, int vn, int target, int *idx, int **adjm);

#endif /* METHOD_H_ */
