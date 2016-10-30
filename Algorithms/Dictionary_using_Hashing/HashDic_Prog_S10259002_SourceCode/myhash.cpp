/*
 * myhash.c
 *
 *  Created on: 2015年12月6日
 *      Author: hao
 */

#include "myhash.h"

Myhash *createTable(int size) {
	return new Myhash[size];
}

void delTable(Myhash *ptr, int size) {
	if (ptr) {
		delete[] ptr;
		ptr = NULL;
	}
}

void outputTable(string filename, int size, int hfun) {
	fstream fp;
	fp.open(filename.c_str(), ios::out);
	if (!fp)
		cout << "Fail to open file: " << filename.c_str() << endl;

	int i, j, k;
	fp << "Table size    : " << left << setw(5) << size << endl;
	if (hfun == 1) {
		//		fp << "Word Number      : " << setw(5) << WordNumber << endl;
		//		fp << "Non-empty Slot   : " << setw(5) << NonEmptySlot << endl;
		fp << "Collision     : " << setw(5) << Collision << endl;
		fp << "Load Factor   : " << setw(5) << setprecision(2) << ((double) NonEmptySlot / TableSize) << endl;
		fp << "Insertion     : " << setw(5) << Insertion << endl;
		fp << "Avg Insertion : " << setw(5) << setprecision(2) << ((double) Insertion / WordNumber) << endl << endl;

		for (i = 0; i < size; i++) {
			fp << "[" << setw(3) << i << "] ";
			j = (int) Table[i].slot.size();
			if (j > 0) {
				for (k = 0; k < j; k++) {
					fp << setw(15) << Table[i].slot[k];
					if (k < j - 1)
						fp << ' ';
				}
			}
			fp << endl;
		}
	} else if (hfun == 2) {
		//		fp << "Word Number      : " << setw(5) << WordNumber2 << endl;
		//		fp << "Non-empty Slot   : " << setw(5) << NonEmptySlot2 << endl;
		fp << "Collision     : " << setw(5) << Collision2 << endl;
		fp << "Load Factor   : " << setw(5) << setprecision(2) << ((double) NonEmptySlot2 / TableSize) << endl;
		fp << "Insertion     : " << setw(5) << Insertion2 << endl;
		fp << "Avg Insertion : " << setw(5) << setprecision(2) << ((double) Insertion2 / WordNumber2) << endl << endl;

		for (i = 0; i < size; i++) {
			fp << "[" << std::setw(3) << i << "] ";
			j = (int) Table2[i].slot.size();
			if (j > 0) {
				for (k = 0; k < j; k++) {
					fp << setw(15) << Table2[i].slot[k];
					if (k < j - 1)
						fp << ' ';
				}
			}
			fp << endl;
		}
	}

	fp.close();
}

void outputTwoTable(string filename, int size) {
	fstream fp;
	fp.open(filename.c_str(), ios::out);
	if (!fp)
		cout << "Fail to open file: " << filename.c_str() << endl;

	int i;
	fp << left << "Cuckoo Hash\n";
	fp << "Collision              : " << setw(5) << CuckooHash.Collision << endl;
	fp << "Insertion Number       : " << setw(5) << CuckooHash.Insertion << endl;
	fp << "Avg Insertion          : " << setw(5) << setprecision(2) << ((double) CuckooHash.Insertion / CuckooHash.WordNumber) << endl << endl;
	fp << "Not Stored Word Number : " << setw(5) << (int) CuckooHash.NotStored.size() << endl;

	for (i = 0; i < (int) CuckooHash.NotStored.size(); i++) {
		fp << setw(15) << CuckooHash.NotStored[i];
		if (((i + 1) % 9) < 9)
			fp << " ";
		if (!((i + 1) % 10))
			fp << endl;
	}

	fp << endl << endl;

	fp << "      Table 1             Table 2" << endl;
	for (i = 0; i < size; i++) {
		fp << "[" << setw(3) << i << "] ";
		fp << setw(15) << ((int) Table[i].slot.size() ? Table[i].slot[0] : " ") << "     " << setw(15) << ((int) Table2[i].slot.size() ? Table2[i].slot[0] : " ") << endl;
	}
	fp.close();
}

