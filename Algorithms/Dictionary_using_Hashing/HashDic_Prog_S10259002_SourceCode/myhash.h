/*
 * myhash.h
 *
 *  Created on: 2015年12月6日
 *      Author: hao
 */

#ifndef MYHASH_H_
#define MYHASH_H_

#include <iostream>
#include <cstdlib>
#include <vector>
#include <string>
#include <string.h>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <unistd.h>
#include <sys/resource.h>
#include <sys/times.h>
#include <time.h>
using namespace std;

typedef struct myhash_s {
	vector<string> slot;
} Myhash;

typedef struct cuckoo_s {
	int Collision;
	int Insertion;
	int Searching;
	int Success;
	int Failure;
	int WordNumber;
	vector<string> NotStored;
} Cuckoo;

extern Myhash *Table;
extern Myhash *Table2;
extern int TableSize;
extern int Big_p;

extern int CuckooHashTimes;
extern Cuckoo CuckooHash;

// Command
extern struct Cmd_s {
	int num;
	int source;
	int hash;
	string str;
} Cmd;

// Table
extern int Collision;
extern int Insertion;
extern int Searching;
extern int WordNumber;
extern int NonEmptySlot;
extern int Success;
extern int Failure;

// Table 2
extern int Collision2;
extern int Insertion2;
extern int Searching2;
extern int WordNumber2;
extern int NonEmptySlot2;
extern int Success2;
extern int Failure2;

Myhash *createTable(int size);
void delTable(Myhash *ptr, int size);
void outputTable(string filename, int size, int hfun);
void outputTwoTable(string filename, int size);

int hash1(const string k, int q);
int hash2(const string k, int q, int p);
void cuckooHashInsert(string k, int q);
string filterw(string k);
void BuildDictionary(string file, int hfun);

void Add(string file_or_word, int mode, int hfun);
void Delete(string file_or_word, int mode, int hfun);
void Search(string file_or_word, int mode, int hfun);
int searchTable(string k, int hfun, int *whichTable);
int find(string str, vector<string> list);

int readCommand(string cmd);
void printHelp();
void ClearGlobalVar();
double getCPUTime();

#endif /* MYHASH_H_ */
