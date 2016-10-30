/*
 * Bmp.h
 *
 *  Created on: 2016年4月28日
 *      Author: hao
 */

#ifndef BMP_H_
#define BMP_H_

#include "DataStruct.h"

class Bmp {
private:
    string fileName;
	FileHeader fileheader;
	InfoHeader infoheader;
	Color **rawData;
public:
	int OPEN_FILE_STATUS;

    Bmp();
	Bmp(char *fileName);
	virtual ~Bmp();

    string getFileName();
    int open(string FileName);

    void setRawData(Color **rawData);
    Color **getRawData();

	FileHeader getFileheader();
	InfoHeader getInfoheader();

	void print();
};

#endif /* BMP_H_ */