int hash1(const string k, int q) {
	int k_len = (int) k.length();
	int i, sum = 0, ascii_val;

	for (i = 0; i < k_len; i++) {
		ascii_val = (int) k[i];
		sum += ascii_val;
	}

	return (sum % q);
}

/*
 * Horner’s rule
 *
 */
int hash2(const string k, int q, int p) {
	int k_len = (int) k.length();
	int i, ascii_val;
	unsigned int h = 0;

	for (i = k_len - 1; i >= 0; i--) {
		ascii_val = (int) k[i];
		h = (h * p + ascii_val) % q;
	}

	return h;
}

void cuckooHashInsert(string k, int q) {
	int hash_val, notStore = 1;
	string x = k, y;

	for (int i = 0; i < CuckooHashTimes; i++) {
		hash_val = hash1(x, q);

		// size = 0 is empty
		if (!(int) Table[hash_val].slot.size()) {
			Table[hash_val].slot.push_back(x);

			CuckooHash.Insertion++;
			notStore--;
			break;
		} else {
			CuckooHash.Collision++;
			y = Table[hash_val].slot.back();
			Table[hash_val].slot.pop_back();
			Table[hash_val].slot.push_back(x);
			CuckooHash.Insertion++;
		}

		hash_val = hash2(y, q, Big_p);

		if (!(int) Table2[hash_val].slot.size()) {
			Table2[hash_val].slot.push_back(y);

			CuckooHash.Insertion++;
			notStore--;
			break;
		} else {
			CuckooHash.Collision++;
			x = Table2[hash_val].slot.back();
			Table2[hash_val].slot.pop_back();
			Table2[hash_val].slot.push_back(y);
			CuckooHash.Insertion++;
		}
	}

	if (notStore)
		CuckooHash.NotStored.push_back(x);
}

string filterw(string k) {
	int i, k_len = (int) k.length(), v, s = 0, e = k_len;

	for (i = 0; i < k_len; i++) {
		v = (int) k[i];
		if ((v > 64 && v < 91) || (v > 96 && v < 123) || (v > 47 && v < 58) || v == 39) {
			s = i;
			break;
		}
	}

	for (i = k_len - 1; i > s; i--) {
		v = (int) k[i];
		if ((v > 64 && v < 91) || (v > 96 && v < 123) || (v > 47 && v < 58) || v == 39) {
			e = i + 1;
			break;
		}
	}

	string t = k.substr(s, e - s);
	for (i = 0; i < (int) t.length(); i++) {
		if ((int) t[i] > 64 && (int) t[i] < 91)
			t[i] = tolower(t[i]);
	}

	if (t[0] == '\'')
		t = t.substr(1, t.length() - 1);
	if (t[t.length() - 1] == '\'')
		t = t.substr(0, t.length() - 1);

	return t;
}

void BuildDictionary(string file, int hfun) {
	int hash_val, slot_size;
	vector < string > AtomicBuffer;
// mode = 1 from file
	fstream fp;
	fp.open(file.c_str(), ios::in);
	if (!fp)
		cout << "Fail to open file: " << file << endl;

	string test, word;
	while (!fp.eof()) {
		getline(fp, test);
		istringstream iss(test);
		while (iss >> word) {
			string ct = filterw(word);
			if (ct[0] != '-') {
				int ffind = 0;

				for (int i = 0; i < (int) AtomicBuffer.size(); i++) {
					if (!ct.compare(AtomicBuffer[i])) {
						ffind = 1;
						break;
					}
				}
				if (!ffind)
					AtomicBuffer.push_back(ct);
			}
		}
	}
	fp.close();

	for (int i = 0; i < (int) AtomicBuffer.size(); i++) {
		string ct = AtomicBuffer[i];
		if (hfun == 1) {
			hash_val = hash1(ct, TableSize);
			slot_size = (int) Table[hash_val].slot.size();

			// collision occurred
			if (slot_size)
				Collision++;
			else
				NonEmptySlot++;
			WordNumber++;
			Insertion += 1 + slot_size;
			Table[hash_val].slot.push_back(ct);
		} else if (hfun == 2) {
			hash_val = hash2(ct, TableSize, Big_p);
			slot_size = (int) Table2[hash_val].slot.size();

			// collision occurred
			if (slot_size)
				Collision2++;
			else
				NonEmptySlot2++;

			WordNumber2++;
			Insertion2 += 1 + slot_size;
			Table2[hash_val].slot.push_back(ct);
		} else if (hfun == 3) {
			cuckooHashInsert(ct, TableSize);
			CuckooHash.WordNumber++;
		}
	}
}

