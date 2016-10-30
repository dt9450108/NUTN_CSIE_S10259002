#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow) {
    ui->setupUi(this);
    this->dbmp = NULL;
    this->bmp = NULL;
    this->desktopSize();
}

MainWindow::~MainWindow() {
    delete ui;
}

void MainWindow::on_BtnOpenFile_clicked() {
    // click all data
    ui->btnClear->click();

    QString FilePath;
    string fileName;

    ui->InputFileName->setText("");
    ui->LbErrorMsg->setStyleSheet("color: red;");
    ui->LbErrorMsg->setText("");

    this->setStyleSheet("MainWindow {font: 16pt \"Time New Roman\";}");
    FilePath = QFileDialog::getOpenFileName(this, "Select the BMP file...", "", tr("Images (*.bmp)"));

    if (!FilePath.isEmpty()) {
        ui->InputFileName->setText(FilePath);
        fileName = FilePath.toLocal8Bit().toStdString();

        /* read bmp file
        *      0: success to open file
        *      1: fail to open file
        *      2: not a bmp file
        *      3: not support a compressed bmp image
        *      4: not support such a bit per pixel bmp
        */
        bmp = new Bmp();
        int status = bmp->open(fileName);
        switch (status) {
            case 1:
                cout << "Fail to open the file: " << fileName << "." << endl;
                ui->LbErrorMsg->setText(QString("Fail to Open the file: ") + QString::fromStdString(fileName) + QString("."));
                return;
            case 2:
                cout << "Not a BMP file." << endl;
                ui->LbErrorMsg->setText(QString("Not a BMP file."));
                return;
            case 3:
                cout << "Not support a compressed BMP file." << endl;
                ui->LbErrorMsg->setText(QString("Not support a compressed BMP file."));
                return;
            case 4:
                cout << "Not support such a BMP file of bit(s) per pixel." << endl;
                ui->LbErrorMsg->setText(QString("Not support such a BMP file of bit(s) per pixel."));
                return;
            default:
                cout << "success to open the file: " << fileName << endl;
        }
        bmp->print();

        setBMPInformation();

        int imgH = bmp->getInfoheader().imgHeight;
        int imgW = bmp->getInfoheader().imgWidth;

        QString title = FilePath.section('/', -1);
        dbmp = new DrawBmp(title, (this->desktopWidth - imgW) / 2, (this->desktopHeight - imgH) / 2, imgW, imgH, bmp->getRawData());
        dbmp->show();
    } else {
        ui->LbErrorMsg->setText("Unselect any file!");
    }
}

void MainWindow::setBMPInformation() {
    ui->LbErrorMsg->setText("");

    // file header
    ui->TfBmpType->setText(QString("%1%2").arg((char) bmp->getFileheader().type1).arg((char) bmp->getFileheader().type2));
    ui->TfBmpFileSize->setText(QString("%1 bytes").arg((int) bmp->getFileheader().fileSize));
    ui->TfBmpReserve1->setText(QString("%1").arg((int) bmp->getFileheader().reserve1));
    ui->TfBmpReserve2->setText(QString("%1").arg((int) bmp->getFileheader().reserve2));
    ui->TfBmpRawOffset->setText(QString("%1 bytes").arg((int) bmp->getFileheader().offset));

    // information header
    ui->TfBmpInfoHeadSize->setText(QString("%1 bytes").arg((int) bmp->getInfoheader().infoHeaderSize));
    ui->TfBmpImgSize->setText(QString("%1 * %2 pixel(s)").arg((int) bmp->getInfoheader().imgWidth).arg((int) bmp->getInfoheader().imgHeight));
    ui->TfBmpBitPlane->setText(QString("%1").arg((int) bmp->getInfoheader().bitPlanes));
    ui->TfBmpBitCount->setText(QString("%1 bit(s)").arg((int) bmp->getInfoheader().bitCounts));
    ui->TfBmpCompression->setText(QString("%1").arg((int) bmp->getInfoheader().compressionType));
    ui->TfBmpImgFileSize->setText(QString("%1 bytes").arg((int) bmp->getInfoheader().imgSize));
    ui->TfBmpHoriRevo->setText(QString("%1 pixels/meter").arg((int) bmp->getInfoheader().horizontalRevo));
    ui->TfBmpVertiRevo->setText(QString("%1 pixels/meter").arg((int) bmp->getInfoheader().verticalRevo));
    ui->TfBmpClrUsed->setText(QString("%1").arg((int) bmp->getInfoheader().clrUsed));
    ui->TfBmpClrImportant->setText(QString("%1").arg((int) bmp->getInfoheader().clrImportant));
}

void MainWindow::on_btnClear_clicked() {
    if (this->dbmp != NULL) {
        delete this->dbmp;
        this->dbmp = NULL;
    }

    if (this->bmp != NULL) {
        this->bmp->setRawData(NULL);
        delete this->bmp;
        this->bmp = NULL;
    }

    ui->InputFileName->setText("");
    ui->LbErrorMsg->setText("");

    // clear file header
    ui->TfBmpType->setText("");
    ui->TfBmpFileSize->setText("");
    ui->TfBmpReserve1->setText("");
    ui->TfBmpReserve2->setText("");
    ui->TfBmpRawOffset->setText("");

    // clear information header
    ui->TfBmpInfoHeadSize->setText("");
    ui->TfBmpImgSize->setText("");
    ui->TfBmpBitPlane->setText("");
    ui->TfBmpBitCount->setText("");
    ui->TfBmpCompression->setText("");
    ui->TfBmpImgFileSize->setText("");
    ui->TfBmpHoriRevo->setText("");
    ui->TfBmpVertiRevo->setText("");
    ui->TfBmpClrUsed->setText("");
    ui->TfBmpClrImportant->setText("");
}

void MainWindow::desktopSize() {
    QDesktopWidget *desktop = QApplication::desktop();
    this->desktopWidth = desktop->width();
    this->desktopHeight = desktop->height();
}

void MainWindow::on_BtnRedraw_clicked() {
    if (this->dbmp != NULL) {
        if (this->dbmp->isHidden()) {
            this->dbmp->show();
        } else {
            dbmp->activateWindow();
        }
    }
}
