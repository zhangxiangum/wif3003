import java.util.concurrent.CompletableFuture;
public class Lab7 {
public static void main(String[] args){

    int[] arr = new int[111111]; 
    // fill the array with random numbers between 1 and 1111111
    for (int i = 0; i < 111111; i++) {
        arr[i] = i;
    }
    final int NUM_THREADS = (int) Math.ceil(arr.length / 1000.0); // calculate the number of threads needed
    final Result[] results = new Result[NUM_THREADS]; // array to hold the results from each thread
    
    for (int i = 0; i < NUM_THREADS; i++) {
        final int startIndex = i * 1000;
        final int endIndex = Math.min((i + 1) * 1000, arr.length);
        final int threadIndex = i;
        Thread t = new Thread(() -> {
            int maxDivisors = 0;
            int maxDivisorsNum = 0;
            for (int j = startIndex; j < endIndex; j++) {
                int divisors = countDivisors(arr[j]);
                if (divisors > maxDivisors) {
                    maxDivisors = divisors;
                    maxDivisorsNum = arr[j];
                }
            }
            results[threadIndex] = new Result(maxDivisorsNum, maxDivisors);
        }, "Thread " + i);
        t.start();
        
        try {
            t.join();
        } catch (InterruptedException ex) {
            // handle interruption
    }
    }
    
    // wait for all threads to complete
    
    
    // find the result with the maximum number of divisors
    Result maxResult = results[0];
    for (int i = 1; i < results.length; i++) {
        if (results[i].getNumDivisors() > maxResult.getNumDivisors()) {
            maxResult = results[i];
        }
    }
    
    System.out.println("Integer with the largest number of divisors: " + maxResult.getNum());
    System.out.println("Number of divisors: " + maxResult.getNumDivisors());
    

int n = 10;
long start;
long end;

// Sequential (Recursive) calculation
start = System.nanoTime();
int fibSeq = SequenceFibonacci(n);
end = System.nanoTime();
System.out.println("Fibonacci Sequence result: " + fibSeq);
System.out.println("Sequential (Recursive) Time taken: " + (end - start));

// Asynchronous (CompletableFuture) calculation
start = System.nanoTime();
CompletableFuture<Integer> fibAsync = CompletableFuture.supplyAsync(() -> fibonacciAsync(n));
end = System.nanoTime();
System.out.println("Asynchronous (CompletableFuture) Time taken: " + (end - start));

// Wait for CompletableFuture to complete and get the result
int fibResult = fibAsync.join();
System.out.println("Fibonacci CompletableFuture result: " + fibResult);
}
    public static int countDivisors(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                count++;
            }
        }
        return count;
    }

    private static class Result {
        private final int num;
        private final int numDivisors;

        public Result(int num, int numDivisors) {
            this.num = num;
            this.numDivisors = numDivisors;
        }

        public int getNum() {
            return num;
        }

        public int getNumDivisors() {
            return numDivisors;
        }
        

    }
       
    
    public static int SequenceFibonacci(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return SequenceFibonacci(n - 1) + SequenceFibonacci(n - 2);
        }
    }
    public static int fibonacciAsync(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> fibonacciAsync(n - 1));
            CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> fibonacciAsync(n - 2));
            return f1.join() + f2.join();
        }
    }
    

}