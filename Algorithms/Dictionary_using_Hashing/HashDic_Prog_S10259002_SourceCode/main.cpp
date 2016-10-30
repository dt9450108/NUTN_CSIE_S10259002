/*
 * main.cpp
 *
 *  Created on: 2015年12月6日
 *      Author: hao
 */

#include "myhash.h"

struct Cmd_s Cmd = { -1, -1, -1, "" };

Myhash *Table = NULL;
Myhash *Table2 = NULL;
Cuckoo CuckooHash = { 0, 0, 0, 0, 0, 0 };
int TableSize = 257;
int Big_p = 11;
int CuckooHashTimes = 14;

// Table
int Collision = 0;
int Insertion = 0;
int Searching = 0;
int WordNumber = 0;
int NonEmptySlot = 0;
int Success = 0;
int Failure = 0;

// Table 2
int Collision2 = 0;
int Insertion2 = 0;
int Searching2 = 0;
int WordNumber2 = 0;
int NonEmptySlot2 = 0;
int Success2 = 0;
int Failure2 = 0;

void onelinefilter(string str);
void filefilter(string file);

int main(void) {
	string cmdstr;
	int fexit = 0, status, dictionary = 0;
	printHelp();
	do {
		getline(cin, cmdstr);
		status = readCommand(cmdstr);

		switch (status) {
			case 0: {
				switch (Cmd.num) {
					case 0:
						fexit = 1;
						break;
					case 1:
						printHelp();
						break;
					case 2:
						if (dictionary)
							Add(Cmd.str, Cmd.source, Cmd.hash);
						else
							cout << "There is no words in dictionary\n\n";
						//						cout << "Add    [" << Cmd.str << "] " << (Cmd.source ? "from file " : "word ") << "using hash function " << Cmd.hash << "\n\n";
						break;
					case 3: {
						if (Cmd.hash == 1) {
							Searching = 0;
							Success = 0;
							Failure = 0;
						} else if (Cmd.hash == 2) {
							Searching2 = 0;
							Success2 = 0;
							Failure2 = 0;
						} else if (Cmd.hash == 3) {
							CuckooHash.Searching = 0;
							CuckooHash.Success = 0;
							CuckooHash.Failure = 0;
						}
						if (dictionary)
							Search(Cmd.str, Cmd.source, Cmd.hash);
						else
							cout << "There is no words in dictionary\n\n";
						//						cout << "Search [" << Cmd.str << "] " << (Cmd.source ? "from file " : "word ") << "using hash function " << Cmd.hash << "\n\n";
						break;
					}
					case 4:
						if (dictionary)
							Delete(Cmd.str, Cmd.source, Cmd.hash);
						else
							cout << "There is no words in dictionary\n\n";
						//						cout << "Delete [" << Cmd.str << "] " << (Cmd.source ? "from file " : "word ") << "using hash function " << Cmd.hash << "\n\n";
						break;
					case 5:
					case 6:
						cout << "Table Size : " << TableSize << "\n";
						cout << "P value    : " << Big_p << endl;
						break;
					case 7:
						cout << "Build Dictionary from file [" << Cmd.str << "] with hash function " << Cmd.hash << "\n\n";
						if (!dictionary)
							dictionary = 1;

						if (Table)
							delTable(Table, TableSize);
						if (Table2)
							delTable(Table2, TableSize);

						Table = createTable(TableSize);
						Table2 = createTable(TableSize);
						ClearGlobalVar();

						BuildDictionary(Cmd.str, Cmd.hash);
						if (Cmd.hash == 1) {
							cout << "Hash Table 1\n";
							cout << "Collision              : " << setw(5) << Collision << endl;
							cout << "Load Factor            : " << setw(5) << setprecision(2) << ((double) NonEmptySlot / TableSize) << endl;
							cout << "Insertion              : " << setw(5) << Insertion << endl;
							cout << "Avg Insertion          : " << setw(5) << setprecision(2) << ((double) Insertion / WordNumber) << endl;
							cout << "Word Number            : " << setw(5) << WordNumber << endl;
							cout << "Non-empty Slot         : " << setw(5) << NonEmptySlot << endl << endl;
						} else if (Cmd.hash == 2) {
							cout << "Hash Table 2\n";
							cout << "Collision              : " << setw(5) << Collision2 << endl;
							cout << "Load Factor            : " << setw(5) << setprecision(2) << ((double) NonEmptySlot2 / TableSize) << endl;
							cout << "Insertion              : " << setw(5) << Insertion2 << endl;
							cout << "Avg Insertion          : " << setw(5) << setprecision(2) << ((double) Insertion2 / WordNumber2) << endl;
							cout << "Word Number            : " << setw(5) << WordNumber2 << endl;
							cout << "Non-empty Slot         : " << setw(5) << NonEmptySlot2 << endl << endl;
						} else if (Cmd.hash == 3) {
							cout << "Cuckoo Hash\n";
							cout << "Collision              : " << left << setw(5) << CuckooHash.Collision << endl;
							cout << "Insertion              : " << setw(5) << CuckooHash.Insertion << endl;
							cout << "Avg Insertion          : " << setw(5) << setprecision(2) << ((double) CuckooHash.Insertion / CuckooHash.WordNumber) << endl;
							cout << "Not Stored Word Number : " << setw(5) << (int) CuckooHash.NotStored.size() << endl << endl;
						}
						break;
					case 8: {
						switch (Cmd.hash) {
							case 1:
								if (dictionary)
									outputTable(Cmd.str, TableSize, Cmd.hash);
								else
									cout << "There is no words in dictionary\n\n";
								cout << "Output content of Table 1 to file [" << Cmd.str << "]\n\n";
								break;
							case 2:
								if (dictionary)
									outputTable(Cmd.str, TableSize, Cmd.hash);
								else
									cout << "There is no words in dictionary\n\n";
								cout << "Output content of Table 2 to file [" << Cmd.str << "]\n\n";
								break;
							case 3:
								if (dictionary)
									outputTwoTable(Cmd.str, TableSize);
								else
									cout << "There is no words in dictionary\n\n";
								cout << "Output content of CuckooHash Table to file [" << Cmd.str << "]\n\n";
								break;
						}
						break;
					}
					default:
						cout << "ERROR!\n";
						exit(1);
				}
				break;
			}
			case 1:
				cout << "Command not existed.\n";
				break;
			case 2:
				cout << "[word/file-name]       parameter of command is not string, e.g. \"apple\".\n";
				break;
			case 3:
				cout << "[hash function number] parameter of command is out of range (1 to 3).\n";
				break;
			default:
				cout << "ERROR!\n";
				exit(1);
		}
		cout << endl;
	} while (!fexit);

	return 0;
}
