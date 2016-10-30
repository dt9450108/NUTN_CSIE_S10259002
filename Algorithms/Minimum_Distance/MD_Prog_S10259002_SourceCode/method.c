/*
 *	Project : algorithm_hw_1
 *	File    : method.c
 *	Time    : 2015_�W��11:36:34
 *	Author  : HUANG SHIH-HAO
 **/

#include "method.h"

/*
 * method 1
 * Time Complexity: O(n^2)
 */
int method1(int *list, int n) {
	int dmin = INT_MAX, i, j, diff = 0;

	for (i = 0; i < n; i++) {
		for (j = i + 1; j < n; j++) {
			diff = abs(list[i] - list[j]);
			if (i != j && diff < dmin) {
				dmin = diff;
			}
		}
	}
	return dmin;
}

/*
 * method 2
 * using quick sort first
 * then pair calculate find min
 * Time Complexity : O(nlogn)
 */
int method2(int *list, int n) {
	int i, dmin = INT_MAX, diff;
	// sort number array
	quick_sort(list, 0, n - 1);

// find minimum number
	for (i = 0; i < n - 1; i++) {
		diff = list[i + 1] - list[i];
		if (dmin > diff)
			dmin = diff;
	}

	return dmin;
}

void quick_sort(int *list, int l, int r) {
	if (l < r) {
		int p = list[(l + r) / 2];
		int i = l - 1, j = r + 1;
		while (i < j) {
			while (list[++i] < p);
			while (list[--j] > p);
			if (i < j) SWAP(list[i], list[j]);
		}
		quick_sort(list, l, i - 1);
		quick_sort(list, j + 1, r);
	}
}
