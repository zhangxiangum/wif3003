import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lab2 {
    public static void main(String[] args) throws InterruptedException {

        Timer timer1 = new Timer();
        timer1.start();
        Q1 q1 = new Q1();
        System.out.println(q1.FindLargestNumber());
        timer1.end();
        System.out.println("Sequence:" + timer1.getDuration());

        
        Timer timer2=new Timer();
        timer2.start();
        int arr[]= new int[1000000];
        Random random=new Random();
        for (int i=0; i<arr.length; i++){
        arr[i]=random.nextInt(50000)+1;
        }
        // Q2 q2_a=new Q2(arr,0,((int)arr.length/2));
        // q2_a.start();
        // Q2 q2_b=new Q2(arr,((int)arr.length/2),arr.length);
        // q2_b.start();
        // q2_a.join();
        // q2_b.join();
        // System.out.println(Math.max(q2_a.getMax(), q2_b.getMax()));

        Q2_Executor q2=new Q2_Executor(arr, 4);
        q2.CoccurentArray();
        System.out.println(q2.getMax());
        timer2.end();
        System.out.println("Coccurent:"+timer2.getDuration());

    }

    static class Q1 {
        int arr[] = new int[1000000];

        public Q1() {
        };

        public String FindLargestNumber() {
            int max = arr[0];
            int index = 0;
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                    index = i;
                }

            }
            return "No: " + index + "; Value: " + max;
        };
    }

    static class Q2 extends Thread {
        private int[] arr;
        private int start;
        private int end;
        private int max;

        // public static AtomicInteger number = new AtomicInteger(0);
        public Q2(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        };

        @Override
        public void run() {
            max = arr[start];
            // int index=0;
            for (int i = start + 1; i < end; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                    // index=i;
                }
            }
        }

        public int getMax() {
            return max;
        }
    }

    static class Timer {
        private long startTime;
        private long endTime;

        public void start() {
            startTime = System.currentTimeMillis();
        }

        public void end() {
            endTime = System.currentTimeMillis();
        }

        public long getDuration() {
            return endTime - startTime;
        }
    }

    static class Q2_Executor {
        private int[] arr;
        private int num;
        private int max;
        public Q2_Executor(int[] arr,int num) {
            this.arr = arr;
            this.num=num;
        }
        public void CoccurentArray() {
            ExecutorService executorService = Executors.newFixedThreadPool(num);
            max=arr[0];
            for (int i = 1; i < arr.length; i++) {
                int index = i;
                executorService.execute(() -> {
                    if(arr[index]>max){
                        max=arr[index];
                    }
                });
            }
    
            executorService.shutdown();
        }
        public int getMax(){
            return max;
        }
    }
}
