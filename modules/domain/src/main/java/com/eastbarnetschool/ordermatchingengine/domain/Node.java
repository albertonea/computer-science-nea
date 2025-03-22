package com.eastbarnetschool.ordermatchingengine.domain;

public class Node<T> {
    T data;
    int priority;

    Node(T data, int priority) {
        this.data = data;
        this.priority = priority;
    }
}
