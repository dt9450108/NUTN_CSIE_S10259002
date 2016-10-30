#ifndef DRAWBMP_H
#define DRAWBMP_H

#include <QWidget>
#include <QPen>
#include <QPainter>
#include <QColor>
#include <QIcon>
#include "DataStruct.h"

class DrawBmp : public QWidget {
        Q_OBJECT
    public:
        explicit DrawBmp(QWidget *parent = 0);
        DrawBmp(QString title, int x, int y, int width, int height, Color **imgData);
        ~DrawBmp();

    signals:

    public slots:

    protected:
        void paintEvent(QPaintEvent *event) Q_DECL_OVERRIDE;
        void closeEvent(QCloseEvent *event) Q_DECL_OVERRIDE;
        void drawImage(QPainter *p);

    private:
        int height;
        int width;
        QPen pen;
        Color **imgData;
};

#endif // DRAWBMP_H