// add from keyboard if mode = 0;
// add from file if mode = 1;
void Add(string file_or_word, int mode, int hfun) {
	vector < string > InputBuffer;
	string tmp;
	if (!mode) {
		tmp = filterw(file_or_word);
		if (tmp.compare("-"))
			InputBuffer.push_back(tmp);
	} else {
		fstream fp;
		fp.open(file_or_word.c_str(), ios::in);
		if (!fp)
			cout << "Fail to open file: " << file_or_word << endl;

		string test;
		while (!fp.eof()) {
			getline(fp, test);
			istringstream iss(test);
			while (iss >> tmp) {
				tmp = filterw(tmp);
				if (tmp[0] != '-')
					InputBuffer.push_back(tmp);
			}
		}
		fp.close();
	}

	int hash_val, slot_len;
	for (int i = 0; i < (int) InputBuffer.size(); i++) {
		tmp = InputBuffer[i];
		if (hfun == 1) {
			hash_val = hash1(tmp, TableSize);
			slot_len = (int) Table[hash_val].slot.size();

			// filter same word
			if (!slot_len || !(find(tmp, Table[hash_val].slot) < slot_len)) {
				Table[hash_val].slot.push_back(tmp);
				cout << "+++ Add Word [" << tmp << "] to Table 1\n";
			} else {
				cout << "+++ Add Word [" << tmp << "] to Table 1 => Failure\n";
			}
		} else if (hfun == 2) {
			hash_val = hash2(tmp, TableSize, Big_p);
			slot_len = (int) Table2[hash_val].slot.size();

			// filter same word
			if (!slot_len || !(find(tmp, Table2[hash_val].slot) < slot_len)) {
				Table2[hash_val].slot.push_back(tmp);
				cout << "+++ Add Word [" << tmp << "] to Table 2\n";
			} else {
				cout << "+++ Add Word [" << tmp << "] to Table 2 => Failure\n";
			}
		} else if (hfun == 3) {
			hash_val = hash1(tmp, TableSize);
			slot_len = (int) Table[hash_val].slot.size();

			// filter same word
			if (!slot_len || !(find(tmp, Table[hash_val].slot) < slot_len)) {
				Table[hash_val].slot.push_back(tmp);
				cout << "+++ Add Word [" << tmp << "] to Table 1\n";
			} else {
				hash_val = hash2(tmp, TableSize, Big_p);
				slot_len = (int) Table2[hash_val].slot.size();

				// filter same word
				if (!slot_len || !(find(tmp, Table2[hash_val].slot) < slot_len)) {
					Table2[hash_val].slot.push_back(tmp);
					cout << "+++ Add Word [" << tmp << "] to Table 2\n";
				} else {
					cout << "+++ Add Word [" << tmp << "] to Table 1 and Table 2 => Failure\n";
				}
			}
		}
	}
}

