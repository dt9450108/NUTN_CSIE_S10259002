/*
 * DataStruct.h
 *
 *  Created on: 2016年4月28日
 *      Author: hao
 */

#ifndef DATASTRUCT_H_
#define DATASTRUCT_H_
#include <iostream>
#include <fstream>
#include <iomanip>
#include <cstdio>
#include <string>
using namespace std;

#define ALIGN(x) ((x + 3) >> 2 << 2)

typedef unsigned char HWORD;
typedef unsigned short WORD;
typedef unsigned int BYTE;
typedef int DWORD;
typedef struct Color_s {
	int r;
	int g;
	int b;
	int reserve;
} Color;

typedef struct FileHeader_s {
	HWORD type1;
	HWORD type2;
	DWORD fileSize;
	WORD reserve1;
	WORD reserve2;
	DWORD offset;
} FileHeader;

typedef struct InfoHeader_s {
	DWORD infoHeaderSize;
	DWORD imgWidth;
	DWORD imgHeight;
	WORD bitPlanes;
	WORD bitCounts;
	DWORD compressionType;
	DWORD imgSize;
	DWORD horizontalRevo;
	DWORD verticalRevo;
	DWORD clrUsed;
	DWORD clrImportant;
} InfoHeader;

int fileSize(const char* filename);

// Color array
Color *ClrArr(int col);
void dClrArr(Color *list);

// 2d Color array
Color **Clr2DArr(int row, int col);
void dClr2DArr(Color **list, int row);

#endif /* DATASTRUCT_H_ */
