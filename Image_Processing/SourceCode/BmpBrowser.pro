#-------------------------------------------------
#
# Project created by QtCreator 2016-04-28T18:57:15
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = BmpBrowser
TEMPLATE = app

SOURCES += main.cpp\
        mainwindow.cpp \
    drawbmp.cpp \
    DataStruct.cpp \
    Bmp.cpp

HEADERS  += mainwindow.h \
    drawbmp.h \
    DataStruct.h \
    Bmp.h

FORMS    += mainwindow.ui

RESOURCES += \
    imgs.qrc

DISTFILES += \
    application.rc

RC_FILE += \
    application.rc