void Delete(string file_or_word, int mode, int hfun) {
	vector < string > InputBuffer;
	string tmp;
	if (!mode) {
		tmp = filterw(file_or_word);
		if (tmp.compare("-"))
			InputBuffer.push_back(tmp);
	} else {
		fstream fp;
		fp.open(file_or_word.c_str(), ios::in);
		if (!fp)
			cout << "Fail to open file: " << file_or_word << endl;

		string test, word;
		while (!fp.eof()) {
			getline(fp, test);
			istringstream iss(test);
			while (iss >> word) {
				string ct = filterw(word);
				if (ct[0] != '-') {
					InputBuffer.push_back(ct);
				}
			}
		}
		fp.close();
	}

	int hash_val, ffind, slot_len;
	for (int i = 0; i < (int) InputBuffer.size(); i++) {
		tmp = InputBuffer[i];
		switch (hfun) {
			case 1:
				hash_val = hash1(tmp, TableSize);
				slot_len = (int) Table[hash_val].slot.size();
				ffind = find(tmp, Table[hash_val].slot);
				if (ffind < slot_len) {
					Table[hash_val].slot.erase(Table[hash_val].slot.begin() + ffind);
					cout << "--- Delete Word [" << tmp << "] in Table 1\n";
				} else {
					cout << "--- Delete Word Failure! Word [" << tmp << "] is not existed in Table 1\n";
				}
				break;
			case 2:
				hash_val = hash2(tmp, TableSize, Big_p);
				slot_len = (int) Table2[hash_val].slot.size();
				ffind = find(tmp, Table2[hash_val].slot);
				if (ffind < slot_len) {
					Table2[hash_val].slot.erase(Table2[hash_val].slot.begin() + ffind);
					cout << "--- Delete Word [" << tmp << "] in Table 2\n";
				} else {
					cout << "--- Delete Word Failure! Word [" << tmp << "] is not existed in Table 2\n";
				}
				break;
			case 3:
				hash_val = hash1(tmp, TableSize);
				slot_len = (int) Table[hash_val].slot.size();
				ffind = find(tmp, Table[hash_val].slot);
				if (ffind < slot_len) {
					Table[hash_val].slot.erase(Table[hash_val].slot.begin() + ffind);
					cout << "--- Delete Word [" << tmp << "] in Table 1\n";
				} else {
					hash_val = hash2(tmp, TableSize, Big_p);
					slot_len = (int) Table2[hash_val].slot.size();
					ffind = find(tmp, Table2[hash_val].slot);
					if (ffind < slot_len) {
						Table2[hash_val].slot.erase(Table2[hash_val].slot.begin() + ffind);
						cout << "--- Delete Word [" << tmp << "] in Table 2\n";
					} else {
						cout << "--- Delete Word Failure! Word [" << tmp << "] is not existed in Table 1\n";
					}
				}
				break;
			default:
				cout << "ERROR!\n";
				exit(1);
		}
	}
}

void Search(string file_or_word, int mode, int hfun) {
	vector < string > InputBuffer;
	string tmp;

	if (!mode) {
		tmp = filterw(file_or_word);
		if (tmp.compare("-"))
			InputBuffer.push_back(tmp);
	} else {
		fstream fp;
		fp.open(file_or_word.c_str(), ios::in);
		if (!fp)
			cout << "Fail to open file: " << file_or_word << endl;

		string test;
		while (!fp.eof()) {
			getline(fp, test);
			istringstream iss(test);
			while (iss >> tmp) {
				tmp = filterw(tmp);
				if (tmp[0] != '-')
					InputBuffer.push_back(tmp);
			}
		}
		fp.close();
	}

	int ffind, tableNum, SearchWordNum = 0;
	double START_TIME, END_TIME;
	START_TIME = getCPUTime();
	for (int i = 0; i < (int) InputBuffer.size(); i++) {
		tmp = InputBuffer[i];

		ffind = searchTable(tmp, hfun, &tableNum);
		SearchWordNum++;
	}
	END_TIME = getCPUTime();
	switch (hfun) {
		case 1:
			cout << "### Searching         : " << setw(5) << Searching << endl;
			cout << "### Average Searching : " << setw(5) << setprecision(2) << ((double) Searching / SearchWordNum) << endl;
			cout << "### Success           : " << setw(5) << setprecision(2) << ((double) Success / SearchWordNum) << endl;
			cout << "### Failure           : " << setw(5) << setprecision(2) << ((double) Failure / SearchWordNum) << "\n\n";
			break;
		case 2:
			cout << "### Searching         : " << setw(5) << Searching2 << endl;
			cout << "### Average Searching : " << setw(5) << setprecision(2) << ((double) Searching2 / SearchWordNum) << endl;
			cout << "### Success           : " << setw(5) << setprecision(2) << ((double) Success2 / SearchWordNum) << endl;
			cout << "### Failure           : " << setw(5) << setprecision(2) << ((double) Failure2 / SearchWordNum) << "\n\n";
			break;
		case 3:
			cout << "### Searching         : " << setw(5) << CuckooHash.Searching << endl;
			cout << "### Average Searching : " << setw(5) << setprecision(2) << ((double) CuckooHash.Searching / SearchWordNum) << endl;
			cout << "### Success           : " << setw(5) << setprecision(2) << ((double) CuckooHash.Success / SearchWordNum) << endl;
			cout << "### Failure           : " << setw(5) << setprecision(2) << ((double) CuckooHash.Failure / SearchWordNum) << endl;
			break;
		default:
			cout << "ERROR!\n";
			exit(1);
	}
	cout << "### Taken Time        : " << setw(5) << (END_TIME - START_TIME) << "\n\n";
}

