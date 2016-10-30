/*
 * main.cpp
 *
 *  Created on: 2015年12月26日
 *      Author: hao
 */

#include "method.h"

int main(void) {
	int exitFlag = 0;
	int mode;

	while (!exitFlag) {
		cout << "================================= USING =================================\n";
		cout << "0 is exit\n";
		cout << "1 is that input two string, and output the edit distance\n";
		cout << "2 is that input two file with file name, and output\n";
		cout << "\t(1): whole file edit distance\n";
		cout << "\t(2): each paragraph edit distance\n\n";
		cout << "Enter the mode: ";
		cin >> mode;
		cin.ignore(256, '\n');
		switch (mode) {
			case 0:
				exitFlag = !exitFlag;
				break;
			case 1: {
				string s1, s2;
				cout << "Please input two string\n";
				cout << "S1 : ";
				getline(cin, s1);
				cout << "S2 : ";
				getline(cin, s2);

				cout << "    Minimum     edit distance: " << editDis(s1, s2, 1) << endl;
				cout << "    Levenshtein edit distance: " << editDis(s1, s2, 2) << endl;
				break;
			}
			case 2: {
				vector<string> *origin = NULL, *test = NULL;
				string file1, file2;
				int i;
				double M1_STIME, M1_ETIME, M2_STIME, M2_ETIME;

				cout << "Please input two file name\n";
				cout << "First  file name : ";
				cin >> file1;
				cout << "Second file name : ";
				cin >> file2;

				// read data from file
				origin = readFile(file1);
				test = readFile(file2);

				// trim duplicated spaces
				for (i = 0; i < (int) origin->size(); i++)
					trailSpace(&((*origin)[i]));
				for (i = 0; i < (int) test->size(); i++)
					trailSpace(&((*test)[i]));

				M1_STIME = getCPUTime();
				double *m1dot = method1(origin, test);
				M1_ETIME = getCPUTime();

				M2_STIME = getCPUTime();
				double *m2dot = method2(origin, test);
				M2_ETIME = getCPUTime();

				cout << "method 1\n";
				cout << "D.o.t with Minimum     edit distance : " << m1dot[0] << endl;
				cout << "D.o.t with Levenshtein edit distance : " << m1dot[1] << "\n";
				cout << "                                Time : " << (M1_ETIME - M1_STIME) << "(ms)\n\n";

				cout << "method 2\n";
				cout << "D.o.t with Minimum     Edit Distance : " << m2dot[0] << endl;
				cout << "                            Variance : " << m2dot[2] << endl;
				cout << "                  Standard Deviation : " << sqrt(m2dot[2]) << "\n\n";

				cout << "D.o.t with Levenshtein Edit Distance : " << m2dot[1] << "\n";
				cout << "                            Variance : " << m2dot[3] << "\n";
				cout << "                  Standard Deviation : " << sqrt(m2dot[3]) << "\n";
				cout << "                                Time : " << (M2_ETIME - M2_STIME) << "(ms)\n\n";

				delete origin;
				delete test;
				delete[] m1dot;
				delete[] m2dot;
				break;
			}
			default:
				cout << "Enter ERROR! Please again!!\n";
		}
		cout << endl;
	}

	cout << "Exit...";
	return 0;
}
