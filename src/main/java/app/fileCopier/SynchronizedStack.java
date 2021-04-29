package app.fileCopier;
import java.util.ArrayDeque;

public class SynchronizedStack<T> {
    private ArrayDeque<T> dequeStore;
    private int stackCapacity;

    public SynchronizedStack(int initialCapacity) {
        this.dequeStore = new ArrayDeque<>(initialCapacity);
        stackCapacity = initialCapacity;
    }

    public SynchronizedStack() {
        dequeStore = new ArrayDeque<>();
    }

    public synchronized T pop() {
        return this.dequeStore.pop();
    }

    public synchronized void push(T element) {
        this.dequeStore.push(element);
    }

    public synchronized T peek() {
        return this.dequeStore.peek();
    }

    public synchronized int size() {
        return this.dequeStore.size();
    }

    public synchronized boolean isEmpty() {
        return this.dequeStore.isEmpty();
    }

    public int getStackCapacity() {
        return stackCapacity;
    }

    // This function is used that that the threads know when the stack is full. When full writer thread can take the whole chunk
    // from the stack and empty it.
    public boolean isFull() {
        return getStackCapacity() == size();
    }
}
