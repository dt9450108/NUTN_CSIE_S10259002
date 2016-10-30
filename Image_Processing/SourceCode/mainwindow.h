#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QFileDialog>
#include <QDesktopWidget>
#include "DataStruct.h"
#include "Bmp.h"
#include "drawbmp.h"

namespace Ui {
    class MainWindow;
}

class MainWindow : public QMainWindow {
        Q_OBJECT

    public:
        explicit MainWindow(QWidget *parent = 0);
        ~MainWindow();

    private slots:
        // click to open the window select the .bmp file
        void on_BtnOpenFile_clicked();

        // click to clear all data of file header and information header
        void on_btnClear_clicked();

        // set information of the image
        void setBMPInformation();

        // get desktop screen size
        void desktopSize();

        void on_BtnRedraw_clicked();

    private:
        Ui::MainWindow *ui;
        DrawBmp *dbmp;
        Bmp *bmp;
        int desktopWidth;
        int desktopHeight;
};

#endif // MAINWINDOW_H
