package com.eastbarnetschool.ordermatchingengine.domain;
public class PriorityQueue<T> {

    private PriorityQueueOrder order;
    private Node<T>[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructs an empty priority queue with the default initial capacity.
     */
    public PriorityQueue(PriorityQueueOrder order) {
        this.order = order;
        heap = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Constructs an empty priority queue with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity for this priority queue
     */
    public PriorityQueue(PriorityQueueOrder order, int initialCapacity) {
        this.order = order;
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be at least 1");
        }
        heap = new Node[initialCapacity];
        size = 0;
    }

    /**
     * Returns the current number of elements in the queue.
     *
     * @return the number of elements in the queue
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the priority queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Inserts the specified element with the given priority into this queue.
     *
     * @param item     the element to add
     * @param priority the priority of the element (lower values indicate higher priority)
     */
    public void enqueue(T item, int priority) {
        // Resize if needed
        if (size == heap.length) {
            resize(heap.length * 2);
        }

        // Add the new node at the end of the heap
        heap[size] = new Node(item, priority);

        // Bubble up the newly added node
        int currentIndex = size;
        int parentIndex = (currentIndex - 1) / 2;

        if (order == PriorityQueueOrder.ASC) {
            while (currentIndex > 0 && heap[currentIndex].priority < heap[parentIndex].priority) {
                // Swap with parent
                Node temp = heap[currentIndex];
                heap[currentIndex] = heap[parentIndex];
                heap[parentIndex] = temp;

                // Update indices
                currentIndex = parentIndex;
                parentIndex = (currentIndex - 1) / 2;
            }
        } else {
            while (currentIndex > 0 && heap[currentIndex].priority > heap[parentIndex].priority) {
                // Swap with parent
                Node temp = heap[currentIndex];
                heap[currentIndex] = heap[parentIndex];
                heap[parentIndex] = temp;

                // Update indices
                currentIndex = parentIndex;
                parentIndex = (currentIndex - 1) / 2;
            }
        }


        size++;
    }

    /**
     * Retrieves and removes the highest priority element from this queue.
     *
     * @return the element with the highest priority
     * @throws IllegalStateException if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }

        // Store the highest priority item to return later
        T highestPriorityItem = heap[0].data;

        // Move the last item to the root position
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        // Heapify from the root
        if (size > 0) {
            siftDown(0);
        }

        // Resize if needed
        if (size > 0 && size == heap.length / 4) {
            resize(heap.length / 2);
        }

        return highestPriorityItem;
    }

    public T findByPriority(int priority) {
        for (int i = 0; i < size; i++) {
            if (heap[i].priority == priority) {
                return heap[i].data;
            }
        }
        return null;
    }

    /**
     * Retrieves, but does not remove, the highest priority element from this queue.
     *
     * @return the element with the highest priority
     * @throws IllegalStateException if the queue is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }

        return heap[0].data;
    }

    /**
     * Removes all elements from the priority queue.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * Restores the heap property by moving the element at the given index downward.
     *
     * @param index the index of the element to move down
     */
    private void siftDown(int index) {
        int smallest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        // Check if left child exists and has higher priority than current smallest
        if (leftChild < size && heap[leftChild].priority < heap[smallest].priority) {
            smallest = leftChild;
        }

        // Check if right child exists and has higher priority than current smallest
        if (rightChild < size && heap[rightChild].priority < heap[smallest].priority) {
            smallest = rightChild;
        }

        // If a child has higher priority, swap and continue sifting down
        if (smallest != index) {
            Node temp = heap[index];
            heap[index] = heap[smallest];
            heap[smallest] = temp;

            siftDown(smallest);
        }
    }

    /**
     * Resizes the internal array used to store the heap.
     *
     * @param newCapacity the new capacity for the internal array
     */
    private void resize(int newCapacity) {
        Node[] newHeap = new Node[newCapacity];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }


}