int searchTable(string k, int hfun, int *whichTable) {
	int ffind = -1, hash_val, idx;
	int slot_len;
	switch (hfun) {
		case 1:
			hash_val = hash1(k, TableSize);
			idx = find(k, Table[hash_val].slot);
			slot_len = (int) Table[hash_val].slot.size();

			if (idx < slot_len) {
				Success++;
				Searching += idx + 1;
				ffind = hash_val;
				*whichTable = 1;
			} else {
				Failure++;
				Searching += idx;
			}
			break;
		case 2:
			hash_val = hash2(k, TableSize, Big_p);
			idx = find(k, Table2[hash_val].slot);
			slot_len = (int) Table2[hash_val].slot.size();

			if (idx < slot_len) {
				Success2++;
				Searching2 += idx + 1;
				ffind = hash_val;
				*whichTable = 2;
			} else {
				Failure2++;
				Searching2 += idx;
			}
			break;
		case 3:
			hash_val = hash1(k, TableSize);
			if (Table[hash_val].slot.size() && !Table[hash_val].slot[0].compare(k)) {
				CuckooHash.Searching++;
				ffind = hash_val;
				*whichTable = 1;
			} else {
				hash_val = hash2(k, TableSize, Big_p);
				if (Table2[hash_val].slot.size() && !Table2[hash_val].slot[0].compare(k)) {
					CuckooHash.Searching++;
					ffind = hash_val;
					*whichTable = 2;
				}
			}

			if ((ffind + 1))
				CuckooHash.Success++;
			else
				CuckooHash.Failure++;
			break;
		default:
			cout << "ERROR!\n";
	}
	return ffind;
}

int find(string str, vector<string> list) {
	int i, len = (int) list.size();
	for (i = 0; i < len && strcmp(str.c_str(), list[i].c_str()); i++)
		;
	return i;
}

/*
 * parse cmd
 * Help print command introduction
 * Add    word to table cmd : /add    [word/file-name] [-f] [hash function number]
 * Search word in table cmd : /search [word/file-name] [-f] [hash function number]
 * Delete word in table cmd : /del    [word/file-name] [-f] [hash function number]
 *
 * Example of cmd:
 * add    : /add "apple" 1
 * search : /search "article.txt" -f 2
 * delete : /del "apple" 1
 *
 * 0: execute command.
 * 1: command not existed.
 * 2: [word/file-name]       parameter of command is not string, ig. "apple".
 * 3: [hash function number] parameter of command is out of range (1 to 3).
 */
