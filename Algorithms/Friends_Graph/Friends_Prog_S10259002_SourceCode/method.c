/*
 * method.c
 *
 *  Created on: 2015年10月24日
 *      Author: hao
 */

#include "method.h"

int maxfriend(int **adjm, int vnum) {
	int tv = 0;
	int i, j, max = 0, tmax;

	for (i = 0; i < vnum; i++)
		if (adjm[0][i])
			max++;

	for (j = 1; j < vnum; j++) {
		tmax = 0;
		for (i = 0; i < vnum; i++) {
			if (adjm[j][i]) {
				tmax++;
			}
		}

		if (max < tmax) {
			max = tmax;
			tv = j;
		}
	}

	return tv;
}

int *fdiameter(int **d, int vnum) {
	int *result = malloc(sizeof(int) * (vnum + 1));
	int **diameterTable, **maxTable, **reTable;
	int i, j, diameterIdx, maxIdx;

	memset(result, 0, sizeof(int) * vnum);

	diameterTable = bfs(0, d, vnum);
	diameterIdx = findmax(diameterTable, vnum);

	for (i = 1; i < vnum; i++) {
		maxTable = bfs(i, d, vnum);
		maxIdx = findmax(maxTable, vnum);
		if (diameterTable[diameterIdx][0] < maxTable[maxIdx][0]) {
			diameterIdx = maxIdx;

			reTable = diameterTable;
			diameterTable = maxTable;
			freeDisTable(reTable, vnum);
		} else {
			freeDisTable(maxTable, vnum);
		}
	}
	result[0] = diameterTable[diameterIdx][0];

	i = diameterIdx;
	for (j = result[0] + 1; j > 0; j--) {
		result[j] = i;
		i = diameterTable[i][1];
	}

	return result;
}

int **bfs(int start, int **adjm, int vnum) {
	int i, j;
	MyQueue *q = mygenQueue(vnum);
	int **dtable = malloc(sizeof(int*) * vnum);
	for (i = 0; i < vnum; i++) {
		dtable[i] = malloc(sizeof(int) * 2);
		dtable[i][0] = -1;
		dtable[i][1] = 0;
	}
	myqAdd(q, start);
	dtable[start][0] = 0;
	dtable[start][1] = start;

	while (!myqEmpty(q)) {
		i = myqPop(q);

		for (j = 0; j < vnum; j++) {
			if (adjm[i][j] && dtable[j][0] == -1) {
				dtable[j][0] = dtable[i][0] + 1;
				dtable[j][1] = i;
				myqAdd(q, j);
			}
		}
	}

	myfreeQueue(q);
	return dtable;
}

int findmax(int **dtable, int n) {
	int i, idx = 0;
	for (i = 1; i < n; i++)
		if (dtable[idx] < dtable[i])
			idx = i;
	return idx;
}

void printDmre(int *dr) {
	int pn = dr[0] + 2;
	int i;
	printf("\n    Result:\n        Diameter: %4d\n        Path: ", dr[0]);

	for (i = 1; i < pn; i++) {
		printf("[%5d]", dr[i]);
		if (i < pn - 1)
			printf(" -> ");
	}
	printf("\n");
}

void freeDisTable(int **d, int n) {
	int i;
	for (i = 0; i < n; i++)
		free(d[i]);
	free(d);
}

void FindkClique(int **adjm, int vnum, int l, int printbool) {
	int *com;
	int i, t;

	com = malloc(sizeof(int) * l);
	for (i = 0; i < l; i++)
		com[i] = i;

	while (com[l - 1] < vnum) {
		if (checkClique(com, l, adjm) && printbool) {
			printf("        %d-Clique: ", l);
			for (i = 0; i < l; i++) {
				printf("%d", (com[i] + 1));
				if (i < l - 1)
					printf(", ");
			}
			printf("\n");
		}

		// let t be last number
		t = l - 1;

		// find already fixed position
		// n - k + t is the number that will be fixed,
		// in the other word, be the number that had picked
		while (t != 0 && com[t] == vnum - l + t)
			t--;

		// first number plus one
		com[t]++;

		// from second number
		for (i = t + 1; i < l; i++)
			com[i] = com[i - 1] + 1;
	}
	free(com);
}

int checkClique(int *v, int n, int **adjm) {
	int i, j;
	for (j = 0; j < n - 1; j++)
		for (i = j + 1; i < n; i++)
			if (v[j] != v[i] && !adjm[v[j]][v[i]])
				return 0;
	return 1;
}

void FindMaxClique(int **adjm, int vnum, int tvertex, int printbool) {
	int i, t, k, j, l, n = vnum - 1;
	int *nums = malloc(sizeof(int) * vnum);
	int *idx, *result = NULL, *max = NULL;
	int first_flag = 1;

	for (i = 0; i < vnum; i++)
		nums[i] = i;

	// swap specific vertex
	t = nums[0];
	nums[0] = nums[tvertex];
	nums[tvertex] = t;

	for (i = 2; i <= vnum; i++) {
		k = i - 1;

		idx = malloc(sizeof(int) * k);
		for (j = 0; j < k; j++)
			idx[j] = j;

		while (idx[k - 1] < n) {
			if (first_flag) {
				max = checkMaxClique(nums, k, tvertex, idx, adjm);
				if (max)
					first_flag = 0;
			} else {
				result = checkMaxClique(nums, k, tvertex, idx, adjm);
			}

			if (!first_flag && result) {
				if (max[0] < result[0]) {
					free(max);
					max = result;
				} else {
					free(result);
				}
			}

			l = k - 1;
			while (l != 0 && idx[l] == n - k + l)
				l--;
			idx[l]++;
			for (j = l + 1; j < k; j++)
				idx[j] = idx[j - 1] + 1;
		}
		free(idx);
	}

	if (printbool) {
		printf("        Maximum clique: %d-Clique (", max[0]);
		for (i = 1; i <= max[0]; i++) {
			printf("%d", max[i] + 1);
			if (i < max[0])
				printf(", ");
		}
		printf(")\n");
	}
	free(max);
	free(nums);
}

int *checkMaxClique(int *v, int vn, int target, int *idx, int **adjm) {
	int i, j, vnum = vn + 1;
	int *result = malloc(sizeof(int) * (vnum + 1));
// record current k-Clique
	result[0] = vnum;
	result[1] = target;

	for (i = 0; i < vn; i++)
		result[i + 2] = v[idx[i] + 1];

// push value in result
	for (j = 1; j < vnum; j++)
		for (i = j + 1; i <= vnum; i++)
			if (result[j] != result[i] && !adjm[result[j]][result[i]]) {
				free(result);
				return NULL;
			}
	return result;
}
