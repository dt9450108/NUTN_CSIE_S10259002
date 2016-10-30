/*
 * myqueue.h
 *
 *  Created on: 2015年10月25日
 *      Author: hao
 */

#ifndef MYQUEUE_H_
#define MYQUEUE_H_

#include <stdio.h>

typedef struct queue_s {
	int size;
	int rear;
	int front;
	int flag;
	int *queue;
} MyQueue;

MyQueue *mygenQueue(int size);
void myfreeQueue(MyQueue *myq);
int myqFull(MyQueue *myq);
int myqEmpty(MyQueue *myq);
void myqAdd(MyQueue *myq, int item);
int myqPop(MyQueue *myq);
void myqPrint(MyQueue *myq);

#endif /* MYQUEUE_H_ */
