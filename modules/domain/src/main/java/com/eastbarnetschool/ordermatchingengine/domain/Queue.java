package com.eastbarnetschool.ordermatchingengine.domain;

public class Queue<T> {
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head; // Front of the queue (for dequeue operations)
    private Node tail; // Back of the queue (for enqueue operations)
    private int size;  // Number of elements in the queue

    // Creates an empty queue.
    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    // Adds an element to the back of the queue.
    public void enqueue(T item) {
        Node newNode = new Node(item);

        if (isEmpty()) {
            // If queue is empty, the new node is both head and tail
            head = newNode;
            tail = newNode;
        } else {
            // Otherwise, add to the end and update the tail
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    // Removes and returns the element at the front of the queue.
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        T data = head.data;
        head = head.next;

        if (head == null) {
            // If queue becomes empty, update tail as well
            tail = null;
        }

        size--;
        return data;
    }

    // Returns, but does not remove, the element at the front of the queue.
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        return head.data;
    }

    // Returns the number of elements in the queue.
    public int size() {
        return size;
    }


    // Checks if the queue is empty.
    public boolean isEmpty() {
        return size == 0;
    }

    // Removes all elements from the queue.
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}
