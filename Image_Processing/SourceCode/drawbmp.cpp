#include "drawbmp.h"

DrawBmp::DrawBmp(QWidget *parent) : QWidget(parent) {
    this->setStyleSheet("background-color: #fff;");
}

DrawBmp::DrawBmp(QString title, int x, int y, int width, int height, Color **imgData) {
    this->setWindowTitle(title);
    this->setMaximumSize(width, height);
    this->setMinimumSize(width, height);
    this->setWindowIcon(QIcon(":/icon.png"));

    this->height = height;
    this->width = width;
    this->imgData = imgData;
    this->setGeometry(x, y, width, height);
    this->setStyleSheet("background-color: #fff;");
}

void DrawBmp::closeEvent(QCloseEvent *event) {
    this->hide();
}

DrawBmp::~DrawBmp() {
    dClr2DArr(this->imgData, height);
}

void DrawBmp::paintEvent(QPaintEvent *) {
    QPainter painter(this);
    drawImage(&painter);
}

void DrawBmp::drawImage(QPainter *p) {
    // save current painting device status
    p->save();
    for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
            pen.setColor(QColor(imgData[i][j].r, imgData[i][j].g, imgData[i][j].b));
            p->setPen(pen);
            p->drawLine(j, i, j, i);
        }
    }
    p->restore();
}
