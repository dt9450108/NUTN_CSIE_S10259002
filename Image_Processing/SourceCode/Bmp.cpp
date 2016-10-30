/*
 * Bmp.cpp
 *
 *  Created on: 2016年4月28日
 *      Author: hao
 */

#include "Bmp.h"

Bmp::Bmp() {
    this->rawData = NULL;
    OPEN_FILE_STATUS = 0;
}

Bmp::Bmp(char *fileName) {
    // TODO Auto-generated constructor stub
    this->fileName = fileName;
    this->rawData = NULL;
    OPEN_FILE_STATUS = 0;
    open(fileName);
}

Bmp::~Bmp() {
    dClr2DArr(this->rawData, infoheader.imgHeight);
}

string Bmp::getFileName() {
    return fileName;
}

/*
 *  open bmp file
 *  return
 *      0: success to open file
 *      1: fail to open file
 *      2: not a bmp file
 *      3: not support a compressed bmp image
 *      4: not support such a bit per pixel bmp
 */
int Bmp::open(string FileName) {
    fstream file;
    this->fileName = FileName;

    int size = fileSize(FileName.c_str());
    file.open(FileName.c_str(), ios::in | ios::binary);

    // cannot open the file
    if(!file) {
        cout << "Fail to open file: " << FileName << endl;
        return 1;
    }

    // bmp file header
    file.read((char *) &fileheader.type1, sizeof(HWORD));
    file.read((char *) &fileheader.type2, sizeof(HWORD));
    file.read((char *) &fileheader.fileSize, sizeof(BYTE));
    file.read((char *) &fileheader.reserve1, sizeof(WORD));
    file.read((char *) &fileheader.reserve2, sizeof(WORD));
    file.read((char *) &fileheader.offset, sizeof(BYTE));

// check is bmp file?
    if (fileheader.type1 != 'B' || fileheader.type2 != 'M' || size != fileheader.fileSize) {
        cout << "Not a BMP image file." << endl;
        OPEN_FILE_STATUS = 0;
        return 2;
    }

    // bmp info header
    file.read((char *) &infoheader.infoHeaderSize, sizeof(BYTE));
    file.read((char *) &infoheader.imgWidth, sizeof(DWORD));
    file.read((char *) &infoheader.imgHeight, sizeof(DWORD));
    file.read((char *) &infoheader.bitPlanes, sizeof(WORD));
    file.read((char *) &infoheader.bitCounts, sizeof(WORD));
    file.read((char *) &infoheader.compressionType, sizeof(DWORD));
    file.read((char *) &infoheader.imgSize, sizeof(DWORD));
    file.read((char *) &infoheader.horizontalRevo, sizeof(DWORD));
    file.read((char *) &infoheader.verticalRevo, sizeof(DWORD));
    file.read((char *) &infoheader.clrUsed, sizeof(BYTE));
    file.read((char *) &infoheader.clrImportant, sizeof(BYTE));

    // not support compressed image
    if (infoheader.compressionType) {
        cout << "Not support compressed image." << endl;
        OPEN_FILE_STATUS = 0;
        return 3;
    }

    // get palette, or not
    int colorPaletteSize;
    if (infoheader.bitCounts != 24 && (int) fileheader.offset != 54) {
        colorPaletteSize = 1 << infoheader.bitCounts;
    } else {
        colorPaletteSize = 0;
    }

    Color *palette = NULL;
    Color clr;
    if (colorPaletteSize) {
        palette = ClrArr(colorPaletteSize);
        HWORD r, g, b, reserve;
        for (int i = 0; i < colorPaletteSize; i++) {
            file.read((char *) &b, sizeof(HWORD));
            file.read((char *) &g, sizeof(HWORD));
            file.read((char *) &r, sizeof(HWORD));
            file.read((char *) &reserve, sizeof(HWORD));
            clr.r = r;
            clr.g = g;
            clr.b = b;
            clr.reserve = reserve;
            palette[i] = clr;
        }
    }

    OPEN_FILE_STATUS = 1;

    // read raw data
    rawData = Clr2DArr(infoheader.imgHeight, infoheader.imgWidth);
    double dRowBytes = (infoheader.imgWidth * infoheader.bitCounts) / 8.0;
    int rowBytes = (int) (infoheader.imgWidth * infoheader.bitCounts + 7) / 8;
    int alignBytes = ALIGN(rowBytes);
    int paddedBits = (int) (((double) alignBytes - dRowBytes) * 8);

    // debug
    cout << "dRowBytes: " << dRowBytes << endl;
    cout << "rowBytes: " << rowBytes << endl;
    cout << "alignBytes: " << alignBytes << endl;
    cout << "paddedBits: " << paddedBits << endl;

    int shiftLowBound = 0;
    int col = 0;
    int COUNT_CHECK = 0;	// debug

    switch (infoheader.bitCounts) {
        case 1: {
                HWORD byte;
                for (int i = infoheader.imgHeight - 1; i >= 0; i--) {
                    col = 0;
                    for (int j = 0; j < alignBytes; j++) {
                        file.read((char *) &byte, sizeof(HWORD));

                        // if not reach padded byte
                        if (j < rowBytes - 1) {
                            // each bit is valid
                            shiftLowBound = 0;
                        } else if (j == rowBytes - 1) {
                            // reach padded byte
                            shiftLowBound = paddedBits % 8;
                        } else {
                            shiftLowBound = 8;
                        }
                        for (int k = 7; k >= shiftLowBound; k--) {
                            COUNT_CHECK++;
                            rawData[i][col++] = palette[(int) ((byte >> k) & 1)];
                        }
                    }
                }
                break;
            }
        case 4: {
                HWORD byte;
                for (int i = infoheader.imgHeight - 1; i >= 0; i--) {
                    col = 0;
                    for (int j = 0; j < alignBytes; j++) {
                        file.read((char *) &byte, sizeof(HWORD));

                        // if not reach padded byte
                        if (j < rowBytes - 1) {
                            // each bit is valid
                            shiftLowBound = 0;
                        } else if (j == rowBytes - 1) {
                            // reach padded byte
                            shiftLowBound = paddedBits;
                        } else {
                            shiftLowBound = 8;
                        }
                        for (int k = 7 - 4 + 1; k >= shiftLowBound; k -= 4) {
                            COUNT_CHECK++;
                            rawData[i][col++] = palette[(int) ((byte >> k) & 0xf)];
                        }
                    }
                }
                break;
            }
        case 8: {
                HWORD byte;
                for (int i = infoheader.imgHeight - 1; i >= 0; i--) {
                    col = 0;
                    for (int j = 0; j < alignBytes; j++) {
                        file.read((char *) &byte, sizeof(HWORD));

                        // if not reach padded byte
                        if (j < rowBytes) {
                            // each bit is valid
                            COUNT_CHECK++;
                            rawData[i][col++] = palette[(int) byte];
                        }
                    }
                }
                break;
            }
        case 16: {
//			octave
//			d1=permute(reshape(d, 64, 3, 127), [1 3 2]);
                WORD byte;
                for (int i = infoheader.imgHeight - 1; i >= 0; i--) {
                    col = 0;
                    for (int j = 0; j < alignBytes; j += 2) {
                        file.read((char *) &byte, sizeof(WORD));
                        if (j < rowBytes) {
                            // each bit is valid
                            COUNT_CHECK++;
                            clr.r = (int) ((byte >> 7) & 0xF8);
                            clr.g = (int) ((byte >> 2) & 0xF8);
                            clr.b = (int) ((byte << 3) & 0xF8);
                            rawData[i][col++] = clr;
                        }
                    }
                }
                break;
            }
        case 24: {
                HWORD byte;
                for (int i = infoheader.imgHeight - 1; i >= 0; i--) {
                    col = 0;
                    for (int j = 0; j < alignBytes; j += 3) {
                        if (j < rowBytes) {
                            // each bit is valid
                            COUNT_CHECK++;
                            file.read((char *) &byte, sizeof(HWORD));
                            clr.b = (int) byte;

                            file.read((char *) &byte, sizeof(HWORD));
                            clr.g = (int) byte;

                            file.read((char *) &byte, sizeof(HWORD));
                            clr.r = (int) byte;

                            rawData[i][col++] = clr;
                        } else {
                            // ignore padded 3 bytes
                            file.read((char *) &byte, 3);
                        }
                    }
                }
                break;
            }
        case 32: {
                HWORD byte;
                for (int i = infoheader.imgHeight - 1; i >= 0; i--) {
                    col = 0;
                    for (int j = 0; j < alignBytes; j += 4) {
                        COUNT_CHECK++;
                        file.read((char *) &byte, sizeof(HWORD));
                        clr.b = (int) byte;

                        file.read((char *) &byte, sizeof(HWORD));
                        clr.g = (int) byte;

                        file.read((char *) &byte, sizeof(HWORD));
                        clr.r = (int) byte;

                        file.read((char *) &byte, sizeof(HWORD));
                        clr.reserve = (int) byte;

                        rawData[i][col++] = clr;
                    }
                }
                break;
            }
        default:
            cout << "Not support " << infoheader.bitCounts << " bit(s)." << endl;
            return 4;
    }

    // delete palette pointer
    if (colorPaletteSize) {
        dClrArr(palette);
    }

    // debug
    cout << "\nCOUNT CHECK : " << COUNT_CHECK << endl;

    file.close();
    return 0;
}