int readCommand(string cmd) {
	// tolower
	int i, quote = 0;
	for (i = 0; i < (int) cmd.length(); i++) {
		if (cmd[i] == '"')
			quote = !quote;

		if (!quote && (int) cmd[i] > 64 && (int) cmd[i] < 91)
			cmd[i] = tolower(cmd[i]);
	}
	istringstream cmdParse(cmd);
	string tmp;

	// confirm cmd type
	cmdParse >> tmp;
	if (!tmp.compare("/exit")) {
		Cmd.num = 0;
		return 0;
	} else if (!tmp.compare("/help")) {
		Cmd.num = 1;
		return 0;
	} else if (!tmp.compare("/psetting")) {
		Cmd.num = 5;
		return 0;
	} else if (!tmp.compare("/ssetting")) {
		Cmd.num = 6;

		int newq, newp;

		cout << "Enter new table size: ";
		cin >> newq;
		if ((newq + 1))
			TableSize = newq;

		cout << "Enter new p value: ";
		cin >> newp;
		if ((newp + 1))
			Big_p = newp;

		cin.ignore(256, '\n');
		return 0;
	} else if (!tmp.compare("/add"))
		Cmd.num = 2;
	else if (!tmp.compare("/search"))
		Cmd.num = 3;
	else if (!tmp.compare("/del"))
		Cmd.num = 4;
	else if (!tmp.compare("/builddic"))
		Cmd.num = 7;
	else if (!tmp.compare("/otable"))
		Cmd.num = 8;
	else
		return 1;

	// get command content
	cmdParse >> tmp;
	if (tmp[0] != '"' || tmp[tmp.length() - 1] != '"')
		return 2;
	Cmd.str = tmp.substr(1, tmp.length() - 2);

	cmdParse >> tmp;
	if (tmp.compare("-f")) {
		// not file
		Cmd.source = 0;
		Cmd.hash = (int) tmp[0] - '0';

		if (Cmd.hash < 1 || Cmd.hash > 3)
			return 3;
	} else {
		// file
		Cmd.source = 1;
		cmdParse >> tmp;
		Cmd.hash = (int) tmp[0] - '0';

		if (Cmd.hash < 1 || Cmd.hash > 3)
			return 3;
	}

	return 0;
}

void printHelp() {
	cout << "/help      print all of commands\n";
	cout << "/buildDic  [word/file-name] [hash function number]\n";
	cout << "/add       [word/file-name] [-f] [hash function number]\n";
	cout << "/search    [word/file-name] [-f] [hash function number]\n";
	cout << "/del       [word/file-name] [-f] [hash function number]\n";
	cout << "/gsetting  get table size and p value\n";
	cout << "/ssetting  set table size and p value, where -1 represent not change\n";
	cout << "/otable    [file-name] [hash function number] | out put table content\n";
	cout << "/exit      exit program\n\n";

	cout << "Example of cmd:\n";
	cout << "add    : /add \"apple\" 1\n";
	cout << "search : /search \"article.txt\" -f 2\n";
	cout << "delete : /del \"apple\" 1\n\n";
}

void ClearGlobalVar() {
	CuckooHash.Collision = 0;
	CuckooHash.Insertion = 0;
	CuckooHash.Searching = 0;
	CuckooHash.WordNumber = 0;
	CuckooHash.Success = 0;
	CuckooHash.Failure = 0;
	CuckooHash.NotStored.clear();

	// Table
	Collision = 0;
	Insertion = 0;
	Searching = 0;
	WordNumber = 0;
	NonEmptySlot = 0;
	Success = 0;
	Failure = 0;

	// Table 2
	Collision2 = 0;
	Insertion2 = 0;
	Searching2 = 0;
	WordNumber2 = 0;
	NonEmptySlot2 = 0;
	Success2 = 0;
	Failure2 = 0;
}

double getCPUTime() {
	struct rusage rusage;
	if (getrusage(0, &rusage) != -1)
		return (double) rusage.ru_utime.tv_sec + (double) rusage.ru_utime.tv_usec / 1000000.0;

	return -1.0; /* Failed. */
}
