/*
 *	Project : algorithm_hw_1
 *	File    : main.c
 *	Time    : 2015_�U��8:05:44
 *	Author  : HUANG SHIH-HAO
 **/

#include <stdio.h>
#include <string.h>
#include "input.h"
#include "getCPUTime.h"
#include "method.h"

void PrintArray(const int *list, int n);

int main(void) {
	// experiment parameter
	double EXPE_START_TIME, EXPE_END_TIME;
	int EXPE_ONE_RUNTIME = 10;
	int EXPE_NSIZE[] = { 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000 };
	int EXPE_NSIZE_COUNT = 10;

	double METHOD1_EXPETIME[10] = { 0 };
	int METHOD1_OUTPUT[10] = { -1 };

	double METHOD2_EXPETIME[10] = { 0 };
	int METHOD2_OUTPUT[10] = { -1 };

	char *FILES_NAME[10] = { "Test1.txt", "Test2.txt", "Test3.txt", "Test4.txt", "Test5.txt", "Test6.txt", "Test7.txt", "Test8.txt", "Test9.txt", "Test10.txt" };

	// declaration
	double START_TIME, END_TIME, TOTAL_TIME = 0;
	int *numList = NULL, *numListCopy = NULL;
	int min_dis = 0, i, j;

	// set rand seed
	srand((unsigned) time(NULL));

	//	 method 1 experiment
	printf("Experiment Start...\n");
	EXPE_START_TIME = getCPUTime();
	for (i = 0; i < EXPE_NSIZE_COUNT; i++) {
		printf("    Experiment [%2d] Start...\n", (i + 1));
//		printf("    Random Input Data...\n");
//		randNumber2file(0, EXPE_NSIZE[i], EXPE_NSIZE[i], FILES_NAME[i]);
		printf("    Read Test Data...\n");
		numList = readFile(FILES_NAME[i]);

		// method 1 part
		TOTAL_TIME = 0;
		printf("        Method 1 Start...\n");
		for (j = 0; j < EXPE_ONE_RUNTIME; j++) {
			printf("            Method 1 : [%2d]th execution...", (j + 1));
			START_TIME = getCPUTime();
			min_dis = method1(numList, EXPE_NSIZE[i]);
			END_TIME = getCPUTime();
			printf("    %5.6f second(s)    Min Distance : %4d\n", (END_TIME - START_TIME), min_dis);
			TOTAL_TIME += END_TIME - START_TIME;
		}
		METHOD1_EXPETIME[i] = (double) TOTAL_TIME / EXPE_ONE_RUNTIME;
		METHOD1_OUTPUT[i] = min_dis;

		// method 2 part
		TOTAL_TIME = 0;
		printf("        Method 2 Start...\n");
		for (j = 0; j < EXPE_ONE_RUNTIME; j++) {
			printf("            Method 2 : [%2d]th execution...", (j + 1));
			// copy a new input without sorted
			numListCopy = (int *) malloc(sizeof(int) * EXPE_NSIZE[i]);
			memcpy(numListCopy, numList, sizeof(int) * EXPE_NSIZE[i]);

			START_TIME = getCPUTime();
			min_dis = method2(numListCopy, EXPE_NSIZE[i]);
			END_TIME = getCPUTime();
			printf("    %5.6f second(s)    Min Distance : %4d\n", (END_TIME - START_TIME), min_dis);
			TOTAL_TIME += END_TIME - START_TIME;
			free(numListCopy);
		}

		METHOD2_EXPETIME[i] = (double) TOTAL_TIME / EXPE_ONE_RUNTIME;
		METHOD2_OUTPUT[i] = min_dis;

		free(numList);
	}
	EXPE_END_TIME = getCPUTime();
	printf("Experiment taken %lf second(s)\n", (EXPE_END_TIME - EXPE_START_TIME));
	printf("\n=============================\n\n");
	printf("Method 1\n");
	for (i = 0; i < EXPE_NSIZE_COUNT; i++)
		printf("%4d : Time : %5.6f second(s)    Min Distance : %6d\n", (i + 1), METHOD1_EXPETIME[i], METHOD1_OUTPUT[i]);
	printf("\n=============================\n\n");
	printf("Method 2\n");
	for (i = 0; i < EXPE_NSIZE_COUNT; i++)
		printf("%4d : Time : %5.6f second(s)    Min Distance : %6d\n", (i + 1), METHOD2_EXPETIME[i], METHOD2_OUTPUT[i]);
	return 0;
}

void PrintArray(const int *list, int n) {
	int i;
	for (i = 0; i < n; i++) {
		printf("%d ", list[i]);
	}
}
