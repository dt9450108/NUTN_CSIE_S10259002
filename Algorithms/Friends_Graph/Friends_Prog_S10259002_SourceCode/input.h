/*
 * input.h
 *
 *  Created on: 2015年10月24日
 *      Author: hao
 */

#ifndef INPUT_H_
#define INPUT_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#define UPPER 4
#define LOWER 0

int **gentdata(int vnum);
void freetdata(int **adjm, int vnum);
int **rffile(char *filename, int *vertices);
void w2file(int **adjm, int vnum, char *filename);

#endif /* INPUT_H_ */
