import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lab4 {
    public static void main(String[] args) {
        Node<Integer> node = new Node<Integer>();
        Write write = new Write(node);
        Operate operate = new Operate(node, 2, new Dummy());
        Thread writeThread = new Thread(write);
        Thread operateThread = new Thread(operate);
        writeThread.start();
        operateThread.start();
    }
    public static class Node<T> {
        private T value;
        private final Lock lock = new ReentrantLock();
        private final Condition vcCondition = lock.newCondition();

        public void setValue(T value) {
            lock.lock();
            try {
                this.value = value;
                vcCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void executeOnValue(T desiredValue, Runnable task) {
            lock.lock();
            try {
                if (value.equals(desiredValue)) {
                    task.run();
                    System.out.println("The desired value is found!");
                } else {
                    while (!value.equals(desiredValue)) {
                        vcCondition.await();
                    }
                    task.run();
                    System.out.println("The desired value is found!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
    public static class Write implements Runnable {
        private final Node<Integer> node;
    
        public Write(Node<Integer> node) {
            this.node = node;
        }
    
        @Override
        public void run() {
            while (true) {
                int value = (int) (Math.random() * 5);
                node.setValue(value);
                System.out.println("Write thread set value to: " + value);
            }
        }
    }
    public static class Operate implements Runnable {
        private final Node<Integer> node;
        private final int target;
        private final Runnable dummyTask;
        private int dummyCount = 0;
    
        public Operate(Node<Integer> node, int target, Runnable dummyTask) {
            this.node = node;
            this.target = target;
            this.dummyTask = dummyTask;
        }
    
        @Override
        public void run() {
            while (true) {
                node.executeOnValue(target, () -> {
                    dummyTask.run();
                    dummyCount++;
                    if (dummyCount == 2) {
                        System.out.println("Operate: dummy task executed twice, terminating program.");
                        System.exit(0);
                    }
                });
            }
        }
    }
    
    public static class Dummy implements Runnable {
        @Override
        public void run() {
            System.out.println("The desired value is found!");
        }
    }
}
