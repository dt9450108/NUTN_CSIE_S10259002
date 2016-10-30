/*
 * input.c
 *
 *  Created on: 2015年10月24日
 *      Author: hao
 */

#include "input.h"

// [low, upper]
//(int) ((rand() / (RAND_MAX + 1.0)) * (upper - low + 1.0) + low);
int **gentdata(int vnum) {
	int **adjm;
	int i, j, r;

	// allocate memory
	adjm = malloc(sizeof(int*) * vnum);
	for (i = 0; i < vnum; i++) {
		adjm[i] = malloc(sizeof(int) * vnum);
		memset(adjm[i], 0, sizeof(int) * vnum);
	}

	// generate test date
	for (j = 0; j < vnum; j++) {
		for (i = j + 1; i < vnum; i++) {
			r = (int) ((rand() / (RAND_MAX + 1.0)) * (UPPER - LOWER + 1.0) + LOWER);
//			if (i == j + 1) {
			if (r) {
				adjm[j][i] = 1;
				adjm[i][j] = 1;
			} else {
				adjm[j][i] = 0;
				adjm[i][j] = 0;
			}
//			}
		}
	}

	return adjm;
}

void freetdata(int **adjm, int vnum) {
	int i;
	for (i = 0; i < vnum; i++)
		free(adjm[i]);
	free(adjm);
}

int **rffile(char *filename, int *vertices) {
	int **adjm = NULL;
	int vnum, i, j;
	FILE *file;
	file = fopen(filename, "r");

	fscanf(file, "%d", &vnum);

// allocate memory
	adjm = malloc(sizeof(int*) * vnum);
	for (i = 0; i < vnum; i++) {
		adjm[i] = malloc(sizeof(int) * vnum);
		memset(adjm[i], 0, sizeof(int) * vnum);
	}

// read data to adjm
	for (j = 0; j < vnum; j++)
		for (i = 0; i < vnum; i++)
			fscanf(file, "%d", &adjm[j][i]);

	*vertices = vnum;
	fclose(file);
	return adjm;
}

void w2file(int **adjm, int vnum, char *filename) {
	FILE *file;
	file = fopen(filename, "w");
	int i, j;

	if (!file) {
		fprintf(stderr, "File [%s] cannot open...\n", filename);
		exit(1);
	}

// write the number of vertices
	fprintf(file, "%d\n", vnum);

// write the adjacency matrix
	for (j = 0; j < vnum; j++) {
		for (i = 0; i < vnum; i++) {
			fprintf(file, "%d ", adjm[j][i]);
		}
		fprintf(file, "\n");
	}

	fclose(file);
}
