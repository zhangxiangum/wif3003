import java.util.Random;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class StackAccess {
    private final Stack<Integer> stack;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public StackAccess() {
        stack = new Stack<>();
    }

    public void push(int value) throws InterruptedException {
        lock.lock();
        try {
            while (stack.size() == 3) {
                if (!notFull.await(1, TimeUnit.SECONDS)) {
                    System.out.println("Push operation discarded.");
                    return;
                }
            }
            stack.push(value);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public int pop() throws InterruptedException {
        lock.lock();
        try {
            while (stack.isEmpty()) {
                if (!notEmpty.await(1, TimeUnit.SECONDS)) {
                    System.out.println("Pop operation discarded.");
                    return -1;
                }
            }
            int value = stack.pop();
            notFull.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }

    public int peek() throws InterruptedException {
        lock.lock();
        try {
            while (stack.isEmpty()) {
                if (!notEmpty.await(1, TimeUnit.SECONDS)) {
                    System.out.println("Peek operation discarded.");
                    return -1;
                }
            }
            return stack.peek();
        } finally {
            lock.unlock();
        }
    }
}

class ReadStack implements Runnable {
    private final StackAccess stackAccess;

    public ReadStack(StackAccess stackAccess) {
        this.stackAccess = stackAccess;
    }

    public void run() {
        for (int i = 0; i < 4; i++) {
            try {
                int value = stackAccess.pop();
                System.out.println("Read value: " + value);
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class WriteStack implements Runnable {
    private final StackAccess stackAccess;

    public WriteStack(StackAccess stackAccess) {
        this.stackAccess = stackAccess;
    }

    public void run() {
        for (int i = 0; i < 4; i++) {
            int value = (int) (Math.random() * 100);
            try {
                stackAccess.push(value);
                System.out.println("Pushed value: " + value);
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PeekStack implements Runnable {
    private final StackAccess stackAccess;

    public PeekStack(StackAccess stackAccess) {
        this.stackAccess = stackAccess;
    }

    public void run() {
        for (int i = 0; i < 4; i++) {
            try {
                int value = stackAccess.peek();
                System.out.println("Peeked value: " + value);
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Lab6 {
    public static void main(String[] args) {
        StackAccess stack = new StackAccess();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int taskType = random.nextInt(3);
            switch (taskType) {
                case 0: // push task
                    executorService.execute(new WriteStack(stack));
                    break;
                case 1: // pop task
                    executorService.execute(new ReadStack(stack));
                    break;
                case 2: // peek task
                    executorService.execute(new PeekStack(stack));
                    break;
            }
            try {
                Thread.sleep(20); // wait for 20ms between tasks
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        // Test
    }
}