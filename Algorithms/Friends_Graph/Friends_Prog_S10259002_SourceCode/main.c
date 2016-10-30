/*
 * main.c
 *
 *  Created on: 2015年10月24日
 *      Author: hao
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include "getCPUTime.h"
#include "input.h"
#include "method.h"
#include "myqueue.h"
#define VERTEX_NUMBER 5
#define FILE_NAME "TestVertex_5.txt"
#define GEN_TESTING_DATA 0
#define WTRITE_TO_FILE 0
#define READ_FROM_FILE 1
#define PRINT_GRAPH 0

#define PROBLEM_ONE 1
#define PROBLEM_TWO 1
#define PROBLEM_TRHEE 1
#define PROBLEM_FOUR 1

void padjm(int **a, int n);

int main(int argc, char *argv[]) {
	srand((unsigned) time(NULL));

	// timestamp
	double PROCESS_START_TIME, PROCESS_END_TIME;
	double DATA_PROCESS_START_TIME, DATA_PROCESS_END_TIME;
	double METHOD_START_TIME, METHOD_END_TIME;

	int **adjacency;
	int vertex_num = VERTEX_NUMBER;
	int maxFriend, run_times, i;
	int *diameter_result;
	double mtime_sum = 0;

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Process Start
	PROCESS_START_TIME = getCPUTime();
	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Process Start

	if (GEN_TESTING_DATA) {
		printf("Generate testing data\n...............");
		DATA_PROCESS_START_TIME = getCPUTime();
		adjacency = gentdata(vertex_num);
		DATA_PROCESS_END_TIME = getCPUTime();
		printf("  %.6f seconds\n\n", (DATA_PROCESS_END_TIME - DATA_PROCESS_START_TIME));
	}

	if (WTRITE_TO_FILE) {
		printf("Write data to file [%s]\n...............", FILE_NAME);
		DATA_PROCESS_START_TIME = getCPUTime();
		w2file(adjacency, vertex_num, FILE_NAME);
		DATA_PROCESS_END_TIME = getCPUTime();
		printf("  %.6f seconds\n\n", (DATA_PROCESS_END_TIME - DATA_PROCESS_START_TIME));
	}

	if (READ_FROM_FILE) {
		printf("Read data from file [%s]\n...............", FILE_NAME);
		DATA_PROCESS_START_TIME = getCPUTime();
		adjacency = rffile(FILE_NAME, &vertex_num);
		DATA_PROCESS_END_TIME = getCPUTime();
		printf("  %.6f seconds\n\n", (DATA_PROCESS_END_TIME - DATA_PROCESS_START_TIME));
	}

	if (PRINT_GRAPH) {
		printf("=-=-=-=-=-=-=-=-=-=-= Graph =-=-=-=-=-=-=-=-=-=-=\n");
		padjm(adjacency, vertex_num);
		printf("=-=-=-=-=-=-=-=-=-=-= Graph =-=-=-=-=-=-=-=-=-=-=\n\n");
	}

	/*
	 * Find who has max friends with adjacency matrix
	 * Time complexity: O(n^2)
	 * Experiment: run ten times and average total time
	 */
	if (PROBLEM_ONE) {
		printf("Find who has max friends...\n");
		run_times = 10;
		mtime_sum = 0;
		for (i = 0; i < run_times; i++) {
			printf("    Run [%2d]...", (i + 1));
			METHOD_START_TIME = getCPUTime();
			maxFriend = maxfriend(adjacency, vertex_num);
			METHOD_END_TIME = getCPUTime();
			printf("%c%.6f\n", ' ', (METHOD_END_TIME - METHOD_START_TIME));
			mtime_sum += (METHOD_END_TIME - METHOD_START_TIME);
		}
		printf("\n    Result:\n        Vertex \"%d\" has max friends.\n", maxFriend);
		printf("\n    Take %.6f second(s).\n", (mtime_sum / run_times));
		printf("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
	}

	/*
	 * Find the diameter of the graph
	 * Time complexity: O(n^3)
	 * Experiment: run  times and average total time
	 */
	if (PROBLEM_TWO) {
		printf("\nFind the diameter of the graph...\n");
		run_times = 10;
		mtime_sum = 0;

		// run remaining times
		for (i = 0; i < run_times; i++) {
			printf("    Run [%2d]...", (i + 1));
			METHOD_START_TIME = getCPUTime();
			diameter_result = fdiameter(adjacency, vertex_num);
			METHOD_END_TIME = getCPUTime();
			printf("%c%.6f\n", ' ', (METHOD_END_TIME - METHOD_START_TIME));
			mtime_sum += (METHOD_END_TIME - METHOD_START_TIME);
			if (i < run_times - 1)
				free(diameter_result);
		}
		printDmre(diameter_result);
		printf("\n    Take %.6f second(s).\n", (mtime_sum / run_times));
		printf("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
	}

	/*
	 * Using "Exhaustive Search (Brute-force) to find all k-Clique of the graph
	 * Time complexity: O(2^n)
	 * Experiment: run  times and average total time
	 */
	if (PROBLEM_TRHEE) {
		int findkclique = 1;
		printf("\nEnter the number of k-Clique desired to find (3 to %d): \n", vertex_num);
		while (!scanf("%d", &findkclique) || (findkclique < 3 || findkclique > vertex_num))
			fprintf(stderr, "Error! Please enter again...\n");
		printf("\nFind all %d-Clique...\n", findkclique);
		run_times = 10;
		mtime_sum = 0;

		for (i = 0; i < run_times; i++) {
			printf("    Run [%2d]...", (i + 1));
			METHOD_START_TIME = getCPUTime();
			FindkClique(adjacency, vertex_num, findkclique, 0);
			METHOD_END_TIME = getCPUTime();
			printf("%c%.6f\n", ' ', (METHOD_END_TIME - METHOD_START_TIME));
			mtime_sum += (METHOD_END_TIME - METHOD_START_TIME);
		}
		printf("\n    Result:\n");
		FindkClique(adjacency, vertex_num, findkclique, 1);
		printf("\n    Take %.6f second(s).\n", (mtime_sum / run_times));
		printf("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
	}

	/*
	 * Using "Exhaustive Search (Brute-force) to find max Clique of the graph with given specific vertex
	 * Time complexity: O(2^n)
	 * Experiment: run  times and average total time
	 */
	if (PROBLEM_FOUR) {
		int specificVertex = 1;
		printf("\nEnter the vertex number (1 to %d): ", vertex_num);
		while (!scanf("%d", &specificVertex) || (specificVertex < 1 || specificVertex > vertex_num))
			fprintf(stderr, "Enter number error, please again...\n");
		printf("\nFind maximum clique with vertex(%d)...\n", specificVertex);
		run_times = 10;
		mtime_sum = 0;
		for (i = 0; i < run_times; i++) {
			printf("    Run [%2d]...", (i + 1));
			METHOD_START_TIME = getCPUTime();
			FindMaxClique(adjacency, vertex_num, specificVertex - 1, 0);
			METHOD_END_TIME = getCPUTime();
			printf("%c%.6f\n", ' ', (METHOD_END_TIME - METHOD_START_TIME));
			mtime_sum += (METHOD_END_TIME - METHOD_START_TIME);
		}
		printf("\n    Result:\n");
		FindMaxClique(adjacency, vertex_num, specificVertex - 1, 1);
		printf("\n    Take %.6f second(s).\n", (mtime_sum / run_times));
		printf("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
	}

	freetdata(adjacency, vertex_num);
// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Process End
	PROCESS_END_TIME = getCPUTime();
// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Process End
	printf("Process taken %.6f second(s)\n", (PROCESS_END_TIME - PROCESS_START_TIME));
	return 0;
}

void padjm(int **a, int n) {
	int i, j;
	for (i = 0; i < n; i++) {
		for (j = 0; j < n; j++) {
			printf("%d ", a[i][j]);
		}
		printf("\n");
	}
}
