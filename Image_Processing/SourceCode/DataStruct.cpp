/*
 * DataStruct.cpp
 *
 *  Created on: 2016年4月28日
 *      Author: hao
 */

#include "DataStruct.h"

int fileSize(const char *filename) {
    ifstream in(filename, ios::ate | ios::binary);
    return in.tellg();
}

// Color array
Color *ClrArr(int col) {
    return new Color[col];
}

void dClrArr(Color *list) {
    if (list != NULL) {
        delete[] list;
        list = NULL;
    }
}

// 2d Color array
Color **Clr2DArr(int row, int col) {
    Color **t = new Color*[row];
    for (int i = 0; i < row; i++) {
        t[i] = new Color[col];
    }
    return t;
}

void dClr2DArr(Color **list, int row) {
    if (list != NULL) {
        for (int i = 0; i < row; i++) {
            delete[] list[i];
        }
        delete[] list;
        list = NULL;
    }
}
