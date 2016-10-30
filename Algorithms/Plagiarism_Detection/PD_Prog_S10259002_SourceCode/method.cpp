/*
 * method.cpp
 *
 *  Created on: 2015年12月26日
 *      Author: hao
 */

#include "method.h"

int editDis(string s1, string s2, int substitCount) {
	int n = (int) s1.length() + 1;
	int m = (int) s2.length() + 1;
	int i, j, insertionCount = 1, deleteCount = 1;
	int re_dis = 0;

	// allocate table
	int **table = new int*[n];
	for (i = 0; i < n; i++)
		table[i] = new int[m];

	// initialize table
	for (i = 0; i < n; i++)
		table[i][0] = i;
	for (j = 0; j < m; j++)
		table[0][j] = j;

	for (i = 1; i < n; i++)
		for (j = 1; j < m; j++) {
			int substitution = (s1[i - 1] != s2[j - 1]) ? substitCount : 0;
			table[i][j] = min(table[i - 1][j] + deleteCount, table[i][j - 1] + insertionCount, table[i - 1][j - 1] + substitution);
		}

	re_dis = table[n - 1][m - 1];

	for (i = 0; i < n; i++)
		delete[] table[i];
	delete[] table;

	return re_dis;
}

int min(int a, int b, int c) {
	int small = (a < b) ? a : b;
	small = (small < c) ? small : c;
	return small;
}

void trailSpace(string *str) {
	int s = 0, e, l = (int) str->length();

	for (e = 0; e < l; e++)
		if ((*str)[e] == '\t')
			(*str).erase(e, 1);

	e = mysfind(' ', 0, 0, *str);
	if (e > 0)
		str->erase(0, e);

	s = mysfind(' ', s, 1, *str);
	e = mysfind(' ', s + 1, 0, *str) - 1;

	while ((s <= e) && s != -1) {
		if ((e - s) > 1)
			str->erase(s, (e - s));
		s = mysfind(' ', s + 1, 1, *str);
		e = mysfind(' ', s + 1, 0, *str) - 1;
	}

	if (s < l && s != -1)
		str->erase(s, l - s + 1);
}

int mysfind(char ch, int pos, int equ, string str) {
	int l = (int) str.length();
	for (int i = pos; i < l; i++) {
		if (equ) {
			if (str[i] == ch)
				return i;
		} else {
			if (str[i] != ch)
				return i;
		}
	}
	return -1;
}

double *method1(vector<string> *org, vector<string> *test) {
	int org_len = (int) (*org).size(), test_len = (int) (*test).size();
	int i, j;

	int org_ch_len = 0, test_ch_len = 0, max_ch_len;
	for (i = 0; i < org_len; i++)
		org_ch_len += (int) (*org)[i].size();
	for (i = 0; i < test_len; i++)
		test_ch_len += (int) (*test)[i].size();
	max_ch_len = (org_ch_len > test_ch_len) ? org_ch_len : test_ch_len;

	int edis = 0, ledis = 0, minloop = (org_len < test_len) ? org_len : test_len;

	for (i = 0; i < minloop; i++) {
		edis += editDis((*org)[i], (*test)[i], 1);
		ledis += editDis((*org)[i], (*test)[i], 2);
	}

	for (; i < org_len; i++) {
		j = (int) (*org)[i].size();
		edis += j;
		ledis += j;
	}

	for (; i < org_len; i++) {
		j = (int) (*test)[i].size();
		edis += j;
		ledis += j;
	}

//	cout << "edis : " << edis << " | ledis : " << ledis << " | max_ch_len : " << max_ch_len << endl;
//	cout << "((double)  edis / max_ch_len)  : " << ((double) edis / max_ch_len) << endl;
//	cout << "((double) ledis / max_ch_len)  : " << ((double) ledis / max_ch_len) << endl;

	double *dot = new double[2];
	dot[0] = (1 - ((double) edis / max_ch_len) >= 0) ? (1 - ((double) edis / max_ch_len)) : 0;
	dot[1] = (1 - ((double) ledis / max_ch_len) >= 0) ? (1 - ((double) ledis / max_ch_len)) : 0;

	return dot;
}

double *method2(vector<string> *org, vector<string> *test) {
	int org_len = (int) (*org).size(), test_len = (int) (*test).size();
	int i, j;
	double edis_dij[org_len][test_len];
	double ledis_dij[org_len][test_len];
	int edis, ledis, max_edis = 0, max_ledis = 0, max_ij;

	// ki is the index of max dij
	int ki_edis[org_len], ki_ledis[org_len];

	for (i = 0; i < org_len; i++) {
		for (j = 0; j < test_len; j++) {
			// get max length
			max_ij = ((int) (*org)[i].size() > (int) (*test)[j].size()) ? (int) (*org)[i].size() : (int) (*test)[j].size();

			// get edit distance
			edis = editDis((*org)[i], (*test)[j], 1);
			ledis = editDis((*org)[i], (*test)[j], 2);

			edis_dij[i][j] = (1 - ((double) edis / max_ij) >= 0) ? (1 - ((double) edis / max_ij)) : 0;
			ledis_dij[i][j] = (1 - ((double) ledis / max_ij) >= 0) ? (1 - ((double) ledis / max_ij)) : 0;

			if (edis_dij[i][max_edis] < edis_dij[i][j])
				max_edis = j;
			if (ledis_dij[i][max_ledis] < ledis_dij[i][j])
				max_ledis = j;
		}

		// find ki
		ki_edis[i] = max_edis;
		ki_ledis[i] = max_ledis;
	}

	double *dot = new double[4];
	dot[0] = 0.0;
	dot[1] = 0.0;
	for (i = 0; i < org_len; i++) {
		dot[0] += edis_dij[i][ki_edis[i]];
		dot[1] += ledis_dij[i][ki_ledis[i]];
	}
	dot[0] /= org_len;
	dot[1] /= org_len;

	dot[2] = 0.0;
	dot[3] = 0.0;
	for (i = 0; i < org_len; i++) {
		dot[2] += (edis_dij[i][ki_edis[i]] - dot[0]) * (edis_dij[i][ki_edis[i]] - dot[0]);
		dot[3] += (ledis_dij[i][ki_ledis[i]] - dot[1]) * (ledis_dij[i][ki_ledis[i]] - dot[1]);
	}
	dot[2] /= org_len;
	dot[3] /= org_len;
	return dot;
}

vector<string> *readFile(string filename) {
	vector < string > *t = new vector<string>;

	fstream file;
	file.open(filename.c_str(), ios::in);
	if (!file)
		cout << "Fail to open file: " << filename << endl;

	string sentence;
	while (!file.eof()) {
		getline(file, sentence);
		t->push_back(sentence);
	}
	file.close();

	return t;
}

// unit: ms
double getCPUTime() {
	struct rusage rusage;
	if (getrusage(0, &rusage) != -1)
		return (double) rusage.ru_utime.tv_sec / 1000.0 + (double) rusage.ru_utime.tv_usec / 1000.0;

	return -1.0; /* Failed. */
}
