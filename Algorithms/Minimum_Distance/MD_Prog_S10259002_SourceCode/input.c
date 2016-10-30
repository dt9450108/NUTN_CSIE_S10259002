/*
 *	Project : algorithm_hw_1
 *	File    : input.c
 *	Time    : 2015_¤W¤È9:33:00
 *	Author  : HUANG SHIH-HAO
 **/

#include "input.h"

void randNumber2file(int low, int upper, int num, char filename[]) {
	// srand((unsigned)time(NULL)); in main function
	// [low, upper]

	// declare
//	int *list = (int *) malloc(sizeof(int) * num);
	int tempNum = 0;
	int i = 0;
	FILE *file;

//	for (int i = 0; i < num; i++)
//		list[i] = (int) ((rand() / (RAND_MAX + 1.0)) * (upper - low + 1.0) + low);

	file = fopen(filename, "w");
	if (!file) {
		printf("File %s cannot be open...\n", filename);
		exit(0);
	}

	// store size
	fprintf(file, "%d\n", num);

	for (i = 0; i < num; i++) {
		tempNum = (int) ((rand() / (RAND_MAX + 1.0)) * (upper - low + 1.0) + low);
		fprintf(file, "%d ", tempNum);
	}

	fclose(file);
}

int *readFile(char filename[]) {
	FILE *file;
	int size = 0;
	int *tmp = NULL;
	int i, tmpNum;

	file = fopen(filename, "r");
	if (!file) {
		printf("File %s cannot be open...\n", filename);
		exit(0);
	}

	// get array size from file
	fscanf(file, "%d", &size);
	tmp = (int *) malloc(size * sizeof(int));

	// read number
	for (i = 0; i < size; i++) {
		fscanf(file, "%d", &tmpNum);
		tmp[i] = tmpNum;
	}

	return tmp;
}
