/*
 * myqueue.c
 *
 *  Created on: 2015年10月25日
 *      Author: hao
 */

#include <stdio.h>
#include <stdlib.h>
#include "myqueue.h"

MyQueue *mygenQueue(int size) {
	MyQueue *t = (MyQueue *) malloc(sizeof(MyQueue));
	t->size = size;
	t->rear = -1;
	t->front = -1;
	t->flag = 0;
	t->queue = malloc(sizeof(int) * size);
	return t;
}

void myfreeQueue(MyQueue *myq) {
	free(myq->queue);
	free(myq);
}

int myqFull(MyQueue *myq) {
	return (myq->rear % myq->size == myq->front);
}

int myqEmpty(MyQueue *myq) {
	return myq->front == myq->rear;
}

void myqAdd(MyQueue *myq, int item) {
	if ((myqFull(myq) && myq->flag == 1) || (myq->rear == myq->size - 1 && myq->front == -1)) {
		printf("Circular Queue is full!\n");
		return;
	}

	myq->rear = (myq->rear + 1) % myq->size;
	myq->queue[myq->rear] = item;
	if (myq->front == myq->rear)
		myq->flag = 1;
}

int myqPop(MyQueue *myq) {
	if (myqEmpty(myq) && myq->flag == 0) {
		printf("Circular Queue is empty!\n");
		return 0;
	}

	myq->front = (myq->front + 1) % myq->size;
	if (myq->front == myq->rear)
		myq->flag = 0;
	return myq->queue[myq->front];
}

void myqPrint(MyQueue *myq) {
	if (myqEmpty(myq) && myq->flag == 0) {
		printf("Circular Queue is empty!\n");
		return;
	}

	int i;
	printf("\n=-=-=-=-=-=-=-=-=-= Circular Queue =-=-=-=-=-=-=-=-=-=\n");
	for (i = 0; i < myq->size; i++)
		printf("%d ", myq->queue[i]);
	printf("\n=-=-=-=-=-=-=-=-=-= Circular Queue =-=-=-=-=-=-=-=-=-=\n\n");

}
