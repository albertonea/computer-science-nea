package com.eastbarnetschool.ordermatchingengine.domain;
public class PriorityQueue<T> {
    private class Node<T> {
        T data;
        int priority;

        Node(T data, int priority) {
            this.data = data;
            this.priority = priority;
        }
    }

    private PriorityQueueOrder order;
    private Node<T>[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    // constructs an empty priority queue with the default initial capacity.
    public PriorityQueue(PriorityQueueOrder order) {
        this.order = order;
        heap = new Node[DEFAULT_CAPACITY];
        size = 0;
    }


    // constructs an empty priority queue with the specified initial capacity.
    public PriorityQueue(PriorityQueueOrder order, int initialCapacity) {
        this.order = order;
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be at least 1");
        }
        heap = new Node[initialCapacity];
        size = 0;
    }

    // returns the current number of elements in the queue.
    public int size() {
        return size;
    }

    // checks if the priority queue is empty.
    public boolean isEmpty() {
        return size == 0;
    }

    // inserts the specified element with the given priority into this queue.
    public void enqueue(T item, int priority) {
        // resize if needed
        if (size == heap.length) {
            resize(heap.length * 2);
        }

        // add the new node at the end of the heap
        heap[size] = new Node(item, priority);

        // bubble up the newly added node
        int currentIndex = size;
        int parentIndex = (currentIndex - 1) / 2;

        if (order == PriorityQueueOrder.ASC) {
            while (currentIndex > 0 && heap[currentIndex].priority < heap[parentIndex].priority) {
                // swap with parent
                Node temp = heap[currentIndex];
                heap[currentIndex] = heap[parentIndex];
                heap[parentIndex] = temp;

                // update indices
                currentIndex = parentIndex;
                parentIndex = (currentIndex - 1) / 2;
            }
        } else {
            while (currentIndex > 0 && heap[currentIndex].priority > heap[parentIndex].priority) {
                // swap with parent
                Node temp = heap[currentIndex];
                heap[currentIndex] = heap[parentIndex];
                heap[parentIndex] = temp;

                // update indices
                currentIndex = parentIndex;
                parentIndex = (currentIndex - 1) / 2;
            }
        }

        size++;
    }

    // retrieves and removes the highest priority element from this queue.
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }

        // store the highest priority item to return later
        T highestPriorityItem = heap[0].data;

        // move the last item to the root position
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        // heapify from the root
        if (size > 0) {
            siftDown(0);
        }

        // resize if needed
        if (size > 0 && size == heap.length / 4) {
            resize(heap.length / 2);
        }

        return highestPriorityItem;
    }

    // find an element in the queue by priority
    public T findByPriority(int priority) {
        for (int i = 0; i < size; i++) {
            if (heap[i].priority == priority) {
                return heap[i].data;
            }
        }
        return null;
    }

    // retrieves, but does not remove, the highest priority element from this queue.
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }

        return heap[0].data;
    }

    // removes all elements from the priority queue.
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    // restores the heap property by moving the element at the given index downward.
    private void siftDown(int index) {
        int smallest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        // check if left child exists and has higher priority than current smallest
        if (leftChild < size && heap[leftChild].priority < heap[smallest].priority) {
            smallest = leftChild;
        }

        // check if right child exists and has higher priority than current smallest
        if (rightChild < size && heap[rightChild].priority < heap[smallest].priority) {
            smallest = rightChild;
        }

        // if a child has higher priority, swap and continue sifting down
        if (smallest != index) {
            Node temp = heap[index];
            heap[index] = heap[smallest];
            heap[smallest] = temp;

            siftDown(smallest);
        }
    }

    // resizes the internal array used to store the heap.
    private void resize(int newCapacity) {
        Node[] newHeap = new Node[newCapacity];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }
}