void Bmp::print() {
    if (OPEN_FILE_STATUS) {
        cout << "====================    Bmp File Header    ====================" << endl;
        cout << "Type: " << fileheader.type1 << fileheader.type2 << endl;
        cout << "File Size: " << fileheader.fileSize << " bytes" << endl;
        cout << "Reserve 1: " << fileheader.reserve1 << endl;
        cout << "Reserve 2: " << fileheader.reserve2 << endl;
        cout << "Raw data offset: " << fileheader.offset << " bytes" << endl;
        cout << endl;
        cout << "====================    Bmp Information Header    ====================" << endl;
        cout << "Information header size: " << infoheader.infoHeaderSize << " bytes" << endl;
        cout << "Image size: " << infoheader.imgWidth << " * " << infoheader.imgHeight << " pixels" << endl;
        cout << "Image bit plane: " << infoheader.bitPlanes << endl;
        cout << "Image bit count: " << infoheader.bitCounts << endl;
        cout << "Image compression type: " << infoheader.compressionType << endl;
        cout << "Image file size: " << infoheader.imgSize << " bytes" << endl;
        cout << "Horizontal revolution: " << infoheader.horizontalRevo << " pixel/m" << endl;
        cout << "Vertical revolution: " << infoheader.verticalRevo << " pixel/m" << endl;
        cout << "Number of colors used: " << infoheader.clrUsed << endl;
        cout << "Number of colors are important: " << infoheader.clrImportant << endl;
        cout << endl;
    } else {
        cout << "Not a BMP image file." << endl;
    }
}

void Bmp::setRawData(Color **rawData) {
    this->rawData = rawData;
}

Color **Bmp::getRawData() {
    return rawData;
}

FileHeader Bmp::getFileheader() {
    return fileheader;
}

InfoHeader Bmp::getInfoheader() {
    return infoheader;
}
